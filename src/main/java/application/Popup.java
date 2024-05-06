package application;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Popup {
	static void message(String s){//simple popup message
		Stage stage = new Stage();
		Pane root = new Pane();
		Scene scene = new Scene(root);
		Text text = new Text();
		text.setX(0);
		text.setY(50);
		text.setText(s);
		text.setFont(Font.font(30));
		root.getChildren().add(text);
		Image icon = new Image("application/icon.png");
		stage.getIcons().add(icon);
		stage.setScene(scene);
		stage.show();
	}
	static void form(String s,String method) {
		Stage stage = new Stage();
		Pane root = new Pane();
		Scene scene = new Scene(root);
		Text text = new Text();
		text.setX(0);
		text.setY(50);
		text.setText(s);
		text.setFont(Font.font(30));
		root.getChildren().add(text);
		TextField answer = new TextField();
		root.getChildren().add(answer);
		answer.setLayoutY(120);
		Button submit = new Button("Submit");
		submit.setLayoutY(200);
		submit.setDefaultButton(true);
		root.getChildren().add(submit);
		submit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if(answer.getText().isEmpty()) {
					message("Please fill the form \n");
				} else if(method.equals("load")){ //submit load form
					String temp = answer.getText();
					String location = String.format("medialab/%s.txt",temp);
					try{
						int[] res = CRfile.read(location);
						if(res[0]!=0) {
							int size;
							if(res[0] == 1) {
								size = 9;
							} else {
								size = 16;
							}
							Main.board = new Board(size,res[1]);
							Main.timer = res[2];
						}
					} catch(InvalidDescriptionException ex) {
						message(ex.getMessage());
					} catch(InvalidValueException ex) {
						message(ex.getMessage());
					}
				} else if(method.equals("create")) { //submit create form
					ArrayList<String> res = sep(answer.getText());
					int[] temp = new int[4];
					temp[0] = Integer.parseInt(res.get(1));
					temp[1] = Integer.parseInt(res.get(2));
					temp[2] = Integer.parseInt(res.get(3));
					temp[3] = Integer.parseInt(res.get(4));
					String location = String.format("medialab/%s.txt", res.get(0));
					CRfile.create(location,temp);
				}
			}
		});
		Image icon = new Image("application/icon.png");
		stage.getIcons().add(icon);
		stage.setScene(scene);
		stage.show();
	}
	
	private static ArrayList<String> sep(String s) { //used to submit create form
		ArrayList<String> res = new ArrayList<String>();
		String cur = "";
		for(int i = 0; i < s.length(); i++) {
			if(s.charAt(i) == ' ') {
				if(cur.equals(""))
					continue;
				else {
					res.add(cur);
					cur = "";
				}
			} else {
				cur += s.charAt(i);
			}
		}
		res.add(cur);
		return res;
	}
}
