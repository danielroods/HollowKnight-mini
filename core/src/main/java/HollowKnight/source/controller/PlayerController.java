package HollowKnight.source.controller;

import HollowKnight.source.controller.enemies.CrystalCrawlerController;
import HollowKnight.source.controller.enemies.CrystalGuardianController;
import HollowKnight.source.controller.enemies.FalseKnightController;
import HollowKnight.source.controller.enemies.HuskHornheadController;
import HollowKnight.source.controller.enemies.MossflyController;
import HollowKnight.source.controller.npc.ZoteController;
import HollowKnight.source.model.charm.CharmConstants;
import HollowKnight.source.model.charm.CharmManager;
import HollowKnight.source.model.charm.CharmType;
import HollowKnight.source.model.player.AttackDirection;
import HollowKnight.source.model.player.Player;
import HollowKnight.source.model.player.PlayerConstants;
import HollowKnight.source.model.player.PlayerState;
import HollowKnight.source.model.spell.VengefulSpiritConstants;
import com.badlogic.gdx.math.Rectangle;

public class PlayerController {
    private static PlayerController instance;
    private final Player player;

    private static boolean godMode = false;
    private static boolean emergencyHeal = false;

    private PlayerController() {
        player = Player.getInstance();
    }

    public static PlayerController getInstance() {
        if (instance == null)
            instance = new PlayerController();
        return instance;
    }

    public void update(float delta) {
        if (!player.isAlive()) {
            player.setVelocityX(0f);
            player.setVelocityY(0f);
            player.setState(PlayerState.DEAD);
            player.setDeathTimer(player.getDeathTimer() + delta);
            return;
        }

        if (player.getAttackTimer() > 0) {
            player.setAttackTimer(player.getAttackTimer() - delta);
            if (player.getAttackTimer() <= 0)
                player.setAttacking(false);
        }
        if (player.getHurtTimer() > 0)
            player.setHurtTimer(player.getHurtTimer() - delta);
        if (player.getHealAnimTimer() > 0)
            player.setHealAnimTimer(player.getHealAnimTimer() - delta);
        if (player.getKnockbackTimer() > 0)
            player.setKnockbackTimer(player.getKnockbackTimer() - delta);

        if (player.getDashTimer() > 0) {
            player.setDashTimer(player.getDashTimer() - delta);
            if (player.getDashTimer() <= 0)
                player.setDashing(false);
        }
        if (player.getDashCooldownTimer() > 0)
            player.setDashCooldownTimer(player.getDashCooldownTimer() - delta);

        if (player.getWallJumpLockTimer() > 0) {
            player.setWallJumpLockTimer(player.getWallJumpLockTimer() - delta);
        }
        if (player.getCastTimer() > 0) {
            player.setCastTimer(player.getCastTimer() - delta);
            if (player.getCastTimer() <= 0)
                player.setCasting(false);
        }
        if (player.isDashing()) {
            player.setVelocityY(0f);
        }
        else {
            player.setVelocityY(player.getVelocity().y + PlayerConstants.GRAVITY * delta);
            if (player.isWallSliding() && player.getVelocity().y < PlayerConstants.WALL_SLIDE_SPEED) {
                player.setVelocityY(PlayerConstants.WALL_SLIDE_SPEED);
            }
        }

        float currentX = player.getPosition().x;
        float currentY = player.getPosition().y;
        float deltaX = player.getVelocity().x * delta;
        float deltaY = player.getVelocity().y * delta;
        player.setPosition(currentX + deltaX, currentY + deltaY);

        player.getBounds().setPosition(player.getPosition().x + 85, player.getPosition().y);

        if (player.getKnockbackTimer() > 0) {
            player.setState(PlayerState.HURT);
        }
        else if (player.isDashing()) {
            player.setState(PlayerState.DASH);
        }
        else if (player.isAttacking()) {
            player.setState(PlayerState.ATTACK);
        }
        else if (player.isCasting()) {
            player.setState(PlayerState.CAST);
        }
        else if (player.isFocusing()) {
            player.setState(PlayerState.FOCUS);
        }
        else if (player.getHealAnimTimer() > 0) {
            player.setState(PlayerState.HEAL);
        }
        else if (!player.isOnGround()) {
            if (player.isWallSliding()) {
                player.setState(PlayerState.WALL_SLIDE);
            }
            else if (player.getVelocity().y > 0) {
                if (player.canDoubleJump()) {
                    player.setState(PlayerState.JUMP);
                }
                else {
                    player.setState(PlayerState.DOUBLE_JUMP);
                }
            }
            else {
                player.setState(PlayerState.FALL);
            }
        }
        else if (Math.abs(player.getVelocity().x) > 0f) {
            player.setState(PlayerState.RUN);
        }
        else {
            player.setState(PlayerState.IDLE);
        }
    }

    public void moveLeft() {
        if (player.isFocusing() || player.isKnockedBack() || player.isDashing() || player.isWallJumpLocked() || player.isCasting()) return;
        player.setVelocityX(-PlayerConstants.SPEED);
        player.setFacingRight(false);
    }

    public void moveRight() {
        if (player.isFocusing() || player.isKnockedBack() || player.isDashing() || player.isWallJumpLocked() || player.isCasting()) return;
        player.setVelocityX(PlayerConstants.SPEED);
        player.setFacingRight(true);
    }

    public void stopHorizontal() {
        if (player.isKnockedBack() || player.isDashing() || player.isWallJumpLocked()) return;
        player.setVelocityX(0f);
    }

    public void jump() {
        if (player.isFocusing() || player.isKnockedBack() || player.isDashing() || player.isCasting()) return;

        if (player.isWallSliding()) {
            wallJump();
            return;
        }

        if (player.isOnGround()) {
            player.setVelocityY(PlayerConstants.JUMP_FORCE);
            player.setOnGround(false);
        }
        else if (player.canDoubleJump()) {
            player.setVelocityY(PlayerConstants.JUMP_FORCE * 0.92f);
            player.consumeDoubleJump();
        }
    }

    private void wallJump() {
        float awayDir = player.isFacingRight() ? -1f : 1f;

        player.setVelocityX(awayDir * PlayerConstants.WALL_JUMP_SPEED_X);
        player.setVelocityY(PlayerConstants.JUMP_FORCE);
        player.setFacingRight(awayDir > 0f);

        player.setWallSliding(false);
        player.setOnGround(false);
        player.setCanDoubleJump(true);
        player.setWallJumpLockTimer(PlayerConstants.WALL_JUMP_LOCK_DURATION);
    }

    public void dash() {
        if (!player.isAlive()) return;
        if (player.isFocusing() || player.isKnockedBack() || player.isDashing() || player.isCasting()) return;
        if (player.getDashCountInAir() >= PlayerConstants.MAX_DASH_IN_AIR) return;
        if (player.getDashCooldownTimer() > 0) return;

        float dir = player.isFacingRight() ? 1f : -1f;

        player.setDashing(true);
        player.setDashTimer(PlayerConstants.DASH_DURATION);
        player.setDashCooldownTimer(getDashCooldown());

        player.setVelocityX(dir * PlayerConstants.DASH_SPEED);
        player.setVelocityY(0f);

        player.setDashCountInAir(player.getDashCountInAir() + 1);

        player.setAttacking(false);
        player.setAttackTimer(0f);
        player.setWallSliding(false);
    }

    public void updateWallSlide(boolean leftHeld, boolean rightHeld, boolean touchingWallLeft, boolean touchingWallRight) {
        if (player.isOnGround() || player.isKnockedBack() || player.isFocusing() || player.isDashing() || player.isWallJumpLocked()) {
            player.setWallSliding(false);
            return;
        }

        boolean pressingIntoLeftWall = leftHeld && touchingWallLeft;
        boolean pressingIntoRightWall = rightHeld && touchingWallRight;

        if (pressingIntoLeftWall) {
            player.setWallSliding(true);
            player.setFacingRight(false);
            player.setCanDoubleJump(true);
        }
        else if (pressingIntoRightWall) {
            player.setWallSliding(true);
            player.setFacingRight(true);
            player.setCanDoubleJump(true);
        }
        else {
            player.setWallSliding(false);
        }
    }

    public static void toggleGodMode() {
        godMode = !godMode;
    }

    public static void toggleEmergencyHeal() {
        emergencyHeal = !emergencyHeal;
    }

    public void takeDamage(int amount) {
        if (player.isInvincible() || !player.isAlive()) return;
        cancelFocus();
        player.setHealth(Math.max(0, player.getHealth() - amount));
        player.setHurtTimer(PlayerConstants.HURT_COOLDOWN);
        if (player.isAlive())
            player.setState(PlayerState.HURT);
    }

    public void addDamageTakenFromBoss(int damage) {
        player.setDamageTakenFromBoss(player.getDamageTakenFromBoss() + damage);
    }

    public void startFocus() {
        if (!player.isOnGround() || player.isInvincible() || player.isAttacking() || player.isDashing() || player.isCasting())
            return;
        if (player.getHealAnimTimer() > 0)
            return;
        if (player.getSoul() < PlayerConstants.SOUL_HEAL_COST)
            return;
        if (player.getHealth() >= PlayerConstants.MAX_HEALTH)
            return;
        player.setFocusing(true);
        player.setFocusTimer(0f);
        player.setVelocityX(0f);
    }

    public void updateFocus(float delta) {
        if (!player.isFocusing())
            return;
        float newFocusTimer = player.getFocusTimer() + delta;
        player.setFocusTimer(newFocusTimer);
        player.setVelocityX(0f);
        if (player.getFocusTimer() >= getFocusDuration()) {
            player.setSoul(Math.max(0, player.getSoul() - PlayerConstants.SOUL_HEAL_COST));
            player.setHealth(Math.min(PlayerConstants.MAX_HEALTH, player.getHealth() + 1));
            player.setFocusing(false);
            player.setFocusTimer(0f);
            player.setHealAnimTimer(PlayerConstants.HEAL_ANIM_DUR);
        }
    }

    public void cancelFocus() {
        player.setFocusing(false);
        player.setFocusTimer(0f);
    }

    public void applyKnockback(float knockBackDirection) {
        player.setVelocityX(knockBackDirection * PlayerConstants.KNOCKBACK_VELOCITY_X);
        player.setVelocityY(PlayerConstants.KNOCKBACK_VELOCITY_VY);
        player.setKnockbackTimer(PlayerConstants.KNOCKBACK_DURATION);
        player.setAttacking(false);
        player.setAttackTimer(0f);
        player.setDashing(false);
        player.setDashTimer(0f);
        player.setWallSliding(false);
        player.setState(PlayerState.HURT);
    }

    public void updateLastSafePosition() {
        player.getLastSafePosition().set(player.getPosition());
    }

    public void attack(AttackDirection dir) {
        if (player.isFocusing() || player.isKnockedBack() || player.isDashing() || player.isCasting()) return;
        if (player.isAttacking()) return;

        player.setAttacking(true);
        player.setAttackTimer(getAttackDuration());
        player.setAttackDirection(dir);
    }

    public boolean castVengefulSpirit() {
        if (!player.isAlive())
            return false;
        if (player.isFocusing() || player.isKnockedBack() || player.isDashing() || player.isCasting() || player.isAttacking())
            return false;
        if (player.getSoul() < PlayerConstants.SPELL_SOUL_COST)
            return false;

        player.setSoul(player.getSoul() - PlayerConstants.SPELL_SOUL_COST);
        player.setCasting(true);
        player.setCastTimer(getCastLockDuration());
        player.setVelocityX(0f);
        return true;
    }

    public boolean castHowlingWraiths() {
        if (!player.isAlive())
            return false;
        if (player.isFocusing() || player.isKnockedBack() || player.isDashing() || player.isCasting() || player.isAttacking())
            return false;
        if (player.getSoul() < PlayerConstants.SPELL_SOUL_COST)
            return false;

        player.setSoul(player.getSoul() - PlayerConstants.SPELL_SOUL_COST);
        player.setCasting(true);
        player.setCastTimer(getCastLockDuration());
        player.setVelocityX(0f);
        return true;
    }

    public float getCastLockDuration() {
        return PlayerConstants.CAST_LOCK_DURATION;
    }

    public int getSpellDamage() {
        return VengefulSpiritConstants.DAMAGE;
    }

    public void checkSwordHits(MossflyController mossflyController,
                               HuskHornheadController huskHornheadController,
                               CrystalGuardianController crystalGuardianController,
                               CrystalCrawlerController crystalCrawlerController,
                               FalseKnightController falseKnightController,
                               ZoteController zoteController) {
        if (!player.isAttacking()) return;

        Rectangle hitbox = getSwordHitbox();
        if (hitbox == null) return;

        if (mossflyController != null)
            mossflyController.checkSwordHits(hitbox, player);

        if (huskHornheadController != null)
            huskHornheadController.checkSwordHits(hitbox, player);

        if (crystalGuardianController != null)
            crystalGuardianController.checkSwordHits(hitbox, player);

        if (crystalCrawlerController != null)
            crystalCrawlerController.checkSwordHits(hitbox, player);

        if (falseKnightController != null)
            falseKnightController.checkSwordHits(hitbox);

        if (zoteController != null)
            zoteController.checkSwordHits(hitbox, player);
    }

    public Rectangle getSwordHitbox() {
        if (!player.isAttacking())
            return null;
        float duration = getAttackDuration();
        float elapsed = duration - player.getAttackTimer();
        float activeStart = duration * PlayerConstants.HITBOX_ACTIVE_START;
        float activeEnd = duration * PlayerConstants.HITBOX_ACTIVE_END;
        if (elapsed < activeStart || elapsed > activeEnd)
            return null;

        float bx = player.getBounds().x;
        float by = player.getBounds().y;
        float bw = player.getBounds().width;
        float bh = player.getBounds().height;

        switch (player.getAttackDirection()) {
            case RIGHT:
                return new Rectangle(bx + bw - 2f, by + bh * 0.1f, 55f, bh * 0.85f);
            case LEFT:
                return new Rectangle(bx - 53f, by + bh * 0.1f, 55f, bh * 0.85f);
            case UP:
                return new Rectangle(bx - 15f, by + bh, bw + 30f, 45f);
            case DOWN:
                return new Rectangle(bx - 17f, by - 25f, bw + 30f, 35f);
            default:
                return null;
        }
    }

    public int getNailDamage() {
        int damage = 1;
        if (CharmManager.isEquipped(CharmType.UNBREAKABLE_STRENGTH))
            damage += CharmConstants.UNBREAKABLE_STRENGTH_BONUS_DAMAGE;
        return damage;
    }

    public float getKnockbackMultiplier() {
        return CharmManager.isEquipped(CharmType.HEAVY_BLOW) ? CharmConstants.HEAVY_BLOW_KNOCKBACK_MULTIPLIER : 1f;
    }

    public float getAttackDuration() {
        return CharmManager.isEquipped(CharmType.QUICK_SLASH) ? PlayerConstants.ATTACK_DUR * CharmConstants.QUICK_SLASH_ATTACK_DUR_MULTIPLIER : PlayerConstants.ATTACK_DUR;
    }

    public float getFocusDuration() {
        return CharmManager.isEquipped(CharmType.QUICK_FOCUS) ? PlayerConstants.FOCUS_DURATION * CharmConstants.QUICK_FOCUS_DURATION_MULTIPLIER : PlayerConstants.FOCUS_DURATION;
    }

    public float getDashCooldown() {
        return CharmManager.isEquipped(CharmType.DASHMASTER) ? PlayerConstants.DASH_COOLDOWN * CharmConstants.DASHMASTER_COOLDOWN_MULTIPLIER : PlayerConstants.DASH_COOLDOWN;
    }

    public int getDamageTakenFromBoss() { return player.getDamageTakenFromBoss(); }

    public boolean isGodMode() {
        return godMode;
    }
    public boolean isEmergencyHeal() {
        return emergencyHeal;
    }

    public void gainSoul() {
        int soulAmount = PlayerConstants.SOUL_GAIN_PER_HIT;
        if (CharmManager.isEquipped(CharmType.SOUL_CATCHER))
            soulAmount += CharmConstants.SOUL_CATCHER_BONUS_SOUL;
        player.addSoul(soulAmount);
    }
}
