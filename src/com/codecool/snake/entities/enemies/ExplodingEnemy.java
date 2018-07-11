package com.codecool.snake.entities.enemies;

import com.codecool.snake.entities.GameEntity;
import com.codecool.snake.Globals;
import com.codecool.snake.entities.Animatable;
import com.codecool.snake.Utils;
import com.codecool.snake.entities.Interactable;
import com.codecool.snake.entities.snakes.SnakeHead;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

import java.util.Random;


public class ExplodingEnemy extends GameEntity implements Animatable, Interactable {
    Random rnd = new Random();
    private Point2D heading;
    private static final int damage = 10;
    private double direction = rnd.nextDouble() * 360;
    private int speed = 2;

    public ExplodingEnemy(Pane pane) {
        super(pane);

        setImage(Globals.explodingEnemy);
        pane.getChildren().add(this);

        setX(rnd.nextDouble() * Globals.WINDOW_WIDTH);
        setY(rnd.nextDouble() * Globals.WINDOW_HEIGHT);

        setRotate(direction);
        heading = Utils.directionToVector(direction, speed);
    }

    private void bounceFromSideWalls(){
        direction = direction * -1;
        setRotate(direction);
        heading = Utils.directionToVector(direction, speed);
    }

    private void bounceFromTopWalls() {
        direction = direction * 2;
        setRotate(direction);
        heading = Utils.directionToVector(direction, speed);
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
    public void apply(SnakeHead player) {
        player.changeHealth(-damage);
        destroy();
    }

    @Override
    public String getMessage() {
        return "10 damage";
    }
}
