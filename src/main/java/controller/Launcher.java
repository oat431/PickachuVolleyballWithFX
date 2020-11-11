package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.DrawingLoop;
import controller.GameLoop;
import view.Platform;

public class Launcher extends Application {

    public static void main(String[] args) { launch(args); }
    public static Stage primaryStage;
    @Override
    public void start(Stage primaryStage) {
        Launcher.primaryStage = primaryStage;
        Platform platform = new Platform();
        GameLoop gameLoop = new GameLoop(platform);
        DrawingLoop drawingLoop = new DrawingLoop(platform);

        Scene scene = new Scene(platform, Platform.WIDTH, Platform.HEIGHT);
        scene.setOnKeyPressed(event-> platform.getKeys().add(event.getCode()));
        scene.setOnKeyReleased(event ->  platform.getKeys().remove(event.getCode()));

        Launcher.primaryStage.setTitle("platformer");
        Launcher.primaryStage.setScene(scene);
        Launcher.primaryStage.show();

        (new Thread(gameLoop)).start();
        (new Thread(drawingLoop)).start();

    }
}
