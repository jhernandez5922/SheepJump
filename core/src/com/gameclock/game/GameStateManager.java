package com.gameclock.game;

import java.util.Stack;

/**
 * Created by Jason on 2/5/2016.
 */
public class GameStateManager {

    private SheepJump game;

    private Stack<com.gameclock.game.State.GameState> gameStack;

    public static final int PLAY = 42;

    public GameStateManager(SheepJump game) {
        this.game = game;
        gameStack = new Stack<com.gameclock.game.State.GameState>();
        pushState(PLAY);
    }

    public SheepJump game() {return game;}

    public void update(float dt) {
        gameStack.peek().update(dt);
    }

    public void render() {
        gameStack.peek().render();
    }

    private com.gameclock.game.State.GameState getState(int state) {
        if (state == PLAY) return new com.gameclock.game.State.Play(this);
        return null;
    }

    public void setState(int state) {
        popState();
        pushState(state);
    }

    public void pushState(int state) {
        gameStack.push(getState(state));
    }

    public void popState() {
        com.gameclock.game.State.GameState g = gameStack.pop();
        g.dispose();
    }


}
