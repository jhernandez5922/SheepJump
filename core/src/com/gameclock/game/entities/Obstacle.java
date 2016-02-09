package com.gameclock.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.gameclock.game.SheepJump;

/**
 * Created by jason on 2/8/16.
 */
public class Obstacle extends B2DSprite {
    public Obstacle(Body body) {
        super(body);
        Texture texture = SheepJump.res.getTexture("obstacle");
        TextureRegion [] region = new TextureRegion[1];
        region[0] = new TextureRegion(texture);
        setAnimation(region, 1 / 12f);
    }
}
