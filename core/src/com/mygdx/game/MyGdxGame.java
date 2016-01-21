package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends Game {

	public SpriteBatch batch;
	public BitmapFont font;
	public MyMenuScreen menuScreen;
	public GameScreen gameScreen;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.getData().setScale(2);
		menuScreen = new MyMenuScreen(this);
		gameScreen = new GameScreen(this);
		setScreen(menuScreen);
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
	}
}
