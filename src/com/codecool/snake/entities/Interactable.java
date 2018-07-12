package com.codecool.snake.entities;

import com.codecool.snake.entities.snakes.SnakeHead;
import com.codecool.snake.entities.weapon.Bullet;
import com.codecool.snake.entities.weapon.Laser;

// interface that all game objects that can be interacted with must implement.
public interface Interactable {

    void apply(SnakeHead snakeHead);

    default void apply (Bullet bullet){
        return;
    }

    default void apply (Laser laserBullet){
        return;
    }

    String getMessage();

}
