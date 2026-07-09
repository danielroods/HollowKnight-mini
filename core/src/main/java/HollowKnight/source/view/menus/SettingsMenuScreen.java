package HollowKnight.source.view.menus;

import HollowKnight.source.controller.menus.SettingsMenuController;
import HollowKnight.source.data.GameSettings;
import HollowKnight.source.model.asset.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

public class SettingsMenuScreen extends BaseMenuScreen {

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Label volumeLabel = new Label("Volume", Assets.getSkin());
        Slider volumeSlider = new Slider(0f, 1f, 0.1f, false, Assets.getSkin(), "hollow-style");
        volumeSlider.setValue(GameSettings.getVolume());

        Label muteLabel = new Label("Mute", Assets.getSkin());
        CheckBox muteCheckBox = new CheckBox("", Assets.getSkin(), "hollow-style");
        muteCheckBox.setChecked(GameSettings.isMuted());

        Label brightnessLabel = new Label("Brightness", Assets.getSkin());
        Slider brightnessSlider = new Slider(0.2f, 1f, 0.1f, false, Assets.getSkin(), "hollow-style");
        brightnessSlider.setValue(GameSettings.getBrightness());

        TextButton backBtn = new TextButton("Back", Assets.getSkin());
        prepareButton(backBtn, 300, 60);

        SettingsMenuController.modifyComponents(
            volumeSlider,
            muteCheckBox,
            brightnessSlider,
            backBtn
        );

        Table table = new Table();
        table.setFillParent(true);

        table.padBottom(100f);

        table.add(volumeLabel).align(Align.left).padRight(50f).padBottom(40f);
        table.add(volumeSlider).width(400f).padBottom(40f).row();

        table.add(muteLabel).align(Align.left).padRight(50f).padBottom(40f);
        table.add(muteCheckBox).align(Align.left).padBottom(40f).row();

        table.add(brightnessLabel).align(Align.left).padRight(50f).padBottom(40f);
        table.add(brightnessSlider).width(400f).padBottom(40f).row();

        backBtn.setPosition(Gdx.graphics.getWidth() / 2f - 150, 80);

        stage.addActor(table);
        stage.addActor(backBtn);
    }
}
