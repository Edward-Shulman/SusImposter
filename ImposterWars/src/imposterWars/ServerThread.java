package imposterWars;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;

public class ServerThread extends Thread 
{

	private DatagramSocket socket;
	private boolean connected;
	private Hashtable<Integer, InetSocketAddress> clients;
	
	public ServerThread(int port) throws SocketException 
	{
		super();
		socket = new DatagramSocket(port);
		connected = true;
		clients = new Hashtable<>();
	}
	
	@Override
	public void run() 
	{
		while (connected)
		{
			byte[] buf = new byte[1024];
			DatagramPacket recievePacket = new DatagramPacket(buf, 0, 1024);
			try 
			{
				socket.receive(recievePacket);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
				break;
			}
			
			DataInputStream recieve = new DataInputStream(new ByteArrayInputStream(recievePacket.getData(), recievePacket.getOffset(), recievePacket.getLength()));
			try 
			{
				switch (PacketTypes.values()[recieve.read()])
				{
				case UPDATE_CONNECTION:
					playerConnectionUpdate(recieve.readNBytes(recievePacket.getLength() - 1), new InetSocketAddress(recievePacket.getAddress(), recievePacket.getPort()));
					break;
				case END_MOVE_Y:
					playerYMovement(recieve.read(), 0);
					break;
				case END_MOVE_X:
					playerXMovement(recieve.read(), 0);
					break;
				case MOVE_DOWN:
					playerYMovement(recieve.read(), 5);
					break;
				case MOVE_LEFT:
					playerXMovement(recieve.read(), -5);
					break;
				case MOVE_RIGHT:
					playerXMovement(recieve.read(), 5);
					break;
				case MOVE_UP:
					playerYMovement(recieve.read(), -5);
					break;
				case SHOOT:
					shot(recieve.read());
					break;
				case UPDATE_ROTATION:
					updateRotation(recieve.read(), recieve.readFloat());
					break;
				case UPDATE_ROOM:
					updateRoom(recieve.read(), Rooms.values()[recieve.read()]);
					break;
				default:
					break;

				}
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		socket.close();
	}
	
	public void disconnect()
	{
		connected = false;
	}
	
	private void playerConnectionUpdate(byte[] buf, InetSocketAddress addr) throws IOException
	{
		if (buf[0] == 255)
		{
			ByteArrayOutputStream send = new ByteArrayOutputStream(42);
			send.write(PacketTypes.UPDATE_PLAYER.getID());
			int nid = AmongUsInProcessing.state.addPlayer(new PlayerClient(buf, 1, buf.length - 1, AmongUsInProcessing.state.getWindow()));
			send.write(nid);
			send.write(buf, 1, buf.length - 1);
			for (Entry<Integer, InetSocketAddress> client : clients.entrySet()) 
			{
				DatagramPacket sendPacket = new DatagramPacket(send.toByteArray(), send.size(), client.getValue());
				socket.send(sendPacket);
			}
			clients.put(nid, addr);
			return;
		}
		
		AmongUsInProcessing.state.removePlayer(buf[0]);
		ByteArrayOutputStream send = new ByteArrayOutputStream(2);
		send.write(PacketTypes.UPDATE_CONNECTION.getID());
		send.write(buf[0]);
	}
	
	private void playerXMovement(int id, float vX) throws IOException 
	{
		PlayerClient p = AmongUsInProcessing.state.getPlayer(id);
		p.setvX(vX);
		PacketTypes response;
		if (vX == 0) 
		{
			response = PacketTypes.END_MOVE_X;
		} 
		else if (vX == 5)
		{
			response = PacketTypes.MOVE_RIGHT;
		}
		else 
		{
			response = PacketTypes.MOVE_LEFT;
		}
		
		for (Entry<Integer, InetSocketAddress> client : clients.entrySet()) 
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(response.getID());
			baos.write(id);
			DatagramPacket sendPacket = new DatagramPacket(baos.toByteArray(), baos.size(), client.getValue());
			socket.send(sendPacket);
		}
	}
	
	private void playerYMovement(int id, float vY) throws IOException 
	{
		PlayerClient p = AmongUsInProcessing.state.getPlayer(id);
		p.setvY(vY);
		PacketTypes response;
		if (vY == 0) 
		{
			response = PacketTypes.END_MOVE_Y;
		} 
		else if (vY == 5) 
		{
			response = PacketTypes.MOVE_DOWN;
		}
		else
		{
			response = PacketTypes.MOVE_UP;
		}
		
		for (Entry<Integer, InetSocketAddress> client : clients.entrySet()) 
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(response.getID());
			baos.write(id);
			DatagramPacket sendPacket = new DatagramPacket(baos.toByteArray(), baos.size(), client.getValue());
			socket.send(sendPacket);
		}
	}
	
	private void updateRoom(int id, Rooms room) throws IOException 
	{
		PlayerClient p = AmongUsInProcessing.state.getPlayer(id);
		p.setRoom(room);
		
		ByteArrayOutputStream send = new ByteArrayOutputStream();
		send.write(PacketTypes.UPDATE_ROOM.getID());
		send.write(id);
		send.write(room.getID());
		for (Entry<Integer, InetSocketAddress> client : clients.entrySet()) 
		{
			DatagramPacket sendPacket = new DatagramPacket(send.toByteArray(), send.size(), client.getValue());
			socket.send(sendPacket);
		}
	}
	
	private void shot(int id) throws IOException 
	{
		AmongUsInProcessing.state.addBullet(id);
		Vector<Bullet> bullets = AmongUsInProcessing.state.getBullets();
		ByteArrayOutputStream send = new ByteArrayOutputStream(21 * bullets.size() + 5);
		
		send.write(PacketTypes.UPDATE_PROJECTILES.getID());
		send.write(bullets.size());
		for (Bullet b : bullets) 
		{
			send.write(b.toBytes());
		}
		
		for (Entry<Integer, InetSocketAddress> client : clients.entrySet()) 
		{
			DatagramPacket sendPacket = new DatagramPacket(send.toByteArray(), send.size(), client.getValue());
			socket.send(sendPacket);
		}
	}
	
	private void updateRotation(int id, float rot) throws IOException 
	{
		AmongUsInProcessing.state.getPlayer(id).setRotation(rot);
		
		ByteArrayOutputStream send = new ByteArrayOutputStream();
		DataOutputStream d = new DataOutputStream(send);
		d.write(PacketTypes.UPDATE_ROTATION.getID());
		d.write(id);
		d.writeFloat(rot);
		for (Entry<Integer, InetSocketAddress> client : clients.entrySet()) 
		{
			DatagramPacket sendPacket = new DatagramPacket(send.toByteArray(), send.size(), client.getValue());
			socket.send(sendPacket);
		}
	}

}
