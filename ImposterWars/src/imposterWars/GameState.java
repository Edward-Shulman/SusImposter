package imposterWars;

import java.util.Vector;

import processing.core.PApplet;

public class GameState
{
	private Vector<PlayerClient> players;
	private Vector<Bullet> bullets;
	private Vector<AmmoDrop> ammoDrops;
	private int currentPlayerIndex;
	private PApplet a;
	
	public GameState(int currentPlayerIndex, PApplet a)
	{
		players = new Vector<>();
		bullets = new Vector<>();
		ammoDrops = new Vector<>();
		this.currentPlayerIndex = currentPlayerIndex;
		this.a = a;
	}
	
	public int getPlayerCount() {
		return players.size();
	}
	
	public PApplet getWindow() {
		return a;
	}
	
	public PlayerClient getPlayer(int id)
	{
		return players.get(id);
	}
	
	public int addPlayer(PlayerClient p)
	{
		players.add(p);
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
	
	public void addBullet(int owner) 
	{
		PlayerClient player = players.get(owner);
		bullets.add(new Bullet(player.getX() + 165 * PApplet.cos(player.getRotation()), player.getY() + 165 * PApplet.sin(player.getRotation()), 
				player.getRColor(), player.getGColor(), player.getBColor(), player.getRotation(), a));
	}
	
	public void setPlayer(int id, PlayerClient p) {
		players.set(id, p);
	}
}
