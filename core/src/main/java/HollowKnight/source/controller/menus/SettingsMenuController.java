package HollowKnight.source.controller.menus;

import HollowKnight.source.controller.MenuController;
import HollowKnight.source.data.GameSettings;
import HollowKnight.source.game_utils.AudioManager;
import HollowKnight.source.view.menus.MainMenuScreen;
import HollowKnight.source.view.menus.SettingsMenuScreen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SettingsMenuController {

    public static void modifyComponents(SettingsMenuScreen view,
                                        Slider volumeSlider,
                                        CheckBox muteCheckBox,
                                        Slider brightnessSlider,
                                        TextButton backBtn) {

        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                float volume = volumeSlider.getValue();

                GameSettings.setVolume(volume);

                if (!muteCheckBox.isChecked()) {
                    AudioManager.getInstance().setMaxVolume(volume);
                }
            }
        });

        muteCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameSettings.setMuted(muteCheckBox.isChecked());

                if (muteCheckBox.isChecked()) {
                    AudioManager.getInstance().setMaxVolume(0f);
                } else {
                    AudioManager.getInstance().setMaxVolume(GameSettings.getVolume());
                }
            }
        });

        brightnessSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameSettings.setBrightness(brightnessSlider.getValue());
            }
        });

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MenuController.setScreen(new MainMenuScreen());
            }
        });
    }
}
