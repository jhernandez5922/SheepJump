package com.gameclock.game.handlers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by Jason on 2/5/2016.
 */
public class SJInputProcessor extends InputAdapter {

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.SPACE) {
            SJInput.setKey(SJInput.BUTTON1, true);
        }
        else if (keycode == Input.Keys.J) {
            SJInput.setKey(SJInput.BUTTON2, true);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            SJInput.setKey(SJInput.BUTTON1, false);
        }
        else if (keycode == Input.Keys.J) {
            SJInput.setKey(SJInput.BUTTON2, false);
        }
        return true;
    }
}
