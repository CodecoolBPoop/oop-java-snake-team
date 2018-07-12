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
    private int player;


    public SnakeHead(Game game, Pane pane, int xc, int yc, int player) {
        super(pane);
        this.game = game;
        this.player = player;
        mainPane = pane;
        setX(xc);
        setY(yc);
        health = 100;
        tail = this;
        if (player == 1) setImage(Globals.snakeHead);
        else setImage(Globals.snakeHead2);
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

        if ((Globals.leftKeyDown && player == 1) || (Globals.AKeyDown && player == 2)) {
            dir = dir - turnRate;
        }
        if ((Globals.rightKeyDown && player == 1) || (Globals.DKeyDown && player == 2)) {
            dir = dir + turnRate;
        }

        if ((Globals.downKeyDown && player == 1) || (Globals.SKeyDown && player == 2)) {
            this.shotTimer--;
            if (!this.imageChanged){
                this.shotSound.loop(Clip.LOOP_CONTINUOUSLY);
                setImage(Globals.snakeHeadShot);
                this.imageChanged = true;
                new Bullet(pane, getX(), getY(), getRotate(), player);
            }
            if (this.shotTimer == 1)
                new Bullet(pane, getX(), getY(), getRotate(), player);
            else if (this.shotTimer == 0)
                this.shotTimer = 5;
        }
        else if (this.imageChanged) {
            this.shotTimer = 5;
            this.shotSound.stop();
            this.shotSoundReverb.setMicrosecondPosition(0);
            this.shotSoundReverb.loop(1);
            if (player == 1) setImage(Globals.snakeHead);
            else setImage(Globals.snakeHead2);
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
            dies();
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

    public void dies() {

        this.health = 0;
        this.shotSound.stop();
        game.playerDied(this, length);
        this.destroy();
        System.out.println("Player " + player + " died. Length: " + this.length);
    }

    public void destroy() {
        if (getParent() != null) {
            pane.getChildren().remove(this);
        }
        Globals.removeGameObject(this);
        tail.destroy();
    }

    public void addPart(int numParts) {
        for (int i = 0; i < numParts; i++) {
            SnakeBody newPart = new SnakeBody(pane, tail, player);
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

    public int getPlayer() {return this.player; }
}
