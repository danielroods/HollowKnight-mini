package HollowKnight.source.controller.spell;

import HollowKnight.source.controller.enemies.CrystalCrawlerController;
import HollowKnight.source.controller.enemies.CrystalGuardianController;
import HollowKnight.source.controller.enemies.FalseKnightController;
import HollowKnight.source.controller.enemies.HuskHornheadController;
import HollowKnight.source.controller.enemies.MossflyController;
import HollowKnight.source.controller.npc.ZoteController;
import HollowKnight.source.model.asset.Assets;
import HollowKnight.source.model.data.GameSettingsData;
import HollowKnight.source.model.player.Player;
import HollowKnight.source.model.spell.HowlingWraiths;
import HollowKnight.source.model.spell.HowlingWraithsConstants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HowlingWraithsController {
    private final List<HowlingWraiths> activeEffects = new ArrayList<>();
    private boolean isFirstTick = true;

    public void spawn(Player player) {
        activeEffects.add(new HowlingWraiths(player));
    }

    public void update(float delta, Player player,
                       MossflyController mossflyController,
                       HuskHornheadController huskHornheadController,
                       CrystalGuardianController crystalGuardianController,
                       CrystalCrawlerController crystalCrawlerController,
                       FalseKnightController falseKnightController,
                       ZoteController zoteController) {

        Iterator<HowlingWraiths> iterator = activeEffects.iterator();
        while (iterator.hasNext()) {
            if (isFirstTick) {
                if (Assets.getHowlingWraithsSfx() != null) {
                    Assets.getHowlingWraithsSfx().play(GameSettingsData.getVolume());
                }
                isFirstTick = false;
            }
            HowlingWraiths effect = iterator.next();

            effect.followPlayer(player);
            effect.setLifeTimer(effect.getLifeTimer() + delta);

            int expectedTicks = Math.min(HowlingWraithsConstants.TICK_COUNT, (int) (effect.getLifeTimer() / HowlingWraithsConstants.TICK_DURATION) + 1);

            while (effect.getTicksFired() < expectedTicks) {
                applyTick(effect, player, mossflyController, huskHornheadController, crystalGuardianController, crystalCrawlerController, falseKnightController, zoteController);
                effect.setTicksFired(effect.getTicksFired() + 1);
            }

            if (effect.getLifeTimer() >= HowlingWraithsConstants.DURATION) {
                effect.setExpired(true);
            }

            if (effect.isExpired()) {
                iterator.remove();
                isFirstTick = true;
            }
        }
    }

    private void applyTick(HowlingWraiths effect, Player player,
                           MossflyController mossflyController,
                           HuskHornheadController huskHornheadController,
                           CrystalGuardianController crystalGuardianController,
                           CrystalCrawlerController crystalCrawlerController,
                           FalseKnightController falseKnightController,
                           ZoteController zoteController) {
        if (mossflyController != null)
            mossflyController.checkHowlingWraithsHit(effect, player);

        if (huskHornheadController != null)
            huskHornheadController.checkHowlingWraithsHit(effect, player);

        if (crystalGuardianController != null)
            crystalGuardianController.checkHowlingWraithsHit(effect, player);

        if (crystalCrawlerController != null)
            crystalCrawlerController.checkHowlingWraithsHit(effect, player);

        if (falseKnightController != null)
            falseKnightController.checkHowlingWraithsHit(effect);

        if (zoteController != null)
            zoteController.checkHowlingWraithsHit(effect, player);
    }

    public List<HowlingWraiths> getActiveEffects() { return activeEffects; }
}
