package HollowKnight.source.controller;

import HollowKnight.source.model.data.SettingsData;
import com.badlogic.gdx.audio.Music;

public class AudioController {

    private static AudioController instance;

    private Music currentMusic;
    private Music nextMusic;

    private boolean isFading;
    private float fadeTimer;
    private float fadeDuration = 2.0f;
    private float maxVolume = 1.0f;

    private AudioController() {}

    public static AudioController getInstance() {
        if (instance == null) {
            instance = new AudioController();
        }
        return instance;
    }

    public void update(float delta) {
        if (isFading) {
            fadeTimer += delta;
            float progress = fadeTimer / fadeDuration;

            if (progress >= 1.0f) {
                if (currentMusic != null) {
                    currentMusic.stop();
                }
                currentMusic = nextMusic;
                currentMusic.setVolume(maxVolume);
                nextMusic = null;
                isFading = false;
            } else {
                if (currentMusic != null) {
                    currentMusic.setVolume(maxVolume * (1.0f - progress));
                }
                if (nextMusic != null) {
                    nextMusic.setVolume(maxVolume * progress);
                }
            }
        }
    }

    public void playMusic(Music newMusic) {
        if (newMusic == null) return;
        if (newMusic == currentMusic) return;
        if (isFading && newMusic == nextMusic) return;

        this.maxVolume = SettingsData.getMusicVolume();
        if (SettingsData.isMuted()) this.maxVolume = 0f;

        if (currentMusic == null) {
            currentMusic = newMusic;
            currentMusic.setVolume(maxVolume);
            currentMusic.setLooping(true);
            currentMusic.play();
            return;
        }

        if (isFading && nextMusic != null) {
            nextMusic.stop();
        }

        nextMusic = newMusic;
        nextMusic.setVolume(0f);
        nextMusic.setLooping(true);
        nextMusic.play();

        isFading = true;
        fadeTimer = 0f;
    }

    public void setFadeDuration(float duration) {
        this.fadeDuration = duration;
    }

    public void setMaxVolume(float volume) {
        this.maxVolume = volume;
        if (currentMusic != null && !isFading) {
            currentMusic.setVolume(volume);
        }
    }
}
