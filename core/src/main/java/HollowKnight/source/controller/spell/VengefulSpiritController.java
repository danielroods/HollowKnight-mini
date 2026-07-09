package HollowKnight.source.controller.spell;

import HollowKnight.source.controller.enemies.CrystalCrawlerController;
import HollowKnight.source.controller.enemies.CrystalGuardianController;
import HollowKnight.source.controller.enemies.FalseKnightController;
import HollowKnight.source.controller.enemies.HuskHornheadController;
import HollowKnight.source.controller.enemies.MossflyController;
import HollowKnight.source.controller.npc.ZoteController;
import HollowKnight.source.model.player.Player;
import HollowKnight.source.model.spell.VengefulSpirit;
import HollowKnight.source.model.spell.VengefulSpiritConstants;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VengefulSpiritController {
    private final List<VengefulSpirit> projectiles = new ArrayList<>();

    public void spawn(Player player) {
        float originX = player.isFacingRight() ? player.getBounds().x - 20f : player.getBounds().x - 45f;
        float originY = player.getBounds().y + player.getBounds().height / 2f - VengefulSpiritConstants.HEIGHT / 2f;

        projectiles.add(new VengefulSpirit(originX, originY, player.isFacingRight()));
    }

    public void update(float delta, Player player, MapLayer logicLayer,
                       MossflyController mossflyController,
                       HuskHornheadController huskHornheadController,
                       CrystalGuardianController crystalGuardianController,
                       CrystalCrawlerController crystalCrawlerController,
                       FalseKnightController falseKnightController,
                       ZoteController zoteController) {

        Iterator<VengefulSpirit> it = projectiles.iterator();
        while (it.hasNext()) {
            VengefulSpirit spirit = it.next();

            move(delta, spirit);

            if (!spirit.isExpired() && hitsSolidObstacle(spirit, logicLayer)) {
                spirit.setExpired(true);
            }

            if (!spirit.isExpired()) {
                dispatchHitChecks(spirit, player, mossflyController, huskHornheadController,
                    crystalGuardianController, crystalCrawlerController, falseKnightController, zoteController);
            }

            if (spirit.isExpired()) {
                it.remove();
            }
        }
    }

    private void move(float delta, VengefulSpirit spirit) {
        float dx = spirit.getVelocityX() * delta;
        spirit.setPosition(spirit.getPosition().x + dx, spirit.getPosition().y);

        spirit.setTraveledDistance(spirit.getTraveledDistance() + Math.abs(dx));
        spirit.setLifeTimer(spirit.getLifeTimer() + delta);

        if (spirit.getTraveledDistance() >= VengefulSpiritConstants.MAX_RANGE
            || spirit.getLifeTimer() >= VengefulSpiritConstants.MAX_LIFETIME) {
            spirit.setExpired(true);
        }
    }

    private boolean hitsSolidObstacle(VengefulSpirit spirit, MapLayer logicLayer) {
        if (logicLayer == null) return false;

        for (MapObject obj : logicLayer.getObjects()) {
            if (!(obj instanceof RectangleMapObject)) continue;
            String name = obj.getName();
            if (name == null) continue;
            if (!name.equals("platform") && !name.equals("wall") && !name.equals("ceiling")) continue;

            if (Intersector.overlaps(spirit.getBounds(), ((RectangleMapObject) obj).getRectangle())) {
                return true;
            }
        }
        return false;
    }

    private void dispatchHitChecks(VengefulSpirit spirit, Player player,
                                   MossflyController mossflyController,
                                   HuskHornheadController huskHornheadController,
                                   CrystalGuardianController crystalGuardianController,
                                   CrystalCrawlerController crystalCrawlerController,
                                   FalseKnightController falseKnightController,
                                   ZoteController zoteController) {
        if (mossflyController != null)
            mossflyController.checkVengefulSpiritHit(spirit, player);

        if (huskHornheadController != null)
            huskHornheadController.checkVengefulSpiritHit(spirit, player);

        if (crystalGuardianController != null)
            crystalGuardianController.checkVengefulSpiritHit(spirit, player);

        if (crystalCrawlerController != null)
            crystalCrawlerController.checkVengefulSpiritHit(spirit, player);

        if (falseKnightController != null)
            falseKnightController.checkVengefulSpiritHit(spirit);

        if (zoteController != null)
            zoteController.checkVengefulSpiritHit(spirit, player);
    }

    public List<VengefulSpirit> getProjectiles() { return projectiles; }
}
