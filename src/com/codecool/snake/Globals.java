package com.codecool.snake;

import com.codecool.snake.entities.GameEntity;
import com.codecool.snake.entities.snakes.SnakeHead;
import javafx.scene.image.Image;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

// class for holding all static stuff
public class Globals {

    public static final double WINDOW_WIDTH = 1000;
    public static final double WINDOW_HEIGHT = 700;

    public static Image snakeHead = new Image("snake_head.png");
    public static Image snakeHeadShot = new Image("snake_head_shot.png");
    public static Image snakeBody = new Image("snake_body.png");
    public static Image simpleEnemy = new Image("simple_enemy.png");
    public static Image explodingEnemy = new Image ("exploding_enemy.png");
    public static Image speedEnemy = new Image("speed_enemy.png");
    public static Image powerupBerry = new Image("powerup_berry.png");
    public static Image powerupSpeed = new Image("powerup_speed.png");
    public static Image powerupLife = new Image("powerup_life.png");
    public static Image powerupDrunk = new Image("powerup_drunk.png");
    public static Image bullet = new Image("bullet.png");
    public static Image laserBullet = new Image("laser_bullet.png");
    public static Image singlePlayer = new Image("single_player.png");
    public static Image multiPlayer = new Image("multi_player.png");
    public static Image snakeHead2 = new Image("snake_head2.png");
    public static Image snakeBody2 = new Image("snake_body2.png");
    //.. put here the other images you want to use

    public static boolean leftKeyDown;
    public static boolean rightKeyDown;
    public static boolean downKeyDown;
    public static boolean AKeyDown;
    public static boolean SKeyDown;
    public static boolean DKeyDown;
    public static List<GameEntity> gameObjects;
    public static List<GameEntity> newGameObjects; // Holds game objects crated in this frame.
    public static List<GameEntity> oldGameObjects; // Holds game objects that will be destroyed this frame.
    public static GameLoop gameLoop;
    public static Game startGame;
    public static boolean isMultiplayer = false;

    static {
        gameObjects = new LinkedList<>();
        newGameObjects = new LinkedList<>();
        oldGameObjects = new LinkedList<>();
    }

    public static void addGameObject(GameEntity toAdd) {
        newGameObjects.add(toAdd);
    }

    public static void removeGameObject(GameEntity toRemove) {
        oldGameObjects.add(toRemove);
    }

    public static List<GameEntity> getGameObjects() {
        return Collections.unmodifiableList(gameObjects);
    }
}
