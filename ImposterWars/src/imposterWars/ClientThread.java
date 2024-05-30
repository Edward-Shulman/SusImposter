package imposterWars;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class ClientThread extends Thread {

	private DatagramSocket socket;
	private boolean connected;
	private InetSocketAddress serverAddr;
	
	public ClientThread(int port, InetAddress serverAddr) throws SocketException {
		super();
		socket = new DatagramSocket(port);
		connected = true;
		this.serverAddr = new InetSocketAddress(serverAddr, port);
	}
	
	@Override
	public void run() {
		while (connected) {
			byte[] buf = new byte[1024];
			DatagramPacket recievePacket = new DatagramPacket(buf, 0, 1024);
			try {
				socket.receive(recievePacket);
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			
			DataInputStream recieve = new DataInputStream(new ByteArrayInputStream(recievePacket.getData(), recievePacket.getOffset(), recievePacket.getLength()));
			try {
				switch (PacketTypes.values()[recieve.read()]) {
				case END_MOVE_X:
					break;
				case END_MOVE_Y:
					break;
				case MOVE_DOWN:
					break;
				case MOVE_LEFT:
					break;
				case MOVE_RIGHT:
					break;
				case MOVE_UP:
					break;
				case UPDATE_AMMO_DROPS:
					break;
				case UPDATE_CONNECTION:
					break;
				case UPDATE_HP:
					break;
				case UPDATE_PLAYER:
					break;
				case UPDATE_PROJECTILES:
					break;
				case UPDATE_ROOM:
					break;
				case UPDATE_ROTATION:
					break;
				default:
					break;
				
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ByteArrayOutputStream dcStream = new ByteArrayOutputStream(2);
		dcStream.write(PacketTypes.UPDATE_CONNECTION.getID());
		dcStream.write(AmongUsInProcessing.state.getCurrentPlayerIndex());
		DatagramPacket dcPacket = new DatagramPacket(dcStream.toByteArray(), 2, serverAddr);
		try {
			socket.send(dcPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		socket.close();
	}
	
	public void disconnect() {
		connected = false;
	}

}
