package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by newmanne on 2016-01-18.
 */
public class MenuScreen implements Screen{

    private final MyGdxGame game;
    private final OrthographicCamera camera;
    private Texture beerImage;
    private List<Texture> textbookTextures;
    private Array<Textbook> textbooks;
    private Rectangle beer;

    public static class Textbook {

        public Textbook(Rectangle rect, Texture text) {
            this.rect = rect;
            this.text = text;
        }

        Rectangle rect;
        Texture text;
    }

    public MenuScreen(MyGdxGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        beerImage = new Texture("beer.png");
        beer = new Rectangle();
        beer.x = 800 / 2 - 64 /2;
        beer.y = 20;
        beer.width = 64;
        beer.height = 64;
        textbookTextures = new ArrayList<Texture>();
        for (FileHandle f : Gdx.files.internal("textbooks").list()) {
            textbookTextures.add(new Texture(f.path()));
        }

        textbooks = new Array<Textbook>();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
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
        game.font.draw(game.batch, "Drops Collected: ", 400, 480);
        for (Textbook raindrop : textbooks) {
            game.batch.draw(raindrop.text, raindrop.rect.x, raindrop.rect.y, raindrop.rect.width, raindrop.rect.height);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            beer.x -= 200 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            beer.x += 200 * Gdx.graphics.getDeltaTime();
        }
        // make sure the bucket stays within the screen bounds
        if(beer.x < 0) beer.x = 0;
        if(beer.x > 800 - 64) beer.x = 800 - 64;


        game.batch.end();

        if (MathUtils.randomBoolean(0.1f)) { // TODO: use time
            Rectangle raindrop = new Rectangle();
            raindrop.x = MathUtils.random(0, 800 - 64);
            raindrop.y = 480;
            raindrop.width = 64;
            raindrop.height = 64;
            textbooks.add(new Textbook(raindrop, textbookTextures.get(MathUtils.random(0, textbookTextures.size() - 1))));
        }

        Iterator<Textbook> iter = textbooks.iterator();
        while (iter.hasNext()) {
            Rectangle raindrop = iter.next().rect;
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (raindrop.y + 64 < 0)
                iter.remove();
            if (raindrop.overlaps(beer)) {
                // TOOD: game over!
                game.font.draw(game.batch, "GAME OVER: ", 0, 480);
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
