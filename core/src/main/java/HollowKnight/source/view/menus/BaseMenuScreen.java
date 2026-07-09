package HollowKnight.source.view.menus;

import HollowKnight.source.Main;
import HollowKnight.source.controller.MenuController;
import HollowKnight.source.view.BrightnessRenderer;
import HollowKnight.source.game_utils.UIContext;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class BaseMenuScreen implements Screen {
    private static Game game = Main.getGameInstance();
    private SpriteBatch batch;
    protected Viewport viewport;
    protected Stage stage;

    public BaseMenuScreen() {
        batch = Main.getGameInstance().getBatch();
        viewport = UIContext.getViewport();
        stage = new Stage(viewport, batch);

        Pixmap cursorPixmap = new Pixmap(Gdx.files.internal("icons/Cursor.png"));

        Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursorPixmap, 0, 0));

        cursorPixmap.dispose();
    }

    public static void prepareButton(TextButton button, float width, float height) {
        button.setSize(width, height);
        button.setTransform(true);
        button.setOrigin(width / 2f, height / 2f);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        MenuController.getMenuBackground().updateAndRender(batch, delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        MenuController.getMenuParticleLayer().updateAndRender(batch, delta);
        batch.end();

        stage.act(delta);
        stage.draw();

        BrightnessRenderer.render(
            batch,
            viewport.getWorldWidth(),
            viewport.getWorldHeight()
        );
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
