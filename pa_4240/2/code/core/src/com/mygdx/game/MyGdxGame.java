package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Observable;
import java.util.Observer;


public class MyGdxGame extends ApplicationAdapter implements PropertyChangeListener {

	SpriteBatch batch;
	Texture img;

	Ball ball;

	int screenX;
	int screenY;

	Rectangle playerPaddle;
	Paddle paddle;

	Rectangle enemyPaddle;
	Texture paddleTexture;

	static String[] hcTextures = new String[]{"paddle.png"};

//	int p1Points = 0;
//	int p2Points = 0;

	CollideSingleton collideSingleton;

	String winningScreen = "";
	boolean playerWon = false;

	@Override
	public void create () {

		// set size for desktop executable jar
		Gdx.graphics.setWindowedMode(1600, 900);

		this.screenX = Gdx.graphics.getWidth();
		this.screenY = Gdx.graphics.getHeight();

		batch = new SpriteBatch();
		paddle = new Paddle(hcTextures, screenX, screenY);

		collideSingleton = CollideSingleton.getInstance();

		ball = new Ball(new Rectangle(screenX/2,screenY/2,34,34), "ball.png", screenX, screenY);

		ball.addPropertyChangeListener(this);

		playerPaddle = new Rectangle(0, 0, 34, 198);
		enemyPaddle = new Rectangle((screenX/6)*5, screenY/2, 34, 198);
		paddleTexture = new Texture("paddle.png");

		collideSingleton.addRectangle(playerPaddle);
		collideSingleton.addRectangle(enemyPaddle);

		// we can now replace wall bouncing logic with CollideSingleton
		collideSingleton.addRectangle(new Rectangle(-1,-1, screenX, 1));
		collideSingleton.addRectangle(new Rectangle(0,screenY, screenX, screenY + 1));

	}

	@Override
	public void render () {
		ScreenUtils.clear(0,0,0, 1);



		playerPaddle.x = paddle.x;
		playerPaddle.y = paddle.y;

		// basic ai
		enemyPaddle.y += (enemyPaddle.y - 45 + (enemyPaddle.height/2) - ball.shape.y > 0) ? -7 : 6;

		batch.begin();

		if(playerWon){
			BitmapFont font = new BitmapFont();
			font.getData().setScale(4, 4);
			font.draw(batch, winningScreen, screenX/2-300, screenY/2);
			ball.shape.x = screenX/2;
			ball.shape.y = screenY/2;
		}else {
			ball.render(batch);
			paddle.render(batch);
			batch.draw(paddleTexture, enemyPaddle.x, enemyPaddle.y);

			BitmapFont font = new BitmapFont();
			font.getData().setScale(4, 4);
			font.draw(batch, "P1: " + ball.p1Points + "       P2: " + ball.p2Points, 0, screenY);
		}

		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	@Override
	public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
		String player = propertyChangeEvent.getPropertyName();
		int points = (int) propertyChangeEvent.getNewValue();

		if(points < 21) return;

		winningScreen = player + " WINS!";
		playerWon = true;
	}
}
