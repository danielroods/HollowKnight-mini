package HollowKnight.source.model.data;

public class GameSettingsData {

    private static float brightness = 0.9f;
    private static float volume = 0.7f;
    private static boolean musicMuted = false;

    public static boolean isMuted() {
        return musicMuted;
    }

    public static void setMuted(boolean muted) {
        GameSettingsData.musicMuted = muted;
    }

    public static float getVolume() {
        return volume;
    }

    public static void setVolume(float volume) {
        GameSettingsData.volume = volume;
    }

    public static float getBrightness() {
        return brightness;
    }

    public static void setBrightness(float brightness) {
        GameSettingsData.brightness = brightness;
    }
}
