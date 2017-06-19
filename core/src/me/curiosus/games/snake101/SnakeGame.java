package me.curiosus.games.snake101;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class SnakeGame extends Game {

	@Override
	public void create () {
        SwipeDirectionListener swipeDirectionListener = new SwipeDirectionListener();
        Gdx.input.setInputProcessor(new SwipeListener(swipeDirectionListener));
        setScreen(new GameScreen(swipeDirectionListener));
	}

}
