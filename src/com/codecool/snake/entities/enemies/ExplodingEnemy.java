package com.codecool.snake.entities.enemies;

import com.codecool.snake.entities.GameEntity;
import com.codecool.snake.Globals;
import com.codecool.snake.entities.Animatable;
import com.codecool.snake.Utils;
import com.codecool.snake.entities.Interactable;
import com.codecool.snake.entities.snakes.SnakeHead;
import com.codecool.snake.entities.weapon.Laser;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Random;


public class ExplodingEnemy extends GameEntity implements Animatable, Interactable {
    Random rnd = new Random();
    private Point2D heading;
    private static final int damage = 10;
    private double direction = rnd.nextDouble() * 360;
    private int speed = 2;
    private int elapsedMillis;
    private int player;
    private Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1), ev -> {
        elapsedMillis++;
        if (elapsedMillis % 300 == 0) {
            shootLaser();
        }
    }));

    public ExplodingEnemy(Pane pane) {
        super(pane);

        setImage(Globals.explodingEnemy);
        pane.getChildren().add(this);

        setX(rnd.nextDouble() * Globals.WINDOW_WIDTH);
        setY(rnd.nextDouble() * Globals.WINDOW_HEIGHT);

        setRotate(direction);
        heading = Utils.directionToVector(direction, speed);
        setTimerForShootingLaser();
    }

    private void bounceFromSideWalls(){
        direction = direction * -1;
        setRotate(direction);
        heading = Utils.directionToVector(direction, speed);
    }

    private void bounceFromTopWalls() {
        direction = 180 - direction;
        setRotate(direction);
        heading = Utils.directionToVector(direction, speed);
    }

    private void shootLaser() {
        new Laser(pane, getX(), getY(), getRotate());

    }

    private void setTimerForShootingLaser() {
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    @Override
    public void step() {
        if (isOutOfBounds()) {
            if (isOutOfBounds()) {
                if(getX() < 0) {
                    setX(1);
                    bounceFromSideWalls();
                } else if (getX() > 1000 ) {
                    setX(999);
                    bounceFromSideWalls();
                } else if(getY() > 700) {
                    setY(680);
                    bounceFromTopWalls();
                } else if (getY() < 0) {
                    setY(20);
                    bounceFromTopWalls();
                }
            }
        }
        setX(getX() + heading.getX());
        setY(getY() + heading.getY());
    }

    @Override
    public void destroy() {
        if (getParent() != null) {
            pane.getChildren().remove(this);
        }
        Globals.removeGameObject(this);
        timeline.stop();
    }

    @Override
    public void apply(SnakeHead snakeHead) {
        if (snakeHead.getPlayer() != player) {
            snakeHead.changeHealth(-1);
            this.destroy();
        }
    }

    @Override
    public String getMessage() {
        return "10 damage";
    }
}
