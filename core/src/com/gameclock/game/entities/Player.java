package com.gameclock.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.gameclock.game.SheepJump;

/**
 * Created by Jason on 2/5/2016.
 */
public class Player extends B2DSprite {

    public Player(Body body) {
        super(body);

        Texture texture = SheepJump.res.getTexture("player");
        TextureRegion[] sprites = TextureRegion.split(texture, 32, 32)[0];
        setAnimation(sprites, 1 / 12f);
    }


}
