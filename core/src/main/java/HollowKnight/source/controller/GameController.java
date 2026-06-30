package HollowKnight.source.controller;

import HollowKnight.source.game_utils.Assets;
import HollowKnight.source.model.player.AttackDirection;
import HollowKnight.source.model.player.Player;
import HollowKnight.source.model.player.PlayerConstants;
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
import com.badlogic.gdx.math.Vector2;

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

        if (!player.isAlive()) {
            playerController.update(delta);
            if (player.getDeathTimer() >= PlayerConstants.DEATH_ANIM_DUR)
                respawnPlayer();
            return;
        }

        handleFocusInput(delta);

        if (!player.isFocusing()) {
            handleMovementInput();
            handleAttackInput();
        } else {
            playerController.stopHorizontal();
        }

        playerController.update(delta);
        handleCollisions();
        handleSpikes();
        checkDoors();

        checkSwordHits();

        if (player.getPosition().y < -300f)
            respawnPlayer();
    }

    private void handleMovementInput() {
        if (player.isKnockedBack()) return;

        boolean left = Gdx.input.isKeyPressed(Keys.LEFT);
        boolean right = Gdx.input.isKeyPressed(Keys.RIGHT);
        boolean jump = Gdx.input.isKeyJustPressed(Keys.SPACE);

        if (left && !right)
            playerController.moveLeft();
        else if (right && !left)
            playerController.moveRight();
        else
            playerController.stopHorizontal();

        if (jump)
            playerController.jump();
    }

    private void handleAttackInput() {
        if (!Gdx.input.isKeyJustPressed(Keys.X)) return;

        AttackDirection dir;
        if (Gdx.input.isKeyPressed(Keys.UP)) {
            dir = AttackDirection.UP;
        }
        else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            dir = AttackDirection.DOWN;
        }
        else {
            dir = player.isFacingRight() ? AttackDirection.RIGHT : AttackDirection.LEFT;
        }

        playerController.attack(dir);
    }

    private void handleFocusInput(float delta) {
        boolean held = Gdx.input.isKeyPressed(Keys.A);
        if (held && player.isOnGround() && !player.isInvincible())
        {
            if (!player.isFocusing()) {
                playerController.startFocus();
            }
            playerController.updateFocus(delta);
        }
        else if (!held && player.isFocusing())
            playerController.cancelFocus();
    }

    private void respawnPlayer() {
        Vector2 spawn = Assets.getSpawnPosition("game_start_spawn");
        player.setPosition(spawn.x, spawn.y);
        player.setVelocityX(0f);
        player.setVelocityY(0f);
        player.restoreFullHealth();
        playerController.updateLastSafePosition();
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

        if (touchingGround)
            playerController.updateLastSafePosition();
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
        if (player.isInvincible() || !player.isAlive()) return;

        MapLayer layer = currentMap.getLayers().get("logic");
        if (layer == null) return;

        Rectangle playerRect = player.getBounds();
        Polygon playerPoly = new Polygon(new float[]{0, 0, playerRect.width, 0, playerRect.width, playerRect.height, 0, playerRect.height});
        playerPoly.setPosition(playerRect.x, playerRect.y);

        for (MapObject obj : layer.getObjects()) {
            if (!(obj instanceof PolygonMapObject)) continue;
            String name = obj.getName();
            if (name == null || !name.equals("spikes")) continue;

            Polygon spikePoly = ((PolygonMapObject) obj).getPolygon();
            if (!Intersector.overlapConvexPolygons(playerPoly, spikePoly)) continue;

            float spikeCX = spikePoly.getBoundingRectangle().x + spikePoly.getBoundingRectangle().width / 2f;
            float playerCX = playerRect.x + playerRect.width / 2f;
            float knockBackDirection = playerCX < spikeCX ? -1f : 1f;

            playerController.takeDamage(1);
            playerController.applyKnockback(knockBackDirection);
            return;
        }
    }

    private void checkSwordHits() {
        if (!player.isAttacking()) return;

        Rectangle hitbox = playerController.getSwordHitbox();
        if (hitbox == null) return;
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
        if (doorName.endsWith("greenpath_room1"))
            return 0;
        if (doorName.endsWith("greenpath_room2"))
            return 1;
        return 0;
    }

    public void setPlayer(Player p) { this.player = p; }
    public void setPlayerController(PlayerController pc) { this.playerController = pc; }
    public void setCurrentMap(TiledMap map) { currentMap = map; }
    public void setCurrentMapIndex(int idx) { this.currentMapIndex = idx; }
    public TiledMap getCurrentMap() { return currentMap; }
    public int getCurrentMapIndex() { return currentMapIndex; }
}
