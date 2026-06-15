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
}
