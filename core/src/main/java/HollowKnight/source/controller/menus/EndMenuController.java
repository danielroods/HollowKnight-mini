package HollowKnight.source.controller.menus;

import HollowKnight.source.controller.GameController;
import HollowKnight.source.controller.MenuController;
import HollowKnight.source.model.data.GameStats;
import HollowKnight.source.view.menus.MainMenuScreen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class EndMenuController {
    public static void modifyComponents(TextButton newGameBtn, TextButton mainMenuBtn) {
        newGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameController.resetInstance();
                GameStats.reset();
                int activeSlot = GameController.getInstance().getActiveSlotIndex();
                MenuController.setGameScreen(activeSlot, null);
            }
        });

        mainMenuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameController.resetInstance();
                MenuController.quitGameScreenToMenu(new MainMenuScreen());
            }
        });

        MenuHoverController.addHoverEffect(newGameBtn);
        MenuHoverController.addHoverEffect(mainMenuBtn);
    }
}
