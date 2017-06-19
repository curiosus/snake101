package me.curiosus.games.snake101;

import com.badlogic.gdx.input.GestureDetector;

/**
 * Copyright 2017, John E Peterson, All rights reserved.
 * jepeterson@gmail.com
 */
public class SwipeListener extends GestureDetector {

    public SwipeListener(DirectionListener listener) {
        super(new DirectionGestureListener(listener));
    }
}
