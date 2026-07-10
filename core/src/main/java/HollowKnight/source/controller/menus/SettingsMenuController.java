package HollowKnight.source.controller.menus;

import HollowKnight.source.controller.MenuController;
import HollowKnight.source.model.data.GameSettingsData;
import HollowKnight.source.controller.AudioController;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SettingsMenuController {

    public static void modifyComponents(Slider volumeSlider,
                                        CheckBox muteCheckBox,
                                        Slider brightnessSlider,
                                        TextButton backBtn) {

        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                float volume = volumeSlider.getValue();

                GameSettingsData.setVolume(volume);

                if (!muteCheckBox.isChecked()) {
                    AudioController.getInstance().setMaxVolume(volume);
                }
            }
        });

        muteCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameSettingsData.setMuted(muteCheckBox.isChecked());

                if (muteCheckBox.isChecked()) {
                    AudioController.getInstance().setMaxVolume(0f);
                } else {
                    AudioController.getInstance().setMaxVolume(GameSettingsData.getVolume());
                }
            }
        });

        brightnessSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameSettingsData.setBrightness(brightnessSlider.getValue());
            }
        });

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MenuController.setMenuScreen(MenuController.settingsReturnScreen());
            }
        });


        MenuHoverController.addHoverEffect(backBtn);
    }
}
