package me.curiosus.games.snake101;

/**
 * Copyright 2017, John E Peterson, All rights reserved.
 * jepeterson@gmail.com
 */
public class SwipeDirectionListener implements DirectionListener {

    private Direction direction;

    @Override
    public void onLeft() {
       direction = Direction.LEFT;
    }

    @Override
    public void onRight() {
        direction = Direction.RIGHT;
    }

    @Override
    public void onUp() {
        direction = Direction.UP;
    }

    @Override
    public void onDown() {
        direction = Direction.DOWN;
    }

    public Direction getDirection() {
        return direction;
    }
}
