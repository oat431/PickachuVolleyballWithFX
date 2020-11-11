package controller;

import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Popup;
import model.Ball;
import model.Character;
import view.Platform;
import view.Score;

import java.util.ArrayList;

public class GameLoop implements Runnable {

    private Platform platform;
    private int frameRate;
    private float interval;
    private boolean running;

    public GameLoop(Platform platform) {
        this.platform = platform;
        frameRate = 5;
        interval = 1000.0f / frameRate; // 1000 ms = 1 second
        running = true;
    }

    private void update(ArrayList<Character> characterList) {
        for (Character character : characterList ) {
            if (platform.getKeys().isPressed(character.getLeftKey()) || platform.getKeys().isPressed(character.getRightKey())) {
                character.getImageView().tick();
            }

            if (platform.getKeys().isPressed(character.getLeftKey())) {
                character.setScaleX(-1);
                character.moveLeft();
                character.trace();
            }

            if (platform.getKeys().isPressed(character.getRightKey())) {
                character.setScaleX(1);
                character.moveRight();
                character.trace();
            }

            if (!platform.getKeys().isPressed(character.getLeftKey()) && !platform.getKeys().isPressed(character.getRightKey()) ) {
                character.stop();
            }

            if (platform.getKeys().isPressed(character.getUpKey())) {
                character.getImageView().tock();
                character.jump();
            }

            if(platform.getKeys().isPressed(character.getDownKey())){
                character.getImageView().tack();
                character.trace();
            }
        }
    }

    private void updateScore(ArrayList<Score> scoreList, ArrayList<Character> characterList){
        javafx.application.Platform.runLater(()->{
            for(int i=0;i<scoreList.size();i++){
                scoreList.get(i).setPoint(characterList.get(i).getScore());
            }
        });
    }

    private void updateBall(Ball ball){
        ball.getImageView().tick();
        if(platform.getKeys().isPressed(KeyCode.I)){
            ball.speedUp();
        }
        if(platform.getKeys().isPressed(KeyCode.K)){
            ball.speedDown();
        }
        if(platform.getKeys().isPressed(KeyCode.F)){
            platform.getCharacterList().get(0).setScore(platform.getCharacterList().get(0).getScore() - 2);
            ball.respawn();
        }
        if(platform.getKeys().isPressed(KeyCode.ENTER)){
            platform.getCharacterList().get(1).setScore(platform.getCharacterList().get(1).getScore() - 2);
            ball.respawn();
        }
    }

    @Override
    public void run() {
        while (running) {

            float time = System.currentTimeMillis();

            update(platform.getCharacterList());
            updateScore(platform.getScoreList(),platform.getCharacterList());
            updateBall(platform.getBall());
            if(platform.getBall().isOnFloor()) {
                if(platform.getBall().getX() > Platform.WIDTH/2){
                    platform.getCharacterList().get(0).setScore(platform.getCharacterList().get(0).getScore() + 1);
                }else{
                    platform.getCharacterList().get(1).setScore(platform.getCharacterList().get(1).getScore() + 1);
                }
                platform.getBall().respawn();
            }
            if(platform.getScoreList().get(0).getScore() == 5 || platform.getScoreList().get(1).getScore() == 5){
                break;
            }
            time = System.currentTimeMillis() - time;

            if (time < interval) {
                try {
                    Thread.sleep((long) (interval - time));
                } catch (InterruptedException ignore) {
                    ignore.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep((long) (interval - (interval % time)));
                } catch (InterruptedException ignore) {
                    ignore.printStackTrace();
                }
            }
        }
        javafx.application.Platform.runLater(()->{
            Label label = new Label();
            if(platform.getScoreList().get(0).getScore() == 5){
                label.setText("player 1 win");
            }else{
                label.setText("player 2 win");
            }
            label.setStyle("-fx-font-size: 26px;");
            Popup popup = new Popup();
            popup.getContent().add(label);
            popup.show(Launcher.primaryStage);
        });

    }
}
