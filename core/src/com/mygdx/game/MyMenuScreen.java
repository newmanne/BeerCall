package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by newmanne on 2016-01-18.
 */
public class MyMenuScreen implements Screen {

    final MyGdxGame game;
    private final OrthographicCamera camera;
    private boolean unlocked = false;
    private final static String VICTORY_MESSAGE = "Congrats on making it to beer call!\n Tonight's location is\n Yagger's Kits Restaurant & Sports Bar";
    private long score = 0;

    public MyMenuScreen(MyGdxGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        if (unlocked) {
            game.font.draw(game.batch, VICTORY_MESSAGE, 100, 450);
        } else {
            game.font.draw(game.batch, "Welcome to Beer Call", 100, 350);
            game.font.draw(game.batch, "Dodge textbooks and make it out to beer call!!!", 100, 250);
        }
        game.font.draw(game.batch, "Tap anywhere to begin!\n Use arrow keys or touch sides to control!", 100, 150);
        game.font.draw(game.batch, "High score is " + score, 100, 50);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
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

    public void setUnlocked(boolean unlocked, long score) {
        if (!this.unlocked) {
            this.unlocked = unlocked;
        }
        if (score > this.score) {
            this.score = score;
        }
    }
}
