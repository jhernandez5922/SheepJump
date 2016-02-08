package com.gameclock.game.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

/**
 * Created by Jason on 2/5/2016.
 */
public class SJContent {

    private HashMap<String, Texture> textureMap;

    public SJContent() {
        textureMap = new HashMap<String, Texture>();
    }

    public void loadTexture(String path, String key) {
        Texture texture = new Texture(Gdx.files.internal(path));
        textureMap.put(key, texture);
    }

    public Texture getTexture(String key) { return textureMap.get(key);}


    public void disposeTexture(String key) {
        Texture texture = textureMap.get(key);
        if (texture != null) texture.dispose();
    }
}
