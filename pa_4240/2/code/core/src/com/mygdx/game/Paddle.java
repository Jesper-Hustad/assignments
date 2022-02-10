package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Paddle extends Entity{

    int x, y, vx, vy, frame, screenX, screenY;
    Rectangle shape;
    Sprite[] textures;

    Paddle(String[] frames, int screenX, int screenY){

        vx = vy = frame = 0;

        x = screenX / 6;
        y = screenY /2;

        shape = new Rectangle(x,y, 34, 198);
        CollideSingleton.getInstance().addRectangle(shape);

        textures = new Sprite[frames.length];
        for (int i = 0; i < frames.length; i++)
            textures[i] = new Sprite(new Texture(frames[i]));

        this.screenX = screenX;
        this.screenY = screenY;


    }

    public void render(SpriteBatch batch){

        int bufferY = 198;

        boolean isValidPosition = 0 < getTouchY() && getTouchY() < screenY - bufferY;

        if(isValidPosition) y = getTouchY();

        frame = (frame + 1) % textures.length;
        batch.draw(textures[frame], x, y);

        shape.setPosition(x, y);
    }

    private int getTouchY(){ return screenY - Gdx.input.getY();}
}
