package com.codecool.snake.entities.menu;

import com.codecool.snake.entities.GameEntity;
import com.codecool.snake.Globals;
import com.codecool.snake.entities.Interactable;
import com.codecool.snake.entities.snakes.SnakeHead;
import javafx.scene.layout.Pane;

import java.util.Random;

// a simple powerup that makes the snake grow TODO make other powerups
public class SinglePlayer extends GameEntity implements Interactable {

    public SinglePlayer(Pane pane, int x, int y) {
        super(pane);
        setImage(Globals.singlePlayer);
        pane.getChildren().add(this);

        Random rnd = new Random();
        setX(x);
        setY(y);
    }

    @Override
    public void apply(SnakeHead snakeHead) {
        Globals.isMultiplayer = false;
        Globals.startGame.startGame();
    }

    @Override
    public String getMessage() {
        return "Single Player";
    }
}
