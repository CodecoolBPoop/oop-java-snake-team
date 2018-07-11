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
    private int player;

    public Bullet(Pane pane, double x, double y, double direction, int player) {
        super(pane);

        this.player = player;
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
    public void apply(SnakeHead snakeHead) {
        if (snakeHead.getPlayer() != player) {
            snakeHead.changeHealth(-1);
            this.destroy();
        }
    }

    @Override
    public String getMessage() {
        return "Shot by player " + player;
    }
}
