package HollowKnight.source.model.enemies.false_knight;

import HollowKnight.source.model.enemies.Enemy;

public class FalseKnight extends Enemy {
    private FalseKnightState state;
    private FalseKnightState lastState;

    private int phase = 1;
    private boolean stunTriggered;

    private float chargeDirectionX = -1f;
    private float jumpDirectionX = -1f;

    private boolean shockwaveActive;
    private float shockwaveTimer;
    private float shockwaveX;
    private float shockwaveOriginX;
    private float shockwaveOriginY;
    private float shockwaveDirection = 1f;
    private boolean attackHasHitPlayer;

    public FalseKnight(float x, float y) {
        super(x, y,
            FalseKnightConstants.MAX_HEALTH,
            FalseKnightConstants.RIGHT_BOUNDS_OFFSET_X,
            FalseKnightConstants.BOUNDS_OFFSET_Y,
            FalseKnightConstants.BOUNDS_W,
            FalseKnightConstants.BOUNDS_H);
        state = FalseKnightState.IDLE;
    }

    @Override
    public void setPosition(float x, float y) {
        position.set(x, y);
        float offsetX = facingRight ? FalseKnightConstants.RIGHT_BOUNDS_OFFSET_X : FalseKnightConstants.LEFT_BOUNDS_OFFSET_X;
        bounds.setPosition(x + offsetX, y + FalseKnightConstants.BOUNDS_OFFSET_Y - 7f);
    }

    public FalseKnightState getState() { return state; }
    public void setState(FalseKnightState state) { this.state = state; }

    public FalseKnightState getLastState() { return lastState; }
    public void setLastState(FalseKnightState state) { this.lastState = state; }

    public int getPhase() { return phase; }
    public void setPhase(int phase) { this.phase = phase; }

    public boolean isStunTriggered() { return stunTriggered; }
    public void setStunTriggered(boolean v) { this.stunTriggered = v; }

    public float getChargeDirectionX() { return chargeDirectionX; }
    public void setChargeDirectionX(float d) { this.chargeDirectionX = d; }

    public float getJumpDirectionX() { return jumpDirectionX; }
    public void setJumpDirectionX(float d) { this.jumpDirectionX = d; }

    public boolean isShockwaveActive() { return shockwaveActive; }
    public void setShockwaveActive(boolean v) { this.shockwaveActive = v; }

    public float getShockwaveTimer() { return shockwaveTimer; }
    public void setShockwaveTimer(float t) { this.shockwaveTimer = t; }

    public float getShockwaveX() { return shockwaveX; }
    public void setShockwaveX(float x) { this.shockwaveX = x; }

    public float getShockwaveOriginX() { return shockwaveOriginX; }
    public void setShockwaveOriginX(float x) { this.shockwaveOriginX = x; }

    public float getShockwaveOriginY() { return shockwaveOriginY; }
    public void setShockwaveOriginY(float shockwaveOriginY) { this.shockwaveOriginY = shockwaveOriginY; }

    public float getShockwaveDirection() { return shockwaveDirection; }
    public void setShockwaveDirection(float d) { this.shockwaveDirection = d; }

    public boolean isAttackHasHitPlayer() { return attackHasHitPlayer; }
    public void setAttackHasHitPlayer(boolean v) { this.attackHasHitPlayer = v; }
}
