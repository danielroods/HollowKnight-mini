package HollowKnight.source.view.menus;

import HollowKnight.source.controller.menus.CheatsMenuController;
import HollowKnight.source.model.asset.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class CheatsMenuScreen extends BaseMenuScreen {

    private static final String[][] CHEATS = {
        {"Ctrl + D", "God Mode"},
        {"Ctrl + A", "Boss Arena Teleport"},
        {"Ctrl + N", "Spectator"},
        {"Ctrl + I", "Charm Master"},
        {"Ctrl + E", "Emergency Heal"},
        {"Ctrl + L", "Refill Soul"},
    };

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        float centerX = Gdx.graphics.getWidth() / 2f;

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Assets.getSkin().getFont("HollowfontGlow");
        titleStyle.fontColor = Color.FIREBRICK;

        Label title = new Label("Cheat Codes", titleStyle);
        title.setFontScale(2.2f);
        title.pack();
        title.setPosition(centerX - title.getWidth() / 2f, 860f);
        stage.addActor(title);

        Label.LabelStyle keyStyle = new Label.LabelStyle();
        keyStyle.font = Assets.getSkin().getFont("HollowfontGlow");
        keyStyle.fontColor = new Color(0.95f, 0.85f, 0.5f, 1f);

        Label.LabelStyle descStyle = new Label.LabelStyle();
        descStyle.font = Assets.getSkin().getFont("AchievementDescFont");
        descStyle.fontColor = new Color(0.85f, 0.85f, 0.85f, 1f);

        float y = 700f;

        for (String[] row : CHEATS) {
            Label key = new Label(row[0], keyStyle);
            key.setPosition(centerX - 220f, y);
            stage.addActor(key);

            Label desc = new Label(row[1], descStyle);
            desc.setPosition(centerX + 50f, y + 7f);
            stage.addActor(desc);

            y -= 63f;
        }

        TextButton backBtn = new TextButton("Back", Assets.getSkin());
        CheatsMenuController.modifyComponents(backBtn);

        prepareButton(backBtn, 300f, 60f);
        backBtn.setPosition(centerX - 150f, 140f);
        stage.addActor(backBtn);
    }
}
