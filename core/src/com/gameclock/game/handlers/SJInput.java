package com.gameclock.game.handlers;

/**
 * Created by Jason on 2/5/2016.
 */
public class SJInput {

    public static boolean [] keys;
    public static boolean [] pKeys;

    public static final int NUM_KEYS = 4;
    public static final int BUTTON1 = 0;
    public static final int BUTTON2 = 1;
    public static final int FORWARD_BUTTON = 2;
    public static final int BACKWARD_BUTTON = 3;

    static {
        keys = new boolean[NUM_KEYS];
        pKeys = new boolean[NUM_KEYS];
    }

    public static void update() {
        System.arraycopy(keys, 0, pKeys, 0, NUM_KEYS);
    }


    public static void setKey(int i, boolean b) {keys[i] = b;}
    public static boolean isDown(int i) { return keys[i];}
    public static boolean isPressed(int i ) {return keys[i] && !pKeys[i];}
}
