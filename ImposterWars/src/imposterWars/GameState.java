package imposterWars;

import java.util.Vector;
import java.util.Hashtable;
import processing.core.PApplet;

public class GameState
{
	Hashtable<Integer, PlayerClient> players;
	Vector<Bullet> bullets;
	Vector<AmmoDrop> ammoDrops;
	int currentPlayerIndex;
	private PApplet a;
	
	public GameState(int currentPlayerIndex, PApplet a)
	{
		players = new Hashtable<>();
		bullets = new Vector<>();
		ammoDrops = new Vector<>();
		this.currentPlayerIndex = currentPlayerIndex;
		this.a = a;
	}
	
	public int getPlayerCount() 
	{
		return players.size();
	}
	
	public PApplet getWindow()
	{
		return a;
	}
	
	public PlayerClient getPlayer(int id)
	{
		return players.get(id);
	}
	
	public int addPlayer(PlayerClient p)
	{
		players.put(players.size() - 1, p);
		return players.size() - 1;
	}
	
	public void removePlayer(int id) 
	{
		players.remove(id);
	}
	
	public Vector<Bullet> getBullets()
	{
		return bullets;
	}
	
	public void removeBullet(int i)
	{
		bullets.remove(i);
	}
	
	public Vector<AmmoDrop> getAmmoDrops()
	{
		return ammoDrops;
	}
	
	public void addAmmoDrop(AmmoDrop ad)
	{
		ammoDrops.add(ad);
	}
	
	public void pickUpAmmoDrop(int id, int ammoID)
	{
		PlayerClient p = players.get(id);
		p.setAmmo(p.getAmmo() + 15);
		ammoDrops.remove(ammoID);
	}

	public int getCurrentPlayerIndex()
	{
		return currentPlayerIndex;
	}
	
	public PlayerClient getCurrentPlayer() 
	{
		return players.get(currentPlayerIndex);
	}
	
	public void addBullet(int owner) 
	{
		PlayerClient player = players.get(owner);
		bullets.add(new Bullet(player.getX() + 165 * PApplet.cos(player.getRotation()), player.getY() + 165 * PApplet.sin(player.getRotation()), 
				player.getRColor(), player.getGColor(), player.getBColor(), player.getRotation(), owner, player.getRoom(), a));
	}
	
	public void setPlayer(int id, PlayerClient p) 
	{
		players.put(id, p);
	}
}
