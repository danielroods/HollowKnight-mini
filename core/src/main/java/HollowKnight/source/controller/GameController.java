package HollowKnight.source.controller;

import HollowKnight.source.controller.enemies.CrystalCrawlerController;
import HollowKnight.source.controller.enemies.CrystalGuardianController;
import HollowKnight.source.controller.enemies.FalseKnightController;
import HollowKnight.source.controller.enemies.HuskHornheadController;
import HollowKnight.source.controller.enemies.MossflyController;
import HollowKnight.source.model.asset.Assets;
import HollowKnight.source.model.enemies.crystal_crawler.CrystalCrawler;
import HollowKnight.source.model.enemies.crystal_guardian.CrystalGuardian;
import HollowKnight.source.model.enemies.false_knight.FalseKnight;
import HollowKnight.source.model.enemies.husk_hornhead.HuskHornhead;
import HollowKnight.source.model.enemies.mossfly.Mossfly;
import HollowKnight.source.model.map.Maps;
import HollowKnight.source.model.player.AttackDirection;
import HollowKnight.source.model.player.Player;
import HollowKnight.source.model.player.PlayerConstants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PointMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private static GameController instance;

    private Player player;
    private PlayerController playerController;
    private MossflyController mossflyController;
    private HuskHornheadController huskHornheadController;
    private CrystalGuardianController crystalGuardianController;
    private CrystalCrawlerController crystalCrawlerController;
    private FalseKnightController falseKnightController;
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
            if (crystalGuardianController != null) {
                crystalGuardianController.updateLasersOnly(delta, player);
            }
            if (falseKnightController != null) {
                falseKnightController.updateShockwaveOnly(delta, player);
            }
            if (player.getDeathTimer() >= PlayerConstants.DEATH_ANIM_DUR)
                respawnPlayer();
            return;
        }

        handleFocusInput(delta);
        if (!player.isFocusing()) {
            handleMovementInput();
            handleWallSlideInput();
            handleDashInput();
            handleAttackInput();
            playerController.checkSwordHits(mossflyController, huskHornheadController, crystalGuardianController, crystalCrawlerController, falseKnightController);
        }
        else {
            playerController.stopHorizontal();
        }
        playerController.update(delta);

        handleCollisions();
        handleSpikes();
        handleDoors();

        MapLayer logicLayer = currentMap.getLayers().get("logic");

        if (mossflyController != null) {
            mossflyController.update(delta, player, logicLayer);
        }

        if (huskHornheadController != null) {
            huskHornheadController.update(delta, player, logicLayer);
        }

        if (crystalGuardianController != null) {
            crystalGuardianController.update(delta, player, logicLayer);
        }

        if (crystalCrawlerController != null) {
            crystalCrawlerController.update(delta, player, logicLayer);
        }

        if (falseKnightController != null) {
            falseKnightController.update(delta, player, logicLayer);
        }

        if (player.getPosition().y < -300f)
            respawnPlayer();
    }

    public void loadEnemiesBySpawnPoints() {
        List<Mossfly> mossflySpawns = new ArrayList<>();
        List<HuskHornhead> huskHornheadSpawns = new ArrayList<>();
        List<CrystalGuardian> crystalGuardianSpawns = new ArrayList<>();
        List<CrystalCrawler> crystalCrawlerSpawns = new ArrayList<>();
        List<FalseKnight> falseKnightSpawns = new ArrayList<>();

        MapLayer layer = currentMap.getLayers().get("logic");
        if (layer != null) {
            for (MapObject obj : layer.getObjects()) {
                if (!(obj instanceof PointMapObject)) continue;
                String name = obj.getName();
                if (name == null) continue;

                PointMapObject pt = (PointMapObject) obj;
                if (name.equals("mossfly_spawn")) {
                    mossflySpawns.add(new Mossfly(pt.getPoint().x, pt.getPoint().y));
                }
                else if (name.equals("husk_hornhead_spawn")) {
                    huskHornheadSpawns.add(new HuskHornhead(pt.getPoint().x, pt.getPoint().y));
                }
                else if (name.equals("crystal_guardian_spawn")) {
                    crystalGuardianSpawns.add(new CrystalGuardian(pt.getPoint().x, pt.getPoint().y));
                }
                else if (name.equals("crystal_crawler_spawn")) {
                    crystalCrawlerSpawns.add(new CrystalCrawler(pt.getPoint().x, pt.getPoint().y));
                }
                else if (name.equals("false_knight_spawn")) {
                    falseKnightSpawns.add(new FalseKnight(pt.getPoint().x, pt.getPoint().y));
                }
            }
        }

        mossflyController = new MossflyController(mossflySpawns);
        huskHornheadController = new HuskHornheadController(huskHornheadSpawns);
        crystalGuardianController = new CrystalGuardianController(crystalGuardianSpawns);
        crystalCrawlerController = new CrystalCrawlerController(crystalCrawlerSpawns);
        falseKnightController = new FalseKnightController(falseKnightSpawns);
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
        if (held && player.isOnGround() && !player.isInvincible()) {
            if (!player.isFocusing()) {
                playerController.startFocus();
            }
            playerController.updateFocus(delta);
        }
        else if (!held && player.isFocusing()) {
            playerController.cancelFocus();
        }
    }

    private void handleDashInput() {
        if (Gdx.input.isKeyJustPressed(Keys.C))
            playerController.dash();
    }

    private void handleWallSlideInput() {
        if (player.isKnockedBack()) return;

        boolean left = Gdx.input.isKeyPressed(Keys.LEFT);
        boolean right = Gdx.input.isKeyPressed(Keys.RIGHT);

        boolean touchingWallLeft = isTouchingWall(false);
        boolean touchingWallRight = isTouchingWall(true);

        playerController.updateWallSlide(left, right, touchingWallLeft, touchingWallRight);
    }

    private boolean isTouchingWall(boolean rightSide) {
        MapLayer layer = currentMap.getLayers().get("logic");
        if (layer == null)
            return false;

        Rectangle b = player.getBounds();
        float wallOverlapX = rightSide ? b.x + b.width : b.x - 4f;
        Rectangle wallOverlap = new Rectangle(wallOverlapX, b.y + 4f, 4f, Math.max(1f, b.height - 8f));

        for (MapObject obj : layer.getObjects()) {
            if (!isSolid(obj)) continue;
            if (Intersector.overlaps(wallOverlap, ((RectangleMapObject) obj).getRectangle()))
                return true;
        }
        return false;
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
        return name.equals("platform") || name.equals("wall") || name.equals("ceiling");
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

    private void handleDoors() {
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
            String spawnName = "spawn_from_" + resolveMapId(currentMapIndex);
            transitionListener.onTransition(targetIndex, spawnName);
            return;
        }
    }

    private void respawnPlayer() {
        Vector2 spawn = getSpawnPosition("game_start_spawn");
        player.setPosition(spawn.x, spawn.y);
        player.setVelocityX(0f);
        player.setVelocityY(0f);
        player.restoreFullHealth();
        playerController.updateLastSafePosition();
    }

    public static Vector2 getSpawnPosition(String spawnName) {
        MapLayer logicLayer = getInstance().getCurrentMap().getLayers().get("logic");
        if (logicLayer == null)
            return new Vector2(220f, 220f);

        Vector2 pos = tryGetPoint(logicLayer, spawnName);
        if (pos != null)
            return pos;

        pos = tryGetPoint(logicLayer, "game_start_spawn");
        if (pos != null)
            return pos;

        return new Vector2(700f, 400f);
    }

    private static Vector2 tryGetPoint(MapLayer layer, String name) {
        MapObject obj = layer.getObjects().get(name);
        if (obj == null) return null;

        if (obj instanceof PointMapObject) {
            PointMapObject pt = (PointMapObject) obj;
            return new Vector2(pt.getPoint().x - 100f, pt.getPoint().y - 20f);
        }
        return null;
    }

    private int resolveMapIndex(String doorName) {
        for (Maps map : Maps.values()) {
            if (doorName.endsWith(map.getId())) {
                return map.getIndex();
            }
        }

        return currentMapIndex;
    }

    private String resolveMapId(int index) {
        return Maps.fromIndex(index).getId();
    }

    public void setPlayer(Player player) { this.player = player; }
    public void setPlayerController(PlayerController playerController) { this.playerController = playerController; }
    public void setCurrentMap(TiledMap map) { currentMap = map; }
    public void setCurrentMapIndex(int idx) { this.currentMapIndex = idx; }
    public TiledMap getCurrentMap() { return currentMap; }
    public int getCurrentMapIndex() { return currentMapIndex; }
    public MossflyController getMossflyController() { return mossflyController; }
    public HuskHornheadController getHuskHornheadController() { return huskHornheadController; }
    public CrystalGuardianController getCrystalGuardianController() { return crystalGuardianController; }
    public CrystalCrawlerController getCrystalCrawlerController() { return crystalCrawlerController; }
    public FalseKnightController getFalseKnightController() { return falseKnightController; }
}
