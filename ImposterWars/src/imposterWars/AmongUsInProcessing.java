package imposterWars;

import java.util.ArrayList;

import processing.core.PApplet;

public class AmongUsInProcessing extends PApplet
{
	
	private int playerX, playerY, centerX, centerY, score, playerRed, playerGreen, playerBlue, startTime, elapsedTime;
	private boolean keyRight, keyLeft, keyUp, keyDown;
	private boolean inStart, inCaf, inCenterHallway, inUpperLeftHallway, inWeapons, inMedbay, inAdmin, inStorage, inRightHallway, inOxygen, inNavigation, inShields, inBottomRightHallway;
	private boolean inComms, inBottomLeftHallway, inElectrical, inUpperEngine, inLowerEngine, inLeftHallway, inReactor, inSecurity, inEmptyRoom, inWinRoom, imposter;
	AmmoDrop[] ammoDrops;
	PlayerClient player;
	ArrayList<Bullet> bullets;
	
	public static void main(String[] args)
	{
		PApplet.main("imposterWars.AmongUsInProcessing");
	}
	
	public void settings()
	{
		size(700, 700);
	}
	
	public void setup()
	{
		player = new PlayerClient(255, 0, 0, this);
		playerX = 400;
		playerY = 400;
		playerRed = 247;
		playerGreen = 37;
		playerBlue = 37;
		keyRight = false;
		keyLeft = false;
		keyUp = false;
		keyDown = false;
		score = 0;
		inStart = true;
		inCaf = false;
		inCenterHallway = false;
		inUpperLeftHallway = false;
		inWeapons = false;
		inMedbay = false;
		inUpperEngine = false;
		inAdmin = false;
		inStorage = false;
		inRightHallway = false;
		inOxygen = false;
		inNavigation = false;
		inShields = false;
		inBottomRightHallway = false;
		inComms = false;
		inBottomLeftHallway = false;
		inElectrical = false;
		inLowerEngine = false;
		inLeftHallway = false;
		inReactor = false;
		inSecurity = false;
		inEmptyRoom = false;
		imposter = false;
		elapsedTime = 0;
		bullets = new ArrayList<>();
		ammoDrops = new AmmoDrop[10];
		for (int i = 0; i < 10; i++)
		{
			ammoDrops[i] = new AmmoDrop(this, Rooms.values()[(int) random(0, 20)]);
		}
	}
	
	public void draw()
	{
		
		if (inStart)
			drawStartScreen();
		
		if(isColliding())
		{
			player.setX(400);
			player.setY(400);
		}
		
		if(inEmptyRoom)
		{
			centerX = 1000;
			score = 0;
			inCaf = false;
			background(0, 0, 0);
			fill(184, 82, 191);
			textSize(50);
			text("ENTERING IMPOSTER MODE", 20, 300);
			elapsedTime = (millis() - startTime) / 1000;
			
			if(elapsedTime >= 5)
			{
				inEmptyRoom = false;
				inCaf = true;
				
			}
		}
		
		if(inWinRoom)
		{
			background(0, 0, 0);
			fill(184, 82, 191);
			textSize(50);
			text("ENDING IMPOSTER MODE", 30, 300);
			elapsedTime = (millis() - startTime) / 1000;
			centerX = 550;
			centerY = 150;
			
			
		}
		
		if(inCaf)
		{
			background(185, 186, 143);
			fill(53, 158, 219);
			arc(350, 300, 160, 160, 0, 360);
			arc(100, 100, 160, 160, 0, 360);
			arc(100, 500, 160, 160, 0, 360);
			arc(600, 100, 160, 160, 0, 360);
			arc(600, 500, 160, 160, 0, 360);
			fill(219, 53, 53);
			rect(335, 290, 30, 30);
			textSize(30);
			fill(184, 82, 191);
			text("You are in the cafeteria", 200, 40);
			if(dist(playerX, playerY, 350, 305) <= 30) //impostermode
			{
				imposter = true;
				elapsedTime = 0;
				startTime = millis();
				inCaf = false;
				inEmptyRoom = true;
				
			}
		}
		
		if(inWeapons)
		{
			background(185, 186, 143);
			fill(130, 157, 168);
			arc(350, 300, 250, 250, 0, 360);
			fill(35, 68, 117);
			rect(300, 340, 100, 30);
			rect(300, 240, 30, 120);
			fill(41, 227, 84);
			rect(440, 160, 135, 120);
			fill(184, 82, 191);
			textSize(30);
			text("You are in weapons", 200, 40);
		}
		
		if(inUpperLeftHallway)
		{
			background(227, 225, 218);
			fill(184, 82, 191);
			textSize(30);
			text("You are in the top left hallway", 200, 40);
		}
		
		if(inCenterHallway)
		{
			background(227, 225, 218);
			fill(184, 82, 191);
			textSize(30);
			text("You are in the center hallway", 200, 40);
		}
		
		if(inRightHallway)
		{
			background(227, 225, 218);
			fill(184, 82, 191);
			textSize(30);
			text("You are in the right hallway", 200, 40);
		}
		
		if(inAdmin)
		{
			background(156, 44, 44);
			fill(51, 143, 58);
			rect(200, 230, 300, 160);
			fill(140, 156, 141);
			rect(200, 230, 20, 160);
			rect(480, 230, 20, 160);
			fill(79, 232, 88);
			rect(235, 230, 230, 160);
			fill(184, 82, 191);
			textSize(30);
			text("You are in admin", 200, 40);
		}
		
		if(inMedbay)
		{
			background(194, 188, 186);
			fill(34, 131, 191);
			rect(0, 80, 130, 100);
			rect(0, 200, 130, 100);
			rect(570, 80, 130, 100);
			rect(570, 200, 130, 100);
			fill(235, 239, 242);
			rect(0, 80, 20, 100);
			rect(0, 200, 20, 100);
			rect(680, 80, 20, 100);
			rect(680, 200, 20, 100);
			fill(237, 250, 239);
			ellipse(500, 400, 180, 180);
			fill(65, 232, 93);
			ellipse(500, 400, 140, 140);
			fill(184, 82, 191);
			textSize(30);
			text("You are in medbay", 200, 40);
		}
		
		if(inOxygen)
		{
			background(171, 173, 132);
			fill(45, 224, 237);
			ellipse(500, 80, 100, 150);
			fill(166, 161, 161);
			ellipse(500, 120, 70, 70);
			ellipse(50, 525, 100, 150);
			ellipse(650, 525, 100, 150);
			ellipse(350, 525, 100, 150);
			rect(0, 250, 60, 20);
			fill(0, 0, 0);
			rect(60, 210, 20, 100);
			fill(184, 82, 191);
			textSize(30);
			text("You are in oxygen", 200, 40);
		}
		
		if(inNavigation)
		{
			background(163, 162, 157);
			fill(35, 68, 117);
			rect(480, 340, 100, 30);
			rect(480, 240, 30, 120);
			rect(480, 140, 100, 30);
			rect(480, 40, 30, 120);
			rect(500, 540, 100, 30);
			rect(500, 440, 30, 120);
			fill(212, 210, 199);
			rect(600, 220, 100, 130);
			rect(600, 50, 100, 110);
			fill(184, 82, 191);
			textSize(30);
			text("You are in navigation", 200, 40);
		}
		
		if(inShields)
		{
			background(185, 186, 143);
			fill(0, 0, 0);
			rect(100, 530, 75, 40);
			fill(125, 121, 109);
			rect(130, 570, 10, 30);
			fill(247, 246, 242);
			rect(125, 540, 20, 20);
			stroke(242, 60, 44);
			strokeWeight(10);
			line(600, 50, 600, 600);
			line(600, 50, 700, 50);
			line(0, 150, 150, 0);
			line(0, 380, 250, 510);
			line(250, 510, 0, 510);
			fill(184, 82, 191);
			noStroke();
			textSize(30);
			text("You are in shields", 200, 40);
		}
		
		if(inBottomRightHallway)
		{
			background(227, 225, 218);
			fill(184, 82, 191);
			textSize(20);
			text("You are in the bottom right hallway", 200, 40);
		}
		
		if(inComms)
		{
			background(99, 99, 97);
			fill(66, 168, 93);
			rect(350, 500, 100, 30);
			rect(350, 400, 30, 120);
			fill(224, 166, 83);
			rect(250, 550, 250, 50);
			rect(0, 150, 50, 150);
			rect(650, 150, 50, 150);
			fill(0, 0, 0);
			rect(380, 565, 75, 30);
			fill(184, 82, 191);
			textSize(30);
			text("You are in communications", 200, 40);
		}
		
		if(inStorage)
		{
			background(84, 84, 80);
			fill(166, 161, 161);
			rect(550, 540, 20, 60);
			fill(0, 0, 0);
			rect(513, 520, 100, 20);
			fill(66, 168, 93);
			rect(300, 200, 150, 150);
			rect(50, 0, 80, 80);
			fill(37, 110, 184);
			rect(250, 200, 50, 100);
			fill(227, 53, 41);
			rect(257, 310, 35, 50);
			fill(191, 119, 17);
			rect(350, 120, 130, 80);
			fill(184, 82, 191);
			textSize(30);
			text("You are in storage", 200, 40);
		}
		
		if(inBottomLeftHallway)
		{
			background(227, 225, 218);
			fill(184, 82, 191);
			textSize(20);
			text("You are in the bottom left hallway", 200, 40);
		}
		
		if(inLeftHallway)
		{
			background(133, 113, 113);
			fill(184, 82, 191);
			textSize(30);
			text("You are in the left hallway", 200, 40);
		}
		
		if(inElectrical) 
		{
			background(128, 111, 46);
			fill(124, 133, 123);
			rect(0, 250, 380, 100);
			rect(450, 0, 250, 55);
			fill(219, 227, 64);
			triangle(100, 270, 140, 320, 60, 320);
			triangle(600, 10, 570, 45, 630, 45);
			strokeWeight(3);
			stroke(0, 0, 0);
			line(0, 150, 600, 55);
			line(0, 450, 300, 350);
			line(255, 350, 550, 600);
			stroke(191, 176, 57);
			line(0, 530, 396, 470);
			line(470, 77, 550, 365);
			stroke(55, 74, 171);
			line(430, 500, 700, 200);
			fill(184, 82, 191);
			noStroke();
			textSize(30);
			text("You are in electrical", 200, 40);
		}
		
		if(inLowerEngine) 
		{
			background(133, 113, 113);
			fill(219, 133, 83);
			rect(0, 150, 400, 250);
			ellipse(400, 275, 150, 250);
			fill(114, 236, 242);
			ellipse(470, 275, 50, 100);
			fill(166, 161, 161);
			rect(0, 350, 100, 100);
			fill(184, 82, 191);
			textSize(30);
			text("You are in lower engine", 200, 40);
		}
		
		if(inUpperEngine) 
		{
			background(133, 113, 113);
			fill(219, 133, 83);
			rect(0, 150, 400, 250);
			ellipse(400, 275, 150, 250);
			fill(114, 236, 242);
			ellipse(470, 275, 50, 100);
			fill(166, 161, 161);
			rect(0, 350, 100, 100);
			fill(184, 82, 191);
			textSize(30);
			text("You are in upper engine", 200, 40);
		}
		
		if(inSecurity)
		{
			background(41, 150, 79);
			fill(153, 91, 64);
			rect(600, 200, 100, 210);
			rect(525, 200, 75, 60);
			fill(70, 214, 60);
			ellipse(555, 320, 70, 70);
			fill(0, 0, 0);
			rect(195, 0, 220, 150);
			strokeWeight(2);
			stroke(250, 250, 250);
			line(195, 75, 415, 75);
			line(305, 0, 305, 150);
			fill(184, 82, 191);
			noStroke();
			textSize(30);
			text("You are in security", 430, 40);
		}
		
		if(inReactor)
		{
			background(102, 72, 212);
			fill(44, 123, 163);
			ellipse(50, 300, 175, 350);
			rect(125, 280, 75, 75);
			fill(144, 157, 163);
			rect(300, 0, 75, 75);
			rect(300, 525, 75, 75);
			fill(184, 82, 191);
			textSize(30);
			text("You are in reactor", 400, 40);
		}

		if (!inStart)
		{
			imposterSpawn();
			player.draw();
			drawHUD();
			player.move();
			rotatePlayer();
			int i = 0;
			while (i < bullets.size())
			{
				Bullet b = bullets.get(i);
				b.draw();
				if (b.move()) 
				{
					i++;
				}
				else
				{
					bullets.remove(i);
				}
			}
		}
	}
	
	
	public boolean isColliding()
	{
		if(player.getY() > 535 && inCaf)
		{
			inCaf = false;
			inCenterHallway = true;
			player.setRoom(Rooms.CenterHallway);
			return true;
		}
		else if(player.getX() < 65 && inCaf)
		{
			inCaf = false;
			inUpperLeftHallway = true;
			player.setRoom(Rooms.UpperLeftHallway);
			return true;
		}
		else if( player.getX() > 635 && inCaf)
		{
			inCaf = false;
			inWeapons = true;
			player.setRoom(Rooms.Weapons);
			return true;
		}
		else if(player.getY() < 65 && inWeapons)
			return true;
		else if(player.getY() > 535 && inWeapons)
		{
			inWeapons = false;
			inRightHallway = true;
			player.setRoom(Rooms.RightHallway);
			return true;
		}
		else if(player.getX() < 65 && inWeapons)
		{
			inCaf = true;
			inWeapons = false;
			player.setRoom(Rooms.Caf);
			return true;
		}
		else if( player.getX() > 635 && inWeapons)
			return true;
		else if(player.getY() < 65 && inUpperLeftHallway)
			return true;
		else if(player.getY() > 535 && inUpperLeftHallway)
		{
			inUpperLeftHallway = false;
			inMedbay = true;
			player.setRoom(Rooms.Medbay);
			return true;
		}
		else if(player.getX() < 65 && inUpperLeftHallway)
		{
			inUpperLeftHallway = false;
			inUpperEngine = true;
			player.setRoom(Rooms.UpperEngine);
			return true;
		}
		else if( player.getX() > 635 && inUpperLeftHallway)
		{
			inCaf = true;
			inUpperLeftHallway = false;
			player.setRoom(Rooms.Caf);
			return true;
		}
		else if(player.getY() < 65 && inCenterHallway)
		{
			inCaf = true;
			inCenterHallway = false;
			player.setRoom(Rooms.Caf);
			return true;
		}
		else if(player.getY() > 535 && inCenterHallway)
		{
			inCenterHallway = false;
			inStorage = true;
			player.setRoom(Rooms.Storage);
			return true;
		}
		else if(player.getX() < 65 && inCenterHallway)
			return true;
		else if( player.getX() > 635 && inCenterHallway)
		{
			inCenterHallway = false;
			inAdmin = true;
			player.setRoom(Rooms.Admin);
			return true;
		}
		else if(player.getY() < 65 && inRightHallway)
		{
			inRightHallway = false;
			inWeapons = true;
			player.setRoom(Rooms.Weapons);
			return true;
		}
		else if(player.getY() > 535 && inRightHallway)
		{
			inRightHallway = false;
			inShields = true;
			player.setRoom(Rooms.Shields);
			return true;
		}
		else if(player.getX() < 65 && inRightHallway)
		{
			inRightHallway = false;
			inOxygen = true;
			player.setRoom(Rooms.Oxygen);
			return true;
		}
		else if( player.getX() > 635 && inRightHallway)
		{
			inRightHallway = false;
			inNavigation = true;
			player.setRoom(Rooms.Navigation);
			return true;
		}
		else if(player.getY() < 65 && inAdmin)
			return true;
		else if(player.getY() > 535 && inAdmin)
			return true;
		else if(player.getX() < 65 && inAdmin)
		{
			inAdmin = false;
			inCenterHallway = true;
			player.setRoom(Rooms.CenterHallway);
			return true;
		}
		else if( player.getX() > 635 && inAdmin)
			return true;
		else if(player.getY() < 65 && inMedbay)
		{
			inMedbay = false;
			inUpperLeftHallway = true;
			player.setRoom(Rooms.UpperLeftHallway);
			return true;
		}
		else if(player.getY() > 535 && inMedbay)
			return true;
		else if(player.getX() < 65 && inMedbay)
			return true;
		else if( player.getX() > 635 && inMedbay)
			return true;
		else if(player.getY() < 65 && inOxygen)
			return true;
		else if(player.getY() > 535 && inOxygen)
			return true;
		else if(player.getX() < 65 && inOxygen)
			return true;
		else if( player.getX() > 635 && inOxygen)
		{
			inOxygen = false;
			inRightHallway = true;
			player.setRoom(Rooms.RightHallway);
			return true;
		}
		else if(player.getY() < 65 && inNavigation)
			return true;
		else if(player.getY() > 535 && inNavigation)
			return true;
		else if(player.getX() < 65 && inNavigation)
		{
			inNavigation = false;
			inRightHallway = true;
			player.setRoom(Rooms.RightHallway);
			return true;
		}
		else if( player.getX() > 635 && inNavigation)
			return true;
		else if(player.getY() < 65 && inShields)
		{
			inShields = false;
			inRightHallway = true;
			player.setRoom(Rooms.RightHallway);
			return true;
		}
		else if(player.getY() > 535 && inShields)
			return true;
		else if(player.getX() < 65 && inShields)
		{
			inShields = false;
			inBottomRightHallway = true;
			player.setRoom(Rooms.BottomRightHallway);
			return true;
		}
		else if( player.getX() > 635 && inShields)
			return true;
		else if(player.getY() < 65 && inBottomRightHallway)
			return true;
		else if(player.getY() > 535 && inBottomRightHallway)
		{
			inBottomRightHallway = false;
			inComms = true;
			player.setRoom(Rooms.Comms);
			return true;
		}
		else if(player.getX() < 65 && inBottomRightHallway)
		{
			inBottomRightHallway = false;
			inStorage = true;
			player.setRoom(Rooms.Storage);
			return true;
		}
		else if( player.getX() > 635 && inBottomRightHallway)
		{
			inBottomRightHallway = false;
			inShields = true;
			player.setRoom(Rooms.Shields);
			return true;
		}
		else if(player.getY() < 65 && inComms)
		{
			inComms = false;
			inBottomRightHallway = true;
			player.setRoom(Rooms.BottomRightHallway);
			return true;
		}
		else if(player.getY() > 535 && inComms)
			return true;
		else if(player.getX() < 65 && inComms)
			return true;
		else if( player.getX() > 635 && inComms)
			return true;
		else if(player.getY() < 65 && inStorage)
		{
			inStorage = false;
			inCenterHallway = true;
			player.setRoom(Rooms.CenterHallway);
			return true;
		}
		else if(player.getY() > 535 && inStorage)
			return true;
		else if(player.getX() < 65 && inStorage)
		{
			inStorage = false;
			inBottomLeftHallway = true;
			player.setRoom(Rooms.BottomLeftHallway);
			return true;
		}
		else if(player.getX() > 635 && inStorage)
		{
			inStorage = false;
			inBottomRightHallway = true;
			player.setRoom(Rooms.BottomRightHallway);
			return true;
		}
		else if(player.getY() < 65 && inBottomLeftHallway)
		{
			inBottomLeftHallway = false;
			inElectrical = true;
			player.setRoom(Rooms.Electrical);
			return true;
		}
		else if(player.getY() > 535 && inBottomLeftHallway)
			return true;
		else if(player.getX() < 65 && inBottomLeftHallway)
		{
			inBottomLeftHallway = false;
			inLowerEngine = true;
			player.setRoom(Rooms.LowerEngine);
			return true;
		}
		else if(player.getX() > 635 && inBottomLeftHallway)
		{
			inBottomLeftHallway = false;
			inStorage = true;
			player.setRoom(Rooms.Storage);
			return true;
		}
		else if(player.getY() < 65 && inElectrical)
			return true;
		else if(player.getY() > 535 && inElectrical)
		{
			inElectrical = false;
			inBottomLeftHallway = true;
			player.setRoom(Rooms.BottomLeftHallway);
			return true;
		}
		else if(player.getX() < 65 && inElectrical)
			return true;
		else if(player.getX() > 635 && inElectrical)
			return true;
		else if(player.getY() < 65 && inLowerEngine)
		{
			inLowerEngine = false;
			inLeftHallway = true;
			player.setRoom(Rooms.LeftHallway);
			return true;
		}
		else if(playerY > 535 && inLowerEngine)
			return true;
		else if(player.getX() < 65 && inLowerEngine)
			return true;
		else if(player.getX() > 635 && inLowerEngine)
		{
			inLowerEngine = false;
			inBottomLeftHallway = true;
			player.setRoom(Rooms.BottomLeftHallway);
			return true;
		}
		else if(player.getY() < 65 && inLeftHallway)
		{
			inLeftHallway = false;
			inUpperEngine = true;
			player.setRoom(Rooms.UpperEngine);
			return true;
		}
		else if(player.getY() > 535 && inLeftHallway)
		{
			inLeftHallway = false;
			inLowerEngine = true;
			player.setRoom(Rooms.LowerEngine);
			return true;
		}
		else if(player.getX() < 65 && inLeftHallway)
		{
			inLeftHallway = false;
			inReactor = true;
			player.setRoom(Rooms.Reactor);
			return true;
		}
		else if( player.getX() > 635 && inLeftHallway)
		{
			inLeftHallway = false;
			inSecurity = true;
			player.setRoom(Rooms.Security);
			return true;
		}
		else if(player.getY() < 65 && inUpperEngine)
			return true;
		else if(player.getY() > 535 && inUpperEngine)
		{
			inUpperEngine = false;
			inLeftHallway = true;
			player.setRoom(Rooms.LeftHallway);
			return true;
		}
		else if(player.getX() < 65 && inUpperEngine)
			return true;
		else if(player.getX() > 635 && inUpperEngine)
		{
			inUpperEngine = false;
			inUpperLeftHallway = true;
			player.setRoom(Rooms.UpperLeftHallway);
			return true;
		}
		else if(player.getY() < 65 && inSecurity)
			return true;
		else if(player.getY() > 535 && inSecurity)
			return true;
		else if(player.getX() < 65 && inSecurity)
		{
			inSecurity = false;
			inLeftHallway = true;
			player.setRoom(Rooms.LeftHallway);
			return true;
		}
		else if( player.getX() > 635 && inSecurity)
			return true;
		else if(player.getY() < 65 && inReactor)
			return true;
		else if(player.getY() > 535 && inReactor)
			return true;
		else if(player.getX() < 65 && inReactor)
			return true;
		else if(player.getX() > 635 && inReactor)
		{
			inReactor = false;
			inLeftHallway = true;
			player.setRoom(Rooms.LeftHallway);
			return true;
		}
		else if(player.getY() < 65 && inEmptyRoom)
			return true;
		else if(player.getY() > 535 && inEmptyRoom)
			return true;
		else if(player.getX() < 65 && inEmptyRoom)
			return true;
		else if(player.getX() > 635 && inEmptyRoom)
			return true;
		else if(player.getY() < 65 && inWinRoom)
			return true;
		else if(player.getY() > 535 && inWinRoom)
			return true;
		else if(player.getX() < 65 && inWinRoom)
			return true;
		else if(player.getX() > 635 && inWinRoom)
			return true;
		else
			return false;
	}

	public void changeColor()
	{
		if(dist(playerX, playerY, 55, 75) <= 70) //red
		{
			playerRed = 247;
			playerGreen = 37;
			playerBlue = 37;
		}
		
		if(dist(playerX, playerY, 205, 55) <= 70) //blue
		{
			playerRed = 28;
			playerGreen = 45;
			playerBlue = 232;
		}
		
		if(dist(playerX, playerY, 355, 55) <= 70) //green
		{
			playerRed = 53;
			playerGreen = 92;
			playerBlue = 54;
		}
		
		if(dist(playerX, playerY, 505, 55) <= 70) //pink
		{
			playerRed = 240;
			playerGreen = 101;
			playerBlue = 228;
		}
		
		if(dist(playerX, playerY, 625, 55) <= 70) //orange
		{
			playerRed = 242;
			playerGreen = 168;
			playerBlue = 7;
		}
		
		if(dist(playerX, playerY, 55, 305) <= 70) //lime
		{
			playerRed = 5;
			playerGreen = 240;
			playerBlue = 44;
		}
		
		if(dist(playerX, playerY, 55, 515) <= 70) //purple
		{
			playerRed = 139;
			playerGreen = 40;
			playerBlue = 209;
		}
		
		if(dist(playerX, playerY, 625, 305) <= 70) //brown
		{
			playerRed = 110;
			playerGreen = 82;
			playerBlue = 38;
		}
		
		if(dist(playerX, playerY, 625, 515) <= 70) //cyan
		{
			playerRed = 65;
			playerGreen = 240;
			playerBlue = 237;
		}
		
		if(dist(playerX, playerY, 205, 515) <= 70) //yellow
		{
			playerRed = 242;
			playerGreen = 250;
			playerBlue = 10;
		}
		
		if(dist(playerX, playerY, 505, 515) <= 70) //black
		{
			playerRed = 41;
			playerGreen = 41;
			playerBlue = 38;
		}
	}
	
//	public void imposterMode()
//	{
//		int random = 0;
//		for(int i = 0; i < 10; i++)
//		{
//			random = (int) (Math.random() * 20);
//		}
//	}

	public void imposterSpawn()
	{
		for(int i = 0; i < ammoDrops.length;i++)
		{
			if(ammoDrops[i].getRoom().equals(player.getRoom()))
			{
				ammoDrops[i].draw();
				if(dist(player.getX(), player.getY(), ammoDrops[i].getX(), ammoDrops[i].getY()) <= 100 && ! inStart)
				{
					ammoDrops[i].centerX = 1000;
					player.setAmmo(player.getAmmo() + 15);
				}
			}
//			else if(inCenterHallway && ammoDrops[i].getRoom() == Rooms.CenterHallway)
//			{
//				ammoDrops[i].draw();
//				if(dist(playerX, playerY, ammoDrops[i].getX(), ammoDrops[i].getY()) <= 130 && ! inStart)
//					ammoDrops[i].centerX = 1000;
//				imposterWin();
//			}
//			else if(inUpperLeftHallway && ammoDrops[i].getRoom() == Rooms.UpperLeftHallway) 
//			{
//				ammoDrops[i].draw();
//				if(dist(playerX, playerY, ammoDrops[i].getX(), ammoDrops[i].getY()) <= 130 && ! inStart)
//					ammoDrops[i].centerX = 1000;
//				imposterWin();
//			}
//			else if(inWeapons && ammoDrops[i].getRoom() == Rooms.Weapons)
//			{
//				ammoDrops[i].draw();
//				if(dist(playerX, playerY, ammoDrops[i].getX(), ammoDrops[i].getY()) <= 130 && ! inStart)
//					ammoDrops[i].centerX = 1000;
//				imposterWin();
//			}
//			else if(inMedbay && ammoDrops[i].getRoom() == Rooms.Medbay)
//			{
//				ammoDrops[i].draw();
//				if(dist(playerX, playerY, ammoDrops[i].getX(), ammoDrops[i].getY()) <= 130 && ! inStart)
//					ammoDrops[i].centerX = 1000;
//				imposterWin();
//			}
//			else if(inAdmin && ammoDrops[i].getRoom() == Rooms.Admin)
//			{
//				ammoDrops[i].draw();
//				if(dist(playerX, playerY, ammoDrops[i].getX(), ammoDrops[i].getY()) <= 130 && ! inStart)
//					ammoDrops[i].centerX = 1000;
//				imposterWin();
//			}
//			else if(inStorage && ammoDrops[i].getRoom() == Rooms.Storage) 
//			{
//				ammoDrops[i].draw();
//				if(dist(playerX, playerY, ammoDrops[i].getX(), ammoDrops[i].getY()) <= 130 && ! inStart)
//					ammoDrops[i].centerX = 1000;
//				imposterWin();
//			}
//			else if(inRightHallway && ammoDrops[i].getRoom() == Rooms.RightHallway)
//			{
//				ammoDrops[i].draw();
//				if(dist(playerX, playerY, ammoDrops[i].getX(), ammoDrops[i].getY()) <= 130 && ! inStart)
//					ammoDrops[i].centerX = 1000;
//				imposterWin();
//			}
//			else if(inNavigation && ammoDrops[i].getRoom() == Rooms.Navigation)
//			{
//				ammoDrops[i].draw();
//				if(dist(playerX, playerY, ammoDrops[i].getX(), ammoDrops[i].getY()) <= 130 && ! inStart)
//					ammoDrops[i].centerX = 1000;
//				imposterWin();
//			}
//			else if(inOxygen && ammoDrops[i].getRoom() == Rooms.Oxygen)
//			{
//				ammoDrops[i].draw();
//				if(dist(playerX, playerY, ammoDrops[i].getX(), ammoDrops[i].getY()) <= 130 && ! inStart)
//					ammoDrops[i].centerX = 1000;
//				imposterWin();
//			}
//			else if(inShields && ammoDrops[i].getRoom() == Rooms.Shields)
//			{
//				ammoDrops[i].draw();
//				if(dist(playerX, playerY, ammoDrops[i].getX(), ammoDrops[i].getY()) <= 130 && ! inStart)
//					ammoDrops[i].centerX = 1000;
//				imposterWin();
//			}
//			else if(inBottomRightHallway && ammoDrops[i].getRoom() == Rooms.BottomRightHallway)
//			{
//				ammoDrops[i].draw();
//				if(dist(playerX, playerY, ammoDrops[i].getX(), ammoDrops[i].getY()) <= 130 && ! inStart)
//					ammoDrops[i].centerX = 1000;
//				imposterWin();
//			}
//			else if(inComms && ammoDrops[i].getRoom() == Rooms.Comms)
//			{
//				ammoDrops[i].draw();
//				if(dist(playerX, playerY, ammoDrops[i].getX(), ammoDrops[i].getY()) <= 130 && ! inStart)
//					ammoDrops[i].centerX = 1000;
//				imposterWin();
//			}
//			else if(inBottomLeftHallway && ammoDrops[i].getRoom() == Rooms.BottomLeftHallway)
//			{
//				ammoDrops[i].draw();
//				if(dist(playerX, playerY, ammoDrops[i].getX(), ammoDrops[i].getY()) <= 130 && ! inStart)
//					ammoDrops[i].centerX = 1000;
//				imposterWin();
//			}
//			else if(inElectrical && ammoDrops[i].getRoom() == Rooms.Electrical)
//			{
//				ammoDrops[i].draw();
//				if(dist(playerX, playerY, ammoDrops[i].getX(), ammoDrops[i].getY()) <= 130 && ! inStart)
//					ammoDrops[i].centerX = 1000;
//				imposterWin();
//			}
//			else if(inLowerEngine && ammoDrops[i].getRoom() == Rooms.LowerEngine)
//			{
//				ammoDrops[i].draw();
//				if(dist(playerX, playerY, ammoDrops[i].getX(), ammoDrops[i].getY()) <= 130 && ! inStart)
//					ammoDrops[i].centerX = 1000;
//				imposterWin();
//			}
//			else if(inUpperEngine && ammoDrops[i].getRoom() == Rooms.UpperEngine)
//			{
//				ammoDrops[i].draw();
//				if(dist(playerX, playerY, ammoDrops[i].getX(), ammoDrops[i].getY()) <= 130 && ! inStart)
//					ammoDrops[i].centerX = 1000;
//				imposterWin();
//			}
//			else if(inLeftHallway && ammoDrops[i].getRoom() == Rooms.LeftHallway) 
//			{
//				ammoDrops[i].draw();
//				if(dist(playerX, playerY, ammoDrops[i].getX(), ammoDrops[i].getY()) <= 130 && ! inStart)
//					ammoDrops[i].centerX = 1000;
//				imposterWin();
//			}
//			else if(inReactor && ammoDrops[i].getRoom() == Rooms.Reactor)
//			{
//				ammoDrops[i].draw();
//				if(dist(playerX, playerY, ammoDrops[i].getX(), ammoDrops[i].getY()) <= 130 && ! inStart)
//					ammoDrops[i].centerX = 1000;
//				imposterWin();
//			}
//			else if(inSecurity && ammoDrops[i].getRoom() == Rooms.Security)
//			{
//				ammoDrops[i].draw();
//				if(dist(playerX, playerY, ammoDrops[i].getX(), ammoDrops[i].getY()) <= 130 && ! inStart)
//					ammoDrops[i].centerX = 1000;
//				imposterWin();
//			}
		}
		
		
	}
	
//	public void imposterWin()
//	{
//		if(ammoDrops[0].getX() == 1000 && ammoDrops[1].getX() == 1000 && ammoDrops[2].getX() == 1000 && ammoDrops[3].getX() == 1000 && ammoDrops[4].getX() == 1000 && ammoDrops[5].getX() == 1000 && ammoDrops[6].getX() == 1000
//		&& ammoDrops[7].getX() == 1000 && ammoDrops[8].getX() == 1000 && ammoDrops[9].getX() == 1000)
//		{
//			startTime = millis();
//			elapsedTime = 0;
//			inStart = false;
//			inCaf = false;
//			inCenterHallway = false;
//			inUpperLeftHallway = false;
//			inWeapons = false;
//			inMedbay = false;
//			inUpperEngine = false;
//			inAdmin = false;
//			inStorage = false;
//			inRightHallway = false;
//			inOxygen = false;
//			inNavigation = false;
//			inShields = false;
//			inBottomRightHallway = false;
//			inComms = false;
//			inBottomLeftHallway = false;
//			inElectrical = false;
//			inLowerEngine = false;
//			inLeftHallway = false;
//			inReactor = false;
//			inSecurity = false;
//			inWinRoom = true;
//		}
//	}
	
	public void keyPressed()
	{
		switch (key) {
		case 'w':
			player.setvY(-5);
			break;
		case 'a':
			player.setvX(-5);
			break;
		case 's':
			player.setvY(5);
			break;
		case 'd':
			player.setvX(5);
			break;
		}
	}
	
	public void keyReleased()
	{
		switch (key) {
		case 'w':
			player.setvY(0);
			break;
		case 'a':
			player.setvX(0);
			break;
		case 's':
			player.setvY(0);
			break;
		case 'd':
			player.setvX(0);
			break;
		}
	}
	
	public void mouseClicked()
	{
		if(inStart)
		{
			if (inRect(mouseX, mouseY, 20, 40, 70, 70))
			{
				player.setColor(247, 37, 37);
			}
			
			if (inRect(mouseX, mouseY, 170, 40, 70, 70))
			{
				player.setColor(28, 45, 232);
			}
			
			if (inRect(mouseX, mouseY, 320, 40, 70, 70))
			{
				player.setColor(53, 92, 54);
			}
			
			if (inRect(mouseX, mouseY, 470, 40, 70, 70))
			{
				player.setColor(240, 101, 228);
			}
			
			if (inRect(mouseX, mouseY, 620, 40, 70, 70))
			{
				player.setColor(242, 168, 7);
			}
			
			if (inRect(mouseX, mouseY, 20, 270, 70, 70))
			{
				player.setColor(5, 240, 44);
			}
			
			if (inRect(mouseX, mouseY, 20, 480, 70, 70))
			{
				player.setColor(139, 40, 209);
			}
			
			if (inRect(mouseX, mouseY, 620, 270, 70, 70))
			{
				player.setColor(110, 82, 38);
			}
			
			if (inRect(mouseX, mouseY, 620, 480, 70, 70))
			{
				player.setColor(65, 240, 237);
			}
			
			if (inRect(mouseX, mouseY, 170, 480, 70, 70))
			{
				player.setColor(242, 250, 10);
			}
			
			if (inRect(mouseX, mouseY, 470, 480, 70, 70))
			{
				player.setColor(41, 41, 38);
			}
		}
		else
		{
			if (player.getAmmo() != 0)
			{
				bullets.add(new Bullet(player.getX() + 165 * cos(player.getRotation()), player.getY() + 165 * sin(player.getRotation()), player.getRColor(), 
						player.getGColor(), player.getBColor(), player.getRotation(), this));
				
				player.setAmmo(player.getAmmo() - 1);
			}
		}
	}
	
	public boolean inRect(float x, float y, float rX, float rY, float w, float h)
	{
		return x >= rX && x <= rX + w && y >= rY && y <= rY + h;
	}
	
	public void drawStartScreen()
	{
		background(0, 0, 0);
		noStroke();
		fill(255);
		for (int i = 0; i < 50; i++)
		{
			float randX = random(700);
			float randY = random(700);
			ellipse(randX, randY, 5, 5);
		}
		
		player.draw();
		
		fill(247, 37, 37); //red
		rect(20, 40, 70, 70);
		fill(28, 45, 232); //blue
		rect(170, 40, 70, 70);
		fill(53, 92, 54); //green
		rect(320, 40, 70, 70);
		fill(240, 101, 228); //pink
		rect(470, 40, 70, 70);
		fill(242, 168, 7); //orange
		rect(620, 40, 70, 70);
		fill(5, 240, 44); //lime
		rect(20, 270, 70, 70);
		fill(139, 40, 209); //purple
		rect(20, 480, 70, 70);
		fill(110, 82, 38); //brown
		rect(620, 270, 70, 70);
		fill(65, 240, 237); //cyan
		rect(620, 480, 70, 70);
		fill(242, 250, 10); //yellow
		rect(170, 480, 70, 70);
		fill(41, 41, 38); //black
		rect(470, 480, 70, 70);
		
		fill(128);
		rect(100, 600, 200, 75);
		rect(400, 600, 200, 75);
		fill(255);
		textSize(30);
		text("Host Game", 120, 650);
		text("Join Game", 520, 650);
	}
	
	public void drawHUD()
	{
		fill(player.getRColor(), player.getGColor(), player.getBColor());
		rect(0, 600, 700, 100);
		fill(255);
		textSize(30);
		text("Kills: " + player.getKills(), 20, 640);
		text("Health: " + player.getHealth(), 250, 640);
		text("Ammo: " + player.getAmmo(), 460, 640);
	}
	
	public void rotatePlayer() 
	{
		player.setRotation(atan2(mouseY - player.getY(), mouseX - player.getX()));
	}
}
