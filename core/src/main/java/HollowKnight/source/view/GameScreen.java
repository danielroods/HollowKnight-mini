package HollowKnight.source.view;

import HollowKnight.source.Main;
import HollowKnight.source.controller.GameController;
import HollowKnight.source.controller.PlayerController;
import HollowKnight.source.game_utils.Assets;
import HollowKnight.source.game_utils.BrightnessRenderer;
import HollowKnight.source.model.player.Player;
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

    private GameController gameController;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private static final float WORLD_W = 1280f;
    private static final float WORLD_H = 720f;
    private static final float CAM_LERP = 0.10f;

    private OrthographicCamera worldCamera;
    private FitViewport worldViewport;
    private OrthographicCamera bgCamera;

    private OrthogonalTiledMapRenderer mapRenderer;
    private float mapPixelW, mapPixelH;

    private Player player;
    private PlayerRenderer playerRenderer;

    @Override
    public void show() {
        batch = Main.getGameInstance().getBatch();
        worldCamera = new OrthographicCamera();
        worldViewport = new FitViewport(WORLD_W, WORLD_H, worldCamera);
        bgCamera = new OrthographicCamera();
        bgCamera.viewportWidth = WORLD_W;
        bgCamera.viewportHeight = WORLD_H;

        gameController = GameController.getInstance();
        gameController.setPlayer(Player.getInstance());
        gameController.setPlayerController(PlayerController.getInstance());

        loadMap(0);

        Vector2 spawn = Assets.getSpawnPosition("game_start_spawn");
        player = Player.getInstance();
        playerRenderer = new PlayerRenderer(Assets.getPlayerAnimations());

        gameController.setTransitionListener((targetIndex, spawnName) -> switchMap(targetIndex, spawnName));

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        gameController.update(delta);
        updateCamera();

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.setView(bgCamera);
        mapRenderer.render(new int[]{0});

        mapRenderer.setView(worldCamera);
        mapRenderer.render(new int[]{1,2,3,4,5,6});

        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        playerRenderer.render(batch, player, delta);
        batch.end();

        mapRenderer.render(new int[]{7,8,9});

        //tempppppp
        shapeRenderer.setProjectionMatrix(worldCamera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.rect(
            player.getBounds().x,
            player.getBounds().y,
            player.getBounds().width,
            player.getBounds().height
        );

        shapeRenderer.end();

        BrightnessRenderer.render(batch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void resize(int width, int height) {
        worldViewport.update(width, height);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        if (mapRenderer != null) mapRenderer.dispose();
    }

    private void loadMap(int index) {
        if (mapRenderer != null) mapRenderer.dispose();

        gameController.setCurrentMap(Assets.getMap(index));
        gameController.setCurrentMapIndex(index);
        mapRenderer = new OrthogonalTiledMapRenderer(gameController.getCurrentMap());

        TiledMapTileLayer ref = (TiledMapTileLayer) gameController.getCurrentMap().getLayers().get("main");
        if (ref == null)
            ref = (TiledMapTileLayer) gameController.getCurrentMap().getLayers().get(0);

        mapPixelW = ref.getWidth() * ref.getTileWidth();
        mapPixelH = ref.getHeight() * ref.getTileHeight();
    }

    private void switchMap(int targetIndex, String spawnPointName) {
        loadMap(targetIndex);

        Vector2 spawn = Assets.getSpawnPosition(spawnPointName);
        player.setPosition(spawn.x, spawn.y);
        player.setVelocityY(0f);
    }

    private void updateCamera() {
        float targetX = player.getPosition().x + Player.WIDTH / 2f;
        float targetY = player.getPosition().y + Player.HEIGHT / 2f;

        worldCamera.position.lerp(new Vector3(targetX, targetY, 0f), CAM_LERP);

        float halfW = worldViewport.getWorldWidth() / 2f;
        float halfH = worldViewport.getWorldHeight() / 2f;

        worldCamera.position.x = MathUtils.clamp(worldCamera.position.x, halfW, mapPixelW - halfW);
        worldCamera.position.y = MathUtils.clamp(worldCamera.position.y, halfH, mapPixelH - halfH);

        worldCamera.update();

        bgCamera.position.x = worldCamera.position.x * 0.3f + 500f;
        bgCamera.position.y = worldCamera.position.y * 0.7f + 260f;

        bgCamera.update();
    }
}
