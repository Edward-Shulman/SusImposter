package imposterWars;

public enum Rooms {

	Caf(0),
	CenterHallway(1), 
	UpperLeftHallway(2),
	Weapons(3), 
	Medbay(4), 
	Admin(5), 
	Storage(6), 
	RightHallway(7), 
	Oxygen(8), 
	Navigation(9), 
	Shields(10), 
	BottomRightHallway(11),
	Comms(12), 
	BottomLeftHallway(13), 
	Electrical(14), 
	UpperEngine(15), 
	LowerEngine(16), 
	LeftHallway(17), 
	Reactor(18), 
	Security(19);
	
	private Rooms(int id) {
		this.id = id;
	}
	
	private int id;
	
	public int getID() {
		return id;
	}
}
