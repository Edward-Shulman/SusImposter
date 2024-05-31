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
	
	private Colors(int r, int g, int b, String file) {
		this.file = file;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	private String file;
	private int r, g, b;
	
	public String getFile() {
		return file;
	}
	public int getR() {
		return r;
	}
	public int getG() {
		return g;
	}
	public int getB() {
		return b;
	}
	
}
