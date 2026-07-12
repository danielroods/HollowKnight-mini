package HollowKnight.source.model.data;

public class SettingsData {

    private static float brightness = 0.9f;
    private static float musicVolume = 0.5f;
    private static float sfxVolume = 0.8f;
    private static boolean musicMuted = false;

    public static boolean isMuted() {
        return musicMuted;
    }

    public static void setMuted(boolean muted) {
        SettingsData.musicMuted = muted;
    }

    public static float getMusicVolume() {
        return musicVolume;
    }

    public static void setMusicVolume(float volume) {
        SettingsData.musicVolume = volume;
    }

    public static float getSfxVolume() {
        return sfxVolume;
    }

    public static void setSfxVolume(float sfxVolume) {
        SettingsData.sfxVolume = sfxVolume;
    }

    public static float getBrightness() {
        return brightness;
    }

    public static void setBrightness(float brightness) {
        SettingsData.brightness = brightness;
    }
}
