package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Tile {
	boolean bomb;
	boolean checked;
	boolean marked;
	boolean superbomb;
	int x;
	int y;
	int neighborsnb;
	Tile(boolean bomb, int size, int x, int y){//initialize a tile at the appropriate place and size on the gui
		Controller c = new Controller();       
		if (size == 16) {
			size = 45;
		}
		else {
			size = 80;
		}
		this.bomb = bomb;
		this.superbomb = false;
		this.checked = false;
		this.marked = false;
		this.neighborsnb = 0;
		rec = new Rectangle(size - 2,size - 2);
		rec.setX(1 + size*x);
		rec.setY(201 + size*y);
		rec.setOnMouseClicked(e -> c.click(e,x,y));
		text = new Text();
		text.setX(5 + size*x);
		text.setY(190 + size*y + size);
		text.setStroke(Color.WHITE);
		text.setVisible(true);
		text.setFont(Font.font(0.9*size));
		text.setText("");
		text.setOnMouseClicked(e -> c.click(e,x,y));
	}
	public String getText() {//returns the text based on whether the tile is checked, marked or not checked
		if(marked == true) {
			return "M";
		} else if (checked == true) {
			if(neighborsnb == -1) {
				return "X";
			} else {
				return Integer.toString(neighborsnb);
			}
		} else return "";
	}
	Rectangle rec;
	Text text;
}