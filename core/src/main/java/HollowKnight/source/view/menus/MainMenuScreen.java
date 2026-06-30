package HollowKnight.source.view.menus;

import HollowKnight.source.model.asset.Assets;
import HollowKnight.source.controller.menus.MainMenuController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MainMenuScreen extends BaseMenuScreen {
    @Override
    public void show() {
        Image title = new Image(new Texture("menu/title.png"));

        TextButton startGameBtn = new TextButton("Start Game", Assets.getSkin());
        TextButton settingsBtn = new TextButton("Settings", Assets.getSkin());
        TextButton guideBtn = new TextButton("Guide", Assets.getSkin());
        TextButton achievementsBtn = new TextButton("Achievements", Assets.getSkin());
        TextButton quitGameBtn = new TextButton("Quit Game", Assets.getSkin());

        Image logos = new Image(new Texture("menu/Hidden_Dreams_Logo.png"));
        Image teamCherry = new Image(new Texture("menu/team_cherry.png"));
        teamCherry.setSize(85f,60f);

        MainMenuController.modifyComponents(
            startGameBtn,
            settingsBtn,
            guideBtn,
            achievementsBtn,
            quitGameBtn
        );

        title.setPosition(Gdx.graphics.getWidth()/2f - title.getWidth() / 2f, 550f);

        prepareButton(startGameBtn, 300f, 60);
        prepareButton(settingsBtn, 300, 60);
        prepareButton(guideBtn, 300, 60);
        prepareButton(achievementsBtn, 300, 60);
        prepareButton(quitGameBtn, 300, 60);

        float centerX = Gdx.graphics.getWidth()/2f - 150;

        startGameBtn.setPosition(centerX, 450);
        settingsBtn.setPosition(centerX, 370);
        guideBtn.setPosition(centerX, 290);
        achievementsBtn.setPosition(centerX, 210);
        quitGameBtn.setPosition(centerX, 130);

        logos.setPosition(40, 40);

        teamCherry.setPosition(Gdx.graphics.getWidth() - teamCherry.getWidth() - 40, 40);

        stage.addActor(title);

        stage.addActor(startGameBtn);
        stage.addActor(settingsBtn);
        stage.addActor(guideBtn);
        stage.addActor(achievementsBtn);
        stage.addActor(quitGameBtn);

        stage.addActor(logos);
        stage.addActor(teamCherry);
    }
}
