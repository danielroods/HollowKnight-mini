package HollowKnight.source.view.menus;

import HollowKnight.source.controller.menus.StartMenuController;
import HollowKnight.source.game_utils.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class StartMenuScreen extends BaseMenuScreen {

    @Override
    public void show() {

        TextButton newGameBtn = new TextButton("New Game", Assets.getSkin());
        TextButton backBtn = new TextButton("Back", Assets.getSkin());

        StartMenuController.modifyComponents(backBtn, newGameBtn);

        float centerX = Gdx.graphics.getWidth() / 2f - 150;

        prepareButton(newGameBtn, 300, 60);
        prepareButton(backBtn, 300, 60);

        newGameBtn.setPosition(centerX, 350);
        backBtn.setPosition(centerX, 250);

        stage.addActor(newGameBtn);
        stage.addActor(backBtn);
    }
}
