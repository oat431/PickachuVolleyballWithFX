package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import model.Ball;
import model.Character;
import model.Keys;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Platform extends Pane {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 400;
    public static final int GROUND = 330;

    private Image platformImg;
    private ArrayList<Character> characterList;
    private ArrayList<Score> scoreList;
    private Ball ball;
    private Keys keys;

    public Platform() {
        characterList = new ArrayList<>();
        scoreList = new ArrayList<>();
        keys = new Keys();
        platformImg = new Image(getClass().getResourceAsStream("/assets/Background.png"));
        ImageView backgroundImg = new ImageView(platformImg);
        backgroundImg.setFitHeight(HEIGHT);
        backgroundImg.setFitWidth(WIDTH);
        characterList.add(new Character(30, 200,0,270, KeyCode.A,KeyCode.D,KeyCode.W,KeyCode.S,KeyCode.F));
        characterList.add(new Character(Platform.WIDTH-80, 200,0,270, KeyCode.LEFT,KeyCode.RIGHT,KeyCode.UP,KeyCode.DOWN,KeyCode.ENTER));
        ball = new Ball(Platform.WIDTH-60,40,88,158);
        scoreList.add(new Score(30,30));
        scoreList.add(new Score(Platform.WIDTH-60,30));
        getChildren().add(backgroundImg);
        getChildren().add(ball);
        getChildren().addAll(characterList);
        getChildren().addAll(scoreList);
    }

    public ArrayList<Score> getScoreList() {
        return scoreList;
    }

    public ArrayList<Character> getCharacterList() {
        return characterList;
    }

    public Ball getBall() {
        return ball;
    }

    public Keys getKeys() {
        return keys;
    }
}

