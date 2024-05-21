package imposterWars;

import processing.core.PApplet;

public class PlayerClient 
{
	private Rooms room;
	private float x;
	private float y;
	private int rColor;
	private int gColor;
	private int bColor;
	private int ammo;
	private int kills;
	private PApplet a;
	private float vX, vY;
	
	public PlayerClient(int r, int g, int b, PApplet a)
	{
		rColor = r;
		gColor = g;
		bColor = b;
		ammo = 9000;
		kills = 0;
		room = Rooms.Caf;
		this.a = a;
		x = 350;
		y = 300;
		setvX(0);
		setvY(0);
	}
	
	public void draw()
	{
		a.fill(rColor, gColor, bColor);
		a.noStroke();
		a.ellipse((x), (y), 130, 130); //body
		a.fill(250, 250, 250);
		a.ellipse((x + 30), y, 60, 40); //visor
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
}
