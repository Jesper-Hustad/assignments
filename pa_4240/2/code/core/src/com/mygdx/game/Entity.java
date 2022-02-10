package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

abstract class Entity {

    Rectangle shape;

    Entity() {}

    public abstract void render(SpriteBatch batch);
}
