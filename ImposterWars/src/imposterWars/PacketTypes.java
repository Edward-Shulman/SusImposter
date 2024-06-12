package imposterWars;

public enum PacketTypes 
{

	UPDATE_CONNECTION(0),
	MOVE_UP(1),
	MOVE_DOWN(2),
	MOVE_LEFT(3),
	MOVE_RIGHT(4),
	END_MOVE_X(5),
	END_MOVE_Y(6),
	SHOOT(7),
	UPDATE_ROOM(8),
	UPDATE_PROJECTILES(9),
	UPDATE_AMMO_DROPS(10),
	UPDATE_ROTATION(11),
	REGISTER_HIT(12),
	UPDATE_PLAYER(13),
	UPDATE_PLAYERS(14);
	
	private PacketTypes(int id) 
	{
		this.id = (byte) id;
	}
	byte id;
	
	public byte getID() 
	{
		return id;
	}
}
