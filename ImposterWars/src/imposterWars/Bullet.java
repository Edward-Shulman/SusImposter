package imposterWars;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

import processing.core.PApplet;
import processing.core.PVector;

public class Bullet
{
	private float x, y;
	private int color;
	private PVector velocity;
	private PApplet a;
	private UUID owner;
	private Rooms room;
	
	public Bullet(float x, float y, int r, int g, int b, float rotation, UUID id, Rooms room, PApplet a)
	{
		this(x, y, a.color(r, g, b), rotation, id, room, a);
	}
	
	public Bullet(float x, float y, int c, float rotation, UUID owner, Rooms room, PApplet a)
	{
		this.x = x;
		this.y = y;
		this.room = room;
		this.owner = owner;
		color = c;
		velocity = PVector.fromAngle(rotation);
		velocity.setMag(10);
		this.a = a;
	}
	
	public Bullet(byte[] buffer, PApplet a) 
	{
		ByteBuffer buf = ByteBuffer.wrap(buffer);
		x = buf.getFloat();
		y = buf.getFloat();
		color = buf.getInt();
		velocity = PVector.fromAngle(buf.getFloat());
		velocity.setMag(10);
		owner = new UUID(buf.getLong(), buf.getLong());
		room = Rooms.values()[buf.get()];
		this.a = a;
	}
	
	public void draw()
	{
		a.pushMatrix();
		a.translate(x, y);
		a.rotate(velocity.heading());
		a.fill(color);
		a.rect(-5, -5, 10, 10);
		a.popMatrix();
	}
	
	public boolean move()
	{
		x += velocity.x;
		y += velocity.y;
		return x >= 0 && x <= 700 && y >= 0 && y <= 600;
	}

	public UUID getOwner()
	{
		return owner;
	}
	
	public byte[] toBytes()
	{
		ByteArrayOutputStream result = new ByteArrayOutputStream(33);
		DataOutputStream d = new DataOutputStream(result);
		try {
			d.writeFloat(x);
			d.writeFloat(y);
			d.writeInt(color);
			d.writeFloat(velocity.heading());
			d.writeLong(owner.getMostSignificantBits());
			d.writeLong(owner.getLeastSignificantBits());
			d.write(room.getID());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toByteArray();
	}

	public Rooms getRoom() 
	{
		return room;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
}
