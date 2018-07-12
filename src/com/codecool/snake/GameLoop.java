package com.codecool.snake;

import com.codecool.snake.entities.GameEntity;
import com.codecool.snake.entities.Animatable;
import com.codecool.snake.entities.snakes.SnakeHead;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class GameLoop extends AnimationTimer {
    private Pane pane;
    private Text player1Health = new Text();
    private Text player2Health = new Text();

    GameLoop(Pane pane) {
        this.pane = pane;
        initTexts();
        BackgroundImage myBI= new BackgroundImage(new Image("background.jpg",Globals.WINDOW_WIDTH,Globals.WINDOW_HEIGHT,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
//then you set to your node
        pane.setBackground(new Background(myBI));
    }

    private void initTexts() {
        player1Health.setFontSmoothingType(FontSmoothingType.LCD);
        player1Health.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        player1Health.setX(810);
        player1Health.setY(20);

        player2Health.setFontSmoothingType(FontSmoothingType.LCD);
        player2Health.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        player2Health.setX(10);
        player2Health.setY(20);
    }

    private void printHealth(int player, int score) {
        String health = "Player " + player + ": " + score + " HP";

        if (player == 1) {
            pane.getChildren().remove(player1Health);
            player1Health.setText(health);
            pane.getChildren().add(player1Health);
        } else {
            pane.getChildren().remove(player2Health);
            player2Health.setText(health);
            pane.getChildren().add(player2Health);
        }
    }

    // This gets called every 1/60 seconds
    @Override
    public void handle(long now) {
        for (GameEntity gameObject : Globals.gameObjects) {
            if (gameObject instanceof Animatable) {
                if (gameObject instanceof SnakeHead) {
                    printHealth(((SnakeHead) gameObject).getPlayer(), gameObject.getHealth());
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
