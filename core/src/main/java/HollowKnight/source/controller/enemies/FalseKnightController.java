package HollowKnight.source.controller.enemies;

import HollowKnight.source.controller.PlayerController;
import HollowKnight.source.game_utils.CameraShake;
import HollowKnight.source.model.achievement.AchievementManager;
import HollowKnight.source.model.achievement.AchievementType;
import HollowKnight.source.model.enemies.false_knight.BossProgressManager;
import HollowKnight.source.model.enemies.false_knight.FalseKnight;
import HollowKnight.source.model.enemies.false_knight.FalseKnightConstants;
import HollowKnight.source.model.enemies.false_knight.FalseKnightState;
import HollowKnight.source.model.player.Player;
import HollowKnight.source.model.spell.HowlingWraiths;
import HollowKnight.source.model.spell.VengefulSpirit;
import HollowKnight.source.model.data.GameStats;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FalseKnightController {
    private enum Choice {
        MACE_SLAM,
        CHARGE_RUN,
        OFFENSIVE_JUMP,
        DEFENSIVE_JUMP,
        SHOCKWAVE_SLAM
    }

    private final PlayerController playerController;
    private final List<FalseKnight> falseKnightList;
    private final Set<FalseKnight> hitBosses = new HashSet<>();

    public FalseKnightController(List<FalseKnight> falseKnightList) {
        this.falseKnightList = falseKnightList;
        this.playerController = PlayerController.getInstance();
    }

    public void update(float delta, Player player, MapLayer logicLayer) {
        if (!player.isAttacking()) {
            hitBosses.clear();
        }

        for (FalseKnight falseKnight : falseKnightList) {
            updateOne(delta, falseKnight, player, logicLayer);
        }
    }

    private void updateOne(float delta, FalseKnight falseKnight, Player player, MapLayer logicLayer) {
        tickTimers(falseKnight, delta);
        checkStunTrigger(falseKnight);

        boolean wasOnGround = falseKnight.isOnGround();

        if (falseKnight.getKnockbackTimer() <= 0) {
            switch (falseKnight.getState()) {
                case IDLE:
                    updateIdle(falseKnight, player);
                    break;
                case CHARGE_RUN:
                    updateChargeRun(falseKnight, player);
                    break;
                case LAND:
                    updateLand(falseKnight);
                    break;
                case MACE_SLAM:
                    updateMaceSlam(falseKnight, player);
                    break;
                case MACE_SLAM_RECOVER:
                    updateMaceSlamRecover(falseKnight);
                    break;
                case STUN_ENTER:
                    updateStunEnter(falseKnight);
                    break;
                case STUNNED:
                    updateStunned(falseKnight);
                    break;
                case STUN_RECOVER:
                    updateStunRecover(falseKnight);
                    break;
                case DEAD:
                    updateDead(falseKnight);
                    break;
            }
        }

        updateShockwave(delta, falseKnight, player);

        if (!falseKnight.isOnGround()) {
            falseKnight.getVelocity().y += FalseKnightConstants.GRAVITY * delta;
        }

        falseKnight.setPosition(falseKnight.getPosition().x + falseKnight.getVelocity().x * delta, falseKnight.getPosition().y + falseKnight.getVelocity().y * delta);

        resolveCollisions(falseKnight, logicLayer);

        if (!wasOnGround && falseKnight.isOnGround() && isInAirState(falseKnight.getState())) {
            onLanding(falseKnight, player);
        }

        checkPlayerContact(falseKnight, player);
    }

    private boolean isInAirState(FalseKnightState state) {
        return state == FalseKnightState.JUMP || state == FalseKnightState.SHOCKWAVE_SLAM;
    }

    private void checkStunTrigger(FalseKnight falseKnight) {
        if (falseKnight.isStunTriggered()) return;
        if (falseKnight.getPhase() != 1) return;
        if (falseKnight.getHealth() > FalseKnightConstants.STUN_HP_THRESHOLD) return;
        if (falseKnight.getState() == FalseKnightState.DEAD) return;

        falseKnight.setStunTriggered(true);
        falseKnight.getVelocity().x = 0f;
        falseKnight.setHurtTimer(0f);
        enterState(falseKnight, FalseKnightState.STUN_ENTER);
    }

    private void updateStunEnter(FalseKnight falseKnight) {
        falseKnight.getVelocity().x = 0f;
        if (falseKnight.getStateTimer() >= FalseKnightConstants.DEATH_ANIM_DUR && falseKnight.isOnGround()) {
            enterState(falseKnight, FalseKnightState.STUNNED);
        }
    }

    private void updateStunned(FalseKnight falseKnight) {
        falseKnight.getVelocity().x = 0f;
        if (falseKnight.getStateTimer() >= FalseKnightConstants.STUN_DURATION) {
            enterState(falseKnight, FalseKnightState.STUN_RECOVER);
        }
    }

    private void updateStunRecover(FalseKnight falseKnight) {
        falseKnight.getVelocity().x = 0f;
        if (falseKnight.getStateTimer() >= FalseKnightConstants.STUN_RECOVER_ANIM_DUR) {
            falseKnight.setPhase(2);
            falseKnight.setLastState(null);
            enterState(falseKnight, FalseKnightState.IDLE);
        }
    }

    private void updateDead(FalseKnight falseKnight) {
        if (falseKnight.isOnGround()) {
            falseKnight.getVelocity().set(0f, 0f);
        }
    }

    private void updateIdle(FalseKnight falseKnight, Player player) {
        falseKnight.getVelocity().x = 0f;
        faceTowardPlayer(falseKnight, player);

        float decisionDelay = falseKnight.getPhase() == 2 ? FalseKnightConstants.DECISION_DELAY_PHASE2 : FalseKnightConstants.DECISION_DELAY_PHASE1;

        if (falseKnight.getStateTimer() >= decisionDelay) {
            startMove(falseKnight, chooseNextMove(falseKnight, player), player);
        }
    }

    private Choice chooseNextMove(FalseKnight falseKnight, Player player) {
        float distance = horizontalDistance(falseKnight, player);

        Map<Choice, Float> weights = new EnumMap<>(Choice.class);

        weights.put(
            Choice.MACE_SLAM,
            distance <= FalseKnightConstants.CLOSE_RANGE ? FalseKnightConstants.MACE_SLAM_CLOSE_WEIGHT : FalseKnightConstants.MACE_SLAM_FAR_WEIGHT
        );

        weights.put(
            Choice.CHARGE_RUN,
            distance >= FalseKnightConstants.FAR_RANGE ? FalseKnightConstants.CHARGE_RUN_FAR_WEIGHT : FalseKnightConstants.CHARGE_RUN_CLOSE_WEIGHT
        );

        weights.put(
            Choice.OFFENSIVE_JUMP,
            distance >= FalseKnightConstants.CLOSE_RANGE ? FalseKnightConstants.OFFENSIVE_JUMP_RANGE_WEIGHT : FalseKnightConstants.OFFENSIVE_JUMP_BASE_WEIGHT
        );

        weights.put(
            Choice.DEFENSIVE_JUMP,
            distance < FalseKnightConstants.EVADE_TRIGGER_DISTANCE ? FalseKnightConstants.DEFENSIVE_JUMP_CLOSE_WEIGHT : FalseKnightConstants.DEFENSIVE_JUMP_BASE_WEIGHT
        );

        if (falseKnight.getPhase() == 2) {
            weights.put(Choice.SHOCKWAVE_SLAM, FalseKnightConstants.SHOCKWAVE_SLAM_WEIGHT);
        }

        FalseKnightState last = falseKnight.getLastState();
        if (last == FalseKnightState.MACE_SLAM) {
            weights.remove(Choice.MACE_SLAM);
        }
        else if(last == FalseKnightState.CHARGE_RUN) {
            weights.remove(Choice.CHARGE_RUN);
        }
        else if (last == FalseKnightState.JUMP) {
            weights.remove(Choice.OFFENSIVE_JUMP);
            weights.remove(Choice.DEFENSIVE_JUMP);
        }
        else if (last == FalseKnightState.SHOCKWAVE_SLAM) {
            weights.remove(Choice.SHOCKWAVE_SLAM);
        }

        if (weights.isEmpty()) {
            return Choice.MACE_SLAM;
        }

        return weightedRandomPick(weights);
    }

    private Choice weightedRandomPick(Map<Choice, Float> weights) {
        float total = 0f;
        for (float w : weights.values())
            total += w;

        float roll = MathUtils.random(0f, total);
        float stored = 0f;
        for (Map.Entry<Choice, Float> entry : weights.entrySet()) {
            stored += entry.getValue();
            if (roll <= stored)
                return entry.getKey();
        }
        return weights.keySet().iterator().next();
    }

    private void startMove(FalseKnight falseKnight, Choice choice, Player player) {
        switch (choice) {
            case MACE_SLAM:
                falseKnight.setLastState(FalseKnightState.MACE_SLAM);
                startMaceSlam(falseKnight, player);
                break;

            case CHARGE_RUN:
                falseKnight.setLastState(FalseKnightState.CHARGE_RUN);
                startCharge(falseKnight, player);
                break;

            case OFFENSIVE_JUMP:
                falseKnight.setLastState(FalseKnightState.JUMP);
                startJump(falseKnight, player, true);
                break;

            case DEFENSIVE_JUMP:
                falseKnight.setLastState(FalseKnightState.JUMP);
                startJump(falseKnight, player, false);
                break;

            case SHOCKWAVE_SLAM:
                falseKnight.setLastState(FalseKnightState.SHOCKWAVE_SLAM);
                startShockwaveSlam(falseKnight, player);
                break;
        }
    }

    private void startMaceSlam(FalseKnight falseKnight, Player player) {
        faceTowardPlayer(falseKnight, player);
        falseKnight.getVelocity().x = 0f;
        falseKnight.setAttackHasHitPlayer(false);
        enterState(falseKnight, FalseKnightState.MACE_SLAM);
    }

    private void updateMaceSlam(FalseKnight falseKnight, Player player) {
        falseKnight.getVelocity().x = 0f;
        if (!falseKnight.isAttackHasHitPlayer() && falseKnight.getStateTimer() >= FalseKnightConstants.MACE_SLAM_TRIGGER_TIME) {
            falseKnight.setAttackHasHitPlayer(true);
            CameraShake.trigger(FalseKnightConstants.SHAKE_DURATION_HEAVY, FalseKnightConstants.SHAKE_MAGNITUDE_HEAVY);

            if (player.isAlive() && !player.isInvincible()) {
                Rectangle slamBox = buildMaceSlamHitbox(falseKnight, FalseKnightConstants.MACE_SLAM_HITBOX_W, FalseKnightConstants.MACE_SLAM_HITBOX_H);
                if (Intersector.overlaps(slamBox, player.getBounds())) {
                    if (playerController.isGodMode()) return;
                    float knockDir = falseKnight.isFacingRight() ? 1f : -1f;
                    playerController.takeDamage(FalseKnightConstants.MACE_SLAM_DAMAGE);
                    playerController.addDamageTakenFromBoss(FalseKnightConstants.MACE_SLAM_DAMAGE);
                    playerController.applyKnockback(knockDir);
                }
            }
        }

        if (falseKnight.getStateTimer() >= FalseKnightConstants.ATTACK_ANIM_DUR) {
            enterState(falseKnight, FalseKnightState.MACE_SLAM_RECOVER);
        }
    }

    private void updateMaceSlamRecover(FalseKnight falseKnight) {
        falseKnight.getVelocity().x = 0f;
        if (falseKnight.getStateTimer() >= FalseKnightConstants.ATTACK_RECOVER_ANIM_DUR) {
            enterState(falseKnight, FalseKnightState.IDLE);
        }
    }

    private void startCharge(FalseKnight falseKnight, Player player) {
        faceTowardPlayer(falseKnight, player);
        falseKnight.setChargeDirectionX(falseKnight.isFacingRight() ? 1f : -1f);
        enterState(falseKnight, FalseKnightState.CHARGE_RUN);
    }

    private void updateChargeRun(FalseKnight falseKnight, Player player) {
        falseKnight.getVelocity().x = falseKnight.getChargeDirectionX() * FalseKnightConstants.RUN_SPEED * speedMultiplier(falseKnight);

        boolean closedTheGap = horizontalDistance(falseKnight, player) <= FalseKnightConstants.CLOSE_RANGE;
        boolean timedOut = falseKnight.getStateTimer() >= FalseKnightConstants.RUN_MAX_DURATION;
        if (closedTheGap || timedOut) {
            endCharge(falseKnight);
        }
    }

    private void endCharge(FalseKnight falseKnight) {
        falseKnight.getVelocity().x = 0f;
        enterState(falseKnight, FalseKnightState.IDLE);
    }

    private void startJump(FalseKnight falseKnight, Player player, boolean offensive) {
        boolean playerIsRight = isPlayerRightOf(falseKnight, player);
        boolean travelRight = offensive == playerIsRight;

        falseKnight.setFacingRight(playerIsRight);
        falseKnight.setJumpDirectionX(travelRight ? 1f : -1f);

        float vx = offensive ? FalseKnightConstants.OFFENSIVE_JUMP_VX : FalseKnightConstants.DEFENSIVE_JUMP_VX;
        float vy = offensive ? FalseKnightConstants.OFFENSIVE_JUMP_VY : FalseKnightConstants.DEFENSIVE_JUMP_VY;

        falseKnight.getVelocity().set(falseKnight.getJumpDirectionX() * vx * speedMultiplier(falseKnight), vy);
        falseKnight.setOnGround(false);

        enterState(falseKnight, FalseKnightState.JUMP);
    }

    private void startShockwaveSlam(FalseKnight falseKnight, Player player) {
        boolean playerIsRight = isPlayerRightOf(falseKnight, player);

        falseKnight.setFacingRight(playerIsRight);
        falseKnight.setJumpDirectionX(playerIsRight ? 1f : -1f);

        falseKnight.getVelocity().set(
            falseKnight.getJumpDirectionX() * FalseKnightConstants.SHOCKWAVE_JUMP_VX * speedMultiplier(falseKnight),
            FalseKnightConstants.SHOCKWAVE_JUMP_VY
        );
        falseKnight.setOnGround(false);
        enterState(falseKnight, FalseKnightState.SHOCKWAVE_SLAM);
    }

    private void onLanding(FalseKnight falseKnight, Player player) {
        boolean isShockwaveSlam = falseKnight.getState() == FalseKnightState.SHOCKWAVE_SLAM;

        CameraShake.trigger(
            isShockwaveSlam ? FalseKnightConstants.SHAKE_DURATION_HEAVY : FalseKnightConstants.SHAKE_DURATION_LAND,
            isShockwaveSlam ? FalseKnightConstants.SHAKE_MAGNITUDE_HEAVY : FalseKnightConstants.SHAKE_MAGNITUDE_LAND
        );

        if (isShockwaveSlam) {
            spawnShockwave(falseKnight);
        }

        falseKnight.getVelocity().x = 0f;
        enterState(falseKnight, FalseKnightState.LAND);
    }

    private void updateLand(FalseKnight falseKnight) {
        if (falseKnight.getStateTimer() >= FalseKnightConstants.LAND_ANIM_DUR) {
            enterState(falseKnight, FalseKnightState.IDLE);
        }
    }

    private void spawnShockwave(FalseKnight falseKnight) {
        falseKnight.setShockwaveActive(true);
        falseKnight.setShockwaveTimer(0f);
        falseKnight.setShockwaveDirection(falseKnight.isFacingRight() ? 1f : -1f);

        float startX = falseKnight.isFacingRight() ? falseKnight.getBounds().x + falseKnight.getBounds().width : falseKnight.getBounds().x;
        falseKnight.setShockwaveOriginX(startX);
        falseKnight.setShockwaveOriginY(falseKnight.getBounds().y);
        falseKnight.setShockwaveX(startX);
    }

    private void updateShockwave(float delta, FalseKnight falseKnight, Player player) {
        if (!falseKnight.isShockwaveActive()) return;

        falseKnight.setShockwaveTimer(falseKnight.getShockwaveTimer() + delta);

        float speed = FalseKnightConstants.SHOCKWAVE_BASE_SPEED + FalseKnightConstants.SHOCKWAVE_ACCEL * falseKnight.getShockwaveTimer();
        falseKnight.setShockwaveX(falseKnight.getShockwaveX() + falseKnight.getShockwaveDirection() * speed * delta);

        float traveled = Math.abs(falseKnight.getShockwaveX() - falseKnight.getShockwaveOriginX());
        if (traveled >= FalseKnightConstants.SHOCKWAVE_MAX_TRAVEL) {
            falseKnight.setShockwaveActive(false);
            return;
        }

        if (player.isAlive() && !player.isInvincible()) {
            if (Intersector.overlaps(buildShockwaveHitbox(falseKnight), player.getBounds())) {
                if (playerController.isGodMode()) return;
                playerController.takeDamage(FalseKnightConstants.SHOCKWAVE_SLAM_DAMAGE);
                playerController.addDamageTakenFromBoss(FalseKnightConstants.SHOCKWAVE_SLAM_DAMAGE);
                playerController.applyKnockback(falseKnight.getShockwaveDirection());
            }
        }
    }

    private Rectangle buildShockwaveHitbox(FalseKnight falseKnight) {
        float w = FalseKnightConstants.SHOCKWAVE_WIDTH;
        float h = FalseKnightConstants.SHOCKWAVE_HEIGHT;
        float x = falseKnight.getShockwaveDirection() > 0 ? falseKnight.getShockwaveX() : falseKnight.getShockwaveX() - w;
        float y = falseKnight.getShockwaveOriginY();
        return new Rectangle(x, y + 4f, w - 15f, h - 35f);
    }

    public void updateShockwaveOnly(float delta, Player player) {
        for (FalseKnight falseKnight : falseKnightList) {
            updateShockwave(delta, falseKnight, player);
        }
    }

    public void checkSwordHits(Rectangle swordHitbox) {
        for (FalseKnight falseKnight : falseKnightList) {
            if (!isHittable(falseKnight.getState())) continue;
            if (falseKnight.isInvincible()) continue;
            if (hitBosses.contains(falseKnight)) continue;

            Rectangle hurtbox = falseKnight.getState() == FalseKnightState.STUNNED ? buildInnerHitbox(falseKnight) : falseKnight.getBounds();

            if (Intersector.overlaps(swordHitbox, hurtbox)) {
                playerController.gainSoul();
                hitBosses.add(falseKnight);
                applyHit(falseKnight, playerController.getNailDamage());
            }
        }
    }

    public void checkVengefulSpiritHit(VengefulSpirit spirit) {
        for (FalseKnight falseKnight : falseKnightList) {
            if (!isHittable(falseKnight.getState())) continue;
            if (falseKnight.isInvincible()) continue;
            if (spirit.hasHit(falseKnight)) continue;

            Rectangle hurtbox = falseKnight.getState() == FalseKnightState.STUNNED ? buildInnerHitbox(falseKnight) : falseKnight.getBounds();

            if (Intersector.overlaps(spirit.getBounds(), hurtbox)) {
                spirit.markHit(falseKnight);
                applyHit(falseKnight, playerController.getSpellDamage());
            }
        }
    }

    public void checkHowlingWraithsHit(HowlingWraiths effect) {
        for (FalseKnight falseKnight : falseKnightList) {
            if (!isHittable(falseKnight.getState())) continue;

            Rectangle hurtbox = falseKnight.getState() == FalseKnightState.STUNNED ? buildInnerHitbox(falseKnight) : falseKnight.getBounds();

            if (Intersector.overlaps(effect.getBounds(), hurtbox)) {
                applyHit(falseKnight, playerController.getSpellDamage());
            }
        }
    }

    private boolean isHittable(FalseKnightState state) {
        return state != FalseKnightState.STUN_ENTER && state != FalseKnightState.STUN_RECOVER && state != FalseKnightState.DEAD;
    }

    private void applyHit(FalseKnight falseKnight, int damage) {
        falseKnight.setHealth(falseKnight.getHealth() - damage);
        falseKnight.setHurtTimer(FalseKnightConstants.HURT_COOLDOWN);

        if (falseKnight.getHealth() <= 0) {
            falseKnight.setShockwaveActive(false);
            enterState(falseKnight, FalseKnightState.DEAD);
            GameStats.increaseDefeatedEnemies();

            BossProgressManager.markDefeated(BossProgressManager.FALSE_KNIGHT);
            AchievementManager.unlock(AchievementType.DEFEAT_FALSE_KNIGHT);
            AchievementManager.unlock(AchievementType.COMPLETION);
            if (playerController.getDamageTakenFromBoss() == 0) {
                AchievementManager.unlock(AchievementType.NO_DAMAGE_FALSE_KNIGHT);
            }
            if (GameStats.getTotalPlayTimeSeconds() < 300f) {
                AchievementManager.unlock(AchievementType.SPEEDRUN);
            }
        }
    }

    private void checkPlayerContact(FalseKnight falseKnight, Player player) {
        if (!canDealContactDamage(falseKnight.getState())) return;
        if (!player.isAlive() || player.isInvincible()) return;

        if (Intersector.overlaps(falseKnight.getBounds(), player.getBounds())) {
            if (playerController.isGodMode()) return;
            float bossCX = falseKnight.getBounds().x + falseKnight.getBounds().width / 2f;
            float playerCX = player.getBounds().x + player.getBounds().width / 2f;
            float knockDir = playerCX < bossCX ? -1f : 1f;
            playerController.takeDamage(1);
            playerController.addDamageTakenFromBoss(1);
            playerController.applyKnockback(knockDir);
        }
    }

    private boolean canDealContactDamage(FalseKnightState state) {
        return state != FalseKnightState.STUN_ENTER && state != FalseKnightState.STUNNED && state != FalseKnightState.STUN_RECOVER && state != FalseKnightState.DEAD;
    }

    private Rectangle buildMaceSlamHitbox(FalseKnight falseKnight, float width, float height) {
        Rectangle b = falseKnight.getBounds();
        float x = falseKnight.isFacingRight() ? b.x + b.width : b.x - width;
        return new Rectangle(x, b.y, width, height);
    }

    private Rectangle buildInnerHitbox(FalseKnight falseKnight) {
        Rectangle b = falseKnight.getBounds();
        float w = FalseKnightConstants.INNER_HITBOX_W;
        float h = FalseKnightConstants.INNER_HITBOX_H;
        float x = falseKnight.isFacingRight() ? b.x + 100f : b.x - 25f;
        float y = b.y + 10;
        return new Rectangle(x, y, w, h);
    }

    private void resolveCollisions(FalseKnight falseKnight, MapLayer logicLayer) {
        if (logicLayer == null) return;

        boolean grounded = false;
        Rectangle bounds = new Rectangle(falseKnight.getBounds());

        for (MapObject obj : logicLayer.getObjects()) {
            if (!(obj instanceof RectangleMapObject)) continue;
            if (!isSolid(obj.getName())) continue;

            Rectangle tile = ((RectangleMapObject) obj).getRectangle();
            Rectangle overlap = new Rectangle();
            if (!Intersector.intersectRectangles(bounds, tile, overlap)) continue;

            if (overlap.width < overlap.height) {
                boolean hitRight = bounds.x < tile.x;
                float push = hitRight ? -overlap.width : overlap.width;
                falseKnight.setPosition(falseKnight.getPosition().x + push, falseKnight.getPosition().y);
                onWallHit(falseKnight);
            }
            else {
                float bodyMidY = bounds.y + bounds.height / 2f;
                float tileMidY = tile.y + tile.height / 2f;

                if (bodyMidY > tileMidY) {
                    if (falseKnight.getVelocity().y <= 0f) {
                        float landedY = tile.y + tile.height - FalseKnightConstants.BOUNDS_OFFSET_Y;
                        falseKnight.setPosition(falseKnight.getPosition().x, landedY);
                        falseKnight.getVelocity().y = 0f;
                        grounded = true;
                    }
                }
                else {
                    if (falseKnight.getVelocity().y > 0f) {
                        float ceilY = tile.y - FalseKnightConstants.BOUNDS_H - FalseKnightConstants.BOUNDS_OFFSET_Y;
                        falseKnight.setPosition(falseKnight.getPosition().x, ceilY);
                        falseKnight.getVelocity().y = 0f;
                    }
                }
            }

            bounds.set(falseKnight.getBounds());
        }

        falseKnight.setOnGround(grounded);
    }

    private void onWallHit(FalseKnight falseKnight) {
        falseKnight.getVelocity().x = 0f;
        if (falseKnight.getState() == FalseKnightState.CHARGE_RUN) {
            endCharge(falseKnight);
        }
    }

    private boolean isSolid(String name) {
        if (name == null) return false;
        return name.equals("platform") || name.equals("wall") || name.equals("ceiling");
    }

    private float horizontalDistance(FalseKnight falseKnight, Player player) {
        float bossCX = falseKnight.getBounds().x + falseKnight.getBounds().width / 2f;
        float playerCX = player.getBounds().x + player.getBounds().width / 2f;
        return Math.abs(playerCX - bossCX);
    }

    private boolean isPlayerRightOf(FalseKnight falseKnight, Player player) {
        float bossCX = falseKnight.getBounds().x + falseKnight.getBounds().width / 2f;
        float playerCX = player.getBounds().x + player.getBounds().width / 2f;
        return playerCX > bossCX;
    }

    private void faceTowardPlayer(FalseKnight falseKnight, Player player) {
        float bossCX = falseKnight.getBounds().x + falseKnight.getBounds().width / 2f;
        float playerCX = player.getBounds().x + player.getBounds().width / 2f;
        float diff = playerCX - bossCX;

        if (Math.abs(diff) < FalseKnightConstants.FACING_DEADZONE) return;

        falseKnight.setFacingRight(diff > 0f);
    }

    private float speedMultiplier(FalseKnight falseKnight) {
        return falseKnight.getPhase() == 2 ? FalseKnightConstants.SPEED_MULTIPLIER_PHASE2 : 1f;
    }

    private float animSpeedMultiplier(FalseKnight falseKnight) {
        return falseKnight.getPhase() == 2 ? FalseKnightConstants.ANIM_SPEED_MULTIPLIER_PHASE2 : 1f;
    }

    private void tickTimers(FalseKnight falseKnight, float delta) {
        if (falseKnight.getHurtTimer() > 0)
            falseKnight.setHurtTimer(Math.max(0f, falseKnight.getHurtTimer() - delta));
        if (falseKnight.getKnockbackTimer() > 0)
            falseKnight.setKnockbackTimer(Math.max(0f, falseKnight.getKnockbackTimer() - delta));

        falseKnight.setStateTimer(falseKnight.getStateTimer() + delta * animSpeedMultiplier(falseKnight));
    }

    private void enterState(FalseKnight falseKnight, FalseKnightState newState) {
        falseKnight.setState(newState);
        falseKnight.setStateTimer(0f);
    }

    public List<FalseKnight> getFalseKnightList() {
        return falseKnightList;
    }
}
