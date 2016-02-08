package com.gameclock.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gameclock.game.handlers.SJContent;
import com.gameclock.game.handlers.SJInput;
import com.gameclock.game.handlers.SJInputProcessor;

public class SheepJump implements ApplicationListener {
	public static final int V_WIDTH = 600;
	public static final int V_HEIGHT = 460;
	public static SJContent res;


	public static final float STEP = 1 / 60f;
	private float accum;

    public SpriteBatch batch;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	private GameStateManager gsm;

	@Override
	public void create () {


        Gdx.input.setInputProcessor(new SJInputProcessor());

		res = new SJContent();
		res.loadTexture("bunny.png", "player");
		batch = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		gsm = new GameStateManager(this);
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render () {
		accum += Gdx.graphics.getDeltaTime();
		while (accum >= STEP) {
			accum -= STEP;
			gsm.update(STEP);
			gsm.render();
            SJInput.update();
		}
//		batch.setProjectionMatrix(hudCam.combined);
//		batch.begin();
//		batch.draw(res.getTexture("player"), 0, 0);
//		batch.end();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}




	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	public OrthographicCamera getCam() {
		return cam;
	}

	public OrthographicCamera getHudCam() {
		return hudCam;
	}
}
