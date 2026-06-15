package HollowKnight.source.game_utils;

import com.badlogic.gdx.audio.Music;

public class AudioManager {

    private static AudioManager instance;

    private Music currentMusic;
    private Music nextMusic;

    private boolean isFading;
    private float fadeTimer;
    private float fadeDuration = 2.0f;
    private float maxVolume = 1.0f;

    private AudioManager() {}

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
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
                    currentMusic.dispose();
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
        if (currentMusic == newMusic || newMusic == null) {
            return;
        }

        if (currentMusic == null) {
            currentMusic = newMusic;
            currentMusic.setVolume(maxVolume);
            currentMusic.setLooping(true);
            currentMusic.play();
        } else {
            nextMusic = newMusic;
            nextMusic.setVolume(0f);
            nextMusic.setLooping(true);
            nextMusic.play();

            isFading = true;
            fadeTimer = 0f;
        }
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
