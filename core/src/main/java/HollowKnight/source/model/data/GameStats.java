package HollowKnight.source.model.data;

public class GameStats {
    private static float totalPlayTimeSeconds;
    private static int enemiesDefeated;
    private static int totalDeaths;

    private GameStats() {}

    public static void tickPlayTime(float delta) {
        totalPlayTimeSeconds += delta;
    }

    public static void increaseDefeatedEnemies() {
        enemiesDefeated++;
    }

    public static void increaseTotalDeath() {
        totalDeaths++;
    }

    public static void reset() {
        totalPlayTimeSeconds = 0f;
        enemiesDefeated = 0;
        totalDeaths = 0;
    }

    public static float getTotalPlayTimeSeconds() { return totalPlayTimeSeconds; }
    public static int getEnemiesDefeated() { return enemiesDefeated; }
    public static int getTotalDeaths() { return totalDeaths; }

    public static void setTotalPlayTimeSeconds(float seconds) { totalPlayTimeSeconds = seconds; }
    public static void setEnemiesDefeated(int count) { enemiesDefeated = count; }
    public static void setTotalDeaths(int count) { totalDeaths = count; }
}
