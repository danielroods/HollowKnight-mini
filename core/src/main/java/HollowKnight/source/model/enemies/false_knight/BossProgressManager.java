package HollowKnight.source.model.enemies.false_knight;

import java.util.HashSet;
import java.util.Set;

public class BossProgressManager {

    public static final String FALSE_KNIGHT = "false_knight";

    private static final Set<String> defeatedBosses = new HashSet<>();

    private BossProgressManager() {}

    public static void markDefeated(String bossId) {
        defeatedBosses.add(bossId);
    }

    public static boolean isDefeated(String bossId) {
        return defeatedBosses.contains(bossId);
    }

    public static void setDefeated(String bossId, boolean defeated) {
        if (defeated) {
            defeatedBosses.add(bossId);
        } else {
            defeatedBosses.remove(bossId);
        }
    }

    public static void reset() {
        defeatedBosses.clear();
    }
}
