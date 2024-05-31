package imposterWars;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import processing.core.PApplet;
import processing.core.PImage;

public class PlayerClient 
{
	private Rooms room;
	private float x;
	private float y;
	private int rColor;
	private int gColor;
	private int bColor;
	private int ammo;
	private int kills, health;
	private PApplet a;
	private float vX, vY, rotation;
	private PImage weapon;
	
	public PlayerClient(int r, int g, int b, PApplet a)
	{
		rColor = r;
		gColor = g;
		bColor = b;
		ammo = 50;
		kills = 0;
		room = Rooms.Caf;
		this.a = a;
		x = 350;
		y = 300;
		setvX(0);
		setvY(0);
		setHealth(99);
		rotation = 0;
		weapon = a.loadImage("assets/real.png");
		weapon.resize(100, 0);
	}
	
	public PlayerClient(byte[] buf, int offset, int len, PApplet a) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(buf, offset, len);
		DataInputStream d = new DataInputStream(bais);
		
		room = Rooms.values()[d.readInt()];
		x = d.readFloat();
		y = d.readFloat();
		int color = d.readInt();
		rColor = (color >> 16) & 0xff;
		gColor = (color >> 8) & 0xFF;
		bColor = color & 0xFF;
		ammo = d.readInt();
		kills = d.readInt();
		health = d.readInt();
		vX = d.readFloat();
		vY = d.readFloat();
		rotation = d.readFloat();
	}
	
	public byte[] toBytes() throws IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream(40);
		DataOutputStream d = new DataOutputStream(result);
		d.writeInt(room.getID());
		d.writeFloat(x);
		d.writeFloat(y);
		d.writeInt(a.color(rColor, gColor, bColor));
		d.writeInt(ammo);
		d.writeInt(kills);
		d.writeInt(health);
		d.writeFloat(vX);
		d.writeFloat(vY);
		d.writeFloat(rotation);
		return result.toByteArray();
	}
	
	public void draw()
	{
		a.fill(rColor, gColor, bColor);
		a.pushMatrix();
		a.noStroke();
		a.translate(x, y);
		a.rotate(rotation);
		a.ellipse(0, 0, 130, 130); //body
		a.fill(250, 250, 250);
		a.ellipse(30, 0, 60, 40); //visor
		a.image(weapon, 65, -20);
		a.popMatrix();
	}
	
	public Rooms getRoom() 
	{
		return room;
	}
	
	public void setRoom(Rooms r) 
	{
		room = r;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}

	public int getRColor()
	{
		return rColor;
	}

	public int getGColor()
	{
		return gColor;
	}

	public int getBColor()
	{
		return bColor;
	}

	public void setColor(int r, int g, int b)
	{
		rColor = r;
		gColor = g;
		bColor = b;
	}

	public int getAmmo()
	{
		return ammo;
	}

	public void setAmmo(int ammo)
	{
		this.ammo = ammo;
	}

	public int getKills()
	{
		return kills;
	}

	public void incrementKills()
	{
		kills++;
	}

	public float getvX() {
		return vX;
	}

	public void setvX(float vX) {
		this.vX = vX;
	}

	public float getvY() {
		return vY;
	}

	public void setvY(float vY) {
		this.vY = vY;
	}
	
	public void move() {
		x += vX;
		y += vY;
	}

	public int getHealth() 
	{
		return health;
	}

	public void setHealth(int health)
	{
		this.health = health;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
}
