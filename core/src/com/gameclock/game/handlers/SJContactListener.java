package com.gameclock.game.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Created by Jason on 2/5/2016.
 */
public class SJContactListener implements ContactListener {

    private int numFootContacts;
    private boolean boost;

    //Called when two fixtures start to collide
    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            numFootContacts++;
            boost = false;
            fa.getBody().setLinearVelocity(.65f, 0);
        }
        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            numFootContacts++;
            boost = false;
            fb.getBody().setLinearVelocity(.65f, 0);
        }


    }

    //Called when two fixtures no longer collide
    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            numFootContacts--;

        }
        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            numFootContacts--;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public boolean isOnGround() {
        return numFootContacts > 0;
    }

    public void boost() {
        boost = !boost;
    }

    public boolean hasBoosted() {
        return boost;
    }
}
