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
import com.gameclock.game.entities.Obstacle;
import com.gameclock.game.entities.Player;
import com.gameclock.game.handlers.B2DVars;
import com.gameclock.game.handlers.SJContactListener;
import com.gameclock.game.handlers.SJInput;

import java.util.ArrayList;
import java.util.Locale;

;

/**
 * Created by Jason on 2/5/2016.
 */
public class Play extends com.gameclock.game.State.GameState {

    private World world;
    private Box2DDebugRenderer dDebugRenderer;
    private OrthographicCamera b2dCam;
    private Player player;
    private ArrayList<Obstacle> obstacles;
    private SJContactListener contactListener;

    private TiledMap tiledMap;
    private float tileSize;
    private OrthogonalTiledMapRenderer tmr;
    private float xPos;

    public Play(GameStateManager gsm) {
        super(gsm);
        //set up Box2d world
        world = new World(new Vector2(0, -9.81f), true);
        contactListener = new SJContactListener();
        world.setContactListener(contactListener);
        dDebugRenderer = new Box2DDebugRenderer();
        obstacles = new ArrayList<Obstacle>();
        createPlayer();
        createTiles();
    }

    @Override
    public void handleInput() {
        //player jump
        if (SJInput.isPressed(SJInput.BUTTON1)) {
            jump();
        }
        System.out.println(SJInput.isDown() + " " + SJInput.x + " " + Gdx.graphics.getWidth());
        if (SJInput.isPressed()) {
            if (SJInput.x > Gdx.graphics.getWidth() / 3 && SJInput.x < (Gdx.graphics.getWidth() * 2) / 3)
                jump();
        }
        if (SJInput.isDown()) {
            if (SJInput.x < Gdx.graphics.getWidth() / 3) {
                moveBackwards();
            } else if (SJInput.x >= (Gdx.graphics.getWidth() * 2) / 3) {
                moveForward();
            }
            else {
                stopMoving();
            }
        }
        //Move Forward
        else if (SJInput.isDown(SJInput.FORWARD_BUTTON)) {
            moveForward();
        }
        //Move backward
        else if (SJInput.isDown(SJInput.BACKWARD_BUTTON)) {
           moveBackwards();
        }
        else {
            stopMoving();
        }
    }

    public void jump() {
        if (contactListener.isOnGround())
            player.getBody().applyForceToCenter(25, 275, true);
        else if (!contactListener.hasBoosted()) {
            contactListener.boost();
            player.getBody().applyForceToCenter(50, 250, true);
        }
    }

    public void moveForward() {
        player.setStationary(false);
        if (contactListener.isOnGround()) {
            player.getBody().setLinearVelocity(.65f, player.getBody().getLinearVelocity().y);
        }
    }

    public void moveBackwards() {
        player.setStationary(false);
        if (contactListener.isOnGround())
            player.getBody().setLinearVelocity(-.65f, player.getBody().getLinearVelocity().y);
        else {
            //Allow user to move back when in the air, for more accuracy
            float newVelocity = player.getBody().getLinearVelocity().x - .05f;
            if (newVelocity < -5f)
                newVelocity = 0;
            player.getBody().setLinearVelocity(newVelocity,
                    player.getBody().getLinearVelocity().y);
        }
    }
    public void stopMoving() {
        //if on the ground, do not move in x direction while preserving y velocity
        if (contactListener.isOnGround()) {
            player.getBody().setLinearVelocity(0, player.getBody().getLinearVelocity().y);
            player.setStationary(true);
        }
    }
    @Override
    public void update(float dt) {
        handleInput();
        world.step(dt, 6, 2);
        //set camera to follow player, unless at border
        xPos = player.getPosition().x * B2DVars.PPM + SheepJump.V_WIDTH / 4;
        //if at beginning
        if (xPos <= B2DVars.START_WIDTH + SheepJump.V_WIDTH / 4)
            xPos = B2DVars.START_WIDTH + SheepJump.V_WIDTH / 4;
            //if at end
        else if (xPos >= B2DVars.END_WIDTH)
            xPos = B2DVars.END_WIDTH;
        player.update(dt);
        //update obstacles
        for (Obstacle obstacle : obstacles) {
            obstacle.update(dt);
        }
    }
    @Override
    public void render() {
        //clear screen
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //update position
        cam.position.set(
                xPos,
                SheepJump.V_HEIGHT / 2,
                0);
        cam.update();
        //draw title map
        tmr.setView(cam);
        tmr.render();
        sb.setProjectionMatrix(cam.combined);
        player.render(sb);
        for (Obstacle obstacle : obstacles)
                obstacle.render(sb);
        //draw box2d world
//        b2dCam.position.set(
//                xPos,
//                SheepJump.V_HEIGHT / 2,
//                0);
        //b2dCam.update();
        //dDebugRenderer.render(world, b2dCam.combined);
    }

    @Override
    public void dispose() {    }

    private void createPlayer() {
        //Tile modifiers
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        // Create Player
        bodyDef.position.set(B2DVars.START_WIDTH / B2DVars.PPM, B2DVars.START_HEIGHT / B2DVars.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearVelocity.set(.65f, 0);
        Body body = world.createBody(bodyDef);
        shape.setAsBox(13 / B2DVars.PPM, 13 / B2DVars.PPM);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fixtureDef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_OBSTACLE;
        body.createFixture(fixtureDef).setUserData("player");

        //create foot sensor
        shape.setAsBox(12 / B2DVars.PPM, 1 / B2DVars.PPM, new Vector2(0, -12 / B2DVars.PPM), 0);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fixtureDef.filter.maskBits = -1;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("foot");
        shape.dispose();
        //Set up box 2d cam
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, SheepJump.V_WIDTH / B2DVars.PPM, SheepJump.V_HEIGHT / B2DVars.PPM);
        player = new Player(body);
    }

    private void createTiles() {
        //load tile map
        tiledMap = new TmxMapLoader().load(B2DVars.PATH_TO_MAP);
        tmr = new OrthogonalTiledMapRenderer(tiledMap);
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("foreground");
        tileSize = layer.getTileWidth();
        createLayer(layer, B2DVars.BIT_GROUND);
        createBoxes(layer);
        layer = (TiledMapTileLayer) tiledMap.getLayers().get("background_items");
        createLayer(layer, B2DVars.BIT_GROUND);
    }


    private void createBoxes(TiledMapTileLayer layer) {
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(35 / B2DVars.PPM, 35 / B2DVars.PPM);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = B2DVars.BIT_OBSTACLE;
        fixtureDef.filter.maskBits = -1;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        for (int col = 2; col < layer.getWidth() - 5; col++) {
            int box = (int) (Math.random() * 2);
            int boxHeight = (int) (Math.random() * 3) + 1;
            if (box == 1) {
                for (int i = 0; i < boxHeight; i++) {
                    bodyDef.position.set(
                            (col + 1.5f) * tileSize / B2DVars.PPM,
                            (i + 1.5f) * tileSize / B2DVars.PPM
                    );
                    Body body = world.createBody(bodyDef);
                    body.setUserData("obstacle");
                    body.createFixture(fixtureDef);
                    obstacles.add(new Obstacle(body));
                    System.out.println(String.format(Locale.US, "Col: %f Row: %f", col + .5f, boxHeight + 1.5f));
                }
            }
        }
    }

    private void createLayer(TiledMapTileLayer layer, short bits) {
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        //go through cells in the layer
        for (int row  = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {
                //1 in 10 chance to generate box
                //Create a cell if tile exists
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                if (cell == null) continue;
                if (cell.getTile() == null) continue;

                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set(
                        (col + 0.5f) * tileSize / B2DVars.PPM,
                        (row + 0.5f) * tileSize / B2DVars.PPM
                );
                ChainShape chainShape = new ChainShape();
                Vector2[] v = new Vector2[4];
                v[1] = new Vector2(
                        -tileSize / 2 /B2DVars.PPM,
                        tileSize / 2/ B2DVars.PPM
                );
                v[2] = new Vector2(
                        tileSize / 2 / B2DVars.PPM,
                        tileSize / 2 / B2DVars.PPM
                );
                v[3] = new Vector2(
                        tileSize / 2 /B2DVars.PPM,
                        -tileSize / 2 / B2DVars.PPM
                );
                v[0] = new Vector2(
                        -tileSize / 2 / B2DVars.PPM,
                        -tileSize / 2 / B2DVars.PPM
                );
                chainShape.createChain(v);
                fixtureDef.friction = 0;
                fixtureDef.restitution = .2f;
                fixtureDef.density = 0.0f;
                fixtureDef.shape = chainShape;
                fixtureDef.filter.categoryBits = bits;
                fixtureDef.filter.maskBits = -1;
                fixtureDef.isSensor = false;
                Body body = world.createBody(bodyDef);
                body.setUserData("ground");
                body.createFixture(fixtureDef);
            }
        }
    }
}
