package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Helicopter {

    int x, y, vx, vy, frame, screenX, screenY;
    Sprite[] textures;

    Helicopter(String[] frames, int screenX, int screenY){
        vy = vx = vy = frame = 0;

        x = screenX / 6;
//        x = 0;
        y = screenY /2;

        textures = new Sprite[frames.length];
        for (int i = 0; i < frames.length; i++)
            textures[i] = new Sprite(new Texture(frames[i]));

        this.screenX = screenX;
        this.screenY = screenY;
    }

    public void render(SpriteBatch batch){

//        x += vx;
//        y += vy;
//
//        if(0 > y || y > screenY) {vy *= -1;}
//        if(0 > x || x > screenX) {vx *= -1; flipFrames();}


//        int bufferX = 198;
        int bufferY = 198;

//        if(0 < getTouchX() && getTouchX() < screenX - bufferX) x = getTouchX();
        if(0 < getTouchY() && getTouchY() < screenY - bufferY) y = getTouchY();

        frame = (frame + 1) % textures.length;
        batch.draw(textures[frame], x, y);
        printPosition(batch);
    }

    private void flipFrames() {for (Sprite texture : this.textures) texture.flip(true, false);}

    private void printPosition(SpriteBatch batch){
//        BitmapFont font = new BitmapFont(); //or use alex answer to use custom font
//        font.getData().setScale(4,4);
//        font.draw(batch, "X: " + x + "  Y: " + y, 0, screenY);

//        font.draw(batch, "X: " + getTouchX() + "  Y: " + getTouchY(), 0, screenY - 50);


    }

//    private boolean helicoptersIntersect(Helicopter other){
//        Rectangle r1 = new Rectangle(this.x, this.y, bufferX, bufferY);
//        Rectangle r2 = new Rectangle(other.x, other.y, bufferX, bufferY);
//        return r1.overlaps(r2);
//    }

    private int getTouchX(){ return Gdx.input.getX();}
    private int getTouchY(){ return screenY - Gdx.input.getY();}

    static void print(String s){ System.out.println(s);}
    static void print(int s){ System.out.println(s);}

}
