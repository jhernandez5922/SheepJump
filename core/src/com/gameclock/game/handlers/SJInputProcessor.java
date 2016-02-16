package com.gameclock.game.handlers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by Jason on 2/5/2016.
 */
public class SJInputProcessor extends InputAdapter {

    public boolean mouseMoved(int x, int y) {
        SJInput.x = x;
        SJInput.y = y;
        return true;
    }

    public boolean touchDragged(int x, int y, int pointer) {
        SJInput.x = x;
        SJInput.y = y;
        SJInput.down = true;
        return true;
    }

    public boolean touchDown(int x, int y, int pointer, int button) {
        SJInput.x = x;
        SJInput.y = y;
        SJInput.down = true;
        return true;
    }

    public boolean touchUp(int x, int y, int pointer, int button) {
        SJInput.x = x;
        SJInput.y = y;
        SJInput.down = false;
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.SPACE) {
            SJInput.setKey(SJInput.BUTTON1, true);
        }
        else if (keycode == Input.Keys.J) {
            SJInput.setKey(SJInput.BUTTON2, true);
        }
        else if (keycode == Input.Keys.D) {
            SJInput.setKey(SJInput.FORWARD_BUTTON, true);
        }
        else if (keycode == Input.Keys.A) {
            SJInput.setKey(SJInput.BACKWARD_BUTTON, true);
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
        else if (keycode == Input.Keys.D) {
            SJInput.setKey(SJInput.FORWARD_BUTTON, false);
        }
        else if (keycode == Input.Keys.A) {
            SJInput.setKey(SJInput.BACKWARD_BUTTON, false);
        }
        return true;
    }
}
