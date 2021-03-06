package com.gameclock.game.handlers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Jason on 2/5/2016.
 */
public class SJAnimation {

    private TextureRegion[] frames;
    private float time;
    private float delay;
    private int currentFrame;
    private int timesPlayed;
    private boolean stationary;
    private int stationaryFrame;

    public SJAnimation() {}

    public SJAnimation(TextureRegion[] frames) {
        this(frames, 1 / 12f);
    }

    public SJAnimation(TextureRegion[] frames, float delay) {

    }

    public void setFrames(TextureRegion[] frames, float delay) {
        this.frames = frames;
        this.delay = delay;
        stationaryFrame = 1;
        time = 0;
        currentFrame = 0;
        timesPlayed = 0;
    }

    public void update(float dt) {
        if (delay <= 0) return;
        time += dt;
        while (time >= delay) {
            step();
        }
    }

    private void step() {
        time -= delay;
        if (stationary) {
            currentFrame = stationaryFrame;
            return;
        }
        currentFrame++;
        if (currentFrame == frames.length) {
            currentFrame = 0;
            timesPlayed++;
        }
    }

    public TextureRegion getFrame() {
        return frames[currentFrame];
    }

    public void setStationary(boolean stationary) {
        this.stationary = stationary;
    }

    public float getTimesPlayed() {
        return timesPlayed;
    }
}
