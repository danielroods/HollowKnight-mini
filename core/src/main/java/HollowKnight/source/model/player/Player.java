package HollowKnight.source.model.player;

import HollowKnight.source.game_utils.Assets;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private static Player instance;

    private final Vector2 position;
    private final Vector2 velocity;
    private final Rectangle bounds;
    private final Vector2 lastSafePosition;

    private PlayerState state;
    private boolean facingRight;
    private boolean isOnGround;

    private boolean isAttacking;
    private float attackTimer;
    private float hurtTimer;

    private int health;
    private int soul;

    private boolean isFocusing;
    private float focusTimer;
    private float healAnimTimer;
    private float deathTimer;
    private float knockbackTimer;

    private boolean canDoubleJump = true;

    private AttackDirection attackDirection = AttackDirection.RIGHT;

    private Player(float x, float y) {
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        bounds = new Rectangle(x + 85, y, PlayerConstants.WIDTH - 170, PlayerConstants.HEIGHT - 48);
        lastSafePosition = new Vector2(x, y);
        state = PlayerState.IDLE;
        facingRight = true;
        isOnGround = false;
        health = PlayerConstants.MAX_HEALTH;
        soul = 0;
    }

    public static Player getInstance() {
        Vector2 startSpawn = Assets.getSpawnPosition("game_start_spawn");
        if (instance == null)
            instance = new Player(startSpawn.x, startSpawn.y);
        return instance;
    }

    public void restoreFullHealth() {
        health = PlayerConstants.MAX_HEALTH;
        deathTimer = 0f;
        isFocusing = false;
        focusTimer = 0f;
        hurtTimer = 0f;
        healAnimTimer = 0f;
        isAttacking = false;
        knockbackTimer = 0f;
        canDoubleJump = true;
    }

    public void setVelocityX(float velocityX) {
        velocity.x = velocityX;
    }
    public void setVelocityY(float velocityY) {
        velocity.y = velocityY;
    }
    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }
    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(PlayerConstants.MAX_HEALTH, health));
    }
    public void setSoul(int soul) {
        this.soul = Math.max(0, Math.min(PlayerConstants.MAX_SOUL, soul));
    }
    public void addSoul(int amount) {
        soul = Math.min(PlayerConstants.MAX_SOUL, soul + amount);
    }
    public void setState(PlayerState state) {
        this.state = state;
    }
    public void setHurtTimer(float timer) {
        hurtTimer = timer;
    }
    public void setOnGround(boolean onGround) {
        this.isOnGround = onGround;
        if (onGround) {
            velocity.y = 0;
            canDoubleJump = true;
        }
    }
    public void setPosition(float x, float y) {
        position.set(x, y);
        bounds.setPosition(x + 85, y);
    }
    public void consumeDoubleJump() {
        canDoubleJump = false;
    }
    public void setFocusing(boolean focusing) {
        isFocusing = focusing;
    }
    public void setFocusTimer(float focusTimer) {
        this.focusTimer = focusTimer;
    }
    public void setKnockbackTimer(float knockbackTimer) {
        this.knockbackTimer = knockbackTimer;
    }
    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }
    public void setAttackTimer(float attackTimer) {
        this.attackTimer = attackTimer;
    }
    public void setHealAnimTimer(float healAnimTimer) {
        this.healAnimTimer = healAnimTimer;
    }
    public void setDeathTimer(float deathTimer) {
        this.deathTimer = deathTimer;
    }
    public void setCanDoubleJump(boolean canDoubleJump) {
        this.canDoubleJump = canDoubleJump;
    }
    public void setAttackDirection(AttackDirection attackDirection) {
        this.attackDirection = attackDirection;
    }

    public Vector2 getPosition() { return position; }
    public Vector2 getVelocity() { return velocity; }
    public Rectangle getBounds() { return bounds; }
    public PlayerState getState() { return state; }
    public boolean isFacingRight() { return facingRight; }
    public boolean isOnGround() { return isOnGround; }
    public int getHealth() { return health; }
    public int getSoul() { return soul; }
    public boolean isAlive() { return health > 0; }
    public boolean isInvincible() { return hurtTimer > 0; }
    public float getHurtTimer() { return hurtTimer; }
    public boolean isFocusing() { return isFocusing; }
    public float getFocusTimer() { return focusTimer; }
    public float getHealAnimTimer() { return healAnimTimer; }
    public float getDeathTimer() { return deathTimer; }
    public boolean isAttacking() { return isAttacking; }
    public float getAttackTimer() { return attackTimer; }
    public Vector2 getLastSafePosition() { return new Vector2(lastSafePosition); }
    public boolean isKnockedBack() { return knockbackTimer > 0; }
    public boolean canDoubleJump() { return canDoubleJump; }
    public AttackDirection getAttackDirection() { return attackDirection; }
    public float getKnockbackTimer() { return knockbackTimer; }
    public boolean isCanDoubleJump() { return canDoubleJump; }
}
