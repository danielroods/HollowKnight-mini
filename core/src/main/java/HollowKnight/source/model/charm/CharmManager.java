package HollowKnight.source.model.charm;

import HollowKnight.source.controller.PlayerController;

import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class CharmManager {
    private static final Set<CharmType> collectedCharms = EnumSet.allOf(CharmType.class);
    private static final Set<CharmType> equippedCharms = new LinkedHashSet<>();

    public static boolean isCollected(CharmType charm) {
        return collectedCharms.contains(charm);
    }

    public static boolean isEquipped(CharmType charm) {
        return equippedCharms.contains(charm);
    }

    public static boolean toggleEquip(CharmType charm) {
        if (!isCollected(charm)) return false;

        if (equippedCharms.contains(charm)) {
            equippedCharms.remove(charm);
            return true;
        }

        if (getUsedNotches() + 1 > getTotalNotches())
            return false;

        equippedCharms.add(charm);
        return true;
    }

    public static int getUsedNotches() {
        int used = 0;
        for (CharmType charm : equippedCharms)
            used++;
        return used;
    }

    public static int getTotalNotches() {
        return PlayerController.isCharmMasterMode() ? CharmConstants.CHARM_MASTER_MODE_NOTCHES : CharmConstants.TOTAL_NOTCHES;
    }
}
