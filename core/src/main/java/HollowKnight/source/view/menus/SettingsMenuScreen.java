package HollowKnight.source.view.menus;

import HollowKnight.source.controller.menus.SettingsMenuController;
import HollowKnight.source.data.GameSettings;
import HollowKnight.source.model.asset.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class SettingsMenuScreen extends BaseMenuScreen {

    @Override
    public void show() {

        Label volumeLabel = new Label("Volume", Assets.getSkin());
        Slider volumeSlider = new Slider(0f, 1f, 0.1f, false, Assets.getSkin());
        volumeSlider.setValue(GameSettings.getVolume());

        Label muteLabel = new Label("Mute", Assets.getSkin());
        CheckBox muteCheckBox = new CheckBox("", Assets.getSkin());
        muteCheckBox.setChecked(GameSettings.isMuted());

        Label brightnessLabel = new Label("Brightness", Assets.getSkin());
        Slider brightnessSlider = new Slider(0.2f, 1f, 0.1f, false, Assets.getSkin());
        brightnessSlider.setValue(GameSettings.getBrightness());

        TextButton backBtn = new TextButton("Back", Assets.getSkin());

        SettingsMenuController.modifyComponents(
            volumeSlider,
            muteCheckBox,
            brightnessSlider,
            backBtn
        );

        volumeLabel.setPosition(320, 470);
        volumeSlider.setSize(450, 20);
        volumeSlider.setPosition(500, 480);

        muteLabel.setPosition(320, 390);
        muteCheckBox.setPosition(500, 385);

        brightnessLabel.setPosition(320, 310);
        brightnessSlider.setSize(450, 20);
        brightnessSlider.setPosition(500, 320);

        prepareButton(backBtn, 300, 60);

        backBtn.setPosition(Gdx.graphics.getWidth() / 2f - 150, 120);

        stage.addActor(volumeLabel);
        stage.addActor(volumeSlider);

        stage.addActor(muteLabel);
        stage.addActor(muteCheckBox);

        stage.addActor(brightnessLabel);
        stage.addActor(brightnessSlider);

        stage.addActor(backBtn);
    }
}
