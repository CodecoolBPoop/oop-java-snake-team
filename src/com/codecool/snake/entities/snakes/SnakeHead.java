package com.codecool.snake.entities.snakes;

import com.codecool.snake.Game;
import com.codecool.snake.entities.GameEntity;
import com.codecool.snake.Globals;
import com.codecool.snake.entities.Animatable;
import com.codecool.snake.Utils;
import com.codecool.snake.entities.Interactable;
import com.codecool.snake.entities.weapon.Bullet;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.*;

public class SnakeHead extends GameEntity implements Animatable {

    private Game game;
    private static final float turnRate = 2;
    private static final int initialSpeed = 2;
    private static final int speedUpSpeed = 4;
    private static final int powerupTime = 500;
    private int shotTimer = 5;
    private static Pane mainPane;
    private GameEntity tail; // the last element. Needed to know where to add the next part.
    private int speed = initialSpeed;
    private int health;
    private int highSpeedTimer = 0;
    private int drunkTimer = 0;
    private int drunkAngle;
    private boolean imageChanged = false;
    private Clip shotSound;
    private Clip shotSoundReverb;
    private int length = 0;


    public SnakeHead(Game game, Pane pane, int xc, int yc) {
        super(pane);
        this.game = game;
        mainPane = pane;
        setX(xc);
        setY(yc);
        health = 100;
        tail = this;
        setImage(Globals.snakeHead);
        pane.getChildren().add(this);
        try {
            this.openAudio();
        }
        catch (Exception e){
            System.out.println("Failed to open audio");
        }
        addPart(4);
    }

    private void openAudio() throws Exception{
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource("/shot.wav"));
        this.shotSound = AudioSystem.getClip();
        this.shotSound.open(audioIn);
        audioIn = AudioSystem.getAudioInputStream(getClass().getResource("/shot_reverb.wav"));
        this.shotSoundReverb = AudioSystem.getClip();
        this.shotSoundReverb.open(audioIn);
    }

    public void step(){
        double dir = getRotate();

        //drunk feature
        if (this.drunkTimer != 0){
            Random rand = new Random();
            if (this.drunkTimer % 20 == 0)
                this.drunkAngle = (rand.nextInt(4) - 1) * 2;
            this.drunkTimer--;
            System.out.println(this.drunkAngle);
            dir += drunkAngle;
        }

        if (Globals.leftKeyDown) {
            dir = dir - turnRate;
        }
        if (Globals.rightKeyDown) {
            dir = dir + turnRate;
        }

        if (Globals.spaceKeyDown) {
            this.shotTimer--;
            if (!this.imageChanged){
                this.shotSound.loop(Clip.LOOP_CONTINUOUSLY);
                setImage(Globals.snakeHeadShot);
                this.imageChanged = true;
                new Bullet(pane, getX(), getY(), getRotate());
            }
            if (this.shotTimer == 1)
                new Bullet(pane, getX(), getY(), getRotate());
            else if (this.shotTimer == 0)
                this.shotTimer = 5;
        }
        else if (!Globals.spaceKeyDown && this.imageChanged) {
            this.shotTimer = 5;
            this.shotSound.stop();
            this.shotSoundReverb.setMicrosecondPosition(0);
            this.shotSoundReverb.loop(1);
            setImage(Globals.snakeHead);
            this.imageChanged = false;
        }

        // set rotation and position
        setRotate(dir);
        Point2D heading = Utils.directionToVector(dir, this.speed);
        setX(getX() + heading.getX());
        setY(getY() + heading.getY());

        // check if collided with an enemy or a powerup
        for (GameEntity entity : Globals.getGameObjects()) {
            if (getBoundsInParent().intersects(entity.getBoundsInParent())) {
                if (entity instanceof Interactable) {
                    Interactable interactable = (Interactable) entity;
                    interactable.apply(this);
                    System.out.println(interactable.getMessage());
                }
            }
        }

        // check for game over condition
        if (isOutOfBounds() || health <= 0) {
            System.out.println("Game Over" + this.length);
            Globals.gameLoop.stop();
            this.destroy();
            game.gameOver(length);
        }

        if ( this.speed != initialSpeed){
            if (this.highSpeedTimer == 0)
                this.highSpeedTimer = powerupTime;
            else if (this.highSpeedTimer == 10)
                this.speed--;
            else if (this.highSpeedTimer == 1)
                this.speed = initialSpeed;
            this.highSpeedTimer--;
        }

    }

    public void addPart(int numParts) {
        for (int i = 0; i < numParts; i++) {
            SnakeBody newPart = new SnakeBody(pane, tail);
            tail = newPart;
            this.length++;
        }
    }


    public void changeHealth(int diff) {
        health += diff;
    }

    public int getHealth() {return health;}


    public void speedUp() { this.speed = speedUpSpeed;}

    public void drunk() { this.drunkTimer = powerupTime;}
}
