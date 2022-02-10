package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/**
 * Singleton class for a singular location where all collidable entities are stored
 */
public class CollideSingleton {

    private static CollideSingleton instance;
    ArrayList<Rectangle> collidable;

    private CollideSingleton() { collidable = new ArrayList<>(); }

    public static CollideSingleton getInstance() {
        if (instance == null) instance = new CollideSingleton();
        return instance;
    }

    public void addRectangle(Rectangle r){
        collidable.add(r);
    }


    public boolean overlapsWith(Rectangle r){
        for(Rectangle c : collidable)
            if(c.overlaps(r)) return true;

        return false;
    }

}
