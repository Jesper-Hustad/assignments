package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Observable;

public class Ball extends Entity{

    public Rectangle shape;
    public Texture t;
    CollideSingleton collideSingleton = CollideSingleton.getInstance();

    public int ballVx, ballVy, screenX, screenY, p1Points, p2Points;

    private PropertyChangeSupport changes = new PropertyChangeSupport(this);


    public Ball(Rectangle r, String path, int screenX, int screenY){
        this.shape = r;
        this.t = new Texture(path);
        ballVx = ballVy = 8;
        p1Points = p2Points = 0;
        this.screenX = screenX;
        this.screenY = screenY;
    }

    public void render(SpriteBatch batch){
        // update position
        shape.x += ballVx;
        shape.y += ballVy;

        // calculate potential new position
        Rectangle newPosX = new Rectangle(shape.x + ballVx, shape.y, shape.width, shape.height);
        Rectangle newPosY = new Rectangle(shape.x, shape.y + ballVx, shape.width, shape.height);

        // find if collision occurs
        boolean collidesX = collideSingleton.overlapsWith(newPosX);
        boolean collidesY = collideSingleton.overlapsWith(newPosY);

        // react to collision
        if(collidesX) ballVx *= -1;
        if(collidesY) ballVy *= -1;


        // p1 scores point
        if(shape.x - 10 > (screenX/6)*5){
            p1Points += 1;
            ballVx *= -1;
            shape.x = (screenX/6)*4;
            shape.y = screenY/2;
            changes.firePropertyChange("PLAYER 1", p1Points - 1, p1Points);
        }

        // p2 scores point
        if(shape.x +10 < (screenX/6) + 34){
            p2Points += 1;
            ballVx *= -1;
            shape.x = (screenX/6)*2;
            shape.y = screenY/2;
            changes.firePropertyChange("PLAYER 2", p2Points - 1, p2Points);
        }

        batch.draw(t, shape.x, shape.y);

    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }


}
