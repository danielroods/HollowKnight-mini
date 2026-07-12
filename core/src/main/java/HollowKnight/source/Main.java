package HollowKnight.source;

import HollowKnight.source.controller.MenuController;
import HollowKnight.source.model.asset.Assets;
import HollowKnight.source.controller.AudioController;
import HollowKnight.source.view.BrightnessRenderer;
import HollowKnight.source.view.menus.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
    private static Main instance;
    private SpriteBatch batch;

    public Main() {
        instance = this;
    }
    public static Main getGameInstance() {
        return instance;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        Assets.load();
        MenuController.setGame(this);
        MenuController.setMenuScreen(new MainMenuScreen());
        BrightnessRenderer.init();
    }

    @Override
    public void render() {
        super.render();
        AudioController.getInstance().update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        batch.dispose();
        Assets.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
