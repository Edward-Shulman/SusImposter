package imposterWars;

import processing.core.PApplet;

public class Killfeed {

	private Colors killerColor;
	private Colors killedColor;
	private int millis;
	private PApplet a;
	
	public Killfeed(Colors killer, Colors killed, int time, PApplet a) {
		killerColor = killer;
		killedColor = killed;
		millis = time;
		this.a = a;
	}
	
	public void draw() {
		if (a.millis() - millis >= 3000)
			return;
		
		a.fill(128, 128, 128, 128);
		a.rect(500, 0, 200, 50);
		
		a.fill(killerColor.getR(), killerColor.getG(), killerColor.getB());
		a.textSize(20);
		a.text(killerColor.toString(), 510, 30);
		
		a.fill(0);
		a.beginShape();
		a.vertex(590, 20);
		a.vertex(620, 20);
		a.vertex(620, 30);
		a.vertex(600, 30);
		a.vertex(600, 40);
		a.vertex(590, 40);
		a.endShape(PApplet.CLOSE);
		
		a.fill(killedColor.getR(), killedColor.getG(), killedColor.getB());
		a.text(killedColor.toString(), 630, 30);
	}
}
