package HollowKnight.source.model.player;

import HollowKnight.source.controller.GameController;
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

    private int damageTakenFromBoss;

    private boolean killedMossfly;
    private boolean killedHuskHornhead;
    private boolean killedCrystalGuardian;
    private boolean killedCrystalCrawler;

    private boolean isFocusing;
    private float focusTimer;
    private float healAnimTimer;
    private float deathTimer;
    private float knockbackTimer;

    private boolean canDoubleJump = true;
    private boolean isDashing;
    private float dashTimer;
    private float dashCooldownTimer;
    private int dashCountInAir;
    private boolean isWallSliding;
    private float wallJumpLockTimer;
    private boolean isCasting;
    private float castTimer;

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
        damageTakenFromBoss = 0;
        killedMossfly = false;
        killedHuskHornhead = false;
        killedCrystalGuardian = false;
        killedCrystalCrawler = false;
    }

    public static Player getInstance() {
        Vector2 startSpawn = GameController.getSpawnPosition("game_start_spawn");
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
        isDashing = false;
        dashTimer = 0f;
        dashCooldownTimer = 0f;
        dashCountInAir = 0;
        isWallSliding = false;
        wallJumpLockTimer = 0f;
        isCasting = false;
        castTimer = 0f;
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
            dashCountInAir = 0;
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
    public void setAttackDirection(AttackDirection attackDirection) {
        this.attackDirection = attackDirection;
    }
    public void setCanDoubleJump(boolean canDoubleJump) {
        this.canDoubleJump = canDoubleJump;
    }
    public void setDashing(boolean dashing) {
        isDashing = dashing;
    }
    public void setDashTimer(float dashTimer) {
        this.dashTimer = dashTimer;
    }
    public void setDashCooldownTimer(float dashCooldownTimer) {
        this.dashCooldownTimer = dashCooldownTimer;
    }
    public void setDashCountInAir(int dashCountInAir) {
        this.dashCountInAir = dashCountInAir;
    }
    public void setWallSliding(boolean wallSliding) {
        isWallSliding = wallSliding;
    }
    public void setWallJumpLockTimer(float wallJumpLockTimer) {
        this.wallJumpLockTimer = wallJumpLockTimer;
    }
    public void setCasting(boolean casting) {
        isCasting = casting;
    }
    public void setCastTimer(float castTimer) {
        this.castTimer = castTimer;
    }
    public void setDamageTakenFromBoss(int damage) { this.damageTakenFromBoss = damage; }
    public void setKilledCrystalCrawler(boolean killedCrystalCrawler) { this.killedCrystalCrawler = killedCrystalCrawler; }
    public void setKilledCrystalGuardian(boolean killedCrystalGuardian) { this.killedCrystalGuardian = killedCrystalGuardian; }
    public void setKilledHuskHornhead(boolean killedHuskHornhead) { this.killedHuskHornhead = killedHuskHornhead; }
    public void setKilledMossfly(boolean killedMossfly) { this.killedMossfly = killedMossfly; }

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
    public boolean isDashing() { return isDashing; }
    public float getDashTimer() { return dashTimer; }
    public float getDashCooldownTimer() { return dashCooldownTimer; }
    public int getDashCountInAir() { return dashCountInAir; }
    public boolean isWallSliding() { return isWallSliding; }
    public float getWallJumpLockTimer() { return wallJumpLockTimer; }
    public boolean isWallJumpLocked() { return wallJumpLockTimer > 0f; }
    public boolean isCasting() { return isCasting; }
    public float getCastTimer() { return castTimer; }
    public int getDamageTakenFromBoss() { return damageTakenFromBoss; }
    public boolean isKilledMossfly() { return killedMossfly; }
    public boolean isKilledCrystalCrawler() { return killedCrystalCrawler; }
    public boolean isKilledCrystalGuardian() { return killedCrystalGuardian; }
    public boolean isKilledHuskHornhead() { return killedHuskHornhead; }
}
