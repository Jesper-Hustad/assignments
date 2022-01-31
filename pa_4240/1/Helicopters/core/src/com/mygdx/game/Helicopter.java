package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;


import java.util.Random;

public class Helicopter {

    int x, y, vx, vy, frame, screenX, screenY;
    Animation<TextureRegion> textures;
    boolean isFlipped;
    Helicopter[] others;

    static int bufferX = 130;
    static int bufferY = 52;

    Helicopter(Texture spritesheet, Helicopter[] others, int screenX, int screenY){
        frame = 0;

        vy = new Random().nextInt(5) + 2;
        vx = new Random().nextInt(5) + 2;


        x = new Random().nextInt(screenX - bufferX);
        y = new Random().nextInt(screenY - bufferY);


        int a = new Random().nextInt(8) + 1;

        TextureRegion[] bounce_frames = TextureRegion.split(spritesheet, spritesheet.getWidth()/4, spritesheet.getHeight())[0];
        this.textures = new Animation<TextureRegion>(0.1f, bounce_frames);

        this.screenX = screenX;
        this.screenY = screenY;
        this.others = others;

        flipFrames();
    }

    public void render(SpriteBatch batch, float dt){

        x += vx;
        y += vy;

        boolean doesCollide = calculateCollision();

        if(doesCollide){
            vy *= -1; vx *= -1; flipFrames();
        }

        if(0 > y || y > screenY - bufferY) {vy *= -1;}
        if(0 > x || x > screenX - bufferX) {vx *= -1; flipFrames();}

        TextureRegion frame = (TextureRegion) textures.getKeyFrame(dt, true);

//        frame = (frame + 1) % textures.length;
        batch.draw(frame, x, y);
//        printPosition(batch);
    }

    private boolean calculateCollision(){
        for( Helicopter h : others){
            if(this.equals(h)) continue; // skip if current helicopter is same as this

            if(helicoptersIntersect(h)) return true;
        }
        return false;
    }

    private boolean helicoptersIntersect(Helicopter other){
        Rectangle r1 = new Rectangle(this.x, this.y, bufferX, bufferY);
        Rectangle r2 = new Rectangle(other.x, other.y, bufferX, bufferY);
        return r1.overlaps(r2);
    }

    private void flipFrames() {
        for (TextureRegion i : (TextureRegion[]) textures.getKeyFrames()) {
            i.flip(true, false);
        }      
        
    }

    private void printPosition(SpriteBatch batch){
        BitmapFont font = new BitmapFont(); //or use alex answer to use custom font
        font.getData().setScale(4,4);
        font.draw(batch, "COLLISION", 0, screenY);

//        font.draw(batch, "X: " + getTouchX() + "  Y: " + getTouchY(), 0, screenY - 50);


    }

    private int getTouchX(){ return Gdx.input.getX();}
    private int getTouchY(){ return screenY - Gdx.input.getY();}

    static void print(String s){ System.out.println(s);}
    static void print(int s){ System.out.println(s);}

}
