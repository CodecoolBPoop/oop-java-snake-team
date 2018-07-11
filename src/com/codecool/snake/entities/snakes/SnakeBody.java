package com.codecool.snake.entities.snakes;

import com.codecool.snake.entities.GameEntity;
import com.codecool.snake.Globals;
import com.codecool.snake.entities.Animatable;
import com.codecool.snake.entities.Interactable;
import com.sun.javafx.geom.Vec2d;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SnakeBody extends GameEntity implements Animatable, Interactable {

    private GameEntity parent;
    private Queue<Vec2d> history = new LinkedList<>();
    private static final int historySize = 3;
    private int player;

    public SnakeBody(Pane pane, GameEntity parent, int player) {
        super(pane);
        this.parent = parent;
        this.player = player;

        if (player == 1) setImage(Globals.snakeBody);
        else setImage(Globals.snakeBody2);

        // place it visually below the current tail
        List<Node> children = pane.getChildren();
        children.add(children.indexOf(parent), this);

        double xc = parent.getX();
        double yc = parent.getY();

        for (int i = 0; i < historySize; i++) {
            history.add(new Vec2d(xc, yc));
        }
    }

    public void destroy() {
        if (getParent() != null) {
            pane.getChildren().remove(this);
        }
        if ((parent != null) && !(parent instanceof SnakeHead)) parent.destroy();
        Globals.removeGameObject(this);
    }

    public void step() {
        Vec2d pos = history.poll(); // remove the oldest item from the history
        setX(pos.x);
        setY(pos.y);
        history.add(new Vec2d(parent.getX(), parent.getY())); // add the parent's current position to the beginning of the history
    }

    @Override
    public void apply(SnakeHead snakeHead) {
        if (snakeHead.getPlayer() != player) snakeHead.dies();
    }

    @Override
    public String getMessage() {
        return "";
    }
}
