package imposterWars;

import processing.core.PApplet;
import processing.core.PVector;

public class Bullet
{
	private float x, y;
	private int color;
	private PVector velocity;
	private PApplet a;
	
	public Bullet(float x, float y, int r, int g, int b, float rotation, PApplet a)
	{
		this.x = x;
		this.y = y;
		color = a.color(r, g, b);
		velocity = PVector.fromAngle(rotation);
		velocity.setMag(10);
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
	
	public boolean move() {
		x += velocity.x;
		y += velocity.y;
		return x >= 0 && x <= 700 && y >= 0 && y <= 600;
	}
	
}
