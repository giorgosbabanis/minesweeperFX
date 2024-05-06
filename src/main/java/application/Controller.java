package application;

import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Controller {

    @FXML
    private Label bombs;
    @FXML
    private Label marks;
    @FXML
    private Label timers;
    @FXML
    private MenuBar menu;

    static int counter = 0;

    Timeline timeline = new Timeline( //timeline for timer
            new KeyFrame(Duration.seconds(1),
                    e ->{
                        Label temp = (Label) Main.root.getChildren().get(3);
                        if(Integer.parseInt(temp.getText()) > 0) {
                            int cur = Integer.parseInt(temp.getText());
                            cur--;
                            Main.board.time = Main.timer - cur;
                            if(Main.board.gameover == false) {
                                temp.setText(Integer.toString(cur));
                            }
                        } else if(Integer.parseInt(temp.getText()) == 0){
                            Popup.message("Game over");
                            Main.board.store("Computer");
                            Main.board.gameover = true;
                        }
                    }));

    public void click(MouseEvent e, int x, int y){
        if (Main.board.gameover == true) {
            return;
        }
        if(e.getButton() == MouseButton.SECONDARY)
            Main.board.mark(x, y);
        else if(e.getButton() == MouseButton.PRIMARY) {
            Main.board.reveal(x, y, 0);
        }
        Label temp = (Label) Main.root.getChildren().get(2);
        temp.setText(Integer.toString(Main.board.marked)); //update the counter of the marked tiles
    }

    public void start(ActionEvent e) throws IOException {//controller that starts a new game
        if(Main.board == null) {
            Popup.message("Please load a scenario");
            return;
        }
        for(int i = Main.root.getChildren().size(); i > 0;i--){//clear the old tiles
            if(Main.root.getChildren().get(i - 1).getId() == null) {
                Main.root.getChildren().remove(i - 1);
            }
        }

        int size = Main.board.getsize();
        int bomb = Main.board.bombs;
        Main.board = new Board(size,bomb);

        Tile[][] tiles = Main.board.getstate();
        for(int  x = 0;x < size; x++) { //add the new tiles
            for (int y =0; y < size; y++) {
                Main.root.getChildren().addAll(tiles[x][y].rec,tiles[x][y].text);

            }
        }
        bombs.setText(Integer.toString(Main.board.bombs)); //set the number of bombs on the GUI
        Label temp = (Label) Main.root.getChildren().get(3);
        temp.setText(Integer.toString(Main.timer));//set the timer on the GUI
        String s = String.format(" %d,%d is a good place to start \n",Main.board.start[1], Main.board.start[2]);
        Popup.message(s);//message indicating a tile with no neighboring bombs
        timeline.setCycleCount(Main.timer + 1); //start the timer
        timeline.play();

    }

    public void load(ActionEvent e) {
        Popup.form("Please enter a Scenario id \n", "load");
    }

    public void create(ActionEvent e) {
        Popup.form("Please enter an id and a description such as: id 1 9 120 0 \n", "create");
    }

    public void exits(ActionEvent e) {
        System.exit(0);
    }

    public void solution(ActionEvent e) {
        int size = Main.board.getsize();
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                Main.board.revealone(i, j);
            }
        }
        Main.board.gameover = true;
    }

    public void rounds() {
        try {
            File file = new File("medialab/history.txt");
            Scanner reader = new Scanner(file);
            String temp = new String();
            while(reader.hasNextLine()) {
                temp += reader.nextLine();
                temp += "\n";
            }
            reader.close();
            Popup.message(temp);
        } catch(IOException e) {
            Popup.message("History could not be accessed");
        }
    }
}
