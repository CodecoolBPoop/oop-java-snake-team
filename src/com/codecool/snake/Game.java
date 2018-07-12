package com.codecool.snake;

import com.codecool.snake.entities.GameEntity;
import com.codecool.snake.entities.enemies.ExplodingEnemy;
import com.codecool.snake.entities.enemies.SimpleEnemy;
import com.codecool.snake.entities.enemies.SpeedEnemy;
import com.codecool.snake.entities.menu.MultiPlayer;
import com.codecool.snake.entities.menu.SinglePlayer;
import com.codecool.snake.entities.powerups.DrunkPowerup;
import com.codecool.snake.entities.powerups.LifePowerup;
import com.codecool.snake.entities.powerups.SimplePowerup;
import com.codecool.snake.entities.powerups.SpeedPowerup;
import com.codecool.snake.entities.snakes.SnakeHead;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class Game extends Pane {

    private double elapsedMillis;
    private int player1length;
    private int player2length;
    private int numOfPlayers;
    private int initNumOfPlayers;

    private void spawnEntities() {
        new SnakeHead(this, this, 750, 500, 1);

        new SimpleEnemy(this);
        new SimpleEnemy(this);
        new SimpleEnemy(this);
        new SimpleEnemy(this);

        new ExplodingEnemy(this);

        new SpeedEnemy(this);

        new SimplePowerup(this);
        new SimplePowerup(this);
        new SimplePowerup(this);
        new SimplePowerup(this);

        new SpeedPowerup(this);

        new LifePowerup(this);

        new DrunkPowerup(this);
    }

    private void spawnPlayerTwo(){
        new SnakeHead(this, this, 250, 500, 2);
    }

    void start() {

        Globals.startGame = this;

        try{
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource("/techno.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.loop(-1);
        }
        catch (Exception e){
            System.out.print("failed to load techno");
        }


        Scene scene = getScene();
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    Globals.leftKeyDown = true;
                    break;
                case RIGHT:
                    Globals.rightKeyDown = true;
                    break;
                case DOWN:
                    Globals.downKeyDown = true;
                    break;
                case A:
                    Globals.AKeyDown = true;
                    break;
                case S:
                    Globals.SKeyDown = true;
                    break;
                case D:
                    Globals.DKeyDown = true;
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
                case DOWN:
                    Globals.downKeyDown = false;
                    break;
                case A:
                    Globals.AKeyDown = false;
                    break;
                case S:
                    Globals.SKeyDown = false;
                    break;
                case D:
                    Globals.DKeyDown = false;
                    break;
                case R:
                    restartGame();
                    break;
            }
        });

        new SnakeHead(this, this, 500, 650, 1);
        new SinglePlayer(this, 150, 200);
        new MultiPlayer(this, 650, 180);
        Globals.gameLoop = new GameLoop(this);
        Globals.gameLoop.start();
    }

    public void startGame(){
        for (GameEntity gameObject : Globals.gameObjects) gameObject.destroy();
        if (Globals.isMultiplayer){
            spawnPlayerTwo();
            numOfPlayers = 2;
            initNumOfPlayers = 2;
        } else {
            numOfPlayers = 1;
            initNumOfPlayers = 1;
        }
        spawnEntities();
        setTimerForSpawningEntities();
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
        if (elapsedMillis % Utils.getRandomNumber(2000, 8000) == 0) {
            new SpeedEnemy(this);
        }
        if (elapsedMillis % Utils.getRandomNumber(4000, 12000) == 0) {
            new ExplodingEnemy(this);
        }
    }

    private void restartGame() {
        for (GameEntity gameObject : Globals.gameObjects) gameObject.destroy();
        spawnEntities();
        if (initNumOfPlayers == 2) spawnPlayerTwo();
        numOfPlayers = initNumOfPlayers;
        player1length = 0;
        player2length = 0;
        Globals.rightKeyDown = false;
        Globals.leftKeyDown = false;
        Globals.downKeyDown = false;
        Globals.AKeyDown = false;
        Globals.SKeyDown = false;
        Globals.DKeyDown = false;
        Globals.gameLoop.start();
    }

    public void playerDied(SnakeHead player, int length) {
        if (player.getPlayer() == 1 && player1length == 0) {
            player1length = length;
            numOfPlayers--;
        }
        if (player.getPlayer() == 2 && player2length == 0) {
            player2length = length;
            numOfPlayers--;
        }
        if (numOfPlayers == 0) gameOver();
    }

    public void gameOver() {
        Globals.gameLoop.stop();

        Stage gameOver = new Stage();
        gameOver.setTitle("GAME OVER!");

        String gameOverTitle;
        if (initNumOfPlayers == 2) {
            gameOverTitle = "Length of Player 1: " + player1length + "\nLength of Player 2: " + player2length;
        } else {
            gameOverTitle = "Length of the Snake: " + player1length;
        }

        Text text = new Text(50, 50, gameOverTitle);
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
