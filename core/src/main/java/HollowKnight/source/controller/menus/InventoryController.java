package HollowKnight.source.controller.menus;

import HollowKnight.source.model.charm.CharmManager;
import HollowKnight.source.model.charm.CharmType;
import HollowKnight.source.view.menus.InventoryScreen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class InventoryController {

    public static void modifyCharmSlot(Stack charmSlot, CharmType charm, InventoryScreen screen) {
        charmSlot.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                CharmManager.toggleEquip(charm);
                screen.refresh();
            }
        });
    }

    public static void modifyCloseButton(TextButton closeBtn, InventoryScreen screen) {
        closeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.close();
            }
        });

        MenuHoverController.addHoverEffect(closeBtn);
    }
}
