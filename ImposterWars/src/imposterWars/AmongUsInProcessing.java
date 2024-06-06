package imposterWars;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.net.InetSocketAddress;
import java.net.SocketException;

import processing.core.PApplet;

public class AmongUsInProcessing extends PApplet
{
	
	private int playerX, playerY, centerX, centerY, score, playerRed, playerGreen, playerBlue, startTime, elapsedTime;
	private boolean keyRight, keyLeft, keyUp, keyDown;
	private boolean inStart, inCaf, inCenterHallway, inUpperLeftHallway, inWeapons, inMedbay, inAdmin, inStorage, inRightHallway, inOxygen, inNavigation, inShields, inBottomRightHallway;
	private boolean inComms, inBottomLeftHallway, inElectrical, inUpperEngine, inLowerEngine, inLeftHallway, inReactor, inSecurity, inEmptyRoom, inWinRoom, inHost, inJoin;
	AmmoDrop[] ammoDrops;
	PlayerClient player;
	ArrayList<Bullet> bullets;
	static GameState state;
	private String ip;
	private ClientThread client;
	private ServerThread server;
	
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
		player = new PlayerClient(Colors.Red, this);
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
		elapsedTime = 0;
		bullets = new ArrayList<>();
		ammoDrops = new AmmoDrop[10];
		for (int i = 0; i < 10; i++)
		{
			ammoDrops[i] = new AmmoDrop(this, Rooms.values()[(int) random(0, 20)]);
		}
		inHost = false;
		inJoin = false;
		ip = "";
	}
	
	public void draw()
	{
		
		if (inStart)
			drawStartScreen();
		
		
		
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

		if (inHost) 
		{
			drawHostScreen();
		}
		else if (inJoin) 
		{
			drawJoinScreen();
		}
		else if (!inStart)
		{
			if(isColliding())
			{
				state.getCurrentPlayer().setX(400);
				state.getCurrentPlayer().setY(400);
			}
			switch (state.getCurrentPlayer().getRoom()) 
			{
			case Admin:
				drawAdmin();
				break;
			case BottomLeftHallway:
				drawBottomLeftHall();
				break;
			case BottomRightHallway:
				drawBottomRightHall();
				break;
			case Caf:
				drawCaf();
				break;
			case CenterHallway:
				drawCenterHall();
				break;
			case Comms:
				drawComms();
				break;
			case Electrical:
				drawElectrical();
				break;
			case LeftHallway:
				drawLeftHall();
				break;
			case LowerEngine:
				drawLowerEngine();
				break;
			case Medbay:
				drawMedbay();
				break;
			case Navigation:
				drawNav();
				break;
			case Oxygen:
				drawO2();
				break;
			case Reactor:
				drawReactor();
				break;
			case RightHallway:
				drawRightHall();
				break;
			case Security:
				drawSecurity();
				break;
			case Shields:
				drawShields();
				break;
			case Storage:
				drawStorage();
				break;
			case UpperEngine:
				drawUpperEngine();
				break;
			case UpperLeftHallway:
				drawUpperLeftHall();
				break;
			case Weapons:
				drawWeapons();
				break;
			default:
				break;
			}
			
			for (int i = 0; i < state.getPlayerCount(); i++) {
				PlayerClient p = state.getPlayer(i);
				if (i != state.getCurrentPlayerIndex() && p.getRoom().equals(state.getCurrentPlayer().getRoom())) {
					p.draw();
					p.move();
				}
			}
			
			int i = 0;
			while (i < state.getAmmoDrops().size()) {
				AmmoDrop ad = state.getAmmoDrops().get(i);
				if (state.getCurrentPlayer().getRoom().equals(ad.getRoom())) 
				{
					ad.draw();
					if (dist(ad.getX(), ad.getY(), state.getCurrentPlayer().getX(), state.getCurrentPlayer().getY()) < 130)
						state.pickUpAmmoDrop(state.getCurrentPlayerIndex(), i);
					else
						i++;
				}
				else
					i++;
			}
//			player.draw();
			state.getCurrentPlayer().draw();
			drawHUD();
//			player.move();
			state.getCurrentPlayer().move();
			rotatePlayer();
			
			i = 0;
//			while (i < bullets.size())
//			{
//				Bullet b = bullets.get(i);
//				b.draw();
//				if (b.move()) 
//				{
//					i++;
//				}
//				else
//				{
//					bullets.remove(i);
//				}
//			}
			
			while (i < state.getBullets().size()) 
			{
				Bullet b = state.getBullets().get(i);
				if (b.getRoom().equals(state.getCurrentPlayer().getRoom()))
				{
					b.draw();
					b.move();
				}
				i++;
			}
		}
	}

	private void drawReactor() 
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

	private void drawSecurity()
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

	private void drawUpperEngine() 
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

	private void drawLowerEngine() 
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

	private void drawElectrical() 
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

	private void drawLeftHall() 
	{
		background(133, 113, 113);
		fill(184, 82, 191);
		textSize(30);
		text("You are in the left hallway", 200, 40);
	}

	private void drawBottomLeftHall() 
	{
		background(227, 225, 218);
		fill(184, 82, 191);
		textSize(20);
		text("You are in the bottom left hallway", 200, 40);
	}

	private void drawStorage() 
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

	private void drawComms() 
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

	private void drawBottomRightHall() 
	{
		background(227, 225, 218);
		fill(184, 82, 191);
		textSize(20);
		text("You are in the bottom right hallway", 200, 40);
	}

	private void drawShields() 
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

	private void drawNav() 
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

	private void drawO2() 
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

	private void drawMedbay() 
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

	private void drawAdmin() 
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

	private void drawRightHall() 
	{
		background(227, 225, 218);
		fill(184, 82, 191);
		textSize(30);
		text("You are in the right hallway", 200, 40);
	}

	private void drawCenterHall() 
	{
		background(227, 225, 218);
		fill(184, 82, 191);
		textSize(30);
		text("You are in the center hallway", 200, 40);
	}

	private void drawUpperLeftHall() 
	{
		background(227, 225, 218);
		fill(184, 82, 191);
		textSize(30);
		text("You are in the top left hallway", 200, 40);
	}

	private void drawWeapons() 
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

	private void drawCaf() 
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
	}
	
	
	public boolean isColliding()
	{
		//TODO networking with rooms
		PlayerClient p = state.getCurrentPlayer();
		if(p.getY() > 535 && p.getRoom().equals(Rooms.Caf))
		{
			inCaf = false;
			inCenterHallway = true;
			p.setRoom(Rooms.CenterHallway);
			return true;
		}
		else if(p.getX() < 65 && p.getRoom().equals(Rooms.Caf))
		{
			inCaf = false;
			inUpperLeftHallway = true;
			p.setRoom(Rooms.UpperLeftHallway);
			return true;
		}
		else if(p.getY() < 65 && p.getRoom().equals(Rooms.Caf))
		{
			return true;
		}
		else if( p.getX() > 635 && p.getRoom().equals(Rooms.Caf))
		{
			inCaf = false;
			inWeapons = true;
			p.setRoom(Rooms.Weapons);
			return true;
		}
		else if(p.getY() < 65 && p.getRoom().equals(Rooms.Weapons))
			return true;
		else if(p.getY() > 535 && p.getRoom().equals(Rooms.Weapons))
		{
			inWeapons = false;
			inRightHallway = true;
			p.setRoom(Rooms.RightHallway);
			return true;
		}
		else if(p.getX() < 65 && p.getRoom().equals(Rooms.Weapons))
		{
			inCaf = true;
			inWeapons = false;
			p.setRoom(Rooms.Caf);
			return true;
		}
		else if( p.getX() > 635 && p.getRoom().equals(Rooms.Weapons))
			return true;
		else if(p.getY() < 65 && p.getRoom().equals(Rooms.UpperLeftHallway))
			return true;
		else if(p.getY() > 535 && p.getRoom().equals(Rooms.UpperLeftHallway))
		{
			inUpperLeftHallway = false;
			inMedbay = true;
			p.setRoom(Rooms.Medbay);
			return true;
		}
		else if(p.getX() < 65 && p.getRoom().equals(Rooms.UpperLeftHallway))
		{
			inUpperLeftHallway = false;
			inUpperEngine = true;
			p.setRoom(Rooms.UpperEngine);
			return true;
		}
		else if( p.getX() > 635 && p.getRoom().equals(Rooms.UpperLeftHallway))
		{
			inCaf = true;
			inUpperLeftHallway = false;
			p.setRoom(Rooms.Caf);
			return true;
		}
		else if(p.getY() < 65 && p.getRoom().equals(Rooms.CenterHallway))
		{
			inCaf = true;
			inCenterHallway = false;
			p.setRoom(Rooms.Caf);
			return true;
		}
		else if(p.getY() > 535 && p.getRoom().equals(Rooms.CenterHallway))
		{
			inCenterHallway = false;
			inStorage = true;
			p.setRoom(Rooms.Storage);
			return true;
		}
		else if(p.getX() < 65 && p.getRoom().equals(Rooms.CenterHallway))
			return true;
		else if( p.getX() > 635 && p.getRoom().equals(Rooms.CenterHallway))
		{
			inCenterHallway = false;
			inAdmin = true;
			p.setRoom(Rooms.Admin);
			return true;
		}
		else if(p.getY() < 65 && p.getRoom().equals(Rooms.RightHallway))
		{
			inRightHallway = false;
			inWeapons = true;
			p.setRoom(Rooms.Weapons);
			return true;
		}
		else if(p.getY() > 535 && p.getRoom().equals(Rooms.RightHallway))
		{
			inRightHallway = false;
			inShields = true;
			p.setRoom(Rooms.Shields);
			return true;
		}
		else if(p.getX() < 65 && p.getRoom().equals(Rooms.RightHallway))
		{
			inRightHallway = false;
			inOxygen = true;
			p.setRoom(Rooms.Oxygen);
			return true;
		}
		else if( p.getX() > 635 && p.getRoom().equals(Rooms.RightHallway))
		{
			inRightHallway = false;
			inNavigation = true;
			p.setRoom(Rooms.Navigation);
			return true;
		}
		else if(p.getY() < 65 && p.getRoom().equals(Rooms.Admin))
			return true;
		else if(p.getY() > 535 && p.getRoom().equals(Rooms.Admin))
			return true;
		else if(p.getX() < 65 && p.getRoom().equals(Rooms.Admin))
		{
			inAdmin = false;
			inCenterHallway = true;
			p.setRoom(Rooms.CenterHallway);
			return true;
		}
		else if( p.getX() > 635 && p.getRoom().equals(Rooms.Admin))
			return true;
		else if(p.getY() < 65 && p.getRoom().equals(Rooms.Medbay))
		{
			inMedbay = false;
			inUpperLeftHallway = true;
			p.setRoom(Rooms.UpperLeftHallway);
			return true;
		}
		else if(p.getY() > 535 && p.getRoom().equals(Rooms.Medbay))
			return true;
		else if(p.getX() < 65 && p.getRoom().equals(Rooms.Medbay))
			return true;
		else if( p.getX() > 635 && p.getRoom().equals(Rooms.Medbay))
			return true;
		else if(p.getY() < 65 && p.getRoom().equals(Rooms.Oxygen))
			return true;
		else if(p.getY() > 535 && p.getRoom().equals(Rooms.Oxygen))
			return true;
		else if(p.getX() < 65 && p.getRoom().equals(Rooms.Oxygen))
			return true;
		else if( p.getX() > 635 && p.getRoom().equals(Rooms.Oxygen))
		{
			inOxygen = false;
			inRightHallway = true;
			p.setRoom(Rooms.RightHallway);
			return true;
		}
		else if(p.getY() < 65 && p.getRoom().equals(Rooms.Navigation))
			return true;
		else if(p.getY() > 535 && p.getRoom().equals(Rooms.Navigation))
			return true;
		else if(p.getX() < 65 && p.getRoom().equals(Rooms.Navigation))
		{
			inNavigation = false;
			inRightHallway = true;
			p.setRoom(Rooms.RightHallway);
			return true;
		}
		else if( p.getX() > 635 && p.getRoom().equals(Rooms.Navigation))
			return true;
		else if(p.getY() < 65 && p.getRoom().equals(Rooms.Shields))
		{
			inShields = false;
			inRightHallway = true;
			p.setRoom(Rooms.RightHallway);
			return true;
		}
		else if(p.getY() > 535 && p.getRoom().equals(Rooms.Shields))
			return true;
		else if(p.getX() < 65 && p.getRoom().equals(Rooms.Shields))
		{
			inShields = false;
			inBottomRightHallway = true;
			p.setRoom(Rooms.BottomRightHallway);
			return true;
		}
		else if( p.getX() > 635 && p.getRoom().equals(Rooms.Shields))
			return true;
		else if(p.getY() < 65 && p.getRoom().equals(Rooms.BottomRightHallway))
			return true;
		else if(p.getY() > 535 && p.getRoom().equals(Rooms.BottomRightHallway))
		{
			inBottomRightHallway = false;
			inComms = true;
			p.setRoom(Rooms.Comms);
			return true;
		}
		else if(p.getX() < 65 && p.getRoom().equals(Rooms.BottomRightHallway))
		{
			inBottomRightHallway = false;
			inStorage = true;
			p.setRoom(Rooms.Storage);
			return true;
		}
		else if( p.getX() > 635 && p.getRoom().equals(Rooms.BottomRightHallway))
		{
			inBottomRightHallway = false;
			inShields = true;
			p.setRoom(Rooms.Shields);
			return true;
		}
		else if(p.getY() < 65 && p.getRoom().equals(Rooms.Comms))
		{
			inComms = false;
			inBottomRightHallway = true;
			p.setRoom(Rooms.BottomRightHallway);
			return true;
		}
		else if(p.getY() > 535 && p.getRoom().equals(Rooms.Comms))
			return true;
		else if(p.getX() < 65 && p.getRoom().equals(Rooms.Comms))
			return true;
		else if( p.getX() > 635 && p.getRoom().equals(Rooms.Comms))
			return true;
		else if(p.getY() < 65 && p.getRoom().equals(Rooms.Storage))
		{
			inStorage = false;
			inCenterHallway = true;
			p.setRoom(Rooms.CenterHallway);
			return true;
		}
		else if(p.getY() > 535 && p.getRoom().equals(Rooms.Storage))
			return true;
		else if(p.getX() < 65 && p.getRoom().equals(Rooms.Storage))
		{
			inStorage = false;
			inBottomLeftHallway = true;
			p.setRoom(Rooms.BottomLeftHallway);
			return true;
		}
		else if(p.getX() > 635 && p.getRoom().equals(Rooms.Storage))
		{
			inStorage = false;
			inBottomRightHallway = true;
			p.setRoom(Rooms.BottomRightHallway);
			return true;
		}
		else if(p.getY() < 65 && p.getRoom().equals(Rooms.BottomLeftHallway))
		{
			inBottomLeftHallway = false;
			inElectrical = true;
			p.setRoom(Rooms.Electrical);
			return true;
		}
		else if(p.getY() > 535 && p.getRoom().equals(Rooms.BottomLeftHallway))
			return true;
		else if(p.getX() < 65 && p.getRoom().equals(Rooms.BottomLeftHallway))
		{
			inBottomLeftHallway = false;
			inLowerEngine = true;
			p.setRoom(Rooms.LowerEngine);
			return true;
		}
		else if(p.getX() > 635 && p.getRoom().equals(Rooms.BottomLeftHallway))
		{
			inBottomLeftHallway = false;
			inStorage = true;
			p.setRoom(Rooms.Storage);
			return true;
		}
		else if(p.getY() < 65 && p.getRoom().equals(Rooms.Electrical))
			return true;
		else if(p.getY() > 535 && p.getRoom().equals(Rooms.Electrical))
		{
			inElectrical = false;
			inBottomLeftHallway = true;
			p.setRoom(Rooms.BottomLeftHallway);
			return true;
		}
		else if(p.getX() < 65 && p.getRoom().equals(Rooms.Electrical))
			return true;
		else if(p.getX() > 635 && p.getRoom().equals(Rooms.Electrical))
			return true;
		else if(p.getY() < 65 && p.getRoom().equals(Rooms.LowerEngine))
		{
			inLowerEngine = false;
			inLeftHallway = true;
			p.setRoom(Rooms.LeftHallway);
			return true;
		}
		else if(p.getY() > 535 && p.getRoom().equals(Rooms.LowerEngine))
			return true;
		else if(p.getX() < 65 && p.getRoom().equals(Rooms.LowerEngine))
			return true;
		else if(p.getX() > 635 && p.getRoom().equals(Rooms.LowerEngine))
		{
			inLowerEngine = false;
			inBottomLeftHallway = true;
			p.setRoom(Rooms.BottomLeftHallway);
			return true;
		}
		else if(p.getY() < 65 && p.getRoom().equals(Rooms.LeftHallway))
		{
			inLeftHallway = false;
			inUpperEngine = true;
			p.setRoom(Rooms.UpperEngine);
			return true;
		}
		else if(p.getY() > 535 && p.getRoom().equals(Rooms.LeftHallway))
		{
			inLeftHallway = false;
			inLowerEngine = true;
			p.setRoom(Rooms.LowerEngine);
			return true;
		}
		else if(p.getX() < 65 && p.getRoom().equals(Rooms.LeftHallway))
		{
			inLeftHallway = false;
			inReactor = true;
			p.setRoom(Rooms.Reactor);
			return true;
		}
		else if( p.getX() > 635 && p.getRoom().equals(Rooms.LeftHallway))
		{
			inLeftHallway = false;
			inSecurity = true;
			p.setRoom(Rooms.Security);
			return true;
		}
		else if(p.getY() < 65 && p.getRoom().equals(Rooms.UpperEngine))
			return true;
		else if(p.getY() > 535 && p.getRoom().equals(Rooms.UpperEngine))
		{
			inUpperEngine = false;
			inLeftHallway = true;
			p.setRoom(Rooms.LeftHallway);
			return true;
		}
		else if(p.getX() < 65 && p.getRoom().equals(Rooms.UpperEngine))
			return true;
		else if(p.getX() > 635 && p.getRoom().equals(Rooms.UpperEngine))
		{
			inUpperEngine = false;
			inUpperLeftHallway = true;
			p.setRoom(Rooms.UpperLeftHallway);
			return true;
		}
		else if(p.getY() < 65 && p.getRoom().equals(Rooms.Security))
			return true;
		else if(p.getY() > 535 && p.getRoom().equals(Rooms.Security))
			return true;
		else if(p.getX() < 65 && p.getRoom().equals(Rooms.Security))
		{
			inSecurity = false;
			inLeftHallway = true;
			p.setRoom(Rooms.LeftHallway);
			return true;
		}
		else if( p.getX() > 635 && p.getRoom().equals(Rooms.Security))
			return true;
		else if(p.getY() < 65 && p.getRoom().equals(Rooms.Reactor))
			return true;
		else if(p.getY() > 535 && p.getRoom().equals(Rooms.Reactor))
			return true;
		else if(p.getX() < 65 && p.getRoom().equals(Rooms.Reactor))
			return true;
		else if(p.getX() > 635 && p.getRoom().equals(Rooms.Reactor))
		{
			inReactor = false;
			inLeftHallway = true;
			p.setRoom(Rooms.LeftHallway);
			return true;
		}
//		else if(p.getY() < 65 && inEmptyRoom)
//			return true;
//		else if(p.getY() > 535 && inEmptyRoom)
//			return true;
//		else if(p.getX() < 65 && inEmptyRoom)
//			return true;
//		else if(p.getX() > 635 && inEmptyRoom)
//			return true;
//		else if(p.getY() < 65 && inWinRoom)
//			return true;
//		else if(p.getY() > 535 && inWinRoom)
//			return true;
//		else if(p.getX() < 65 && inWinRoom)
//			return true;
//		else if(p.getX() > 635 && inWinRoom)
//			return true;
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
		}
	}
	
	public void keyPressed()
	{
		if (client != null) 
		{
			try {
				switch (key) {
				case 'w':
					client.updateMovement(PacketTypes.MOVE_UP);
					break;
				case 'a':
					client.updateMovement(PacketTypes.MOVE_LEFT);
					break;
				case 's':
					client.updateMovement(PacketTypes.MOVE_RIGHT);
					break;
				case 'd':
					client.updateMovement(PacketTypes.MOVE_DOWN);
					break;
				default:
					break;
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (server != null) 
		{
			try {
				switch (key) {
				case 'w':
					server.updateMovement(PacketTypes.MOVE_UP);
					break;
				case 'a':
					server.updateMovement(PacketTypes.MOVE_LEFT);
					break;
				case 's':
					server.updateMovement(PacketTypes.MOVE_DOWN);
					break;
				case 'd':
					server.updateMovement(PacketTypes.MOVE_RIGHT);
					break;
				default:
					break;
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void keyReleased()
	{
		if (client != null) 
		{
			try {
				switch (key) {
				case 'w':
					client.updateMovement(PacketTypes.END_MOVE_Y);
					break;
				case 'a':
					client.updateMovement(PacketTypes.END_MOVE_X);
					break;
				case 's':
					client.updateMovement(PacketTypes.END_MOVE_Y);
					break;
				case 'd':
					client.updateMovement(PacketTypes.END_MOVE_X);
					break;
				default:
					break;
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (server != null) 
		{
			try {
				switch (key) {
				case 'w':
					server.updateMovement(PacketTypes.END_MOVE_Y);
					break;
				case 'a':
					server.updateMovement(PacketTypes.END_MOVE_X);
					break;
				case 's':
					server.updateMovement(PacketTypes.END_MOVE_Y);
					break;
				case 'd':
					server.updateMovement(PacketTypes.END_MOVE_X);
					break;
				default:
					break;
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void mouseClicked()
	{
		if(inStart)
		{
			if(inRect(mouseX, mouseY, 25, 600, 150, 75)) //host
			{
				inHost = true;
				inStart = false;
				try
				{
					ByteBuffer buf = ByteBuffer.wrap(Inet4Address.getLocalHost().getAddress());
					ip = Integer.toHexString(buf.getInt());
				} 
				catch (UnknownHostException e)
				{
					e.printStackTrace();
				}
			}
			if(inRect(mouseX, mouseY, 275, 600, 150, 75)) //join
			{
				inJoin = true;
				inStart = false;
			}
			
			if (inRect(mouseX, mouseY, 20, 40, 70, 70))
			{
				player.setColor(Colors.Red);
			}
			
			if (inRect(mouseX, mouseY, 170, 40, 70, 70))
			{
				player.setColor(Colors.Blue);
			}
			
			if (inRect(mouseX, mouseY, 320, 40, 70, 70))
			{
				player.setColor(Colors.Green);
			}
			
			if (inRect(mouseX, mouseY, 470, 40, 70, 70))
			{
				player.setColor(Colors.Pink);
			}
			
			if (inRect(mouseX, mouseY, 620, 40, 70, 70))
			{
				player.setColor(Colors.Orange);
			}
			
			if (inRect(mouseX, mouseY, 20, 270, 70, 70))
			{
				player.setColor(Colors.Lime);
			}
			
			if (inRect(mouseX, mouseY, 20, 480, 70, 70))
			{
				player.setColor(Colors.Purple);
			}
			
			if (inRect(mouseX, mouseY, 620, 270, 70, 70))
			{
				player.setColor(Colors.Brown);
			}
			
			if (inRect(mouseX, mouseY, 620, 480, 70, 70))
			{
				player.setColor(Colors.Cyan);
			}
			
			if (inRect(mouseX, mouseY, 170, 480, 70, 70))
			{
				player.setColor(Colors.Yellow);
			}
			
			if (inRect(mouseX, mouseY, 470, 480, 70, 70))
			{
				player.setColor(Colors.Black);
			}
		}
		else if (inJoin || inHost) 
		{
			if (inRect(mouseX, mouseY, 10, 10, 100, 50)) 
			{
				inJoin = false;
				inHost = false;
				inStart = true;
				ip = "";
			}
			if (inRect(mouseX, mouseY, 250, 550, 200, 100))
			{
				if (inJoin)
				{
					try 
					{
						ByteBuffer buf = ByteBuffer.allocate(4);
						buf.putInt((int) Long.parseLong(ip, 16));
						state = new GameState(0, this);
						state.addPlayer(player);
						client = new ClientThread(420, InetAddress.getByAddress(buf.array()));
						client.start();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					inJoin = false;
				}
				else 
				{
					try {
						state = new GameState(0, this);
						state.addPlayer(player);
						for (int i = 0; i < 10; i++) {
							state.addAmmoDrop(new AmmoDrop(this, Rooms.values()[(int) random(20)]));
						}
						server = new ServerThread(420);
						server.start();
					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					inHost = false;
				}
			}
		}
		else
		{
			if (state.getCurrentPlayer().getAmmo() != 0)
			{
//				bullets.add(new Bullet(player.getX() + 165 * cos(player.getRotation()), player.getY() + 165 * sin(player.getRotation()), player.getRColor(), 
//						player.getGColor(), player.getBColor(), player.getRotation(), 0, this));
//				
//				player.setAmmo(player.getAmmo() - 1);
				try 
				{
					if (client != null)
						client.shoot();
					else
						server.shot(state.getCurrentPlayerIndex());
				}
				catch (IOException e) {
					e.printStackTrace();
				}
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
		for (int i = 0; i < 30; i++)
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
		rect(25, 600, 150, 75);
		rect(275, 600, 150, 75);
		rect(525, 600, 150, 75);
		fill(255);
		textSize(25);
		text("Host Game", 35, 650);
		text("Join Game", 290, 650);
		text("Practice", 552, 650);
	}
	
	public void drawHUD()
	{
		PlayerClient p = state.getCurrentPlayer();
		fill(p.getRColor(), p.getGColor(), p.getBColor());
		rect(0, 600, 700, 100);
		fill(255);
		textSize(30);
		text("Kills: " + p.getKills(), 20, 640);
		text("Health: " + p.getHealth(), 250, 640);
		text("Ammo: " + p.getAmmo(), 460, 640);
	}
	
	public void rotatePlayer() 
	{
		state.getCurrentPlayer().setRotation(atan2(mouseY - player.getY(), mouseX - player.getX()));
	}
	
	public void mouseMoved() {
		if (client != null)
		{
			try {
				client.updateRotataion(state.getCurrentPlayer().getRotation());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (server != null)
		{
			try {
				server.updateRotation(state.getCurrentPlayerIndex(), state.getCurrentPlayer().getRotation());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void drawHostScreen()
	{
		background(0, 0, 0);
		noStroke();
		fill(255);
		for (int i = 0; i < 15; i++)
		{
			float randX = random(700);
			float randY = random(700);
			ellipse(randX, randY, 5, 5);
		}
		
		fill(128);
		rect(100, 120, 500, 300);
		textSize(68);
		fill(240);
		text("(JOIN CODE) \n" + ip, 150, 220);
		
		fill(59, 212, 95);
		rect(250, 550, 200, 100);
		textSize(50);
		fill(240);
		text("Start", 294, 615);
		
		fill(128);
		rect(10, 10, 100, 50);
		textSize(40);
		fill(240);
		text("<-----", 16, 47);
	}
	
	public void drawJoinScreen()
	{
		background(0, 0, 0);
		noStroke();
		fill(255);
		for (int i = 0; i < 15; i++)
		{
			float randX = random(700);
			float randY = random(700);
			ellipse(randX, randY, 5, 5);
		}
		
		fill(128);
		rect(100, 120, 500, 300);
		textSize(60);
		fill(240);
		text("(ENTER CODE) \n" + ip, 136, 220);
		
		fill(59, 212, 95);
		rect(250, 550, 200, 100);
		textSize(50);
		fill(240);
		text("Join", 310, 615);
		
		fill(128);
		rect(10, 10, 100, 50);
		textSize(40);
		fill(240);
		text("<-----", 16, 47);
		
		
	}
	
	public void keyTyped()
	{
		if (inJoin)
		{
			if (key == BACKSPACE)
			{
				if (!ip.isEmpty())
					ip = ip.substring(0, ip.length() - 1);
			}
			else if (ip.length() >= 0 && ip.length() < 8)
				ip += key;
		}
	}
	
}
