package HollowKnight.source.model.data;

public class GameData {
    private int masksCount;
    private int soulsCount;

    private String mapId;

    private float playerX;
    private float playerY;

    private boolean falseKnightDefeated;

    private float totalPlayTimeSeconds;
    private int enemiesDefeated;
    private int totalDeaths;

    private String savedAt;

    public GameData() {
    }

    public int getMasksCount() { return masksCount; }
    public void setMasksCount(int masksCount) { this.masksCount = masksCount; }

    public int getSoulsCount() { return soulsCount; }
    public void setSoulsCount(int soulsCount) { this.soulsCount = soulsCount; }

    public String getMapId() { return mapId; }
    public void setMapId(String mapId) { this.mapId = mapId; }

    public float getPlayerX() { return playerX; }
    public void setPlayerX(float playerX) { this.playerX = playerX; }

    public float getPlayerY() { return playerY; }
    public void setPlayerY(float playerY) { this.playerY = playerY; }

    public boolean isFalseKnightDefeated() { return falseKnightDefeated; }
    public void setFalseKnightDefeated(boolean falseKnightDefeated) { this.falseKnightDefeated = falseKnightDefeated; }

    public float getTotalPlayTimeSeconds() { return totalPlayTimeSeconds; }
    public void setTotalPlayTimeSeconds(float totalPlayTimeSeconds) { this.totalPlayTimeSeconds = totalPlayTimeSeconds; }

    public int getEnemiesDefeated() { return enemiesDefeated; }
    public void setEnemiesDefeated(int enemiesDefeated) { this.enemiesDefeated = enemiesDefeated; }

    public int getTotalDeaths() { return totalDeaths; }
    public void setTotalDeaths(int totalDeaths) { this.totalDeaths = totalDeaths; }

    public String getSavedAt() { return savedAt; }
    public void setSavedAt(String savedAt) { this.savedAt = savedAt; }
}
