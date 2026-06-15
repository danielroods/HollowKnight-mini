package HollowKnight.source.view.menus;

import HollowKnight.source.game_utils.Assets;
import HollowKnight.source.controller.menus.StartMenuController;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class StartMenuScreen extends BaseMenuScreen {
    @Override
    public void show() {
        rootTable.defaults()
            .width(300)
            .height(80)
            .pad(10);

        TextButton backBtn = new TextButton("Back", Assets.getSkin());
        TextButton newGameBtn = new TextButton("New Game", Assets.getSkin());

        StartMenuController.modifyComponents(backBtn, newGameBtn);

        rootTable.add(newGameBtn).row();
        rootTable.add(backBtn).row();

        stage.addActor(rootTable);
    }
}
