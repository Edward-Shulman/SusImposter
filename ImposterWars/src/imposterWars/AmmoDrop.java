package imposterWars;

import processing.core.PApplet;
import processing.core.PImage;

public class AmmoDrop
{
	
	private PApplet parent;
	int centerX, centerY;
	private Rooms room;
	private PImage img;
	
	public AmmoDrop(PApplet p, Rooms r)
	{
		parent = p;
		centerX = (int) ((Math.random() * 560) + 70);
		centerY = (int) ((Math.random() * 480) + 70);
		room = r;
		img = p.loadImage("assets/real2.png");
		img.resize(75, 0);
	}
	
	public Rooms getRoom()
	{
		return room;
	}
	
	public int getX()
	{
		return centerX;
	}
	
	public int getY()
	{
		return centerY;
	}
	
	public void draw() 
	{
		parent.image(img, centerX - 25, centerY - 25);
	}
}
