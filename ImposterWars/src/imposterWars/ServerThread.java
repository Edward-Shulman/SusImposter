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
		if (buf[0] == Byte.MAX_VALUE)
		{
			System.out.println("New player joined (" + addr.getAddress().toString() + ":" + addr.getPort()
				+ ")");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream send = new DataOutputStream(baos);
			send.write(PacketTypes.UPDATE_PLAYERS.getID());
			int nid = AmongUsInProcessing.state.addPlayer(new PlayerClient(buf, 1, buf.length - 1, AmongUsInProcessing.state.getWindow()));
			clients.put(nid, addr);
			send.writeInt(AmongUsInProcessing.state.getPlayerCount());
			
			for (int i = 0; i < AmongUsInProcessing.state.getPlayerCount(); i++)
			{
				send.write(AmongUsInProcessing.state.getPlayer(i).toBytes());
			}
			for (Entry<Integer, InetSocketAddress> client : clients.entrySet())
			{
				DatagramPacket sendPacket = new DatagramPacket(baos.toByteArray(), baos.size(), client.getValue());
				socket.send(sendPacket);
			}
			return;
		}
		
		AmongUsInProcessing.state.removePlayer(buf[0]);
		ByteArrayOutputStream send = new ByteArrayOutputStream(2);
		send.write(PacketTypes.UPDATE_CONNECTION.getID());
		send.write(buf[0]);
		clients.remove(Integer.valueOf(buf[0]));
		for (Entry<Integer, InetSocketAddress> client : clients.entrySet())
		{
			DatagramPacket sendPacket = new DatagramPacket(send.toByteArray(), send.size(), client.getValue());
			socket.send(sendPacket);
		}
	}
	
	private void playerXMovement(int id, float vX) throws IOException 
	{
		System.out.println(id + " " + vX);
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
		System.out.println(id + " " + vY);
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
	
	public void updateRoom(int id, Rooms room) throws IOException 
	{
		PlayerClient p = AmongUsInProcessing.state.getPlayer(id);
		p.setRoom(room);
		p.setX(id);
		p.setY(id);
		
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
	
	public void shot(int id) throws IOException 
	{
		AmongUsInProcessing.state.addBullet(id);
		AmongUsInProcessing.state.getCurrentPlayer().setAmmo(AmongUsInProcessing.state.getCurrentPlayer().getAmmo() - 1);
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
	
	public void updateRotation(int id, float rot) throws IOException 
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

	public void updateMovement(PacketTypes movement) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(6);
		DataOutputStream send = new DataOutputStream(baos);
		send.write(movement.getID());
		send.write(AmongUsInProcessing.state.getCurrentPlayerIndex());
		switch (movement) {
		case END_MOVE_X:
			AmongUsInProcessing.state.getCurrentPlayer().setvX(0);
			send.writeFloat(0);
			break;
		case END_MOVE_Y:
			AmongUsInProcessing.state.getCurrentPlayer().setvY(0);
			send.writeFloat(0);
			break;
		case MOVE_DOWN:
			AmongUsInProcessing.state.getCurrentPlayer().setvY(5);
			send.writeFloat(5);
			break;
		case MOVE_LEFT:
			AmongUsInProcessing.state.getCurrentPlayer().setvX(-5);
			send.writeFloat(-5);
			break;
		case MOVE_RIGHT:
			AmongUsInProcessing.state.getCurrentPlayer().setvX(5);
			send.writeFloat(5);
			break;
		case MOVE_UP:
			AmongUsInProcessing.state.getCurrentPlayer().setvY(-5);
			send.writeFloat(-5);
			break;
		default:
			break;
		
		}
		for (Entry<Integer, InetSocketAddress> client : clients.entrySet()) 
		{
			DatagramPacket sendPacket = new DatagramPacket(baos.toByteArray(), 6, client.getValue());
			socket.send(sendPacket);
		}
	}
	
	public void refreshPlayer(int id) throws IOException
	{
		ByteArrayOutputStream send = new ByteArrayOutputStream();
		send.write(PacketTypes.UPDATE_PLAYER.getID());
		send.write(AmongUsInProcessing.state.getPlayer(id).toBytes());
		
		for (Entry<Integer, InetSocketAddress> client : clients.entrySet())
		{
			DatagramPacket sendPacket = new DatagramPacket(send.toByteArray(), send.size(), client.getValue());
			socket.send(sendPacket);
		}
	}
}
