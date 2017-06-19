package me.curiosus.games.snake101;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Copyright 2017, John E Peterson, All rights reserved.
 * jepeterson@gmail.com
 */
public class GameScreen extends ScreenAdapter {

    private static final float MOVE_TIME = 0.25f;
    private static final int SPEED = 128;
    private static final int WORLD_WIDTH = 1408;
    private static final int WORLD_HEIGHT = 2560;
    private static final String GAME_OVER_TEXT = "Game Over... Tap to restart!";
    public static final int POINTS = 10;

    private Direction snakeDirection = Direction.RIGHT;
    private float timer = MOVE_TIME;
    private SpriteBatch batch;
    private Texture snakeHead;
    private Texture snakeBody;
    private float snakeX = 0.0f;
    private float snakeY = 0.0f;
    private Texture apple;
    private boolean applePlaced;
    private int appleX;
    private int appleY;
    private Array<SnakeBodyPart> snakeBodyParts;
    private float snakeXPrevious = 0.0f;
    private float snakeYPrevious = 0.0f;
    private ShapeRenderer shapeRenderer;
    private SwipeDirectionListener swipeDirectionListener;
    private GameState gameState;
    private BitmapFont bitmapFont;
    private GlyphLayout layout;
    private int score;
    private BitmapFont scoreFont;

    private Viewport viewport;
    private Camera camera;

    public GameScreen(SwipeDirectionListener listener) {
        swipeDirectionListener = listener;
        gameState = GameState.PLAYING;
        layout = new GlyphLayout();
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(WORLD_WIDTH / 2.0f, WORLD_HEIGHT / 2.0f, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH + 0.0f, WORLD_HEIGHT + 0.0f);
        batch = new SpriteBatch();

        snakeHead = new Texture(Gdx.files.internal("head.png"));
        snakeBody = new Texture(Gdx.files.internal("body.png"));
        apple = new Texture(Gdx.files.internal("apple.png"));
        snakeBodyParts = new Array<SnakeBodyPart>();
        shapeRenderer = new ShapeRenderer();
        bitmapFont = new BitmapFont();
        bitmapFont.getData().setScale(5.0f, 5.0f);
        scoreFont = new BitmapFont();
        scoreFont.getData().setScale(5.0f, 5.0f);

    }

    @Override
    public void render(float delta) {
        switch (gameState) {
            case PLAYING: {
                timer -= delta;
                if (timer <= 0.0f) {
                    timer = MOVE_TIME;
                    moveSnake();
                    checkForOutOfBounds();
                    updateSnakeBody();
                    checkSnakeSelfCollision();
                    checkAppleCollision();
                    placeApple();
                }
                break;
            }
            case GAME_OVER: {
                if (Gdx.input.isTouched()) {
                    playAgain();
                }
                break;
            }
        }
        clearScreen();
        drawGrid();
        draw();
    }


    @Override
    public void dispose() {
        batch.dispose();
        snakeHead.dispose();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(.5f, .75f, .4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void checkForOutOfBounds() {
        if (snakeX >= WORLD_WIDTH) {
            snakeX = 0;
        }

        if (snakeX < 0) {
            snakeX = WORLD_WIDTH - SPEED;
        }

        if (snakeY >= WORLD_HEIGHT) {
            snakeY = 0;
        }

        if (snakeY < 0) {
            snakeY = WORLD_HEIGHT - SPEED;
        }

    }

    private boolean isReverse(Direction proposedDirection) {
        if (snakeBodyParts.size > 0) {
            if (snakeDirection == Direction.UP && proposedDirection == Direction.DOWN) {
                return true;
            } else if (snakeDirection == Direction.DOWN && proposedDirection == Direction.UP) {
                return true;
            } else if (snakeDirection == Direction.LEFT && proposedDirection == Direction.RIGHT) {
                return true;
            } else if (snakeDirection == Direction.RIGHT && proposedDirection == Direction.LEFT) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void moveSnake() {
        snakeXPrevious = snakeX;
        snakeYPrevious = snakeY;
        Direction proposedDirection = swipeDirectionListener.getDirection();
        if (!isReverse(proposedDirection)) {
            snakeDirection = proposedDirection;
        }
        if (snakeDirection != null) {
            switch (snakeDirection) {
                case RIGHT:
                    snakeX += SPEED;
                    break;
                case LEFT:
                    snakeX -= SPEED;
                    break;
                case UP:
                    snakeY += SPEED;
                    break;
                case DOWN:
                    snakeY -= SPEED;
                    break;
            }
        }
    }

    private void placeApple() {
        if (!applePlaced) {
            do {
                appleX = MathUtils.random(((int)viewport.getWorldWidth()) / SPEED - 1) * SPEED;
                appleY = MathUtils.random(((int)viewport.getWorldHeight()) / SPEED - 1) * SPEED;
                applePlaced = true;
            } while (appleX == snakeX && appleY == snakeY);
        }
    }

    private void checkAppleCollision() {
        if (applePlaced && appleX == snakeX && appleY == snakeY) {
            Vector2 snakePosition = new Vector2(snakeX, snakeY);
            SnakeBodyPart snakeBodyPart = new SnakeBodyPart(snakeBody, snakePosition);
            snakeBodyPart.updateBodyPosition(snakeX, snakeY);
            snakeBodyParts.insert(0, snakeBodyPart);
            addToScore();
            applePlaced = false;
        }
    }

    private void checkSnakeSelfCollision() {
        for (SnakeBodyPart snakeBodyPart : snakeBodyParts) {
            if (snakeBodyPart.getX() == snakeX && snakeBodyPart.getY() == snakeY) {
                gameState = GameState.GAME_OVER;
                score = 0;
            }
        }
    }

    private void updateSnakeBody() {
        if (snakeBodyParts.size > 0) {
            SnakeBodyPart snakeBodyPart = snakeBodyParts.removeIndex(0);
            snakeBodyPart.updateBodyPosition(snakeXPrevious, snakeYPrevious);
            snakeBodyParts.add(snakeBodyPart);
        }
    }

    private void draw() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        batch.draw(snakeHead, snakeX, snakeY);
        for (SnakeBodyPart snakeBodyPart : snakeBodyParts) {
            snakeBodyPart.draw(batch);
        }
        if (applePlaced) {
            batch.draw(apple, appleX, appleY);
        }
        if (gameState == GameState.GAME_OVER) {
            layout.setText(bitmapFont, GAME_OVER_TEXT);
            bitmapFont.draw(batch, GAME_OVER_TEXT, (WORLD_WIDTH - layout.width) / 2, (WORLD_HEIGHT - layout.height) / 2);
        }
        showScore();
        batch.end();
    }

    private void drawGrid() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int x = 0; x < WORLD_WIDTH; x += SPEED) {
            for (int y = 0; y < WORLD_HEIGHT; y += SPEED) {
                shapeRenderer.rect(x, y, SPEED, SPEED);
            }
        }
        shapeRenderer.end();
    }

    private void addToScore() {
        score += POINTS;
    }

    private void showScore() {
            String scoreText = Integer.toString(score);
            scoreFont.setColor(Color.BLACK);
            layout.setText(scoreFont, scoreText);
            scoreFont.draw(batch, scoreText, ((WORLD_WIDTH - layout.width)  / 2), WORLD_HEIGHT - layout.height);
    }

    private void playAgain() {
        reset();
        timer = MOVE_TIME;
        gameState = GameState.PLAYING;
    }

    private void reset() {
        snakeBodyParts.clear();
        snakeX = snakeY = snakeXPrevious = snakeYPrevious = 0.0f;
        snakeDirection = Direction.RIGHT;
        applePlaced = false;
        score = 0;
    }
}
