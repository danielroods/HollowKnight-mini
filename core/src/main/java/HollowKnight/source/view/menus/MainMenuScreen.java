package HollowKnight.source.view.menus;

import HollowKnight.source.game_utils.Assets;
import HollowKnight.source.controller.menus.MainMenuController;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MainMenuScreen extends BaseMenuScreen {
    @Override
    public void show() {
        rootTable.defaults()
            .width(300)
            .height(80)
            .pad(10);

        TextButton startGameBtn = new TextButton("Start Game", Assets.getSkin());
        TextButton settingsBtn = new TextButton("Settings", Assets.getSkin());
        TextButton guideBtn = new TextButton("Guide", Assets.getSkin());
        TextButton achievementsBtn = new TextButton("Achievements", Assets.getSkin());
        TextButton quitGameBtn = new TextButton("Quit Game", Assets.getSkin());

        MainMenuController.modifyComponents(startGameBtn, settingsBtn, guideBtn, achievementsBtn, quitGameBtn);

        rootTable.add(startGameBtn).row();
        rootTable.add(settingsBtn).row();
        rootTable.add(guideBtn).row();
        rootTable.add(achievementsBtn).row();
        rootTable.add(quitGameBtn).row();

        stage.addActor(rootTable);
    }
}
