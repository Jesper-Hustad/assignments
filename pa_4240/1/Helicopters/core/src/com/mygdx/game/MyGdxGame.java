package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;

public class MyGdxGame extends ApplicationAdapter {

	SpriteBatch batch;
	Texture img;

	Helicopter h1;
	Helicopter h2;
	Helicopter h3;
	Helicopter[] helicopters;
	TextureAtlas textureAtlas;

	static float state_time;


//	static String[] hcTextures = new String[]{"heli1.png", "heli2.png", "heli3.png", "heli4.png"};

	@Override
	public void create () {
		// set size for desktop executable jar
		Gdx.graphics.setWindowedMode(1600, 900);

		batch = new SpriteBatch();
		state_time = 0f;

		Texture spritesheet = new Texture("spritesheet.png");

		int hlCount = 6;
		helicopters = new Helicopter[hlCount];
		for (int i = 0; i < hlCount; i++) {
			helicopters[i] = new Helicopter(spritesheet, helicopters, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}

		int spriteX = 130;
		int spriteY = 52;
	}

	@Override
	public void render () {
		ScreenUtils.clear(135/255f, 206/255f, 235/255f, 1);

		state_time += Gdx.graphics.getDeltaTime();

		batch.begin();
		for(Helicopter h : helicopters) h.render(batch, state_time);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

}
