package model;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.Platform;
import java.util.concurrent.TimeUnit;

public class Character extends Pane {

    Logger logger = LogManager.getLogger(Character.class.getName());

    public static final int CHARACTER_WIDTH = 64;
    public static final int CHARACTER_HEIGHT = 65;

    private Image characterImg;


    private AnimatedSprite imageView;

    private int x;
    private int y;
    private int startX;
    private int startY;
    private int offsetX;
    private int offsetY;

    private int score = 0;

    private KeyCode leftKey;
    private KeyCode rightKey;
    private KeyCode upKey;
    private KeyCode downKey;
    private KeyCode skill;

    int xVelocity = 0;
    int yVelocity = 0;
    int xAcceleration = 1;
    int yAcceleration = 1;
    int xMaxVelocity = 7;
    int yMaxVelocity = 17;
    boolean isMoveLeft = false;
    boolean isMoveRight = false;
    boolean isDrive = false;
    boolean falling = true;
    boolean canJump = false;
    boolean jumping = false;

    public Character(int x, int y, int offsetX, int offsetY, KeyCode leftKey, KeyCode rightKey, KeyCode upKey, KeyCode downKey,KeyCode skill) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.characterImg = new Image(getClass().getResourceAsStream("/assets/sprite_sheet.png"));
        this.imageView = new AnimatedSprite(characterImg,4,4,offsetX,offsetY,64,65);
        this.imageView.setFitWidth(CHARACTER_WIDTH);
        this.imageView.setFitHeight(CHARACTER_HEIGHT);
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.upKey = upKey;
        this.downKey = downKey;
        this.skill = skill;
        this.getChildren().addAll(this.imageView);
    }

    public void moveLeft() {
        isMoveLeft = true;
        isMoveRight = false;
    }
    public void moveRight() {
        isMoveRight = true;
        isMoveLeft = false;
    }

    public void stop() {
        isMoveLeft = false;
        isMoveRight = false;
        xVelocity = 0;
    }

    public void jump() {
        if (canJump) {
            yVelocity = yMaxVelocity;
            canJump = false;
            jumping = true;
            falling = false;
        }
    }

    public boolean isDrive() {
        isDrive = !isDrive;
        return isDrive;
    }

    public void checkReachHighest() {
        if(jumping &&  yVelocity <= 0) {
            jumping = false;
            falling = true;
            yVelocity = 0;
        }
    }

    public void checkReachFloor() {
        if(falling && y >= Platform.GROUND - CHARACTER_HEIGHT) {
            falling = false;
            canJump = true;
            yVelocity = 0;
        }
    }

    public void checkReachGameWall() {
        if(x <= 0) {
            x = 0;
        } else if( x+getWidth() >= Platform.WIDTH) {
            x = Platform.WIDTH-CHARACTER_WIDTH;
        }
    }

    public void checkHitPole(){
        if(x >= (Platform.WIDTH / 2)-CHARACTER_WIDTH){
            if(startX == 30){
                x = (Platform.WIDTH/2)-CHARACTER_WIDTH - 5;
            }
        }
        if(x <= (Platform.WIDTH / 2)){
            if(startX == Platform.WIDTH-80){
                x = (Platform.WIDTH/2) + 5;
            }
        }
    }

    public void moveX() {
        setTranslateX(x);

        if(isMoveLeft) {
            xVelocity = xVelocity >= xMaxVelocity? xMaxVelocity : xVelocity+xAcceleration;
            x = x - xVelocity;
        }
        if(isMoveRight) {
            xVelocity = xVelocity >= xMaxVelocity? xMaxVelocity : xVelocity+xAcceleration;
            x = x + xVelocity;
        }
    }

    public void moveY() {
        setTranslateY(y);

        if(falling) {
            yVelocity = yVelocity >= yMaxVelocity? yMaxVelocity : yVelocity+yAcceleration;
            y = y + yVelocity;
        }
        else if(jumping) {
            yVelocity = yVelocity <= 0 ? 0 : yVelocity-yAcceleration;
            y = y - yVelocity;
        }
    }

    public void repaint() {
        moveX();
        moveY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void collided(Character c) throws InterruptedException {
        if (isMoveLeft){
            x = c.getX() + CHARACTER_WIDTH + 1;
            stop();
        }else if(isMoveRight){
            x = c.getX() - CHARACTER_WIDTH - 1;
            stop();
        }

        if(y < Platform.GROUND - CHARACTER_HEIGHT){
            if(falling && y < c.getY() && Math.abs(y-c.getY()) <= CHARACTER_HEIGHT + 1){
                score++;
                y = Platform.GROUND - CHARACTER_HEIGHT - 5;
                repaint();
                c.collapsed();
                c.respawn();
            }
        }
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void collapsed() throws InterruptedException {
        imageView.setFitHeight(5);
        y = Platform.GROUND - 5;
        repaint();
        TimeUnit.MICROSECONDS.sleep(500);
    }

    public void respawn(){
        x = startX;
        y = startY;
        imageView.setFitWidth(CHARACTER_WIDTH);
        imageView.setFitHeight(CHARACTER_HEIGHT);
        isMoveLeft = false;
        isMoveRight = false;
        falling = true;
        canJump = false;
        jumping = false;
    }

    public int getScore() {
        return score;
    }

    public void trace() {
        logger.info("x:{} y:{} vx:{} vy:{}",x,y,xVelocity,yVelocity);
    }

    public KeyCode getLeftKey() {
        return leftKey;
    }

    public KeyCode getRightKey() {
        return rightKey;
    }

    public KeyCode getUpKey() {
        return upKey;
    }

    public KeyCode getDownKey() {
        return downKey;
    }

    public AnimatedSprite getImageView() { return imageView; }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public boolean isMoveLeft() {
        return isMoveLeft;
    }

    public boolean isMoveRight() {
        return isMoveRight;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public KeyCode getSkill() {
        return skill;
    }
}
