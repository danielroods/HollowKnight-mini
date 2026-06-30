package HollowKnight.source.view.menus;

import HollowKnight.source.controller.menus.GuideMenuController;
import HollowKnight.source.model.asset.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class GuideMenuScreen extends BaseMenuScreen {

    @Override
    public void show() {

        TextButton backBtn = new TextButton("Back", Assets.getSkin());

        GuideMenuController.modifyComponents(backBtn);

        prepareButton(backBtn, 300, 60);

        backBtn.setPosition(Gdx.graphics.getWidth() / 2f - 150, 120);

        stage.addActor(backBtn);
    }
}
