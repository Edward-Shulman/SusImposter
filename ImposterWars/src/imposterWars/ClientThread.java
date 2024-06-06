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
		try {
			initConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
				case UPDATE_PLAYERS:
					setPlayers(recieve.readNBytes(recievePacket.getLength() - 1));
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
	
	private void setPlayers(byte[] buf) throws IOException 
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(buf);
		DataInputStream recieve = new DataInputStream(bais);
		int size = recieve.readInt();
		Vector<PlayerClient> updated = new Vector<PlayerClient>(size);
		for (int i = 0; i < size; i++) 
		{
			updated.add(new PlayerClient(recieve.readNBytes(40), 0, 40, AmongUsInProcessing.state.getWindow()));
		}
		AmongUsInProcessing.state.players = updated;
		AmongUsInProcessing.state.currentPlayerIndex = updated.size() - 1;
	}
	
	private void initConnection() throws IOException
	{
		ByteArrayOutputStream send = new ByteArrayOutputStream(46);
		send.write(PacketTypes.UPDATE_CONNECTION.getID());
		send.write(Byte.MAX_VALUE);
		send.write(AmongUsInProcessing.state.getCurrentPlayer().toBytes());
		DatagramPacket sendPacket = new DatagramPacket(send.toByteArray(), send.size(), serverAddr);
		socket.send(sendPacket);
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
		//TODO inital index conflict
		if (AmongUsInProcessing.state.getCurrentPlayerIndex() == 0) {
			AmongUsInProcessing.state.addPlayer(new PlayerClient(buf, 0, buf.length, AmongUsInProcessing.state.getWindow()));
			return;
		}
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
	
	public void shoot() throws IOException {
		byte[] shoot = new byte[2];
		shoot[0] = PacketTypes.SHOOT.getID();
		shoot[1] = (byte) AmongUsInProcessing.state.getCurrentPlayerIndex();
		DatagramPacket send = new DatagramPacket(shoot, 2, serverAddr);
		socket.send(send);
	}
	
	public void updateRotataion(float rot) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(6);
		DataOutputStream send = new DataOutputStream(baos);
		send.write(PacketTypes.UPDATE_ROTATION.getID());
		send.write(AmongUsInProcessing.state.getCurrentPlayerIndex());
		send.writeFloat(rot);
		DatagramPacket sendPacket = new DatagramPacket(baos.toByteArray(), 6, serverAddr);
		socket.send(sendPacket);
	}
	
	public void updateMovement(PacketTypes movement) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(6);
		DataOutputStream send = new DataOutputStream(baos);
		send.write(movement.getID());
		send.write(AmongUsInProcessing.state.getCurrentPlayerIndex());
		switch (movement) {
		case END_MOVE_X:
			send.writeFloat(0);
			break;
		case END_MOVE_Y:
			send.writeFloat(0);
			break;
		case MOVE_DOWN:
			send.writeFloat(5);
			break;
		case MOVE_LEFT:
			send.writeFloat(-5);
			break;
		case MOVE_RIGHT:
			send.writeFloat(5);
			break;
		case MOVE_UP:
			send.writeFloat(-5);
			break;
		default:
			break;
		
		}
		DatagramPacket sendPacket = new DatagramPacket(baos.toByteArray(), 6, serverAddr);
		socket.send(sendPacket);
	}

}
