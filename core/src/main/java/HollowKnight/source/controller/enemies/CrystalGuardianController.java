package HollowKnight.source.controller.enemies;

import HollowKnight.source.controller.PlayerController;
import HollowKnight.source.model.enemies.crystal_guardian.CrystalGuardian;
import HollowKnight.source.model.enemies.crystal_guardian.CrystalGuardianConstants;
import HollowKnight.source.model.enemies.crystal_guardian.CrystalGuardianState;
import HollowKnight.source.model.player.Player;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CrystalGuardianController {

    private final PlayerController playerController;
    private final List<CrystalGuardian> guardianList;
    private final Set<CrystalGuardian> hitGuardians = new HashSet<>();

    public CrystalGuardianController(List<CrystalGuardian> guardianList) {
        this.guardianList = guardianList;
        this.playerController = PlayerController.getInstance();
    }

    public void update(float delta, Player player, MapLayer logicLayer) {
        if (!player.isAttacking()) {
            hitGuardians.clear();
        }

        for (CrystalGuardian guardian : guardianList) {
            updateOne(delta, guardian, player, logicLayer);
        }
    }

    public void updateLasersOnly(float delta, Player player) {
        for (CrystalGuardian guardian : guardianList) {
            updateLaser(delta, guardian, player);
        }
    }

    private void updateOne(float delta, CrystalGuardian guardian, Player player, MapLayer logicLayer) {
        tickTimers(guardian, delta);

        if (guardian.getKnockbackTimer() <= 0) {
            switch (guardian.getState()) {
                case IDLE:
                    updateIdle(guardian, player);
                    break;
                case SHOOT:
                    updateShoot(guardian, player);
                    break;
                case ENRAGED:
                    updateEnraged(delta, guardian, player);
                    break;
                case EVADE:
                    updateEvade(guardian);
                    break;
                case RETURN:
                    updateReturn(guardian);
                    break;
                case DEAD:
                    updateDead(guardian);
                    break;
            }
        }

        updateLaser(delta, guardian, player);

        if (!guardian.isOnGround()) {
            guardian.getVelocity().y += CrystalGuardianConstants.GRAVITY * delta;
        }
        else if (guardian.getVelocity().y < 0f) {
            guardian.getVelocity().y = 0f;
        }

        guardian.setPosition(guardian.getPosition().x + guardian.getVelocity().x * delta, guardian.getPosition().y + guardian.getVelocity().y * delta);

        resolveCollisions(guardian, logicLayer);

        checkPlayerContact(guardian, player);
    }

    private void updateIdle(CrystalGuardian guardian, Player player) {
        guardian.getVelocity().x = 0f;

        if (playerInDetectionRect(guardian, player)) {
            float playerCX = player.getBounds().x + player.getBounds().width / 2f;
            float guardianCX = guardian.getBounds().x + guardian.getBounds().width / 2f;
            guardian.setFacingRight(playerCX > guardianCX);

            startShoot(guardian);
        }
    }

    private Rectangle buildDetectionRect(CrystalGuardian guardian) {
        Rectangle b = guardian.getBounds();
        float dw = CrystalGuardianConstants.DETECTION_W;
        float dh = CrystalGuardianConstants.DETECTION_H;

        float detX = guardian.isFacingRight() ? b.x + b.width : b.x - dw;
        float detY = b.y + b.height / 2f - dh / 2f;

        return new Rectangle(detX, detY, dw, dh);
    }

    private boolean playerInDetectionRect(CrystalGuardian guardian, Player player) {
        return Intersector.overlaps(buildDetectionRect(guardian), player.getBounds());
    }

    private void startShoot(CrystalGuardian guardian) {
        guardian.setLaserFiredThisCycle(false);
        enterState(guardian, CrystalGuardianState.SHOOT);
    }

    private void updateShoot(CrystalGuardian guardian, Player player) {
        guardian.getVelocity().x = 0f;

        if (!guardian.isLaserFiredThisCycle()
            && guardian.getStateTimer() >= CrystalGuardianConstants.LASER_FIRE_DELAY) {
            fireLaser(guardian);
        }

        if (guardian.getStateTimer() >= CrystalGuardianConstants.SHOOT_ANIM_DUR) {
            enterState(guardian, CrystalGuardianState.ENRAGED);
        }
    }

    private void fireLaser(CrystalGuardian guardian) {
        guardian.setLaserFiredThisCycle(true);
        guardian.setLaserActive(true);
        guardian.setLaserTimer(0f);
        guardian.setLaserHasHitPlayer(false);

        guardian.setLaserOriginX(guardian.getBounds().x);
        guardian.setLaserOriginY(guardian.getBounds().y);
        guardian.setLaserFacingRight(guardian.isFacingRight());
    }

    private void updateLaser(float delta, CrystalGuardian guardian, Player player) {
        if (!guardian.isLaserActive()) return;

        guardian.setLaserTimer(guardian.getLaserTimer() + delta);

        if (!guardian.isLaserHasHitPlayer()
            && guardian.getLaserTimer() <= CrystalGuardianConstants.LASER_ACTIVE_DUR
            && player.isAlive() && !player.isInvincible()) {

            if (Intersector.overlaps(buildLaserHitbox(guardian), player.getBounds())) {
                guardian.setLaserHasHitPlayer(true);

                float guardianCX = guardian.getBounds().x + guardian.getBounds().width / 2f;
                float playerCX = player.getBounds().x + player.getBounds().width / 2f;
                float knockBackDirection = playerCX < guardianCX ? -1f : 1f;

                playerController.takeDamage(1);
                playerController.applyKnockback(knockBackDirection);
            }
        }

        if (guardian.getLaserTimer() >= CrystalGuardianConstants.LASER_EFFECT_DUR) {
            guardian.setLaserActive(false);
        }
    }

    private Rectangle buildLaserHitbox(CrystalGuardian guardian) {
        float bx = guardian.getLaserOriginX();
        float by = guardian.getLaserOriginY();
        float bw = CrystalGuardianConstants.BOUNDS_W;
        float bh = CrystalGuardianConstants.BOUNDS_H;
        float range = CrystalGuardianConstants.LASER_RANGE;
        float height = CrystalGuardianConstants.LASER_HEIGHT - 20f;
        float y = by + bh / 2f - height / 2f;

        return guardian.isLaserFacingRight() ? new Rectangle(bx + bw, y, range, height) : new Rectangle(bx - range, y, range, height);
    }

    private void updateEnraged(float delta, CrystalGuardian guardian, Player player) {
        guardian.setEnrageTimer(guardian.getEnrageTimer() + delta);

        float playerCX = player.getBounds().x + player.getBounds().width / 2f;
        float guardianCX = guardian.getBounds().x + guardian.getBounds().width / 2f;
        float dx = playerCX - guardianCX;

        guardian.setFacingRight(dx > 0f);
        guardian.getVelocity().x = Math.signum(dx) * CrystalGuardianConstants.CHASE_SPEED;

        if (guardian.getEvadeCooldownTimer() <= 0f
            && Math.abs(dx) < CrystalGuardianConstants.EVADE_TRIGGER_DISTANCE) {
            startEvade(guardian, -Math.signum(dx));
        }

        if (guardian.getEnrageTimer() >= CrystalGuardianConstants.ENRAGE_DURATION) {
            guardian.setEnrageTimer(0f);
            enterState(guardian, CrystalGuardianState.RETURN);
        }
    }

    private void startEvade(CrystalGuardian guardian, float direction) {
        guardian.setEvadeDirection(direction == 0f ? 1f : direction);
        guardian.setEvadeCooldownTimer(CrystalGuardianConstants.EVADE_COOLDOWN);
        enterState(guardian, CrystalGuardianState.EVADE);
    }

    private void updateEvade(CrystalGuardian guardian) {
        guardian.getVelocity().x = guardian.getEvadeDirection() * CrystalGuardianConstants.EVADE_SPEED;

        if (guardian.getStateTimer() >= CrystalGuardianConstants.EVADE_ANIM_DUR) {
            enterState(guardian, CrystalGuardianState.ENRAGED);
        }
    }

    private void updateReturn(CrystalGuardian guardian) {
        float dx = guardian.getSpawnPosition().x - guardian.getPosition().x;

        if (Math.abs(dx) <= CrystalGuardianConstants.RETURN_ARRIVE_EPSILON) {
            guardian.setPosition(guardian.getSpawnPosition().x, guardian.getPosition().y);
            guardian.getVelocity().x = 0f;
            guardian.setFacingRight(guardian.isHomeFacingRight());
            enterState(guardian, CrystalGuardianState.IDLE);
            return;
        }

        guardian.setFacingRight(dx > 0f);
        guardian.getVelocity().x = Math.signum(dx) * CrystalGuardianConstants.RETURN_SPEED;
    }

    private void updateDead(CrystalGuardian guardian) {
        if (guardian.isOnGround()) {
            guardian.getVelocity().set(0f, 0f);
        }
    }

    public void checkSwordHits(Rectangle swordHitbox, Player player) {
        for (CrystalGuardian guardian : guardianList) {
            if (guardian.getState() == CrystalGuardianState.DEAD) continue;
            if (guardian.isInvincible()) continue;
            if (hitGuardians.contains(guardian)) continue;

            if (Intersector.overlaps(swordHitbox, guardian.getBounds())) {
                playerController.gainSoul();
                hitGuardians.add(guardian);
                applyHit(guardian, player);
            }
        }
    }

    private void applyHit(CrystalGuardian guardian, Player player) {
        guardian.setHealth(guardian.getHealth() - playerController.getNailDamage());
        guardian.setHurtTimer(CrystalGuardianConstants.HURT_COOLDOWN);
        guardian.setOnGround(false);

        float knockBackDirectionX = player.isFacingRight() ? 1f : -1f;

        if (guardian.getHealth() <= 0) {
            guardian.setLaserActive(false);
            enterState(guardian, CrystalGuardianState.DEAD);
        }
        else {
            guardian.setKnockbackTimer(CrystalGuardianConstants.KNOCKBACK_DURATION);
        }

        guardian.getVelocity().set(
            knockBackDirectionX * CrystalGuardianConstants.KNOCKBACK_SPEED_X * playerController.getKnockbackMultiplier(),
            CrystalGuardianConstants.KNOCKBACK_SPEED_Y * playerController.getKnockbackMultiplier()
        );
    }

    private void checkPlayerContact(CrystalGuardian guardian, Player player) {
        if (guardian.getState() == CrystalGuardianState.DEAD) return;
        if (!player.isAlive() || player.isInvincible()) return;

        if (Intersector.overlaps(guardian.getBounds(), player.getBounds())) {
            float guardianCX = guardian.getBounds().x + guardian.getBounds().width / 2f;
            float playerCX = player.getBounds().x + player.getBounds().width / 2f;
            float knockBackDirection = playerCX < guardianCX ? -1f : 1f;

            playerController.takeDamage(1);
            playerController.applyKnockback(knockBackDirection);
        }
    }

    private void resolveCollisions(CrystalGuardian guardian, MapLayer logicLayer) {
        if (logicLayer == null) return;

        boolean grounded = false;
        Rectangle bounds = new Rectangle(guardian.getBounds());

        for (MapObject obj : logicLayer.getObjects()) {
            if (!(obj instanceof RectangleMapObject)) continue;
            if (!isSolid(obj.getName())) continue;

            Rectangle tile = ((RectangleMapObject) obj).getRectangle();
            Rectangle overlap = new Rectangle();
            if (!Intersector.intersectRectangles(bounds, tile, overlap)) continue;

            if (overlap.width < overlap.height) {
                boolean hitRight = bounds.x < tile.x;
                float push = hitRight ? -overlap.width : overlap.width;
                guardian.setPosition(guardian.getPosition().x + push, guardian.getPosition().y);
                guardian.getVelocity().x = 0f;
            }
            else {
                float bodyMidY = bounds.y + bounds.height / 2f;
                float tileMidY = tile.y + tile.height / 2f;

                if (bodyMidY > tileMidY) {
                    if (guardian.getVelocity().y <= 0f) {
                        float landedY = tile.y + tile.height - CrystalGuardianConstants.BOUNDS_OFFSET_Y;
                        guardian.setPosition(guardian.getPosition().x, landedY);
                        guardian.getVelocity().y = 0f;
                        grounded = true;
                    }
                }
                else {
                    if (guardian.getVelocity().y > 0f) {
                        float ceilY = tile.y - CrystalGuardianConstants.BOUNDS_H - CrystalGuardianConstants.BOUNDS_OFFSET_Y;
                        guardian.setPosition(guardian.getPosition().x, ceilY);
                        guardian.getVelocity().y = 0f;
                    }
                }
            }

            bounds.set(guardian.getBounds());
        }

        guardian.setOnGround(grounded);
    }

    private boolean isSolid(String name) {
        if (name == null) return false;
        return name.equals("platform") || name.equals("wall") || name.equals("ceiling");
    }

    private void tickTimers(CrystalGuardian guardian, float delta) {
        if (guardian.getHurtTimer() > 0)
            guardian.setHurtTimer(Math.max(0f, guardian.getHurtTimer() - delta));
        if (guardian.getKnockbackTimer() > 0)
            guardian.setKnockbackTimer(Math.max(0f, guardian.getKnockbackTimer() - delta));
        if (guardian.getEvadeCooldownTimer() > 0)
            guardian.setEvadeCooldownTimer(Math.max(0f, guardian.getEvadeCooldownTimer() - delta));
        guardian.setStateTimer(guardian.getStateTimer() + delta);
    }

    private void enterState(CrystalGuardian guardian, CrystalGuardianState newState) {
        guardian.setState(newState);
        guardian.setStateTimer(0f);
    }

    public List<CrystalGuardian> getGuardianList() {
        return guardianList;
    }
}
