package imposterWars;

import processing.core.PApplet;

public class AmmoDrop
{
	
	private PApplet parent;
	int centerX, centerY;
	private int room;
	
	public AmmoDrop(PApplet p, int x, int y)
	{
		parent = p;
		centerX = x;
		centerY = y;
		room = -1;
	}
	
	public AmmoDrop(PApplet p, int x, int y, int r)
	{
		parent = p;
		centerX = x;
		centerY = y;
		room = r;
	}
	
	public int getRoom()
	{
		return room;
	}
	
	public int generateX()
	{
		centerX = (int) ((Math.random() * 560) + 70);
		return centerX;
	}
	
	public int generateY()
	{
		centerY = (int) ((Math.random() * 480) + 70);
		return centerY;
	}
	
	public int getX()
	{
		return centerX;
	}
	
	public int getY()
	{
		return centerY;
	}
	
	public void drawAmongUs(int centerX, int centerY)
	{
		parent.fill(0, 200, 0);
		parent.ellipse((centerX), (centerY), 130, 130); //body
		parent.fill(250, 250, 250);
		parent.ellipse((centerX + 30), centerY, 60, 40); //visor
	}
	
	public void drawAmongUs() 
	{
		parent.fill(0, 200, 0);
		parent.ellipse((centerX), (centerY), 130, 130); //body
		parent.fill(250, 250, 250);
		parent.ellipse((centerX + 30), centerY, 60, 40); //visor
	}
}
