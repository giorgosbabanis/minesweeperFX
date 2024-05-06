package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Board {
	
	
	private int size;//
	private Tile[][] state;//size*size array of tiles
	public int marked;//number of marked tiles
	public int bombs;//number of bombs
	public int[] start = {100,0,0};//used to find a tile with no neighboring bombs
	public boolean gameover;
	private int revealed;//number of the tiles that have been revealed
	private int clicks;//counter of successful left clicks
	public int time;
	
	/**
	 * Constructor of a new board, it automatically creates the tiles and randomly allocates the bombs
	 * on them, it initializes the various counters and writes the locations of the bombs to the mines.txt file
	 * @param size the dimension of the board
	 * @param bombs the amount of the bombs to be allocated
	 */
	Board(int size, int bombs){//creates a new board
		this.gameover = false;
		revealed = size*size - bombs;
		this.time = 0;
		this.clicks = 0;
		this.size = size;
		this.marked = 0;
		this.bombs = bombs;
		this.state = new Tile[size][size];
		for(int i = 0; i < size;i++) { //randomly generate bombs
			for(int j = 0; j < size; j++) {
				state[i][j] = new Tile(false,size,i,j);
			}
		}
		int counter = 0;
		Random rand = new Random();
		while(counter < bombs) {
			int xr = rand.nextInt(size);
			int yr = rand.nextInt(size);
				boolean temp = state[xr][yr].bomb;
				if(temp == false) {
					counter++;
					state[xr][yr].bomb = true;
					if(counter == 1 && size == 16) {
						state[xr][yr].superbomb = true;
					}
				}
		}
		
		File file = new File("medialab/mines.txt");
		try{ //writes the location of the mines to the file
			file.createNewFile();
			FileWriter w = new FileWriter("medialab/mines.txt");
			for (int i = 0; i < size; i++) {
				for(int j = 0; j < size; j++) {
					if(state[i][j].bomb == true) {
						int sup = 0;
						if(state[i][j].superbomb == true) {
							sup = 1;
						}
					String temp = Integer.toString(i);
					temp += " ";
					temp += Integer.toString(j);
					temp += " ";
					temp += Integer.toString(sup);
					temp += "\n";
					w.write(temp);
					}
				}
			}
			w.close();
		} catch(IOException e){
			Popup.message("An error has occured, the mines file was not created");
		}
		
		
		for (int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				state[i][j].neighborsnb = neighbors(i,j);
				if(state[i][j].neighborsnb < start[0] && state[i][j].neighborsnb >= 0) {
					start[0] = state[i][j].neighborsnb;
					start[1] = i;
					start[2] = j;
				}
			}
		}
	}
	
	private int neighbors(int x, int y) {
		if(state[x][y].bomb == true) {
			return -1;
		}
		int counter = 0;
		for (int i = x - 1; i < x + 2 ;i++) {
			for (int j = y - 1; j < y + 2 ;j++) {
				if(i > -1 && j > - 1 && i < size && j < size) {
					if(state[i][j].bomb == true) {
						counter++;
					}
				}
			}
		}
		return counter;
	}
	
	/**
	 * Reveals the specified tile, if it has 0 neighbor bombs it recursively reveals all its neighbors. 
	 * If a bomb is revealed the game ends. When a tile is revealed, its text becomes visible, X for a bomb, 
	 * or for a normal tile the number of neighbor bombs. 
	 * @param x coordinate of the tile
	 * @param y coordinate of the tile
	 * @param depth used for the recursion, if it is 0 then this means that the user manually revealed
	 * the tile, so a successful click is registered and the clicks counter is increased
	 */
	public void reveal(int x, int y, int depth) { 
		if(state[x][y].checked == true) {	
			return;
		}
		if(depth == 0) {
			clicks++;
		}
		int res = state[x][y].neighborsnb;
		if (res == -1) {
			state[x][y].checked = true;
			state[x][y].text.setText(state[x][y].getText());
			gameover = true;
			store("Computer");
			Popup.message(" Game over \n");
			return;
		}
		state[x][y].checked = true;
		state[x][y].marked = false;
		if (res == 0) {
			for (int i = x - 1; i < x + 2 ;i++) {
				for (int j = y - 1; j < y + 2 ;j++) {
					if(i > -1 && j > - 1 && i < size && j < size) {
						reveal(i,j, depth + 1);
					}
				}
			}
		}
		state[x][y].text.setText(state[x][y].getText());
		revealed--;
		if(gameover == false && revealed == 0) {
			Popup.message(" YOU WON!!! \n");
			store("Player");
			gameover = true;
		}
	}
	
	/**
	 * Reveals only the specified tile, it has no recursive quality and it does not end the game if a 
	 * bomb is revealed. Used only by the superbomb and to show the solution.
	 * @param x coordinate of the tile
	 * @param y coordinate of the tile
	 */

	public void revealone(int x, int y) { 
		if(state[x][y].checked == true) {
			return ;
		}
		int res = state[x][y].neighborsnb;
		
		state[x][y].checked = true;
		if(state[x][y].marked) {
			state[x][y].marked = false;
			marked--;
		}
		state[x][y].text.setText(state[x][y].getText());
		if (res != -1) {
			revealed--;
			return;
		}
	}
	
	/**
	 * Marks a tile if it is unmarked and the amount of marked tiles smaller than the bombs.
	 * Unmarks a previously marked tile. If the superbomb get marked within the first 4 clicks
	 * reveals its row and column. Revealing a bomb this way does not end the game
	 * @param x coordinate of the tile
	 * @param y coordinate of the tile
	 */
	public void mark(int x, int y) {
		if(state[x][y].superbomb == true && clicks < 5) {
			for(int i = 0;i < size ;i++) {
					revealone(x,i);
			}
			for(int i = 0;i < size ;i++) {
					revealone(i,y);
			}
			return;
		}
		if(state[x][y].checked == true )
			return;
		if(state[x][y].marked == true) {
			state[x][y].marked = false;
			marked--;
		} else if(marked < bombs) {
			state[x][y].marked = true;
			marked++;
		}
		state[x][y].text.setText(state[x][y].getText());	
	}
	
	/**
	 * @return The array of tiles o the board
	 */
	public Tile[][] getstate(){
		return state;
	}
	/**
	 * @return The dimension size of the board
	 */
	public int getsize() {
		return size;
	}
	
	/**
	 * Stores the result of a game that ended in the history.txt file, removing the oldest result
	 * @param res the result o the game, player if the player wins, computer otherwise
	 */
	public void store(String res) {
		try {
			File file = new File("medialab/history.txt");
			String[] temp = new String[5];
			Scanner reader = new Scanner(file);
			for(int i = 0; i < 5; i++) {
				temp[i] = reader.nextLine();
			}
			reader.close();
			FileWriter w = new FileWriter(file);
			String cur = new String();
			cur = Integer.toString(bombs);
			cur += " ";
			cur += Integer.toString(clicks);
			cur += " ";
			cur += Integer.toString(time);
			cur += " ";
			cur += res;
			cur += '\n';
			w.write(cur);
			for(int i = 0; i < 4; i++) {
				cur = temp[i];
				cur += '\n';
				w.write(cur);
			}
			w.close();
		} catch(IOException e) {
			Popup.message("The result was not stored");
		}
	}
}
