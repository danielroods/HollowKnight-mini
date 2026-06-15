package HollowKnight.source.data;

public class GameSettings {

    private static float brightness = 0.9f;
    private static float volume = 0.7f;
    private static boolean musicMuted = false;

    public static boolean isMuted() {
        return musicMuted;
    }

    public static void setMuted(boolean muted) {
        GameSettings.musicMuted = muted;
    }

    public static float getVolume() {
        return volume;
    }

    public static void setVolume(float volume) {
        GameSettings.volume = volume;
    }

    public static float getBrightness() {
        return brightness;
    }

    public static void setBrightness(float brightness) {
        GameSettings.brightness = brightness;
    }
}
