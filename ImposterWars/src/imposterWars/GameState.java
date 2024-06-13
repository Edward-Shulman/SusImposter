package imposterWars;

import java.util.Vector;
import java.util.HashMap;
import java.util.UUID;

import processing.core.PApplet;

public class GameState
{
	HashMap<UUID, PlayerClient> players;
	HashMap<UUID, Bullet> bullets;
	Vector<AmmoDrop> ammoDrops;
	UUID currentPlayerIndex;
	private PApplet a;
	
	public GameState(PlayerClient initPlayer, PApplet a)
	{
		players = new HashMap<>();
		bullets = new HashMap<>();
		ammoDrops = new Vector<>();
		this.currentPlayerIndex = UUID.randomUUID();
		players.put(currentPlayerIndex, initPlayer);
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
	
	public PlayerClient getPlayer(UUID id)
	{
		return players.get(id);
	}
	
	public UUID addPlayer(PlayerClient p)
	{
		UUID id = UUID.randomUUID();
		players.put(id, p);
		return id;
	}
	
	public void removePlayer(UUID id) 
	{
		players.remove(id);
	}
	
	public HashMap<UUID, Bullet> getBullets()
	{
		return bullets;
	}
	
	public void removeBullet(UUID i)
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
	
	public void pickUpAmmoDrop(UUID id, int ammoID)
	{
		PlayerClient p = players.get(id);
		p.setAmmo(p.getAmmo() + 15);
		ammoDrops.remove(ammoID);
	}

	public UUID getCurrentPlayerUUID()
	{
		return currentPlayerIndex;
	}
	
	public PlayerClient getCurrentPlayer() 
	{
		return players.get(currentPlayerIndex);
	}
	
	public UUID addBullet(UUID owner) 
	{
		PlayerClient player = players.get(owner);
		UUID bid = UUID.randomUUID();
		bullets.put(bid, new Bullet(player.getX() + 165 * PApplet.cos(player.getRotation()), player.getY() + 165 * PApplet.sin(player.getRotation()), 
				player.getRColor(), player.getGColor(), player.getBColor(), player.getRotation(), owner, player.getRoom(), a));
		return bid;
	}
	
	public void setPlayer(UUID id, PlayerClient p) 
	{
		players.put(id, p);
	}
	
	public void nullBullet(UUID id) {
		bullets.put(id, null);
	}
}
