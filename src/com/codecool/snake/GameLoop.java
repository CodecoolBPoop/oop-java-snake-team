package com.codecool.snake;

import com.codecool.snake.entities.GameEntity;
import com.codecool.snake.entities.Animatable;
import com.codecool.snake.entities.snakes.SnakeHead;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.text.*;

public class GameLoop extends AnimationTimer {
    private Pane pane;
    private Text text;

    GameLoop(Pane pane) {
        this.pane = pane;
        text = new Text();
        setHealthText();
    }

    private void setHealthText() {
        text.setFontSmoothingType(FontSmoothingType.LCD);
        text.setFont(Font.font("verdana", FontPosture.REGULAR, 16));
        text.setX(10);
        text.setY(20);
    }

    // This gets called every 1/60 seconds
    @Override
    public void handle(long now) {
        for (GameEntity gameObject : Globals.gameObjects) {
            if (gameObject instanceof Animatable) {
                if (gameObject instanceof SnakeHead) {
                    pane.getChildren().remove(text);
                    text.setText("Health: " + Integer.toString(gameObject.getHealth()));
                    pane.getChildren().add(text);
                }
                Animatable animObject = (Animatable)gameObject;
                animObject.step();
            }
        }
        Globals.gameObjects.addAll(Globals.newGameObjects);
        Globals.newGameObjects.clear();

        Globals.gameObjects.removeAll(Globals.oldGameObjects);
        Globals.oldGameObjects.clear();
    }
}
