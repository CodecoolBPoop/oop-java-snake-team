package com.codecool.snake.entities.snakes;

import com.codecool.snake.entities.GameEntity;
import com.codecool.snake.Globals;
import com.codecool.snake.entities.Animatable;
import com.codecool.snake.Utils;
import com.codecool.snake.entities.Interactable;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import java.util.Random;

public class SnakeHead extends GameEntity implements Animatable {

    private static final float turnRate = 2;
    private static final int initialSpeed = 2;
    private static final int speedUpSpeed = 4;
    private static final int powerupTime = 500;
    private GameEntity tail; // the last element. Needed to know where to add the next part.
    private int speed = initialSpeed;
    private int health;
    private int highSpeedTimer = 0;
    private int drunkTimer = 0;
    private int drunkAngle;

    public SnakeHead(Pane pane, int xc, int yc) {
        super(pane);
        setX(xc);
        setY(yc);
        health = 100;
        tail = this;
        setImage(Globals.snakeHead);
        pane.getChildren().add(this);

        addPart(4);
    }

    public void step() {
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
            System.out.println("Game Over");
            Globals.gameLoop.stop();
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
        }
    }

    public void changeHealth(int diff) {
        health += diff;
    }

    public void speedUp() { this.speed = speedUpSpeed;}

    public void drunk() { this.drunkTimer = powerupTime;}
}
