package HollowKnight.source.controller.menus;

import HollowKnight.source.controller.MenuController;
import HollowKnight.source.view.menus.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuController {
    public static void modifyComponents(TextButton startGameBtn,
                                        TextButton settingsBtn,
                                        TextButton guideBtn,
                                        TextButton achievementsBtn,
                                        TextButton quitGameBtn) {

        startGameBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MenuController.setScreen(new StartMenuScreen());
            }
        });

        settingsBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MenuController.setScreen(new SettingsMenuScreen());
            }
        });

        guideBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MenuController.setScreen(new GuideMenuScreen());
            }
        });

        achievementsBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MenuController.setScreen(new AchievementsMenuScreen());
            }
        });

        quitGameBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }
}
