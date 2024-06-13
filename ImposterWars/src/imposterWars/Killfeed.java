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
		a.vertex(590, 15);
		a.vertex(620, 15);
		a.vertex(620, 25);
		a.vertex(600, 25);
		a.vertex(600, 35);
		a.vertex(590, 35);
		a.endShape(PApplet.CLOSE);
		a.stroke(0);
		a.strokeWeight(2);
		a.noFill();
		a.arc(600, 25, 5, 5, 0, PApplet.HALF_PI);

		a.noStroke();
		a.fill(killedColor.getR(), killedColor.getG(), killedColor.getB());
		a.text(killedColor.toString(), 630, 30);
	}
}
