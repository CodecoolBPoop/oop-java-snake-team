package com.codecool.snake;

import com.codecool.snake.entities.GameEntity;
import com.codecool.snake.entities.enemies.SimpleEnemy;
import com.codecool.snake.entities.powerups.DrunkPowerup;
import com.codecool.snake.entities.powerups.LifePowerup;
import com.codecool.snake.entities.powerups.SimplePowerup;
import com.codecool.snake.entities.powerups.SpeedPowerup;
import com.codecool.snake.entities.snakes.SnakeHead;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Game extends Pane {

    private double elapsedMillis;

    private void spawnEntities() {
        new SnakeHead(this, this, 500, 500);

        new SimpleEnemy(this);
        new SimpleEnemy(this);
        new SimpleEnemy(this);
        new SimpleEnemy(this);

        new SimplePowerup(this);
        new SimplePowerup(this);
        new SimplePowerup(this);
        new SimplePowerup(this);

        new SpeedPowerup(this);

        new LifePowerup(this);

        new DrunkPowerup(this);
    }

    void start() {
        spawnEntities();
        Scene scene = getScene();
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    Globals.leftKeyDown = true;
                    break;
                case RIGHT:
                    Globals.rightKeyDown = true;
                    break;
                case SPACE:
                    Globals.spaceKeyDown = true;
                    break;
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT:
                    Globals.leftKeyDown = false;
                    break;
                case RIGHT:
                    Globals.rightKeyDown = false;
                    break;
                case SPACE:
                    Globals.spaceKeyDown = false;
                    break;
                case R:
                    restartGame();
                    break;
            }
        });

        setTimerForSpawningEntities();
        Globals.gameLoop = new GameLoop(this);
        Globals.gameLoop.start();
    }

    private void setTimerForSpawningEntities() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1), ev -> {
            elapsedMillis++;
            spawnNewEntities();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void spawnNewEntities() {

        // PowerUps
        if (elapsedMillis % Utils.getRandomNumber(1000, 5000) == 0) {
            new SimplePowerup(this);
        }
        if (elapsedMillis % Utils.getRandomNumber(6000, 15000) == 0) {
            new SpeedPowerup(this);
        }
        if (elapsedMillis % Utils.getRandomNumber(15000, 30000) == 0) {
            new LifePowerup(this);
        }
        if (elapsedMillis % Utils.getRandomNumber(15000, 30000) == 0) {
            new DrunkPowerup(this);
        }

        // Enemies
        if (elapsedMillis % Utils.getRandomNumber(2000, 8000) == 0) {
            new SimpleEnemy(this);
        }
    }

    private void restartGame() {
        for (GameEntity gameObject : Globals.gameObjects) gameObject.destroy();
        spawnEntities();
        Globals.gameLoop.start();
    }

    public void gameOver(int length) {
        Stage gameOver = new Stage();
        gameOver.setTitle("Your game is over");
        Text text = new Text(50, 50, "Game over \nSnake's length: " + length);
        StackPane root = new StackPane();
        root.getChildren().add(text);

        Button restart = new Button();
        restart.setTranslateX(100);
        restart.setTranslateY(100);
        restart.setText("RESTART");
        restart.setOnAction(e -> {
            gameOver.close();
            restartGame();
        });

        root.getChildren().add(restart);

        Button exit = new Button();
        exit.setTranslateX(-115);
        exit.setTranslateY(100);
        exit.setText("EXIT");
        exit.setOnAction(e -> System.exit(0));
        root.getChildren().add(exit);

        gameOver.setScene(new Scene(root, 300, 250));
        gameOver.show();
    }
}
