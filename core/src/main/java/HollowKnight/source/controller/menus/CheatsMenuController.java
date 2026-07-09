package HollowKnight.source.controller.menus;

import HollowKnight.source.controller.MenuController;
import HollowKnight.source.view.menus.PauseMenuScreen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class CheatsMenuController {

    public static void modifyComponents(TextButton backBtn) {
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MenuController.setMenuScreen(new PauseMenuScreen());
            }
        });

        MenuHoverController.addHoverEffect(backBtn);
    }
}
