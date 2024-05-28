package imposterWars;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class HostServer
{
	private ArrayList<PlayerClient> players;
	private ArrayList<Bullet> bullets;
	private ArrayList<AmmoDrop> ammoDrops;
	private DatagramSocket socket;
	
	public HostServer() throws SocketException 
	{
		players = new ArrayList<>();
		bullets = new ArrayList<>();
		ammoDrops = new ArrayList<>();
		socket = new DatagramSocket(420);
	}
}
