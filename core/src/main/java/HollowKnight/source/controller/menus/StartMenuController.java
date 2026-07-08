package HollowKnight.source.controller.menus;

import HollowKnight.source.controller.MenuController;
import HollowKnight.source.data.GameData;
import HollowKnight.source.data.SaveLoadManager;
import HollowKnight.source.view.menus.MainMenuScreen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class StartMenuController {
    public static void modifyComponents(TextButton[] slotButtons, TextButton backBtn) {
        for (int i = 0; i < slotButtons.length; i++) {
            final int slotIndex = i;

            slotButtons[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    GameData existingData = SaveLoadManager.exists(slotIndex) ? SaveLoadManager.load(slotIndex) : null;
                    MenuController.setGameScreen(slotIndex, existingData);
                }
            });

            MenuHoverController.addHoverEffect(slotButtons[i]);
        }

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MenuController.setMenuScreen(new MainMenuScreen());
            }
        });

        MenuHoverController.addHoverEffect(backBtn);
    }
}
