package HollowKnight.source.controller;

import HollowKnight.source.game_utils.Assets;
import HollowKnight.source.model.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class GameController {
    private static GameController instance;

    private Player player;

    private PlayerController playerController;

    private static final String[] MAP_IDS = {
        "greenpath_room1",
        "greenpath_room2"
    };

    private TiledMap currentMap;
    private int currentMapIndex = 0;

    private GameController(TiledMap map, int mapIndex) {
        this.currentMap = map;
        this.currentMapIndex = mapIndex;
    }

    public static GameController getInstance() {
        if (instance == null)
            instance = new GameController(Assets.getMap(0), 0);
        return instance;
    }

    public interface MapTransitionListener {
        void onTransition(int targetMapIndex, String spawnPointName);
    }

    private MapTransitionListener transitionListener;

    public void setTransitionListener(MapTransitionListener transitionListener) {
        this.transitionListener = transitionListener;
    }

    public void update(float delta) {
        if (playerController.hurtTimer > 0)
            playerController.hurtTimer -= delta;

        handleInput();
        player.update(delta);
        handleCollisions();
        handleSpikes();
        checkDoors();

        if (player.getPosition().y < -300f) {
            player.setPosition(200f, 300f);
            player.setVelocityY(0f);
        }
    }

    private void handleInput() {
        boolean left = Gdx.input.isKeyPressed(Keys.LEFT)
            || Gdx.input.isKeyPressed(Keys.A);

        boolean right = Gdx.input.isKeyPressed(Keys.RIGHT)
            || Gdx.input.isKeyPressed(Keys.D);

        boolean jump = Gdx.input.isKeyJustPressed(Keys.SPACE)
            || Gdx.input.isKeyJustPressed(Keys.W)
            || Gdx.input.isKeyJustPressed(Keys.UP);

        boolean attack = Gdx.input.isKeyJustPressed(Keys.Z)
            || Gdx.input.isKeyJustPressed(Keys.J);

        if (left && !right) playerController.moveLeft();
        else if (right && !left) playerController.moveRight();
        else playerController.stopHorizontal();

        if (jump) playerController.jump();
    }

    private void handleCollisions() {
        MapLayer layer = currentMap.getLayers().get("logic");
        if (layer == null) return;

        boolean touchingGround = false;
        Rectangle playerRect = new Rectangle(player.getBounds());

        for (MapObject obj : layer.getObjects()) {
            if (!isSolid(obj)) continue;

            Rectangle tile = ((RectangleMapObject) obj).getRectangle();
            Rectangle overlap = new Rectangle();

            if (!Intersector.intersectRectangles(playerRect, tile, overlap)) continue;

            if (overlap.width < overlap.height) {
                if (playerRect.x < tile.x)
                    player.setPosition(player.getPosition().x - overlap.width, player.getPosition().y);
                else
                    player.setPosition(player.getPosition().x + overlap.width, player.getPosition().y);

            }
            else {
                float playerMidY = playerRect.y + playerRect.height / 2f;
                float tileMidY = tile.y + tile.height / 2f;

                if (playerMidY > tileMidY) {
                    player.setPosition(player.getPosition().x, tile.y + tile.height);
                    player.setVelocityY(0f);
                    touchingGround = true;
                }
                else {
                    float hitboxH = player.getBounds().height;
                    player.setPosition(player.getPosition().x, tile.y - hitboxH);
                    player.setVelocityY(0f);
                }
            }

            playerRect.set(player.getBounds());
        }

        player.setOnGround(touchingGround);
    }

    private boolean isSolid(MapObject obj) {
        if (!(obj instanceof RectangleMapObject))
            return false;

        String name = obj.getName();
        if (name == null)
            return false;

        return name.equals("platform") || name.equals("wall") || name.equals("ceiling") || name.equals("spikes platform");
    }

    private void handleSpikes() {
        if (playerController.hurtTimer > 0) return;

        MapLayer layer = currentMap.getLayers().get("logic");
        if (layer == null) return;

        Rectangle playerRect = player.getBounds();

        Polygon playerPoly = new Polygon(new float[]{
            0, 0,
            playerRect.width, 0,
            playerRect.width, playerRect.height,
            0, playerRect.height
        });

        playerPoly.setPosition(playerRect.x, playerRect.y);

        for (MapObject obj : layer.getObjects()) {
            if (!(obj instanceof PolygonMapObject)) continue;

            String name = obj.getName();
            if (name == null) continue;
            if (!name.equals("spikes")) continue;

            Polygon spikePoly = ((PolygonMapObject)obj).getPolygon();

            if (Intersector.overlapConvexPolygons(playerPoly, spikePoly)) {
                System.out.println(player.getHealth());
                playerController.takeDamage(1);
                if (player.getHealth() <= 0) {

                }
                playerController.hurtTimer = Player.HURT_COOLDOWN;
                return;
            }
        }
    }

    private void checkDoors() {
        if (transitionListener == null) return;

        MapLayer layer = currentMap.getLayers().get("logic");
        if (layer == null) return;

        Rectangle playerRect = player.getBounds();

        for (MapObject obj : layer.getObjects()) {
            if (!(obj instanceof RectangleMapObject)) continue;
            String name = obj.getName();
            if (name == null || !name.startsWith("door_to_")) continue;

            Rectangle doorRect = ((RectangleMapObject) obj).getRectangle();
            if (!Intersector.overlaps(playerRect, doorRect)) continue;

            int targetIndex = resolveMapIndex(name);
            String spawnName = "spawn_from_" + MAP_IDS[currentMapIndex];
            transitionListener.onTransition(targetIndex, spawnName);
            return;
        }
    }

    private int resolveMapIndex(String doorName) {
        if (doorName.endsWith("room1")) return 0;
        if (doorName.endsWith("room2")) return 1;
        return 0;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    public void setPlayerController(PlayerController playerController) {
        this.playerController = playerController;
    }
    public void setCurrentMap(TiledMap map) { currentMap = map;}
    public void setCurrentMapIndex(int currentMapIndex) {
        this.currentMapIndex = currentMapIndex;
    }

    public TiledMap getCurrentMap() { return currentMap;}
    public int getCurrentMapIndex() { return currentMapIndex;}
}
