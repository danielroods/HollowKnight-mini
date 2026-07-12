package HollowKnight.source.model.achievement;

import HollowKnight.source.model.data.AchievementSaveData;
import HollowKnight.source.model.data.AchievementSaveManager;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;

public class AchievementManager {
    private static final HashSet<AchievementType> unlockedAchievements = new HashSet<>();
    private static final Queue<AchievementType> pendingPopups = new ArrayDeque<>();
    private static boolean loaded = false;

    public static void unlock(AchievementType achievement) {
        ensureLoaded();

        if (unlockedAchievements.add(achievement)) {
            pendingPopups.add(achievement);
            persist();
        }
    }

    public static boolean isUnlocked(AchievementType achievement) {
        ensureLoaded();
        return unlockedAchievements.contains(achievement);
    }

    public static AchievementType pollPendingPopup() {
        return pendingPopups.poll();
    }

    public static void ensureLoaded() {
        if (loaded) return;
        loaded = true;

        AchievementSaveData data = AchievementSaveManager.load();
        if (data == null || data.getUnlockedAchievementNames() == null) return;

        for (String name : data.getUnlockedAchievementNames()) {
            try {
                unlockedAchievements.add(AchievementType.valueOf(name));
            }
            catch (IllegalArgumentException ignored) {}
        }
    }

    private static void persist() {
        AchievementSaveData data = new AchievementSaveData();
        data.setUnlockedAchievementNames(getUnlockedNames());
        AchievementSaveManager.save(data);
    }

    public static String[] getUnlockedNames() {
        String[] names = new String[unlockedAchievements.size()];
        int i = 0;
        for (AchievementType type : unlockedAchievements) {
            names[i++] = type.name();
        }
        return names;
    }

    public static void resetAll() {
        unlockedAchievements.clear();
        pendingPopups.clear();
        AchievementSaveManager.delete();
    }
}
