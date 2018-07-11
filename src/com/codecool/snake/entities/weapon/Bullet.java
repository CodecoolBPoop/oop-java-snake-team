package com.codecool.snake.entities.weapon;

import com.codecool.snake.entities.GameEntity;
import com.codecool.snake.Globals;
import com.codecool.snake.entities.Animatable;
import com.codecool.snake.Utils;
import com.codecool.snake.entities.Interactable;
import com.codecool.snake.entities.snakes.SnakeHead;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

import java.util.Random;

// a simple enemy TODO make better ones.
public class Bullet extends GameEntity implements Animatable, Interactable {

    private Point2D heading;

    private SnakeHead thisSnake;

    public Bullet(Pane pane, double x, double y, double direction) {
        super(pane);

        setImage(Globals.bullet);
        pane.getChildren().add(this);
        int speed = 10;
        setX(x);
        setY(y);
        setRotate(direction);
        heading = Utils.directionToVector(direction, speed);
    }

    @Override
    public void step() {
        for (GameEntity entity : Globals.getGameObjects()) {
            if (getBoundsInParent().intersects(entity.getBoundsInParent())) {
                if (entity instanceof Interactable) {
                    Interactable interactable = (Interactable) entity;
                    interactable.apply(this);
                }
            }
        }
        if (isOutOfBounds()) {
            destroy();
        }
        setX(getX() + heading.getX());
        setY(getY() + heading.getY());
    }

    @Override
    public void apply(SnakeHead player) {
        System.out.print("ads");
    }

    @Override
    public String getMessage() {
        return "shot";
    }
}
