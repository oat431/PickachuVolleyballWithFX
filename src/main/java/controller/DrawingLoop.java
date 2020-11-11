package controller;

import model.Ball;
import model.Character;
import view.Platform;

import java.util.ArrayList;

public class DrawingLoop implements Runnable {

    private Platform platform;
    private int frameRate;
    private float interval;
    private boolean running;

    public DrawingLoop(Platform platform) {
        this.platform = platform;
        frameRate = 60;
        interval = 1000.0f / frameRate; // 1000 ms = 1 second
        running = true;
    }

    private void checkDrawCollisions(ArrayList<Character> characterList,Ball ball) throws InterruptedException{

        for (Character character : characterList ) {
            character.checkReachGameWall();
            character.checkReachHighest();
            character.checkReachFloor();
            character.checkHitPole();
            ball.checkCollisionWithCharacter(character);
        }
        for(Character cA : characterList){
            for(Character cB : characterList){
                if(cA != cB){
                    if(cA.getBoundsInParent().intersects(cB.getBoundsInParent())){
                        cA.collided(cB);
                        cB.collided(cA);
                        return ;
                    }
                }
            }
        }
        ball.checkReachFloor();
        ball.checkHitPole();
        ball.checkReachGameWall();
    }

    private void paint(ArrayList<Character> characterList) {
        for (Character character : characterList ) {
            character.repaint();
        }
    }

    private void paint(Ball ball){
        ball.paint();
    }

    private void hit(Ball ball){
        ball.hitByCharacter();
    }

    @Override
    public void run() {
        while (running) {

            float time = System.currentTimeMillis();

            try {
                checkDrawCollisions(platform.getCharacterList(),platform.getBall());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            paint(platform.getCharacterList());
            if(platform.getBall().isHit()){
                hit(platform.getBall());
            }else{
                paint(platform.getBall());
            }

            time = System.currentTimeMillis() - time;

            if (time < interval) {
                try {
                    Thread.sleep((long) (interval - time));
                } catch (InterruptedException ignore) {
                }
            } else {
                try {
                    Thread.sleep((long) (interval - (interval % time)));
                } catch (InterruptedException ignore) {
                }
            }
        }
    }
}
