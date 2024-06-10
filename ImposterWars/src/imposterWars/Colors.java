package imposterWars;

public enum Colors
{

	Red(247, 37, 37, "assets/realred.png"),
	Blue(28, 45, 232, "assets/realblue.png"),
	Green(53, 92, 54, "assets/realgreen.png"),
	Yellow(242, 250, 10, "assets/realyellow.png"),
	Orange(242, 168, 7, "assets/realorange.png"), 
	Lime(5, 240, 44, "assets/reallime.png"),
	Cyan(65, 240, 237, "assets/realcyan.png"), 
	Purple(139, 40, 209, "assets/realpurple.png"), 
	Pink(240, 101, 228, "assets/realpink.png"),
	Brown(110, 82, 38, "assets/realbrown.png"),
	Black(41, 41, 38, "assets/realblack.png");
	
	private Colors(int r, int g, int b, String file) 
	{
		this.file = file;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	private String file;
	private int r, g, b;
	
	public String getFile() 
	{
		return file;
	}
	
	public int getR()
	{
		return r;
	}
	
	public int getG() 
	{
		return g;
	}
	
	public int getB() 
	{
		return b;
	}
	
	public static Colors getByRGB(int r, int g, int b)
	{
		if (r == 247 && g == 37 && b == 37)
			return Red;
		else if (r == 28 && g == 45 && b == 232)
			return Blue;
		else if (r == 53 && g == 92 && b == 54)
			return Green;
		else if (r == 242 && g == 250 && b == 10)
			return Yellow;
		else if (r == 242 && g == 168 && b == 7)
			return Orange;
		else if (r == 5 && g == 240 && b == 44)
			return Lime;
		else if (r == 65 && g == 240 && b == 237)
			return Cyan; 
		else if (r == 139 && g == 40 && b ==209)
			return Purple;
		else if (r == 240 && g == 101 && b == 228)
			return Pink;
		else if (r == 110 && g == 82 && b == 38)
			return Brown;
		else if (r == 41 && g == 41 && b == 38)
			return Black;
		throw new IllegalArgumentException("Invalid color");
	}
	
}
