package HollowKnight.source.view.menus;

import HollowKnight.source.controller.menus.CheatsMenuController;
import HollowKnight.source.model.asset.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class CheatsMenuScreen extends BaseMenuScreen {

    private static final String[] CHEAT_CODES = {
        "Ctrl + G   -   God Mode (no damage)",
        "Ctrl + R   -   Refill Soul",
        "Ctrl + H   -   Emergency Heal",
        "Ctrl + B   -   Boss Arena Teleport",
    };

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Assets.getSkin().getFont("HollowfontGlow");
        titleStyle.fontColor = Color.WHITE;

        Label title = new Label("Cheat Codes", titleStyle);
        title.setFontScale(1.3f);
        title.pack();
        title.setPosition(Gdx.graphics.getWidth() / 2f - title.getWidth() / 2f, 620f);
        stage.addActor(title);

        Label.LabelStyle codeStyle = new Label.LabelStyle();
        codeStyle.font = Assets.getSkin().getFont("AchievementDescFont");
        codeStyle.fontColor = new Color(0.85f, 0.85f, 0.85f, 1f);

        float y = 530f;
        for (String code : CHEAT_CODES) {
            Label line = new Label(code, codeStyle);
            line.pack();
            line.setPosition(Gdx.graphics.getWidth() / 2f - line.getWidth() / 2f, y);
            stage.addActor(line);
            y -= 45f;
        }

        TextButton backBtn = new TextButton("Back", Assets.getSkin());
        CheatsMenuController.modifyComponents(backBtn);

        prepareButton(backBtn, 300f, 60f);
        backBtn.setPosition(Gdx.graphics.getWidth() / 2f - 150f, 140f);
        stage.addActor(backBtn);
    }
}
