package HollowKnight.source.view.menus;

import HollowKnight.source.controller.menus.SettingsMenuController;
import HollowKnight.source.model.data.SettingsData;
import HollowKnight.source.model.asset.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

public class SettingsMenuScreen extends BaseMenuScreen {

    @Override
    public void show() {
        setupCursor();
        Gdx.input.setInputProcessor(stage);

        float panelWidth = 700;
        float x = (Gdx.graphics.getWidth() - panelWidth) / 2f;

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Assets.getSkin().getFont("HollowfontGlow");
        titleStyle.fontColor = Color.WHITE;

        Label menuTitle = new Label("SETTINGS", titleStyle);
        menuTitle.setFontScale(2.2f);
        menuTitle.setColor(Color.WHITE);
        menuTitle.setPosition(x + 155f , 930f);

        Label volumeLabel = new Label("Music Volume", Assets.getSkin());
        Slider volumeSlider = new Slider(0f, 1f, 0.1f, false, Assets.getSkin(), "hollow-style");
        volumeSlider.setValue(SettingsData.getMusicVolume());

        Label sfxVolumeLabel = new Label("SFX Volume", Assets.getSkin());
        Slider sfxVolumeSlider = new Slider(0f, 1f, 0.1f, false, Assets.getSkin(), "hollow-style");
        sfxVolumeSlider.setValue(SettingsData.getSfxVolume());

        Label muteLabel = new Label("Mute Music", Assets.getSkin());
        CheckBox muteCheckBox = new CheckBox("", Assets.getSkin(), "hollow-style");
        muteCheckBox.setChecked(SettingsData.isMuted());

        Label brightnessLabel = new Label("Brightness", Assets.getSkin());
        Slider brightnessSlider = new Slider(0.2f, 1f, 0.1f, false, Assets.getSkin(), "hollow-style");
        brightnessSlider.setValue(SettingsData.getBrightness());

        TextButton backBtn = new TextButton("Back", Assets.getSkin());
        prepareButton(backBtn, 300, 60);

        SettingsMenuController.modifyComponents(
            volumeSlider,
            sfxVolumeSlider,
            muteCheckBox,
            brightnessSlider,
            backBtn
        );

        Table table = new Table();
        table.setFillParent(true);

        table.padBottom(120f);

        table.add(volumeLabel).align(Align.left).padRight(50f).padBottom(40f);
        table.add(volumeSlider).width(400f).padBottom(40f).row();

        table.add(sfxVolumeLabel).align(Align.left).padRight(50f).padBottom(40f);
        table.add(sfxVolumeSlider).width(400f).padBottom(40f).row();

        table.add(muteLabel).align(Align.left).padRight(50f).padBottom(40f);
        table.add(muteCheckBox).align(Align.left).padBottom(40f).row();

        table.add(brightnessLabel).align(Align.left).padRight(50f).padBottom(40f);
        table.add(brightnessSlider).width(400f).padBottom(40f).row();

        backBtn.setPosition(Gdx.graphics.getWidth() / 2f - 150, 80);

        stage.addActor(menuTitle);
        stage.addActor(table);
        stage.addActor(backBtn);
    }
}
