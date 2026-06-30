package HollowKnight.source.controller;

import HollowKnight.source.model.player.AttackDirection;
import HollowKnight.source.model.player.Player;
import HollowKnight.source.model.player.PlayerConstants;
import HollowKnight.source.model.player.PlayerState;
import com.badlogic.gdx.math.Rectangle;

public class PlayerController {
    private static PlayerController instance;
    private final Player player;

    private PlayerController() {
        player = Player.getInstance();
    }

    public static PlayerController getInstance() {
        if (instance == null) instance = new PlayerController();
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

        player.setVelocityY(player.getVelocity().y + PlayerConstants.GRAVITY * delta);

        float currentX = player.getPosition().x;
        float currentY = player.getPosition().y;
        float deltaX = player.getVelocity().x * delta;
        float deltaY = player.getVelocity().y * delta;
        player.setPosition(currentX + deltaX, currentY + deltaY);

        player.getBounds().setPosition(player.getPosition().x + 85, player.getPosition().y);

        if (player.getKnockbackTimer() > 0) {
            player.setState(PlayerState.HURT);
        }
        else if (player.isAttacking()) {
            player.setState(PlayerState.ATTACK);
        }
        else if (player.isFocusing()) {
            player.setState(PlayerState.FOCUS);
        }
        else if (player.getHealAnimTimer() > 0) {
            player.setState(PlayerState.HEAL);
        }
        else if (!player.isOnGround()) {
            if (player.getVelocity().y > 0) {
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
        if (player.isFocusing() || player.isKnockedBack()) return;
        player.setVelocityX(-PlayerConstants.SPEED);
        player.setFacingRight(false);
    }

    public void moveRight() {
        if (player.isFocusing() || player.isKnockedBack()) return;
        player.setVelocityX(PlayerConstants.SPEED);
        player.setFacingRight(true);
    }

    public void stopHorizontal() {
        if (player.isKnockedBack()) return;
        player.setVelocityX(0f);
    }

    public void jump() {
        if (player.isFocusing() || player.isKnockedBack()) return;

        if (player.isOnGround()) {
            player.setVelocityY(PlayerConstants.JUMP_FORCE);
            player.setOnGround(false);
        }
        else if (player.canDoubleJump()) {
            player.setVelocityY(PlayerConstants.JUMP_FORCE * 0.92f);
            player.consumeDoubleJump();
        }
    }

    public void attack(AttackDirection dir) {
        if (player.isFocusing() || player.isKnockedBack()) return;
        if (player.isAttacking()) return;

        startAttack(dir);
        player.addSoul(PlayerConstants.SOUL_GAIN_PER_HIT);
    }

    public void takeDamage(int amount) {
        if (player.isInvincible() || !player.isAlive()) return;
        cancelFocus();
        player.setHealth(Math.max(0, player.getHealth() - amount));
        player.setHurtTimer(PlayerConstants.HURT_COOLDOWN);
        if (player.isAlive())
            player.setState(PlayerState.HURT);
    }
    public void startFocus() {
        if (!player.isOnGround() || player.isInvincible() || player.isAttacking())
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
        if (player.getFocusTimer() >= PlayerConstants.FOCUS_DURATION) {
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
        player.setState(PlayerState.HURT);
    }

    public void updateLastSafePosition() {
        player.getLastSafePosition().set(player.getPosition());
    }

    public void startAttack(AttackDirection dir) {
        if (player.isFocusing() || player.isKnockedBack()) return;
        player.setAttacking(true);
        player.setAttackTimer(PlayerConstants.ATTACK_DUR);
        player.setAttackDirection(dir);
    }

    public Rectangle getSwordHitbox() {
        if (!player.isAttacking()) return null;
        float elapsed = PlayerConstants.ATTACK_DUR - player.getAttackTimer();
        if (elapsed < PlayerConstants.HITBOX_ACTIVE_START || elapsed > PlayerConstants.HITBOX_ACTIVE_END)
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
}
