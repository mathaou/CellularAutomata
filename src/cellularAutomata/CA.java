package cellularAutomata;

import java.security.SecureRandom;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class CA extends BasicGame{
	static int width = 640, height = 480, w = 10, columns = width/w, rows = height/w;
	SecureRandom rand = new SecureRandom();
	static Cell[][] board;
	public CA(String title) {
		super(title);
	}
	public void fillCells(){
		board = new Cell[columns][rows];
		for(int i = 0; i < columns;i++){
			for(int j = 0; j < rows; j++){
				board[i][j] = new Cell(rand.nextInt(2));
			}
		}
	}
	public void printMap(Graphics g){
		for(int i = 0; i < columns;i++){
			for(int j = 0; j < rows; j++){
				if(board[i][j].getState()==1){
					g.setColor(Color.white);
					g.fillRect(i * w, j * w, w, w);
				}
				else{
					g.setColor(Color.black);
					g.fillRect(i * w, j * w, w, w);
				}
			}
		}
	}
	
	//everything in this class involves the theory of cellular automata, but now I need to apply this to a relative location of an object performing the tasks that the cellular automata usually did
	//itself in order to make a map that is much more organic and less random. Work on that. 
	
	//a miner that picks a random direction and starts digging out cells that are alive - this means we have to fill the grid with alive cells, not random cells
	
	public void checkNeighbors(){
		Cell[][] next = new Cell[columns][rows];
		for(int i = 0; i < columns;i++){
			for(int j = 0; j < rows; j++){
				next[i][j] = new Cell(rand.nextInt(2));
			}
		}
		for(int i = 1; i < columns - 1; i++){
			for(int j = 1; j < rows - 1; j++){
				int neighbors = 0;
				for(int x = -1; x <= 1; x++){
					for(int y = -1; y <= 1; y++){
						neighbors+=board[i+x][j+y].getState();
					}
				}
				neighbors-=board[i][j].getState();
				
				if      ((board[i][j].getState() == 1) && (neighbors <=  2)) next[i][j].setState(0);
				if(board[i][j].getState() == 0 && (neighbors >= 3)) next[i][j].setState(1);
			    if ((board[i][j].getState() == 1) && (neighbors >  3)) next[i][j].setState(0);
			    if ((board[i][j].getState() == 0) && (neighbors == 3)) next[i][j].setState(1);
			    else next[i][j].setState(board[i][j].getState());
			}
		}
		board = next;
	}
	public void cleanUp(){
		for(int i = 1; i < columns - 1; i++){
			for(int j = 1; j < rows - 1; j++){
				int neighbors = 0;
				for(int x = -1; x <= 1; x++){
					for(int y = -1; y <= 1; y++){
						neighbors+=board[i+x][j+y].getState();
					}
				}
				neighbors-=board[i][j].getState();
				if(board[i][j].getState() == 1 && neighbors <= 3){
					board[i][j].setState(0);
				}
				if (board[i][j].getState() == 0 && neighbors >= 7){
					board[i][j].setState(1);
				}
				neighbors = 0;
				for(int x = -1; x <= 1; x++){
					for(int y = -1; y <= 1; y++){
						neighbors+=board[i+x][j+y].getState();
					}
				}
				if(neighbors == 0){
					for(int x = 0; x <= 1; x++){
						for(int y = 0; y <= 1; y++){
							board[i+x][j+y].setState(0);
						}
					}
				}
				if(neighbors == 1){
					board[i][j].setState(0);	
				}
				if(neighbors >=6){
					board[i][j].setState(1);	
				}
				if(board[i][j].getState()==1&&board[i+1][j].getState()==0&&board[i][j+1].getState()==0&&board[i-1][j].getState()==0&&board[i][j-1].getState()==0){
					board[i][j].setState(0);	
				}
				clearEdges(i,j);
			}
		}
	}
	public void clearEdges(int i, int j){
		board[0][0].setState(0);
		board[0][j].setState(0);
		board[i][0].setState(0);
		board[columns-1][j].setState(0);
		board[i][rows-1].setState(0);
		board[columns-1][rows-1].setState(0);
		board[columns-1][0].setState(0);
		board[0][rows-1].setState(0);
	}
	@Override
	public void init(GameContainer c) throws SlickException {
		fillCells();
		checkNeighbors();
		cleanUp();
		checkNeighbors();
		cleanUp();
	}
	@Override
	public void update(GameContainer c, int d) throws SlickException {
		if(c.getInput().isKeyPressed(Input.KEY_R)){
			fillCells();
			checkNeighbors();
			cleanUp();
			checkNeighbors();
			cleanUp();
		}
	}
	@Override
	public void render(GameContainer c, Graphics g) throws SlickException {
		printMap(g);
		c.setTargetFrameRate(60);
	}
	public static void main(String[]args) throws SlickException{
		AppGameContainer game = new AppGameContainer(new CA("Cellular Automata Practice"));
		game.setDisplayMode(width, height, false);
		game.setAlwaysRender(true);
		game.start();
	}
}
