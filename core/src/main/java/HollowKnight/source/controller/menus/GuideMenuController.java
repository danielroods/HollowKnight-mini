package HollowKnight.source.controller.menus;

import HollowKnight.source.controller.MenuController;
import HollowKnight.source.view.menus.GuideMenuScreen;
import HollowKnight.source.view.menus.MainMenuScreen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GuideMenuController {
    public static void modifyComponents(TextButton backBtn) {
        backBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MenuController.setMenuScreen(new MainMenuScreen());
            }
        });

        MenuHoverController.addHoverEffect(backBtn);
    }

    public static void modifyTabs(TextButton controlsTab, TextButton abilitiesTab, GuideMenuScreen screen) {
        controlsTab.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.showControls();
            }
        });

        abilitiesTab.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.showAbilities();
            }
        });

        MenuHoverController.addHoverEffect(controlsTab);
        MenuHoverController.addHoverEffect(abilitiesTab);
    }
}
