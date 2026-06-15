package HollowKnight.source.view.menus;

import HollowKnight.source.Main;
import HollowKnight.source.game_utils.BrightnessRenderer;
import HollowKnight.source.game_utils.UIContext;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class BaseMenuScreen implements Screen {
    protected Viewport viewport;
    protected Stage stage;
    protected Texture backgroundTexture;
    protected Table rootTable;

    public BaseMenuScreen() {
        viewport = UIContext.getViewport();
        stage = new Stage(viewport, Main.getGameInstance().getBatch());
        Gdx.input.setInputProcessor(stage);
        rootTable = new Table();
        rootTable.setFillParent(true);
    }

    public void setBackground(Texture backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (backgroundTexture != null) {
            Main.getGameInstance().getBatch().begin();
            Main.getGameInstance().getBatch().draw(
                backgroundTexture,
                0,
                0,
                viewport.getWorldWidth(),
                viewport.getWorldHeight()
            );
            Main.getGameInstance().getBatch().end();
        }

        stage.act(delta);
        stage.draw();

        BrightnessRenderer.render(
            Main.getGameInstance().getBatch(),
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
