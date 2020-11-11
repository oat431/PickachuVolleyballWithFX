package model;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import view.Platform;
import java.util.Random;

public class Ball extends Pane {

    public static final int CHARACTER_WIDTH = 40;
    public static final int CHARACTER_HEIGHT = 40;
    private int x;
    private int y;
    private int temp = 1;
    private int startX;
    private int startY;
    private int offsetX;
    private int offsetY;
    private KeyCode speed;
    private Image characterImg;
    private AnimatedSprite imageView;
    private boolean isFalling = true;
    private boolean isHit = false;
    private boolean isOnFloor = false;
    public Ball(int x, int y, int offsetX, int offsetY){
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.speed = speed;
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.characterImg = new Image(getClass().getResourceAsStream("/assets/sprite_sheet.png"));
        this.imageView = new AnimatedSprite(characterImg,4,4,offsetX,offsetY,42,42);
        this.imageView.setFitWidth(CHARACTER_WIDTH);
        this.imageView.setFitHeight(CHARACTER_HEIGHT);
        this.getChildren().addAll(this.imageView);
    }

    public void moveY() {
        setTranslateY(y);
        setTranslateX(x);
        if (isFalling) {
            y = y + 4;
            x = x + temp;
        }
    }

    public void moveX(){
        setTranslateX(x);
    }

    public void hitByCharacter(){
        setTranslateY(y);
        setTranslateX(x);
        y = y - 5;
        x = x - temp;
        isFalling = false;
    }

    public void paint(){
        moveX();
        moveY();
    }

    public void checkReachFloor() {
        if(isFalling && y >= Platform.GROUND - CHARACTER_HEIGHT) {
            isFalling = false;
        }
    }

    public void speedUp(){
        if(temp >= 10){
            temp = 10;
        }else{
            temp += 2;
        }
    }

    public void speedDown(){
        if(temp <= 1){
            temp = 1;
        }else{
            temp -= 2;
        }
    }

    public void checkHitPole(){
        if((x >= (Platform.WIDTH/2) - CHARACTER_WIDTH && x <= (Platform.WIDTH/2) + CHARACTER_WIDTH) && (y <= Platform.GROUND && y >= Platform.HEIGHT/2-CHARACTER_HEIGHT)){
            Random rand = new Random();
            temp = rand.nextInt(10)%2 == 0 ? -temp:temp;
            isHit = true;
            isFalling = false;
        }
    }

    public void checkReachGameWall() {
        if(x <= 0) {
            Random rand = new Random();
            temp = rand.nextInt(10)%2 == 0 ? -temp:temp;
            x = 10;
            isHit = false;
            isFalling = true;
        } else if(x + getWidth() >= Platform.WIDTH) {
            Random rand = new Random();
            temp = rand.nextInt(10)%2 == 0 ? -temp:temp;
            x = Platform.WIDTH - 90;
            isHit = true;
            isFalling = false;
        }
        if(y <= 0){
            Random rand = new Random();
            temp = rand.nextInt(10)%2 == 0 ? -temp:temp;
            isHit = false;
            isFalling = true;
        }else if(y + getHeight() >= Platform.GROUND) {
            isHit = false;
            isFalling = false;
            isOnFloor = true;
        }
    }

    public void respawn(){
        Random rand = new Random();
        x = rand.nextInt(10)%2 == 0 ? Platform.WIDTH-80 : 30;
        y = 40;
        isHit = false;
        isFalling = true;
        isOnFloor = false;
    }

    public void checkCollisionWithCharacter(Character a){
        if(this.getBoundsInParent().intersects(a.getBoundsInParent())){
            Random rand = new Random();
            temp = rand.nextInt(10)%2 == 0 ? -temp:temp;
            isHit = true;
        }
    }

    public boolean isHit(){
        return isHit;
    }

    public AnimatedSprite getImageView() {
        return imageView;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isOnFloor() {
        return isOnFloor;
    }

    public KeyCode getSpeed() {
        return speed;
    }
}
