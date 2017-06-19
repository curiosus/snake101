package me.curiosus.games.snake101;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

/**
 * Copyright 2017, John E Peterson, All rights reserved.
 * jepeterson@gmail.com
 */
public class SnakeBodyPart {

    private float x;
    private float y;
    private Texture texture;
    private Vector2 snakePosition;


    public SnakeBodyPart(Texture texture, Vector2 snakePosition) {
        this.texture = texture;
        this.snakePosition = snakePosition;
    }

    public void updateBodyPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Batch batch) {
        if (!(x == snakePosition.x && y == snakePosition.y)) {
           batch.draw(texture, x, y);
        }
    }

    float getX() {
        return x;
    }

    float getY() {
        return y;
    }




}
