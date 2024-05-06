package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;



public class Main extends Application {

    public static Pane root;
    public static Board board; //active board
    public static Stage stage;
    public static Scene scene;
    public static int[] start;
    public static int timer;
    @Override
    public void start(Stage s) {
        try{
            root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
            scene = new Scene(root,Color.GRAY);
            Image icon = new Image("application/icon.png");
            stage = s;
            stage.getIcons().add(icon);
            stage.setTitle("Medialab Minesweeper");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
