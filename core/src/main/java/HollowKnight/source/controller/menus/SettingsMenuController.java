package HollowKnight.source.controller.menus;

import HollowKnight.source.controller.MenuController;
import HollowKnight.source.model.data.SettingsData;
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
                                        Slider sfxVolumeSlider,
                                        CheckBox muteCheckBox,
                                        Slider brightnessSlider,
                                        TextButton backBtn) {

        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                float volume = volumeSlider.getValue();

                SettingsData.setMusicVolume(volume);

                if (!muteCheckBox.isChecked()) {
                    AudioController.getInstance().setMaxVolume(volume);
                }
            }
        });

        sfxVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SettingsData.setSfxVolume(sfxVolumeSlider.getValue());
            }
        });

        muteCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SettingsData.setMuted(muteCheckBox.isChecked());

                if (muteCheckBox.isChecked()) {
                    AudioController.getInstance().setMaxVolume(0f);
                }
                else {
                    AudioController.getInstance().setMaxVolume(SettingsData.getMusicVolume());
                }
            }
        });

        brightnessSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SettingsData.setBrightness(brightnessSlider.getValue());
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
