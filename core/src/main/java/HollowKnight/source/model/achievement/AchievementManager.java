package HollowKnight.source.model.achievement;

import java.util.HashSet;
import java.util.Set;

public class AchievementManager {
    private static final HashSet<AchievementType> unlockedAchievements = new HashSet<>();

    public static void unlock(AchievementType achievement) {
        unlockedAchievements.add(achievement);
    }

    public static boolean isUnlocked(AchievementType achievement) {
        return unlockedAchievements.contains(achievement);
    }

    public static String[] getUnlockedNames() {
        String[] names = new String[unlockedAchievements.size()];
        int i = 0;
        for (AchievementType type : unlockedAchievements) {
            names[i++] = type.name();
        }
        return names;
    }

    public static void loadUnlocked(String[] names) {
        unlockedAchievements.clear();
        if (names == null) return;

        for (String name : names) {
            try {
                unlockedAchievements.add(AchievementType.valueOf(name));
            }
            catch (IllegalArgumentException ignored) {
                // Achievement no longer exists under this name - skip it :)
            }
        }
    }

    public static void reset() {
        unlockedAchievements.clear();
    }
}
