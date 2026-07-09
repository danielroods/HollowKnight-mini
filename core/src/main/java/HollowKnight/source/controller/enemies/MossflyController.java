package HollowKnight.source.controller.enemies;

import HollowKnight.source.controller.PlayerController;
import HollowKnight.source.model.achievement.AchievementManager;
import HollowKnight.source.model.achievement.AchievementType;
import HollowKnight.source.model.enemies.mossfly.MossflyState;
import HollowKnight.source.model.enemies.mossfly.Mossfly;
import HollowKnight.source.model.enemies.mossfly.MossflyConstants;
import HollowKnight.source.model.player.Player;
import HollowKnight.source.model.spell.VengefulSpirit;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MossflyController {
    PlayerController playerController;

    private final List<Mossfly> mossflyList;
    private final Set<Mossfly> hitMossflies = new HashSet<>();

    public MossflyController(List<Mossfly> mossflyList) {
        this.mossflyList = mossflyList;
        playerController = PlayerController.getInstance();
    }

    public void update(float delta, Player player, MapLayer logicLayer) {
        if (!player.isAttacking()) {
            hitMossflies.clear();
        }

        for (Mossfly mossfly : mossflyList) {
            updateOneMossfly(delta, mossfly, player, logicLayer);
        }
    }

    private void updateOneMossfly(float delta, Mossfly mossfly, Player player, MapLayer logicLayer) {
        if (mossfly.getHurtTimer() > 0)
            mossfly.setHurtTimer(Math.max(0f, mossfly.getHurtTimer() - delta));
        if (mossfly.getKnockbackTimer() > 0)
            mossfly.setKnockbackTimer(Math.max(0f, mossfly.getKnockbackTimer() - delta));

        mossfly.setStateTimer(mossfly.getStateTimer() + delta);

        switch (mossfly.getState()) {
            case HIDDEN:
                updateHidden(mossfly, player);
                break;
            case APPEAR:
                updateAppear(mossfly);
                break;
            case FLY:
                updateFly(delta, mossfly, player);
                break;
            case DEAD:
                updateDead(delta, mossfly, logicLayer);
                break;
        }
    }

    private void updateHidden(Mossfly mossfly, Player player) {
        float playerX = player.getBounds().x + player.getBounds().width / 2f;
        float playerY = player.getBounds().y + player.getBounds().height / 2f;
        float enemyX = mossfly.getBounds().x + mossfly.getBounds().width / 2f;
        float enemyY = mossfly.getBounds().y + mossfly.getBounds().height / 2f;

        if (Vector2.dst(playerX, playerY, enemyX, enemyY) <= MossflyConstants.DETECTION_RADIUS) {
            enterState(mossfly, MossflyState.APPEAR);
        }
    }

    private void updateAppear(Mossfly mossfly) {
        if (mossfly.getStateTimer() >= MossflyConstants.APPEAR_ANIM_DUR) {
            enterState(mossfly, MossflyState.FLY);
        }
    }

    private void updateFly(float delta, Mossfly mossfly, Player player) {
        checkPlayerContact(mossfly, player);

        if (mossfly.getKnockbackTimer() <= 0f) {
            float playerX = player.getBounds().x + player.getBounds().width / 2f;
            float playerY = player.getBounds().y + player.getBounds().height / 2f;
            float enemyX = mossfly.getPosition().x + MossflyConstants.WIDTH / 2f;
            float enemyY = mossfly.getPosition().y + MossflyConstants.HEIGHT / 2f;

            float dx = playerX - enemyX;
            float dy = playerY - enemyY;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);

            if (dist > 1f) {
                float desiredVX = (dx / dist) * MossflyConstants.FLY_SPEED;
                float desiredVY = (dy / dist) * MossflyConstants.FLY_SPEED + 30f; // I use 30 here for more realistic flying

                float alpha = Math.min(1f, 3.5f * delta);
                mossfly.getVelocity().x = lerp(mossfly.getVelocity().x, desiredVX, alpha);
                mossfly.getVelocity().y = lerp(mossfly.getVelocity().y, desiredVY, alpha);

                mossfly.setFacingRight(dx > 0f);
            }
        }

        applyVelocity(delta, mossfly);
    }
    private void checkPlayerContact(Mossfly mossfly, Player player) {
        if (!player.isAlive() || player.isInvincible()) return;

        if (Intersector.overlaps(mossfly.getBounds(), player.getBounds())) {
            if (playerController.isGodMode()) return;
            float enemyXCenter = mossfly.getBounds().x + mossfly.getBounds().width / 2f;
            float playerXCenter = player.getBounds().x + player.getBounds().width / 2f;
            float knockBackDirection = playerXCenter < enemyXCenter ? -1f : 1f;
            playerController.takeDamage(1);
            playerController.applyKnockback(knockBackDirection);
        }
    }

    private void updateDead(float delta, Mossfly mossfly, MapLayer logicLayer) {
        if (mossfly.isOnGround()) {
            mossfly.getVelocity().set(0f, 0f);
            return;
        }

        mossfly.getVelocity().y += MossflyConstants.GRAVITY * delta;
        applyVelocity(delta, mossfly);

        checkDeadLanding(mossfly, logicLayer);
    }

    private void checkDeadLanding(Mossfly mossfly, MapLayer logicLayer) {
        Rectangle bounds = mossfly.getBounds();

        for (MapObject obj : logicLayer.getObjects()) {
            if (!(obj instanceof RectangleMapObject)) continue;

            String name = obj.getName();
            if (name == null) continue;
            if (!name.equals("platform")) continue;

            Rectangle tile = ((RectangleMapObject) obj).getRectangle();
            Rectangle overlap = new Rectangle();
            if (!Intersector.intersectRectangles(bounds, tile, overlap)) continue;

            float bodyMidY = bounds.y + bounds.height / 2f;
            float tileMidY = tile.y + tile.height / 2f;
            if (bodyMidY <= tileMidY) continue;

            float landedY = tile.y + tile.height - MossflyConstants.BOUNDS_OFFSET_Y;
            mossfly.setPosition(mossfly.getPosition().x, landedY);
            mossfly.getVelocity().set(0f, 0f);
            mossfly.setOnGround(true);
            return;
        }
    }

    public void checkSwordHits(Rectangle swordHitbox, Player player) {
        for (Mossfly mossfly : mossflyList) {
            if (mossfly.getState() == MossflyState.HIDDEN) continue;
            if (mossfly.getState() == MossflyState.DEAD) continue;
            if (mossfly.isInvincible()) continue;
            if (hitMossflies.contains(mossfly)) continue;

            if (Intersector.overlaps(swordHitbox, mossfly.getBounds())) {
                playerController.gainSoul();
                hitMossflies.add(mossfly);
                applyHit(mossfly, player, playerController.getNailDamage());
            }
        }
    }

    public void checkVengefulSpiritHit(VengefulSpirit spirit, Player player) {
        for (Mossfly mossfly : mossflyList) {
            if (mossfly.getState() == MossflyState.HIDDEN) continue;
            if (mossfly.getState() == MossflyState.DEAD) continue;
            if (mossfly.isInvincible()) continue;
            if (spirit.hasHit(mossfly)) continue;

            if (Intersector.overlaps(spirit.getBounds(), mossfly.getBounds())) {
                spirit.markHit(mossfly);
                applyHit(mossfly, player, playerController.getSpellDamage());
            }
        }
    }

    private void applyHit(Mossfly mossfly, Player player, int damage) {
        mossfly.setHealth(mossfly.getHealth() - damage);
        mossfly.setHurtTimer(MossflyConstants.HURT_COOLDOWN);

        float playerY = player.getBounds().y + player.getBounds().height / 2f;
        float enemyY = mossfly.getBounds().y + mossfly.getBounds().height / 2f;

        float knockBackDirectionX = mossfly.isFacingRight() ? -1f : 1f;
        float knockBackDirectionY = (playerY > enemyY) ? -1f : 1f;

        if (mossfly.getHealth() <= 0) {
            player.setKilledMossfly(true);
            if (player.isKilledHuskHornhead() && player.isKilledCrystalCrawler() && player.isKilledCrystalGuardian())
            {
                AchievementManager.unlock(AchievementType.TRUE_HUNTER);
            }
            enterState(mossfly, MossflyState.DEAD);
            mossfly.setOnGround(false);
        }

        mossfly.setKnockbackTimer(MossflyConstants.KNOCKBACK_DURATION);
        mossfly.getVelocity().set(knockBackDirectionX * MossflyConstants.KNOCKBACK_SPEED_X * playerController.getKnockbackMultiplier(),
            knockBackDirectionY * MossflyConstants.KNOCKBACK_SPEED_Y * playerController.getKnockbackMultiplier());
    }

    private void enterState(Mossfly mossfly, MossflyState newState) {
        mossfly.setState(newState);
        mossfly.setStateTimer(0f);
    }

    private void applyVelocity(float delta, Mossfly mossfly) {
        mossfly.setPosition(
            mossfly.getPosition().x + mossfly.getVelocity().x * delta,
            mossfly.getPosition().y + mossfly.getVelocity().y * delta
        );
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * Math.min(1f, Math.max(0f, t));
    }

    public List<Mossfly> getMossflyList() {
        return mossflyList;
    }
}
