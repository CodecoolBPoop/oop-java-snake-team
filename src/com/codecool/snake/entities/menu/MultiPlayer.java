package com.codecool.snake.entities.menu;

import com.codecool.snake.entities.GameEntity;
import com.codecool.snake.Globals;
import com.codecool.snake.entities.Interactable;
import com.codecool.snake.entities.snakes.SnakeHead;
import javafx.scene.layout.Pane;

import java.util.Random;

// a simple powerup that makes the snake grow TODO make other powerups
public class MultiPlayer extends GameEntity implements Interactable {

    private Pane pane;

    public MultiPlayer(Pane pane, int x, int y) {
        super(pane);
        this.pane = pane;
        setImage(Globals.multiPlayer);
        pane.getChildren().add(this);

        Random rnd = new Random();
        setX(x);
        setY(y);
    }

    @Override
    public void apply(SnakeHead snakeHead) {
        Globals.isMultiplayer = true;
        Globals.startGame.startGame();
    }

    @Override
    public String getMessage() {
        return "Multi Player";
    }
}
