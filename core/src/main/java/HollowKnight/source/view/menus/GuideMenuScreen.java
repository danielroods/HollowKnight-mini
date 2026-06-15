package HollowKnight.source.view.menus;

import HollowKnight.source.controller.menus.GuideMenuController;
import HollowKnight.source.controller.menus.StartMenuController;
import HollowKnight.source.game_utils.Assets;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class GuideMenuScreen extends BaseMenuScreen {
    @Override
    public void show() {
        rootTable.defaults()
            .width(300)
            .height(80)
            .pad(10);

        TextButton backBtn = new TextButton("Back", Assets.getSkin());

        GuideMenuController.modifyComponents(backBtn);

        rootTable.add(backBtn).row();

        stage.addActor(rootTable);
    }
}
