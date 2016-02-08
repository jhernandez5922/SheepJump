package com.gameclock.game.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.gameclock.game.GameStateManager;
import com.gameclock.game.SheepJump;
import com.gameclock.game.entities.Player;
import com.gameclock.game.handlers.B2DVars;
import com.gameclock.game.handlers.SJContactListener;
import com.gameclock.game.handlers.SJInput;

;

/**
 * Created by Jason on 2/5/2016.
 */
public class Play extends com.gameclock.game.State.GameState {

    private World world;
    private Box2DDebugRenderer dDebugRenderer;
    private OrthographicCamera b2dCam;
    private Player player;
    private SJContactListener contactListener;

    private TiledMap tiledMap;
    private float tileSize;
    private OrthogonalTiledMapRenderer tmr;


    public Play(GameStateManager gsm) {
        super(gsm);

        //set up Box2d world
        world = new World(new Vector2(0, -9.81f), true);
        contactListener = new SJContactListener();
        world.setContactListener(contactListener);
        dDebugRenderer = new Box2DDebugRenderer();
        createPlayer();
        createTiles();
    }

    @Override
    public void handleInput() {

        //player jump
        if (SJInput.isPressed(SJInput.BUTTON1)) {
            if (contactListener.isOnGround())
                player.getBody().applyForceToCenter(0, 200, true);
            else if (!contactListener.hasBoosted()) {
                contactListener.boost();
                player.getBody().setLinearVelocity(2f, 0);
                player.getBody().applyForceToCenter(0, 250, true);
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        world.step(dt, 6, 2);
        player.update(dt);
    }

    @Override
    public void render() {

        //clear screen
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //set camera to follow player
        cam.position.set(
                player.getPosition().x * B2DVars.PPM + SheepJump.V_WIDTH / 4,
                SheepJump.V_HEIGHT / 2,
                0);

        cam.update();

        //draw title map
        tmr.setView(cam);
        tmr.render();

        sb.setProjectionMatrix(cam.combined);
        player.render(sb);

        //draw box2d world
        b2dCam.position.set(
                player.getPosition().x * B2DVars.PPM + SheepJump.V_WIDTH / 4,
                SheepJump.V_HEIGHT / 2,
                0
        );
        b2dCam.update();
        dDebugRenderer.render(world, b2dCam.combined);

    }

    @Override
    public void dispose() {

    }

    private void createPlayer() {
        //Tile modifiers
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();

        // Create Player
        bodyDef.position.set(160 / B2DVars.PPM, 200 / B2DVars.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearVelocity.set(.65f, 0);
        Body body = world.createBody(bodyDef);

        shape.setAsBox(13 / B2DVars.PPM, 13 / B2DVars.PPM);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fixtureDef.filter.maskBits = B2DVars.BIT_GROUND;
        body.createFixture(fixtureDef).setUserData("player");


        //create foot sensor
        shape.setAsBox(13 / B2DVars.PPM, 2 / B2DVars.PPM, new Vector2(0, -13 / B2DVars.PPM), 0);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fixtureDef.filter.maskBits = B2DVars.BIT_GROUND;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("foot");
        //Set up box 2d cam
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, SheepJump.V_WIDTH / B2DVars.PPM, SheepJump.V_HEIGHT / B2DVars.PPM);


        player = new Player(body);
    }

    private void createTiles() {
        //load tile map
        tiledMap = new TmxMapLoader().load("sheepJumpMap.tmx");

        tmr = new OrthogonalTiledMapRenderer(tiledMap);

        TiledMapTileLayer groundLayer = (TiledMapTileLayer) tiledMap.getLayers().get("foreground");

        tileSize = groundLayer.getTileWidth();
        createLayer(groundLayer, B2DVars.BIT_GROUND);


    }

    private void createLayer(TiledMapTileLayer layer, short bits) {
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        //go through cells in the layer
        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                if (cell == null) continue;
                if (cell.getTile() == null) continue;

                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set(
                        (col + 0.5f) * tileSize / B2DVars.PPM,
                        (row + 0.5f) * tileSize / B2DVars.PPM
                );

                ChainShape chainShape = new ChainShape();
                Vector2[] v = new Vector2[2];
                v[0] = new Vector2(
                        -tileSize / 2 /B2DVars.PPM,
                        tileSize / 2/ B2DVars.PPM
                );
                v[1] = new Vector2(
                        tileSize / 2 / B2DVars.PPM,
                        tileSize / 2 / B2DVars.PPM
                );
                chainShape.createChain(v);
                fixtureDef.friction = 0;
                fixtureDef.shape = chainShape;
                fixtureDef.filter.categoryBits = bits;
                fixtureDef.filter.maskBits = -1;
                fixtureDef.isSensor = false;
                world.createBody(bodyDef).createFixture(fixtureDef);

            }
        }
    }
}
