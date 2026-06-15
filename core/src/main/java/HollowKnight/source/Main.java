package HollowKnight.source;

import HollowKnight.source.controller.MenuController;
import HollowKnight.source.game_utils.Assets;
import HollowKnight.source.game_utils.AudioManager;
import HollowKnight.source.game_utils.BrightnessRenderer;
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
        MenuController.setScreen(new MainMenuScreen());
        //MenuController.playMusic();
        BrightnessRenderer.init();
    }

    @Override
    public void render() {
        super.render();
        AudioManager.getInstance().update(Gdx.graphics.getDeltaTime());
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
