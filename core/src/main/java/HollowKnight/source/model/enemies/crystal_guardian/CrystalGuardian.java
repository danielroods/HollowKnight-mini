package HollowKnight.source.model.enemies.crystal_guardian;

import HollowKnight.source.model.enemies.Enemy;

public class CrystalGuardian extends Enemy {

    private CrystalGuardianState state;
    private final boolean homeFacingRight;
    private float enrageTimer;
    private float evadeCooldownTimer;
    private float evadeDirection = 1f;
    private boolean laserFiredThisCycle;
    private boolean laserActive;
    private float laserTimer;
    private boolean laserHasHitPlayer;
    private float laserOriginX;
    private float laserOriginY;
    private boolean laserFacingRight;

    public CrystalGuardian(float x, float y) {
        super(x, y,
            CrystalGuardianConstants.MAX_HEALTH,
            CrystalGuardianConstants.BOUNDS_OFFSET_X,
            CrystalGuardianConstants.BOUNDS_OFFSET_Y,
            CrystalGuardianConstants.BOUNDS_W,
            CrystalGuardianConstants.BOUNDS_H);

        state = CrystalGuardianState.IDLE;
        homeFacingRight = facingRight;
    }

    public CrystalGuardianState getState() { return state; }
    public void setState(CrystalGuardianState state) { this.state = state; }

    public boolean isHomeFacingRight() { return homeFacingRight; }

    public float getEnrageTimer() { return enrageTimer; }
    public void setEnrageTimer(float t) { this.enrageTimer = t; }

    public float getEvadeCooldownTimer() { return evadeCooldownTimer; }
    public void setEvadeCooldownTimer(float t) { this.evadeCooldownTimer = t; }

    public float getEvadeDirection() { return evadeDirection; }
    public void setEvadeDirection(float d) { this.evadeDirection = d; }

    public boolean isLaserFiredThisCycle() { return laserFiredThisCycle; }
    public void setLaserFiredThisCycle(boolean v) { this.laserFiredThisCycle = v; }

    public boolean isLaserActive() { return laserActive; }
    public void setLaserActive(boolean v) { this.laserActive = v; }

    public float getLaserTimer() { return laserTimer; }
    public void setLaserTimer(float t) { this.laserTimer = t; }

    public boolean isLaserHasHitPlayer() { return laserHasHitPlayer; }
    public void setLaserHasHitPlayer(boolean v) { this.laserHasHitPlayer = v; }

    public float getLaserOriginX() { return laserOriginX; }
    public void setLaserOriginX(float x) { this.laserOriginX = x; }

    public float getLaserOriginY() { return laserOriginY; }
    public void setLaserOriginY(float y) { this.laserOriginY = y; }

    public boolean isLaserFacingRight() { return laserFacingRight; }
    public void setLaserFacingRight(boolean facingRight) { this.laserFacingRight = facingRight; }
}
