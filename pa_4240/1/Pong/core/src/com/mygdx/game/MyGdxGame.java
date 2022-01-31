package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;



public class MyGdxGame extends ApplicationAdapter {

	SpriteBatch batch;
	Texture img;

	Rectangle ball;
	Texture ballTexture;
	int ballVx = 8;
	int ballVy = 8;
	int screenX;
	int screenY;

	Rectangle playerPaddle;
	Helicopter helicopter;

	Rectangle enemyPaddle;
	Texture paddleTexture;

	static String[] hcTextures = new String[]{"paddle.png"};

	int p1Points = 0;
	int p2Points = 0;

	@Override
	public void create () {

		// set size for desktop executable jar
		Gdx.graphics.setWindowedMode(1600, 900);

		this.screenX = Gdx.graphics.getWidth();
		this.screenY = Gdx.graphics.getHeight();

		batch = new SpriteBatch();
		helicopter = new Helicopter(hcTextures, screenX, screenY);


		ball = new Rectangle(screenX/2,screenY/2,34,34);
		ballTexture = new Texture("ball.png");

		playerPaddle = new Rectangle(0, 0, 34, 198);
		enemyPaddle = new Rectangle((screenX/6)*5, screenY/2, 34, 198);
		paddleTexture = new Texture("paddle.png");

	}

	@Override
	public void render () {
		ScreenUtils.clear(0,0,0, 1);

		if(0 > ball.x || ball.x > screenX - 34) ballVx *= -1;
		if(0 > ball.y || ball.y > screenY - 34) ballVy *= -1;
		ball.x += ballVx;
		ball.y += ballVy;

		playerPaddle.x = helicopter.x;
		playerPaddle.y = helicopter.y;

		// basic ai
		enemyPaddle.y += (enemyPaddle.y - 45 + (enemyPaddle.height/2) - ball.y > 0) ? -7 : 6;

		// paddle hits ball
		if(playerPaddle.overlaps(ball) || enemyPaddle.overlaps(ball)){
			ballVx *= -1;

		}

		// p1 scores point
		if(ball.x - 10 > (screenX/6)*5){
			p1Points += 1;
			ballVx *= -1;
			ball.x = (screenX/6)*4;
			ball.y = screenY/2;
		}

		// p2 scores point
		if(ball.x +10 < (screenX/6) + 34){
			p2Points += 1;
			ballVx *= -1;
			ball.x = (screenX/6)*2;
			ball.y = screenY/2;
		}



		batch.begin();

		if(p2Points >= 21 || p1Points >= 21){
			BitmapFont font = new BitmapFont();
			font.getData().setScale(4, 4);
			font.draw(batch, p1Points >= 21 ? "PLAYER 1 WINS": "PLAYER 2 WINS", screenX/2-300, screenY/2);
			ball.x = screenX/2;
			ball.y = screenY/2;
		}else {
			helicopter.render(batch);
			batch.draw(ballTexture, ball.x, ball.y);
			batch.draw(paddleTexture, enemyPaddle.x, enemyPaddle.y);

			BitmapFont font = new BitmapFont();
			font.getData().setScale(4, 4);
			font.draw(batch, "P1: " + p1Points + "       P2: " + p2Points, 0, screenY);
		}

		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

}
