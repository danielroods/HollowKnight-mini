package HollowKnight.source.controller;

import HollowKnight.source.Main;
import HollowKnight.source.model.asset.Assets;
import HollowKnight.source.model.data.GameData;
import HollowKnight.source.view.menus.*;
import HollowKnight.source.view.GameScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class MenuController {
    private static Game game = Main.getGameInstance();
    private static BaseMenuScreen currentMenuScreen;
    private static GameScreen gameScreen;
    private static MenuBackground menuBackground;
    private static MenuParticleLayer menuParticleLayer;

    private static BaseMenuScreen settingsReturnScreen;

    public static void setGame(Main game) {
        if (MenuController.game == null)
            MenuController.game = game;

        menuBackground = new MenuBackground();
        menuParticleLayer = new MenuParticleLayer(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public static void setMenuScreen(BaseMenuScreen menuScreen) {
        if (menuScreen == null) return;

        if (currentMenuScreen != null) {
            currentMenuScreen.dispose();
        }
        currentMenuScreen = menuScreen;
        game.setScreen(menuScreen);

        if (menuScreen instanceof EndMenuScreen) {
            AudioController.getInstance().playMusic(Assets.getEndMenuMusic());
        }
        else if (!isInGameMenuScreen(menuScreen)) {
            AudioController.getInstance().playMusic(Assets.getMenuMusic());
        }
    }

    private static boolean isInGameMenuScreen(BaseMenuScreen menuScreen) {
        if (menuScreen instanceof PauseMenuScreen)
            return true;
        if (menuScreen instanceof CheatsMenuScreen)
            return true;
        return menuScreen instanceof SettingsMenuScreen && settingsReturnScreen instanceof PauseMenuScreen;
    }

    public static void setGameScreen(int slotIndex, GameData dataToLoad) {
        gameScreen = new GameScreen(slotIndex, dataToLoad);
        if (currentMenuScreen != null) {
            currentMenuScreen.dispose();
            currentMenuScreen = null;
        }
        game.setScreen(gameScreen);
    }

    public static void resumeGameScreen() {
        if (currentMenuScreen != null) {
            currentMenuScreen.dispose();
            currentMenuScreen = null;
        }
        if (gameScreen != null) {
            game.setScreen(gameScreen);
        }
    }

    public static void quitGameScreenToMenu(BaseMenuScreen destination) {
        if (gameScreen != null) {
            gameScreen.dispose();
            gameScreen = null;
        }

        GameController.resetInstance();

        setMenuScreen(destination);
    }

    public static void openSettings(BaseMenuScreen returnTo) {
        settingsReturnScreen = returnTo;
        setMenuScreen(new SettingsMenuScreen());
    }

    public static BaseMenuScreen settingsReturnScreen() {
        BaseMenuScreen target = settingsReturnScreen;
        settingsReturnScreen = null;
        return target;
    }

    public static GameScreen getGameScreen() { return gameScreen;}
    public static MenuParticleLayer getMenuParticleLayer() {
        return menuParticleLayer;
    }
    public static MenuBackground getMenuBackground() {
        return menuBackground;
    }

    public static void setMenuParticleLayer(MenuParticleLayer menuParticleLayer) {
        MenuController.menuParticleLayer = menuParticleLayer;
    }
    public static void setMenuBackground(MenuBackground menuBackground) {
        MenuController.menuBackground = menuBackground;
    }
}
