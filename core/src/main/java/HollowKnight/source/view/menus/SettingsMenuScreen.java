package HollowKnight.source.view.menus;

import HollowKnight.source.controller.menus.SettingsMenuController;
import HollowKnight.source.data.GameSettings;
import HollowKnight.source.game_utils.Assets;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class SettingsMenuScreen extends BaseMenuScreen {
    @Override
    public void show() {
        setBackground(Assets.getSettingsMenuBG());
        rootTable.defaults().pad(15);

        Slider volumeSlider = new Slider(0f, 1f, 0.1f, false, Assets.getSkin());
        volumeSlider.setValue(GameSettings.getVolume());

        CheckBox muteCheckBox = new CheckBox(" Mute Music", Assets.getSkin());
        muteCheckBox.setChecked(GameSettings.isMuted());

        Slider brightnessSlider = new Slider(0.2f, 1f, 0.1f, false, Assets.getSkin());
        brightnessSlider.setValue(GameSettings.getBrightness());

        TextButton backBtn = new TextButton("Back", Assets.getSkin());

        SettingsMenuController.modifyComponents(this, volumeSlider, muteCheckBox, brightnessSlider, backBtn);

        rootTable.add(new Label("Volume:", Assets.getSkin()));
        rootTable.add(volumeSlider).width(350).row();

        rootTable.add(new Label("Mute:", Assets.getSkin()));
        rootTable.add(muteCheckBox).row();

        rootTable.add(new Label("Brightness:", Assets.getSkin()));
        rootTable.add(brightnessSlider).width(350).row();

        rootTable.add(backBtn).colspan(2).width(200).padTop(50);

        stage.addActor(rootTable);
    }
}
