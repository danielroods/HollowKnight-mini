package HollowKnight.source.controller.menus;

import HollowKnight.source.controller.GameController;
import HollowKnight.source.controller.MenuController;
import HollowKnight.source.view.menus.CheatsMenuScreen;
import HollowKnight.source.view.menus.MainMenuScreen;
import HollowKnight.source.view.menus.PauseMenuScreen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class PauseMenuController {

    public static void modifyComponents(TextButton resumeBtn, TextButton optionsBtn, TextButton cheatsBtn, TextButton saveAndQuitBtn) {

        resumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MenuController.resumeGameScreen();
            }
        });

        optionsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MenuController.openSettings(new PauseMenuScreen());
            }
        });

        cheatsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MenuController.setMenuScreen(new CheatsMenuScreen());
            }
        });

        saveAndQuitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameController.getInstance().saveToActiveSlot();
                MenuController.quitGameScreenToMenu(new MainMenuScreen());
            }
        });

        MenuHoverController.addHoverEffect(resumeBtn);
        MenuHoverController.addHoverEffect(optionsBtn);
        MenuHoverController.addHoverEffect(cheatsBtn);
        MenuHoverController.addHoverEffect(saveAndQuitBtn);
    }
}
