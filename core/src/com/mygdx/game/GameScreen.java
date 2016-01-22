package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by newmanne on 2016-01-18.
 */
public class GameScreen implements Screen{

    private static final long REQUIRED_SCORE = 75;
    private final MyGdxGame game;
    private final OrthographicCamera camera;
    private Texture beerImage;
    private List<Texture> textbookTextures;
    private Array<Textbook> textbooks;
    private Rectangle beer;

    private long lastSpawnTime = TimeUtils.nanoTime();

    public static final int SCREEN_WIDTH = 960;
    public static final int SCREEN_HEIGHT = 640;

    public static final int BEER_HEIGHT = 64;
    public static final int BEER_WIDTH = 64;

    public static final int TEXTBOOK_WIDTH = 200;
    public static final int TEXTBOOK_HEIGHT = 150;

    final int N_TEXTBOOKS = 15;

    public static final int BEER_SPEED = 400;

    private long score;

    private final Vector3 touchPoint = new Vector3();
    private boolean dragging = false;
    private boolean draggingLeft = false;


    public static class Textbook {

        public Textbook(Rectangle rect, Texture text, long speed) {
            this.rect = rect;
            this.text = text;
            this.speed = speed;
        }

        Rectangle rect;
        Texture text;
        long speed;
    }

    public GameScreen(MyGdxGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        beerImage = new Texture("beer.png");
        beer = new Rectangle();
        beer.x = SCREEN_WIDTH - BEER_WIDTH /2;
        beer.y = 20;
        beer.width = BEER_WIDTH;
        beer.height = BEER_HEIGHT;
        textbookTextures = new ArrayList<Texture>();
        for (int i = 0; i < N_TEXTBOOKS; i++) {
            try {
                textbookTextures.add(new Texture("textbook" + i + ".jpg"));
            } catch (Exception e) {
                Gdx.app.error("ERROR", "Couldn't load texture " + i, e);
            }
        }
        if (textbookTextures.size() != N_TEXTBOOKS) {
            Gdx.app.error("ERROR", "COUDLNT LOAD TEXTBOOKS PROPERLY");
            Gdx.app.exit();
        }
        textbooks = new Array<Textbook>();
    }

    @Override
    public void show() {
        beer.x = SCREEN_WIDTH - BEER_WIDTH /2;
        beer.y = 20;
        score = 0;
        textbooks.clear();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the bucket and
        // all drops
        game.batch.begin();
        game.batch.draw(beerImage, beer.x, beer.y, beer.width, beer.height);
        game.font.draw(game.batch, "Lectures avoided: " + score, 50, SCREEN_HEIGHT - 50);
        for (Textbook raindrop : textbooks) {
            game.batch.draw(raindrop.text, raindrop.rect.x, raindrop.rect.y, raindrop.rect.width, raindrop.rect.height);
        }
        // TODO: need mobile controls
        if ((dragging && draggingLeft) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            beer.x -= BEER_SPEED * Gdx.graphics.getDeltaTime();
        } else if ((dragging && !draggingLeft) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            beer.x += BEER_SPEED * Gdx.graphics.getDeltaTime();
        }
        // make sure the bucket stays within the screen bounds
        beer.x = Math.max(beer.x, 0);
        if(beer.x > SCREEN_WIDTH - BEER_WIDTH) {
            beer.x = SCREEN_WIDTH - BEER_WIDTH;
        }

        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button != Input.Buttons.LEFT || pointer > 0) return false;
                camera.unproject(touchPoint.set(screenX, screenY, 0));
                dragDir();
                dragging = true;
                return true;
            }

            private void dragDir() {
                draggingLeft = touchPoint.x < SCREEN_WIDTH / 2;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                dragging = false;
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                camera.unproject(touchPoint.set(screenX, screenY, 0));
                dragDir();
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                return false;
            }
        });


        game.batch.end();

        if(TimeUtils.nanoTime() - lastSpawnTime > Math.max(250000000, 1000000000 - score * 10000000)) {
            Rectangle textbook = new Rectangle();
            textbook.x = MathUtils.random(0, SCREEN_WIDTH - 64);
            textbook.y = SCREEN_HEIGHT;
            textbook.width = TEXTBOOK_WIDTH;
            textbook.height = TEXTBOOK_HEIGHT;
            lastSpawnTime = TimeUtils.nanoTime();
            long speed = score * 7 + MathUtils.random(150, 300);
            Textbook textbook1 = new Textbook(textbook, textbookTextures.get(MathUtils.random(0, textbookTextures.size() - 1)), speed);
            textbooks.add(textbook1);
        }

        Iterator<Textbook> iter = textbooks.iterator();
        while (iter.hasNext()) {
            Textbook next = iter.next();
            Rectangle raindrop = next.rect;
            raindrop.y -= next.speed * Gdx.graphics.getDeltaTime();
            if (raindrop.y + TEXTBOOK_HEIGHT < 0) {
                iter.remove();
                score += 1;
            }
            if (raindrop.overlaps(beer)) {
                // TOOD: game over!
                game.menuScreen.setUnlocked(score >= REQUIRED_SCORE, score);
                game.setScreen(game.menuScreen);
                break;
            }
        }



    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
