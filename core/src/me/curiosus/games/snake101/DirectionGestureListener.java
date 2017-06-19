package me.curiosus.games.snake101;

import com.badlogic.gdx.input.GestureDetector;

/**
 * Copyright 2017, John E Peterson, All rights reserved.
 * jepeterson@gmail.com
 */
public class DirectionGestureListener extends GestureDetector.GestureAdapter {

    private DirectionListener directionListener;

    public DirectionGestureListener(DirectionListener listener) {
        directionListener = listener;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if (Math.abs(velocityX) > Math.abs(velocityY)) {
            if (velocityX > 0.0f) {
                directionListener.onRight();
            } else {
                directionListener.onLeft();
            }
        } else {
            if (velocityY > 0.0f) {
                directionListener.onDown();
            } else {
                directionListener.onUp();
            }
        }
        return super.fling(velocityX, velocityY, button);
    }
}
