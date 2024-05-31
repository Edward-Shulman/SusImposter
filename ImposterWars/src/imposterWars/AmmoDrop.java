package imposterWars;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import processing.core.PApplet;
import processing.core.PImage;

public class AmmoDrop
{
	
	private PApplet parent;
	float centerX, centerY;
	private Rooms room;
	private PImage img;
	
	public AmmoDrop(PApplet p, Rooms r)
	{
		parent = p;
		centerX = (float) ((Math.random() * 560) + 70);
		centerY = (float) ((Math.random() * 480) + 70);
		room = r;
		img = p.loadImage("assets/real2.png");
		img.resize(75, 0);
	}
	
	public AmmoDrop(PApplet p, byte[] buffer) {
		parent = p;
		ByteBuffer buf = ByteBuffer.wrap(buffer);
		centerX = buf.getFloat();
		centerY = buf.getFloat();
		room = Rooms.values()[buf.get()];
	}
	
	public byte[] toBytes() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(9);
		DataOutputStream d = new DataOutputStream(baos);
		d.writeFloat(centerX);
		d.writeFloat(centerY);
		d.write(room.getID());
		return baos.toByteArray();
	}
	
	public Rooms getRoom()
	{
		return room;
	}
	
	public float getX()
	{
		return centerX;
	}
	
	public float getY()
	{
		return centerY;
	}
	
	public void draw() 
	{
		parent.image(img, centerX - 25, centerY - 25);
	}
}
