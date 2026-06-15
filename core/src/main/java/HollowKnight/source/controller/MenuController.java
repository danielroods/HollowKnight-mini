package HollowKnight.source.controller;

import HollowKnight.source.Main;
import HollowKnight.source.game_utils.AudioManager;
import HollowKnight.source.view.menus.BaseMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MenuController {
    private static Game game = Main.getGameInstance();
    private static BaseMenuScreen currentMenuScreen;

    public static void playMusic() {
        Music menuBGM = Gdx.audio.newMusic(Gdx.files.internal("musics/MoogCity2.mp3"));
        AudioManager.getInstance().playMusic(menuBGM);
    }

    public static void setScreen(BaseMenuScreen menuScreen) {
        if (currentMenuScreen != null) {
            currentMenuScreen.dispose();
        }

        currentMenuScreen = menuScreen;
        game.setScreen(menuScreen);
    }

    public static void setGame(Main game) {
        if (MenuController.game == null)
            MenuController.game = game;
    }
}
