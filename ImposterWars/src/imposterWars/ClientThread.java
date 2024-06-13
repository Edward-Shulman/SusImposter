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
import java.nio.BufferUnderflowException;
import java.util.HashMap;
import java.util.UUID;
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
		try 
		{
			initConnection();
		} catch (IOException e) 
		{
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
					playerXMovement(new UUID(recieve.readLong(), recieve.readLong()), 0);
					break;
				case END_MOVE_Y:
					playerYMovement(new UUID(recieve.readLong(), recieve.readLong()), 0);
					break;
				case MOVE_DOWN:
					playerYMovement(new UUID(recieve.readLong(), recieve.readLong()), 5);
					break;
				case MOVE_LEFT:
					playerXMovement(new UUID(recieve.readLong(), recieve.readLong()), -5);
					break;
				case MOVE_RIGHT:
					playerXMovement(new UUID(recieve.readLong(), recieve.readLong()), 5);
					break;
				case MOVE_UP:
					playerYMovement(new UUID(recieve.readLong(), recieve.readLong()), -5);
					break;
				case UPDATE_AMMO_DROPS:
					updateAmmoDrops(recieve.readNBytes(recievePacket.getLength() - 1));
					break;
				case UPDATE_CONNECTION:
					updateConnection(new UUID(recieve.readLong(), recieve.readLong()));
					break;
				case REGISTER_HIT:
					registerHit(new UUID(recieve.readLong(), recieve.readLong()), recieve.readInt(), recieve.read());
					break;
				case UPDATE_PLAYER:
					updatePlayer(new UUID(recieve.readLong(), recieve.readLong()), recieve.readNBytes(recievePacket.getLength() - 17));
					break;
				case UPDATE_PROJECTILES:
					updateProjectiles(recieve.readNBytes(recievePacket.getLength() - 1));
					break;
				case UPDATE_ROOM:
					updateRoom(new UUID(recieve.readLong(), recieve.readLong()), Rooms.values()[recieve.read()]);
					break;
				case UPDATE_ROTATION:
					updateRotation(new UUID(recieve.readLong(), recieve.readLong()), recieve.readFloat());
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
				e.printStackTrace();
			}
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream(17);
		DataOutputStream dcStream = new DataOutputStream(baos);
		try {
			dcStream.write(PacketTypes.UPDATE_CONNECTION.getID());
			dcStream.writeLong(AmongUsInProcessing.state.getCurrentPlayerUUID().getMostSignificantBits());
			dcStream.writeLong(AmongUsInProcessing.state.getCurrentPlayerUUID().getLeastSignificantBits());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DatagramPacket dcPacket = new DatagramPacket(baos.toByteArray(), 2, serverAddr);
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
		HashMap<UUID, PlayerClient> updated = new HashMap<>(size);
		for (int i = 0; i < size; i++) 
		{
			updated.put(new UUID(recieve.readLong(), recieve.readLong()), new PlayerClient(recieve.readNBytes(40), 0, 40, AmongUsInProcessing.state.getWindow()));
		}
		AmongUsInProcessing.state.players = updated;
	}
	
	private void initConnection() throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(61);
		DataOutputStream send = new DataOutputStream(baos);
		send.write(PacketTypes.UPDATE_CONNECTION.getID());
		send.writeLong(AmongUsInProcessing.state.getCurrentPlayerUUID().getMostSignificantBits());
		send.writeLong(AmongUsInProcessing.state.getCurrentPlayerUUID().getLeastSignificantBits());
		send.write(AmongUsInProcessing.state.getCurrentPlayer().toBytes());
		DatagramPacket sendPacket = new DatagramPacket(baos.toByteArray(), baos.size(), serverAddr);
		socket.send(sendPacket);
	}
	
	private void updateAmmoDrops(byte[] buf) throws IOException
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(buf);
		DataInputStream read = new DataInputStream(bais);
		UUID pickupID = new UUID(read.readLong(), read.readLong());
		if (pickupID.getMostSignificantBits() != 0 && pickupID.getLeastSignificantBits() != 0) {
			PlayerClient p = AmongUsInProcessing.state.getPlayer(pickupID);
			p.setAmmo(p.getAmmo() + 15);
		}
		int size = read.readInt();
		Vector<AmmoDrop> ammoDrops = AmongUsInProcessing.state.getAmmoDrops();
		ammoDrops.clear();
		for (int i = 0; i < size; i++)
		{
			ammoDrops.add(new AmmoDrop(AmongUsInProcessing.state.getWindow(), read.readNBytes(9)));
		}
	}
	
	private void updateConnection(UUID id) 
	{
		AmongUsInProcessing.state.removePlayer(id);
	}
	
	private void updatePlayer(UUID id, byte[] buf) throws IOException 
	{
		AmongUsInProcessing.state.setPlayer(id, new PlayerClient(buf, 0, buf.length, AmongUsInProcessing.state.getWindow()));
	}
	
	private void registerHit(UUID id, int bid, int kRoom) 
	{
		PlayerClient p = AmongUsInProcessing.state.getPlayer(id);
		Bullet b = AmongUsInProcessing.state.bullets.get(bid);

		if (kRoom != Byte.MAX_VALUE) {
			p.setHealth(100);
			p.setAmmo(15);
			p.setRoom(Rooms.values()[kRoom]);
			p.setX(400);
			p.setY(400);
			PlayerClient killer = AmongUsInProcessing.state.getPlayer(b.getOwner());
			killer.incrementKills();
			AmongUsInProcessing.killfeed = new Killfeed(Colors.getByRGB(killer.getRColor(), killer.getGColor(), killer.getBColor()
				), Colors.getByRGB(p.getRColor(), p.getGColor(), p.getBColor()), AmongUsInProcessing.state.getWindow().millis(), AmongUsInProcessing.state.getWindow());
		}
		else {
			p.setHealth(p.getHealth() - 10);
		}
		AmongUsInProcessing.state.bullets.remove(bid);
	}
	
	private void updateProjectiles(byte[] buf) throws IOException 
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(buf);
		DataInputStream read = new DataInputStream(bais);
		int size = read.readInt();
		Vector<Bullet> bullets = AmongUsInProcessing.state.getBullets();
		bullets.clear();
		try 
		{
			for (int i = 0; i < size; i++) 
			{
				bullets.add(new Bullet(read.readNBytes(21), AmongUsInProcessing.state.getWindow()));
			}
		}
		catch (BufferUnderflowException e) 
		{
			System.out.println(size + "dog");
		}
		AmongUsInProcessing.state.bullets = bullets;
	}
	
	private void updateRoom(UUID id, Rooms room) 
	{
		PlayerClient p = AmongUsInProcessing.state.getPlayer(id);
		p.setRoom(room);
		p.setX(400);
		p.setY(400);
	}
	
	private void updateRotation(UUID id, float rot) throws IOException 
	{
		AmongUsInProcessing.state.getPlayer(id).setRotation(rot);
	}
	
	private void playerXMovement(UUID id, float vX) throws IOException 
	{
		PlayerClient p = AmongUsInProcessing.state.getPlayer(id);
		p.setvX(vX);
	}
	
	private void playerYMovement(UUID id, float vY) throws IOException 
	{
		PlayerClient p = AmongUsInProcessing.state.getPlayer(id);
		p.setvY(vY);
	}
	
	public void disconnect() 
	{
		connected = false;
	}
	
	public void shoot() throws IOException 
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(17);
		DataOutputStream send = new DataOutputStream(baos);
		send.write(PacketTypes.SHOOT.getID());
		send.writeLong(AmongUsInProcessing.state.getCurrentPlayerUUID().getMostSignificantBits());
		send.writeLong(AmongUsInProcessing.state.getCurrentPlayerUUID().getLeastSignificantBits());
		AmongUsInProcessing.state.getCurrentPlayer().setAmmo(AmongUsInProcessing.state.getCurrentPlayer().getAmmo() - 1);
		DatagramPacket sendPacket = new DatagramPacket(baos.toByteArray(), 2, serverAddr);
		socket.send(sendPacket);
	}
	
	public void updateRotataion(float rot) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(21);
		DataOutputStream send = new DataOutputStream(baos);
		send.write(PacketTypes.UPDATE_ROTATION.getID());
		send.writeLong(AmongUsInProcessing.state.getCurrentPlayerUUID().getMostSignificantBits());
		send.writeLong(AmongUsInProcessing.state.getCurrentPlayerUUID().getLeastSignificantBits());
		send.writeFloat(rot);
		DatagramPacket sendPacket = new DatagramPacket(baos.toByteArray(), 6, serverAddr);
		socket.send(sendPacket);
	}
	
	public void updateMovement(PacketTypes movement) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(21);
		DataOutputStream send = new DataOutputStream(baos);
		send.write(movement.getID());
		send.writeLong(AmongUsInProcessing.state.getCurrentPlayerUUID().getMostSignificantBits());
		send.writeLong(AmongUsInProcessing.state.getCurrentPlayerUUID().getLeastSignificantBits());
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
		DatagramPacket sendPacket = new DatagramPacket(baos.toByteArray(), baos.size(), serverAddr);
		socket.send(sendPacket);
	}

	public void networkRoom(Rooms room) throws IOException 
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(18);
		DataOutputStream send = new DataOutputStream(baos);
		send.write(PacketTypes.UPDATE_ROOM.getID());
		send.writeLong(AmongUsInProcessing.state.getCurrentPlayerUUID().getMostSignificantBits());
		send.writeLong(AmongUsInProcessing.state.getCurrentPlayerUUID().getLeastSignificantBits());
		send.write(room.getID());
		
		DatagramPacket sendPacket = new DatagramPacket(baos.toByteArray(), baos.size(), serverAddr);
		socket.send(sendPacket);
	}
}
