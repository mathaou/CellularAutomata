package cellularAutomata;

public class Cell {
	int state = 0;
	public Cell(int state){
		this.state = state;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}
