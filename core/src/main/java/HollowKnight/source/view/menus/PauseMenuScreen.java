package HollowKnight.source.view.menus;

import HollowKnight.source.controller.menus.PauseMenuController;
import HollowKnight.source.model.asset.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class PauseMenuScreen extends BaseMenuScreen {

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Assets.getSkin().getFont("HollowfontGlow");
        titleStyle.fontColor = Color.WHITE;

        Label title = new Label("Paused", titleStyle);
        title.setFontScale(1.4f);
        title.pack();
        title.setPosition(Gdx.graphics.getWidth() / 2f - title.getWidth() / 2f, 560f);

        TextButton resumeBtn = new TextButton("Resume", Assets.getSkin());
        TextButton optionsBtn = new TextButton("Options", Assets.getSkin());
        TextButton cheatsBtn = new TextButton("Cheats", Assets.getSkin());
        TextButton saveAndQuitBtn = new TextButton("Save and Quit to Main Menu", Assets.getSkin());

        PauseMenuController.modifyComponents(resumeBtn, optionsBtn, cheatsBtn, saveAndQuitBtn);

        prepareButton(resumeBtn, 300f, 60f);
        prepareButton(optionsBtn, 300f, 60f);
        prepareButton(cheatsBtn, 300f, 60f);
        prepareButton(saveAndQuitBtn, 460f, 60f);

        float centerX = Gdx.graphics.getWidth() / 2f - 150f;

        resumeBtn.setPosition(centerX, 440f);
        optionsBtn.setPosition(centerX, 360f);
        cheatsBtn.setPosition(centerX, 280f);
        saveAndQuitBtn.setPosition(Gdx.graphics.getWidth() / 2f - 230f, 190f);

        stage.addActor(title);
        stage.addActor(resumeBtn);
        stage.addActor(optionsBtn);
        stage.addActor(cheatsBtn);
        stage.addActor(saveAndQuitBtn);
    }
}
