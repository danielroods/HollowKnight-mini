package HollowKnight.source.model.enemies;

import HollowKnight.source.model.enemies.mossfly.MossflyState;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy {

    protected final Vector2 position;
    protected final Vector2 velocity;
    protected final Rectangle bounds;
    protected final Vector2 spawnPosition;

    private final float boundsOffsetX;
    private final float boundsOffsetY;

    protected boolean facingRight;
    protected boolean onGround;

    protected float stateTimer;
    protected float hurtTimer;
    protected float knockbackTimer;

    protected int health;
    private final int maxHealth;

    protected Enemy(float x, float y, int maxHealth, float boundsOffsetX, float boundsOffsetY, float boundsW, float boundsH) {

        this.maxHealth = maxHealth;
        this.boundsOffsetX = boundsOffsetX;
        this.boundsOffsetY = boundsOffsetY;

        spawnPosition = new Vector2(x, y);
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        bounds = new Rectangle(x + boundsOffsetX, y + boundsOffsetY, boundsW, boundsH);

        health = maxHealth;
        facingRight = true;
        onGround = false;
        stateTimer = 0f;
        hurtTimer = 0f;
        knockbackTimer = 0f;
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
        bounds.setPosition(x + boundsOffsetX, y + boundsOffsetY - 7f);
    }

    public void setHealth(int health) { this.health = health; }
    public void setFacingRight(boolean facingRight) { this.facingRight = facingRight; }
    public void setOnGround(boolean onGround) { this.onGround = onGround; }
    public void setStateTimer(float t) { this.stateTimer = t; }
    public void setHurtTimer(float t) { this.hurtTimer = t; }
    public void setKnockbackTimer(float t) { this.knockbackTimer = t; }

    public Vector2 getPosition() { return position; }
    public Vector2 getVelocity() { return velocity; }
    public Rectangle getBounds() { return bounds; }
    public Vector2 getSpawnPosition() { return spawnPosition; }
    public int getHealth() { return health; }
    public float getStateTimer() { return stateTimer; }
    public float getHurtTimer() { return hurtTimer; }
    public float getKnockbackTimer(){ return knockbackTimer; }
    public boolean isFacingRight() { return facingRight; }
    public boolean isOnGround() { return onGround; }
    public boolean isAlive() { return health > 0; }
    public boolean isInvincible() { return hurtTimer > 0; }
}
