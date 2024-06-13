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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.Vector;

public class ServerThread extends Thread 
{

	private DatagramSocket socket;
	private boolean connected;
	private Hashtable<UUID, InetSocketAddress> clients;
	
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
					playerYMovement(new UUID(recieve.readLong(), recieve.readLong()), 0);
					break;
				case END_MOVE_X:
					playerXMovement(new UUID(recieve.readLong(), recieve.readLong()), 0);
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
				case SHOOT:
					shot(new UUID(recieve.readLong(), recieve.readLong()));
					break;
				case UPDATE_ROTATION:
					updateRotation(new UUID(recieve.readLong(), recieve.readLong()), recieve.readFloat());
					break;
				case UPDATE_ROOM:
					updateRoom(new UUID(recieve.readLong(), recieve.readLong()), Rooms.values()[recieve.read()]);
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
		ByteArrayInputStream bais = new ByteArrayInputStream(buf);
		UUID id = UUID.nameUUIDFromBytes(bais.readNBytes(16));
		if (!AmongUsInProcessing.state.players.containsKey(id))
		{
			System.out.println("New player joined (" + addr.getAddress().toString() + ":" + addr.getPort()
				+ ")");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream send = new DataOutputStream(baos);
			send.write(PacketTypes.UPDATE_PLAYERS.getID());
			AmongUsInProcessing.state.setPlayer(id, new PlayerClient(buf, 1, buf.length - 1, AmongUsInProcessing.state.getWindow()));
			clients.put(id, addr);
			send.writeInt(AmongUsInProcessing.state.getPlayerCount());
			
			var players = AmongUsInProcessing.state.players.entrySet().iterator();
			while (players.hasNext())
			{
				Entry<UUID, PlayerClient> p = players.next();
				send.write(p.getKey().getMostSignificantBits());
				send.write(p.getKey().getLeastSignificantBits());
				send.write(p.getValue().toBytes());
			}
			for (Entry<UUID, InetSocketAddress> client : clients.entrySet())
			{
				DatagramPacket sendPacket = new DatagramPacket(baos.toByteArray(), baos.size(), client.getValue());
				socket.send(sendPacket);
			}
			updateAmmoDrops(new UUID(0, 0));
			return;
		}
		
		AmongUsInProcessing.state.removePlayer(id);
		ByteArrayOutputStream send = new ByteArrayOutputStream(17);
		send.write(PacketTypes.UPDATE_CONNECTION.getID());
		send.write(buf, 1, 16);
		clients.remove(id);
		for (Entry<UUID, InetSocketAddress> client : clients.entrySet())
		{
			DatagramPacket sendPacket = new DatagramPacket(send.toByteArray(), send.size(), client.getValue());
			socket.send(sendPacket);
		}
	}
	
	private void playerXMovement(UUID id, float vX) throws IOException 
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
		
		for (Entry<UUID, InetSocketAddress> client : clients.entrySet()) 
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream send = new DataOutputStream(baos);
			send.write(response.getID());
			send.writeLong(id.getMostSignificantBits());
			send.writeLong(id.getLeastSignificantBits());
			DatagramPacket sendPacket = new DatagramPacket(baos.toByteArray(), baos.size(), client.getValue());
			socket.send(sendPacket);
		}
	}
	
	private void playerYMovement(UUID id, float vY) throws IOException 
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
		
		for (Entry<UUID, InetSocketAddress> client : clients.entrySet()) 
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream send = new DataOutputStream(baos);
			send.write(response.getID());
			send.writeLong(id.getMostSignificantBits());
			send.writeLong(id.getLeastSignificantBits());
			DatagramPacket sendPacket = new DatagramPacket(baos.toByteArray(), baos.size(), client.getValue());
			socket.send(sendPacket);
		}
	}
	
	public void updateRoom(UUID id, Rooms room) throws IOException 
	{
		PlayerClient p = AmongUsInProcessing.state.getPlayer(id);
		p.setRoom(room);
		p.setX(400);
		p.setY(400);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream send = new DataOutputStream(baos);
		send.write(PacketTypes.UPDATE_ROOM.getID());
		send.writeLong(id.getMostSignificantBits());
		send.writeLong(id.getLeastSignificantBits());
		send.write(room.getID());
		for (Entry<UUID, InetSocketAddress> client : clients.entrySet()) 
		{
			DatagramPacket sendPacket = new DatagramPacket(baos.toByteArray(), baos.size(), client.getValue());
			socket.send(sendPacket);
		}
	}
	
	public void shot(UUID id) throws IOException 
	{
		AmongUsInProcessing.state.addBullet(id);
		AmongUsInProcessing.state.getPlayer(id).setAmmo(AmongUsInProcessing.state.getCurrentPlayer().getAmmo() - 1);
		refreshBullets();
	}

	public void refreshBullets() throws IOException {
		Vector<Bullet> bullets = AmongUsInProcessing.state.getBullets();
		ByteArrayOutputStream baos = new ByteArrayOutputStream(21 * bullets.size() + 5);
		DataOutputStream send = new DataOutputStream(baos);
		
		send.write(PacketTypes.UPDATE_PROJECTILES.getID());
		send.writeInt(bullets.size());
		for (Bullet b : bullets) 
		{
			send.write(b.toBytes());
		}
		
		for (Entry<UUID, InetSocketAddress> client : clients.entrySet()) 
		{
			DatagramPacket sendPacket = new DatagramPacket(baos.toByteArray(), send.size(), client.getValue());
			socket.send(sendPacket);
		}
	}
	
	public void updateRotation(UUID id, float rot) throws IOException 
	{
		AmongUsInProcessing.state.getPlayer(id).setRotation(rot);
		
		ByteArrayOutputStream send = new ByteArrayOutputStream();
		DataOutputStream d = new DataOutputStream(send);
		d.write(PacketTypes.UPDATE_ROTATION.getID());
		d.writeLong(id.getMostSignificantBits());
		d.writeLong(id.getLeastSignificantBits());
		d.writeFloat(rot);
		for (Entry<UUID, InetSocketAddress> client : clients.entrySet()) 
		{
			DatagramPacket sendPacket = new DatagramPacket(send.toByteArray(), send.size(), client.getValue());
			socket.send(sendPacket);
		}
	}

	public void updateMovement(PacketTypes movement) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(18);
		DataOutputStream send = new DataOutputStream(baos);
		send.write(movement.getID());
		send.writeLong(AmongUsInProcessing.state.getCurrentPlayerUUID().getMostSignificantBits());
		send.writeLong(AmongUsInProcessing.state.getCurrentPlayerUUID().getLeastSignificantBits());
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
		for (Entry<UUID, InetSocketAddress> client : clients.entrySet()) 
		{
			DatagramPacket sendPacket = new DatagramPacket(baos.toByteArray(), 18, client.getValue());
			socket.send(sendPacket);
		}
	}
	
	public void refreshPlayer(UUID id) throws IOException
	{
		ByteArrayOutputStream send = new ByteArrayOutputStream();
		send.write(PacketTypes.UPDATE_PLAYER.getID());
		send.write(AmongUsInProcessing.state.getPlayer(id).toBytes());
		
		for (Entry<UUID, InetSocketAddress> client : clients.entrySet())
		{
			DatagramPacket sendPacket = new DatagramPacket(send.toByteArray(), send.size(), client.getValue());
			socket.send(sendPacket);
		}
	}
	
	public void updateAmmoDrops(UUID id) throws IOException 
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream send = new DataOutputStream(baos);
		
		send.write(PacketTypes.UPDATE_AMMO_DROPS.getID());
		send.writeLong(id.getMostSignificantBits());
		send.writeLong(id.getLeastSignificantBits());
		send.writeInt(AmongUsInProcessing.state.ammoDrops.size());
		for (int i = 0; i < AmongUsInProcessing.state.ammoDrops.size(); i++)
		{
			send.write(AmongUsInProcessing.state.ammoDrops.get(i).toBytes());
		}
		
		for (Entry<UUID, InetSocketAddress> client : clients.entrySet())
		{
			DatagramPacket sendPacket = new DatagramPacket(baos.toByteArray(), baos.size(), client.getValue());
			socket.send(sendPacket);
		}
	}

	public void registerHit(UUID id, int bid) throws IOException
	{
		PlayerClient p = AmongUsInProcessing.state.getPlayer(id);
		Bullet b = AmongUsInProcessing.state.bullets.get(bid);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream send = new DataOutputStream(baos);
		
		p.setHealth(p.getHealth() - 10);
		int updatedRoom = Byte.MAX_VALUE;
		if (p.getHealth() <= 0) {
			p.setHealth(100);
			p.setAmmo(15);
			updatedRoom = (int) (Math.random() * 20);
			p.setRoom(Rooms.values()[updatedRoom]);
			p.setX(400);
			p.setY(400);
			PlayerClient killer = AmongUsInProcessing.state.getPlayer(b.getOwner());
			killer.incrementKills();
			AmongUsInProcessing.killfeed = new Killfeed(Colors.getByRGB(killer.getRColor(), killer.getGColor(), killer.getBColor()
				), Colors.getByRGB(p.getRColor(), p.getGColor(), p.getBColor()),AmongUsInProcessing.state.getWindow().millis(), AmongUsInProcessing.state.getWindow());
		}
		AmongUsInProcessing.state.removeBullet(bid);
		
		send.write(PacketTypes.REGISTER_HIT.getID());
		send.writeLong(id.getMostSignificantBits());
		send.writeLong(id.getLeastSignificantBits());
		send.writeInt(bid);
		send.write(updatedRoom);
		for (Entry<UUID, InetSocketAddress> client : clients.entrySet()) {
			DatagramPacket sendPacket = new DatagramPacket(baos.toByteArray(), baos.size(), client.getValue());
			socket.send(sendPacket);
		}
	}
}
