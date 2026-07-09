package HollowKnight.source.controller.enemies;

import HollowKnight.source.controller.PlayerController;
import HollowKnight.source.model.achievement.AchievementManager;
import HollowKnight.source.model.achievement.AchievementType;
import HollowKnight.source.model.enemies.husk_hornhead.HuskHornhead;
import HollowKnight.source.model.enemies.husk_hornhead.HuskHornheadConstants;
import HollowKnight.source.model.enemies.husk_hornhead.HuskHornheadState;
import HollowKnight.source.model.player.Player;
import HollowKnight.source.model.spell.VengefulSpirit;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HuskHornheadController {
    private final PlayerController playerController;

    private final List<HuskHornhead> huskList;
    private final Set<HuskHornhead> hitHuskHornheads = new HashSet<>();

    public HuskHornheadController(List<HuskHornhead> huskList) {
        this.huskList = huskList;
        this.playerController = PlayerController.getInstance();
    }

    public void update(float delta, Player player, MapLayer logicLayer) {
        if (!player.isAttacking()) {
            hitHuskHornheads.clear();
        }

        for (HuskHornhead huskHornhead : huskList) {
            updateOne(delta, huskHornhead, player, logicLayer);
        }
    }

    public List<HuskHornhead> getHuskHornheadList() { return huskList; }

    private void updateOne(float delta, HuskHornhead huskHornhead, Player player, MapLayer logicLayer) {

        tickTimers(huskHornhead, delta);

        if (huskHornhead.getKnockbackTimer() <= 0) {
            switch (huskHornhead.getState()) {
                case WALK:
                    updateWalk(huskHornhead, player);
                    break;
                case REST:
                    updateRest(huskHornhead, player);
                    break;
                case CHARGE:
                    updateCharge(huskHornhead, player);
                    break;
                case DEAD:
                    updateDead(huskHornhead, player);
                    break;
            }
        }

        if (!huskHornhead.isOnGround()) {
            huskHornhead.getVelocity().y += HuskHornheadConstants.GRAVITY * delta;
        }
        else {
            if (huskHornhead.getVelocity().y < 0f)
                huskHornhead.getVelocity().y = 0f;
        }

        huskHornhead.setPosition(huskHornhead.getPosition().x + huskHornhead.getVelocity().x * delta, huskHornhead.getPosition().y + huskHornhead.getVelocity().y * delta);

        resolveCollisions(huskHornhead, logicLayer);

        if (huskHornhead.getState() == HuskHornheadState.WALK && huskHornhead.isOnGround()) {
            if (isCliffAhead(huskHornhead, logicLayer)) {
                flip(huskHornhead);
            }
        }

        checkPlayerContact(huskHornhead, player);
    }

    private void updateWalk(HuskHornhead huskHornhead, Player player) {
        if (playerInDetectionRect(huskHornhead, player)) {
            startCharge(huskHornhead, player);
            return;
        }
        if (huskHornhead.getStateTimer() >= HuskHornheadConstants.PATROL_DURATION) {
            enterState(huskHornhead, HuskHornheadState.REST);
            return;
        }
        huskHornhead.getVelocity().x = huskHornhead.isFacingRight() ? HuskHornheadConstants.PATROL_SPEED : -HuskHornheadConstants.PATROL_SPEED;
    }
    private void updateRest(HuskHornhead huskHornhead, Player player) {
        huskHornhead.getVelocity().x = 0f;
        if (playerInDetectionRect(huskHornhead, player)) {
            startCharge(huskHornhead, player);
            return;
        }
        if (huskHornhead.getStateTimer() >= HuskHornheadConstants.REST_DURATION) {
            enterState(huskHornhead, HuskHornheadState.WALK);
        }
    }
    private void updateCharge(HuskHornhead huskHornhead, Player player) {
        if (huskHornhead.getKnockbackTimer() <= 0) {
            huskHornhead.getVelocity().x = huskHornhead.getChargeDirectionX() * HuskHornheadConstants.CHARGE_SPEED;
        }
    }
    private void updateDead(HuskHornhead huskHornhead, Player player) {
        if (huskHornhead.isOnGround()) {
            huskHornhead.getVelocity().set(0f, 0f);
        }
    }

    private void resolveCollisions(HuskHornhead huskHornhead, MapLayer logicLayer) {
        if (logicLayer == null) return;

        boolean grounded = false;
        Rectangle bounds = new Rectangle(huskHornhead.getBounds());

        for (MapObject obj : logicLayer.getObjects()) {
            if (!(obj instanceof RectangleMapObject)) continue;
            if (!isSolid(obj.getName())) continue;

            Rectangle tile = ((RectangleMapObject) obj).getRectangle();
            Rectangle overlap = new Rectangle();
            if (!Intersector.intersectRectangles(bounds, tile, overlap)) continue;

            if (overlap.width < overlap.height) {
                boolean hitRight = bounds.x < tile.x;
                float push = hitRight ? -overlap.width : overlap.width;
                huskHornhead.setPosition(huskHornhead.getPosition().x + push, huskHornhead.getPosition().y);

                onWallHit(huskHornhead);
            }
            else {
                float bodyMidY = bounds.y + bounds.height / 2f;
                float tileMidY = tile.y + tile.height / 2f;

                if (bodyMidY > tileMidY) {
                    if (huskHornhead.getVelocity().y <= 0f) {
                        float landedY = tile.y + tile.height - HuskHornheadConstants.BOUNDS_OFFSET_Y;
                        huskHornhead.setPosition(huskHornhead.getPosition().x, landedY);
                        huskHornhead.getVelocity().y = 0f;
                        grounded = true;
                    }
                }
                else {
                    if (huskHornhead.getVelocity().y > 0f) {
                        float ceilY = tile.y - HuskHornheadConstants.BOUNDS_H - HuskHornheadConstants.BOUNDS_OFFSET_Y;
                        huskHornhead.setPosition(huskHornhead.getPosition().x, ceilY);
                        huskHornhead.getVelocity().y = 0f;
                    }
                }
            }

            bounds.set(huskHornhead.getBounds());
        }

        huskHornhead.setOnGround(grounded);
    }

    private void onWallHit(HuskHornhead huskHornhead) {
        huskHornhead.getVelocity().x = 0f;

        switch (huskHornhead.getState()) {
            case WALK:
                flip(huskHornhead);
                break;
            case CHARGE:
                flip(huskHornhead);
                endCharge(huskHornhead);
                break;
            default:
                break;
        }
    }

    private boolean isCliffAhead(HuskHornhead huskHornhead, MapLayer logicLayer) {
        if (logicLayer == null) return false;

        Rectangle b = huskHornhead.getBounds();

        float probeW = 6f;
        float probeH = 28f;
        float probeX = huskHornhead.isFacingRight() ? b.x + b.width - probeW : b.x;
        float probeY = b.y - probeH;

        Rectangle probe = new Rectangle(probeX, probeY, probeW, probeH);

        for (MapObject obj : logicLayer.getObjects()) {
            if (!(obj instanceof RectangleMapObject)) continue;
            if (!isSolid(obj.getName())) continue;
            if (Intersector.overlaps(probe, ((RectangleMapObject) obj).getRectangle()))
                return false;
        }
        return true;
    }

    private Rectangle buildDetectionRect(HuskHornhead huskHornhead) {
        Rectangle b = huskHornhead.getBounds();
        float dw = HuskHornheadConstants.DETECTION_W;
        float dh = HuskHornheadConstants.DETECTION_H;

        float detX = huskHornhead.isFacingRight() ? b.x + b.width : b.x - dw;
        float detY = b.y + b.height / 2f - dh / 2f;

        return new Rectangle(detX, detY, dw, dh);
    }

    private boolean playerInDetectionRect(HuskHornhead huskHornhead, Player player) {
        return Intersector.overlaps(buildDetectionRect(huskHornhead), player.getBounds());
    }

    private void startCharge(HuskHornhead huskHornhead, Player player) {
        float playerCX = player.getBounds().x + player.getBounds().width / 2f;
        float enemyCX = huskHornhead.getBounds().x + huskHornhead.getBounds().width / 2f;
        boolean goRight = playerCX > enemyCX;

        huskHornhead.setChargeDirectionX(goRight ? 1f : -1f);
        huskHornhead.setFacingRight(goRight);
        enterState(huskHornhead, HuskHornheadState.CHARGE);
    }

    private void endCharge(HuskHornhead huskHornhead) {
        huskHornhead.getVelocity().x = 0f;
        enterState(huskHornhead, HuskHornheadState.REST);
    }

    private void checkPlayerContact(HuskHornhead huskHornhead, Player player) {
        if (huskHornhead.getState() == HuskHornheadState.DEAD) return;
        if (!player.isAlive() || player.isInvincible()) return;

        if (Intersector.overlaps(huskHornhead.getBounds(), player.getBounds())) {
            if (playerController.isGodMode()) return;
            float enemyXCenter = huskHornhead.getBounds().x + huskHornhead.getBounds().width / 2f;
            float playerXCenter = player.getBounds().x + player.getBounds().width / 2f;
            float knockBackDirection = playerXCenter < enemyXCenter ? -1f : 1f;
            playerController.takeDamage(1);
            playerController.applyKnockback(knockBackDirection);
        }
    }

    public void checkSwordHits(Rectangle swordHitbox, Player player) {
        for (HuskHornhead huskHornhead : huskList) {
            if (huskHornhead.getState() == HuskHornheadState.DEAD) continue;
            if (huskHornhead.isInvincible()) continue;
            if (hitHuskHornheads.contains(huskHornhead)) continue;

            if (Intersector.overlaps(swordHitbox, huskHornhead.getBounds())) {
                playerController.gainSoul();
                hitHuskHornheads.add(huskHornhead);
                applyHit(huskHornhead, player, playerController.getNailDamage());
            }
        }
    }

    public void checkVengefulSpiritHit(VengefulSpirit spirit, Player player) {
        for (HuskHornhead huskHornhead : huskList) {
            if (huskHornhead.getState() == HuskHornheadState.DEAD) continue;
            if (huskHornhead.isInvincible()) continue;
            if (spirit.hasHit(huskHornhead)) continue;

            if (Intersector.overlaps(spirit.getBounds(), huskHornhead.getBounds())) {
                spirit.markHit(huskHornhead);
                applyHit(huskHornhead, player, playerController.getSpellDamage());
            }
        }
    }

    private void applyHit(HuskHornhead huskHornhead, Player player, int damage) {
        huskHornhead.setHealth(huskHornhead.getHealth() - damage);
        huskHornhead.setHurtTimer(HuskHornheadConstants.HURT_COOLDOWN);
        huskHornhead.setKnockbackTimer(HuskHornheadConstants.KNOCKBACK_DURATION);
        huskHornhead.setOnGround(false);

        float knockBackDirectionX = player.isFacingRight() ? 1f : -1f;

        if (huskHornhead.getHealth() <= 0) {
            player.setKilledHuskHornhead(true);
            if (player.isKilledMossfly() && player.isKilledCrystalCrawler() && player.isKilledCrystalGuardian())
            {
                AchievementManager.unlock(AchievementType.TRUE_HUNTER);
            }
            enterState(huskHornhead, HuskHornheadState.DEAD);
        }

        huskHornhead.setKnockbackTimer(HuskHornheadConstants.KNOCKBACK_DURATION);
        huskHornhead.getVelocity().set(knockBackDirectionX * HuskHornheadConstants.KNOCKBACK_SPEED_X * playerController.getKnockbackMultiplier(),
            HuskHornheadConstants.KNOCKBACK_SPEED_Y * playerController.getKnockbackMultiplier());
    }

    private void tickTimers(HuskHornhead huskHornhead, float delta) {
        if (huskHornhead.getHurtTimer() > 0)
            huskHornhead.setHurtTimer(Math.max(0f, huskHornhead.getHurtTimer() - delta));
        if (huskHornhead.getKnockbackTimer() > 0)
            huskHornhead.setKnockbackTimer(Math.max(0f, huskHornhead.getKnockbackTimer() - delta));
        huskHornhead.setStateTimer(huskHornhead.getStateTimer() + delta);
    }

    private void enterState(HuskHornhead huskHornhead, HuskHornheadState newState) {
        huskHornhead.setState(newState);
        huskHornhead.setStateTimer(0f);
    }

    private void flip(HuskHornhead huskHornhead) {
        huskHornhead.setFacingRight(!huskHornhead.isFacingRight());
    }

    private boolean isSolid(String name) {
        if (name == null)
            return false;
        return name.equals("platform") || name.equals("wall") || name.equals("ceiling");
    }
}
