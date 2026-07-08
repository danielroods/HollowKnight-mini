package HollowKnight.source.view;

import HollowKnight.source.Main;
import HollowKnight.source.controller.GameController;
import HollowKnight.source.controller.enemies.CrystalCrawlerController;
import HollowKnight.source.controller.enemies.CrystalGuardianController;
import HollowKnight.source.controller.enemies.FalseKnightController;
import HollowKnight.source.controller.enemies.HuskHornheadController;
import HollowKnight.source.controller.enemies.MossflyController;
import HollowKnight.source.controller.npc.ZoteController;
import HollowKnight.source.controller.PlayerController;
import HollowKnight.source.data.GameData;
import HollowKnight.source.game_utils.CameraShake;
import HollowKnight.source.model.achievement.AchievementManager;
import HollowKnight.source.model.asset.Assets;
import HollowKnight.source.model.enemies.false_knight.BossProgressManager;
import HollowKnight.source.model.map.Maps;
import HollowKnight.source.model.player.Player;
import HollowKnight.source.model.player.PlayerConstants;
import HollowKnight.source.view.enemies.CrystalCrawlerRenderer;
import HollowKnight.source.view.enemies.CrystalGuardianRenderer;
import HollowKnight.source.view.enemies.FalseKnightRenderer;
import HollowKnight.source.view.enemies.HuskHornheadRenderer;
import HollowKnight.source.view.enemies.MossflyRenderer;
import HollowKnight.source.view.menus.InventoryScreen;
import HollowKnight.source.view.npc.ZoteDialogueRenderer;
import HollowKnight.source.view.npc.ZoteRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen implements Screen {

    private static final float WORLD_W = 1280f;
    private static final float WORLD_H = 720f;
    private static final float CAM_LERP = 0.02f;

    private GameController gameController;
    private SpriteBatch batch;

    private OrthographicCamera worldCamera;
    private FitViewport worldViewport;
    private OrthographicCamera bgCamera;

    private OrthographicCamera uiCamera;
    private HUDRenderer hudRenderer;
    private InventoryScreen inventoryScreen;

    private OrthogonalTiledMapRenderer mapRenderer;
    private float mapPixelW, mapPixelH;

    private Player player;
    private PlayerRenderer playerRenderer;

    private MossflyRenderer mossflyRenderer;
    private HuskHornheadRenderer huskHornheadRenderer;
    private CrystalGuardianRenderer crystalGuardianRenderer;
    private CrystalCrawlerRenderer crystalCrawlerRenderer;
    private FalseKnightRenderer falseKnightRenderer;
    private ZoteRenderer zoteRenderer;
    private ZoteDialogueRenderer zoteDialogueRenderer;

    // for debug
    private ShapeRenderer shapeRenderer;
    private final int slotIndex;
    private final GameData dataToLoad;

    public GameScreen(int slotIndex, GameData dataToLoad) {
        this.slotIndex = slotIndex;
        this.dataToLoad = dataToLoad;
    }

    @Override
    public void show() {
        batch = Main.getGameInstance().getBatch();

        worldCamera = new OrthographicCamera();
        worldCamera.zoom = 0.95f;
        worldViewport = new FitViewport(WORLD_W, WORLD_H, worldCamera);

        bgCamera = new OrthographicCamera();
        bgCamera.viewportWidth = WORLD_W;
        bgCamera.viewportHeight = WORLD_H;

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, WORLD_W, WORLD_H);
        uiCamera.update();

        gameController = GameController.getInstance();
        gameController.setPlayer(Player.getInstance());
        gameController.setPlayerController(PlayerController.getInstance());
        gameController.setActiveSlotIndex(slotIndex);

        AchievementManager.reset();
        BossProgressManager.reset();

        int startMapIndex = 0;
        if (dataToLoad != null) {
            Maps targetMap = Maps.fromId(dataToLoad.getMapId());
            if (targetMap != null) {
                startMapIndex = targetMap.getIndex();
            }
        }

        loadMap(startMapIndex);

        if (dataToLoad != null) {
            gameController.applyGameData(dataToLoad);

            float targetX = dataToLoad.getPlayerX() + PlayerConstants.WIDTH / 2f;
            float targetY = dataToLoad.getPlayerY() + PlayerConstants.HEIGHT / 2f;
            worldCamera.position.set(targetX, targetY, 0f);
            worldCamera.update();
        }

        player = Player.getInstance();
        playerRenderer = new PlayerRenderer(Assets.getPlayerAnimations());
        hudRenderer = new HUDRenderer();
        inventoryScreen = new InventoryScreen();
        gameController.setInventoryScreen(inventoryScreen);

        mossflyRenderer = new MossflyRenderer();
        huskHornheadRenderer = new HuskHornheadRenderer();
        crystalGuardianRenderer = new CrystalGuardianRenderer();
        crystalCrawlerRenderer = new CrystalCrawlerRenderer();
        falseKnightRenderer = new FalseKnightRenderer();
        zoteRenderer = new ZoteRenderer();
        zoteDialogueRenderer = new ZoteDialogueRenderer();

        shapeRenderer = new ShapeRenderer();

        gameController.setTransitionListener((targetIndex, spawnName) -> switchMap(targetIndex, spawnName));
    }

    @Override
    public void render(float delta) {
        boolean paused = inventoryScreen.isOpen();

        if (!paused) {
            gameController.update(delta);
            hudRenderer.update(delta);
            updateCamera(delta);
        }

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderWorld(delta);
        renderHUD();
        zoteDialogueRenderer.render(batch, uiCamera, gameController.getZoteController());

        BrightnessRenderer.render(batch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (paused) {
            inventoryScreen.render(delta);
        }
    }

    private void renderWorld(float delta) {
        switch (gameController.getCurrentMapIndex()) {
            case 0:
                mapRenderer.setView(bgCamera);
                mapRenderer.render(new int[]{0});

                mapRenderer.setView(worldCamera);
                mapRenderer.render(new int[]{1, 2, 3, 4, 5, 6});

                batch.setProjectionMatrix(worldCamera.combined);

                batch.begin();
                renderMossfly();
                renderHuskHornhead();
                renderCrystalGuardian();
                playerRenderer.render(batch, player, delta);
                batch.end();

                mapRenderer.render(new int[]{7, 8, 9});
                break;

            case 1:
                mapRenderer.setView(bgCamera);
                mapRenderer.render(new int[]{0});

                mapRenderer.setView(worldCamera);
                mapRenderer.render(new int[]{1, 2, 3, 4, 5, 6, 7, 8});

                batch.setProjectionMatrix(worldCamera.combined);

                batch.begin();
                renderMossfly();
                renderHuskHornhead();
                renderCrystalCrawler();
                playerRenderer.render(batch, player, delta);
                batch.end();

                mapRenderer.render(new int[]{9, 10, 11, 12, 13});
                break;

            case 2:
                mapRenderer.setView(bgCamera);
                mapRenderer.render(new int[]{0});

                mapRenderer.setView(worldCamera);
                mapRenderer.render(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});

                batch.setProjectionMatrix(worldCamera.combined);


                batch.begin();
                renderCrystalGuardian();
                renderCrystalCrawler();
                renderZote();
                playerRenderer.render(batch, player, delta);
                batch.end();

                mapRenderer.render(new int[]{10, 11, 12, 13, 14, 15});
                break;

            case 3:
                mapRenderer.setView(bgCamera);
                mapRenderer.render(new int[]{0});

                mapRenderer.setView(worldCamera);
                mapRenderer.render(new int[]{1, 2, 3, 4, 5, 6, 7});

                batch.setProjectionMatrix(worldCamera.combined);

                batch.begin();
                renderFalseKnight();
                playerRenderer.render(batch, player, delta);
                batch.end();

                mapRenderer.render(new int[]{8, 9, 10});

                break;
        }
    }

    private void renderMossfly() {
        MossflyController mossflyController = gameController.getMossflyController();
        if (mossflyController != null) {
            mossflyRenderer.render(batch, mossflyController.getMossflyList());
        }
    }
    private void renderHuskHornhead() {
        HuskHornheadController huskHornheadController = gameController.getHuskHornheadController();
        if (huskHornheadController != null) {
            huskHornheadRenderer.render(batch, huskHornheadController.getHuskHornheadList());
        }
    }
    private void renderCrystalGuardian() {
        CrystalGuardianController crystalGuardianController = gameController.getCrystalGuardianController();
        if (crystalGuardianController != null) {
            crystalGuardianRenderer.render(batch, crystalGuardianController.getGuardianList());
        }
    }
    private void renderCrystalCrawler() {
        CrystalCrawlerController crystalCrawlerController = gameController.getCrystalCrawlerController();
        if (crystalCrawlerController != null) {
            crystalCrawlerRenderer.render(batch, crystalCrawlerController.getCrystalCrawlerList());
        }
    }
    private void renderFalseKnight() {
        FalseKnightController falseKnightController = gameController.getFalseKnightController();
        if (falseKnightController != null) {
            falseKnightRenderer.render(batch, falseKnightController.getFalseKnightList());
        }
    }

    private void renderZote() {
        ZoteController zoteController = gameController.getZoteController();
        if (zoteController != null) {
            zoteRenderer.render(batch, zoteController.getZoteList(), zoteController, player);
        }
    }

    private void renderHUD() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        hudRenderer.render(batch, uiCamera, player);
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void loadMap(int index) {
        if (mapRenderer != null)
            mapRenderer.dispose();

        gameController.setCurrentMap(Assets.getMap(index));
        gameController.setCurrentMapIndex(index);
        mapRenderer = new OrthogonalTiledMapRenderer(gameController.getCurrentMap());

        TiledMapTileLayer ref = (TiledMapTileLayer)
            gameController.getCurrentMap().getLayers().get("main");
        if (ref == null)
            ref = (TiledMapTileLayer)
                gameController.getCurrentMap().getLayers().get(0);

        mapPixelW = ref.getWidth() * ref.getTileWidth();
        mapPixelH = ref.getHeight() * ref.getTileHeight();

        gameController.loadEnemiesBySpawnPoints();
    }

    private void switchMap(int targetIndex, String spawnPointName) {
        loadMap(targetIndex);
        Vector2 spawn = GameController.getSpawnPosition(spawnPointName);
        Player.getInstance().setPosition(spawn.x, spawn.y);
        Player.getInstance().setVelocityY(0f);

        float targetX = spawn.x + PlayerConstants.WIDTH / 2f;
        float targetY = spawn.y + PlayerConstants.HEIGHT / 2f;

        worldCamera.position.set(targetX, targetY, 0);
        worldCamera.update();
    }

    private void updateCamera(float delta) {
        float targetX = player.getPosition().x + PlayerConstants.WIDTH / 2f;
        float targetY = player.getPosition().y + PlayerConstants.HEIGHT / 2f;

        worldCamera.position.lerp(new Vector3(targetX, targetY, 0f), CAM_LERP);

        float halfW = worldViewport.getWorldWidth() / 2f;
        float halfH = worldViewport.getWorldHeight() / 2f;

        worldCamera.position.x = (mapPixelW <= worldViewport.getWorldWidth()) ? mapPixelW / 2f : MathUtils.clamp(worldCamera.position.x, halfW, mapPixelW - halfW);

        worldCamera.position.y = (mapPixelH <= worldViewport.getWorldHeight()) ? mapPixelH / 2f : MathUtils.clamp(worldCamera.position.y, halfH, mapPixelH - halfH);

        if (player.isFocusing()) {
            worldCamera.zoom = MathUtils.lerp(worldCamera.zoom, 0.7f, delta);
        }
        else {
            worldCamera.zoom = MathUtils.lerp(worldCamera.zoom, 1f, 4f * delta);
        }

        Vector2 shakeOffset = CameraShake.update(delta);
        worldCamera.position.add(shakeOffset.x, shakeOffset.y, 0f);
        worldCamera.update();

        if (gameController.getCurrentMapIndex() == 1) {
            bgCamera.position.set(worldCamera.position);
        }
        else {
            bgCamera.position.x = worldCamera.position.x * 0.3f + 500f;
            bgCamera.position.y = worldCamera.position.y * 0.7f + 260f;
        }
        bgCamera.update();

        worldCamera.position.sub(shakeOffset.x, shakeOffset.y, 0f);
    }

    @Override public void resize(int width, int height) {
        worldViewport.update(width, height);
        uiCamera.setToOrtho(false, WORLD_W, WORLD_H);
        uiCamera.update();
        if (inventoryScreen != null)
            inventoryScreen.resize(width, height);
    }

    @Override public void dispose() {
        if (mapRenderer != null) mapRenderer.dispose();
        if (hudRenderer != null) hudRenderer.dispose();
        if (inventoryScreen != null) inventoryScreen.dispose();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
