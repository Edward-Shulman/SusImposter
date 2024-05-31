package imposterWars;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Map.Entry;
import java.util.Vector;

public class ClientThread extends Thread 
{

	private DatagramSocket socket;
	private boolean connected;
	private InetSocketAddress serverAddr;
	
	public ClientThread(int port, InetAddress serverAddr) throws SocketException 
	{
		super();
		socket = new DatagramSocket(port);
		connected = true;
		this.serverAddr = new InetSocketAddress(serverAddr, port);
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
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			
			DataInputStream recieve = new DataInputStream(new ByteArrayInputStream(recievePacket.getData(), recievePacket.getOffset(), recievePacket.getLength()));
			try 
			{
				switch (PacketTypes.values()[recieve.read()]) 
				{
				case END_MOVE_X:
					playerXMovement(recieve.read(), 0);
					break;
				case END_MOVE_Y:
					playerYMovement(recieve.read(), 0);
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
				case UPDATE_AMMO_DROPS:
					updateAmmoDrops(recieve.readNBytes(recievePacket.getLength() - 1));
					break;
				case UPDATE_CONNECTION:
					updateConnection(recieve.read());
					break;
				case UPDATE_HP:
					updateHP(recieve.read(), recieve.readInt());
					break;
				case UPDATE_PLAYER:
					updatePlayer(recieve.read(), recieve.readNBytes(recievePacket.getLength() - 2));
					break;
				case UPDATE_PROJECTILES:
					updateProjectiles(recieve.readNBytes(recievePacket.getLength() - 1));
					break;
				case UPDATE_ROOM:
					updateRoom(recieve.read(), Rooms.values()[recieve.read()]);
					break;
				case UPDATE_ROTATION:
					updateRotation(recieve.read(), recieve.readFloat());
					break;
				default:
					break;
				
				}
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ByteArrayOutputStream dcStream = new ByteArrayOutputStream(2);
		dcStream.write(PacketTypes.UPDATE_CONNECTION.getID());
		dcStream.write(AmongUsInProcessing.state.getCurrentPlayerIndex());
		DatagramPacket dcPacket = new DatagramPacket(dcStream.toByteArray(), 2, serverAddr);
		try 
		{
			socket.send(dcPacket);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		socket.close();
	}
	
	private void updateAmmoDrops(byte[] buf) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(buf);
		DataInputStream read = new DataInputStream(bais);
		int size = read.readInt();
		Vector<AmmoDrop> ammoDrops = AmongUsInProcessing.state.getAmmoDrops();
		ammoDrops.clear();
		for (int i = 0; i < size; i++) {
			ammoDrops.add(new AmmoDrop(AmongUsInProcessing.state.getWindow(), read.readNBytes(9)));
		}
	}
	
	private void updateConnection(int id) {
		AmongUsInProcessing.state.removePlayer(id);
	}
	
	private void updatePlayer(int id, byte[] buf) throws IOException {
		if (id >= AmongUsInProcessing.state.getPlayerCount()) {
			AmongUsInProcessing.state.addPlayer(new PlayerClient(buf, 0, buf.length, AmongUsInProcessing.state.getWindow()));
			return;
		}
		AmongUsInProcessing.state.setPlayer(id, new PlayerClient(buf, 0, buf.length, AmongUsInProcessing.state.getWindow()));
	}
	
	private void updateHP(int id, int hp) {
		AmongUsInProcessing.state.getPlayer(id).setHealth(hp);
	}
	
	private void updateProjectiles(byte[] buf) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(buf);
		DataInputStream read = new DataInputStream(bais);
		int size = read.readInt();
		Vector<Bullet> bullets = AmongUsInProcessing.state.getBullets();
		bullets.clear();
		for (int i = 0; i < size; i++) {
			bullets.add(new Bullet(read.readNBytes(21), AmongUsInProcessing.state.getWindow()));
		}
	}
	
	private void updateRoom(int id, Rooms room) {
		PlayerClient p = AmongUsInProcessing.state.getPlayer(id);
		p.setRoom(room);
	}
	
	private void updateRotation(int id, float rot) throws IOException 
	{
		AmongUsInProcessing.state.getPlayer(id).setRotation(rot);
	}
	
	private void playerXMovement(int id, float vX) throws IOException 
	{
		PlayerClient p = AmongUsInProcessing.state.getPlayer(id);
		p.setvX(vX);
	}
	
	private void playerYMovement(int id, float vY) throws IOException 
	{
		PlayerClient p = AmongUsInProcessing.state.getPlayer(id);
		p.setvY(vY);
	}
	
	public void disconnect() 
	{
		connected = false;
	}

}
