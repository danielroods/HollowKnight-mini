package HollowKnight.source.model.spell;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.HashSet;
import java.util.Set;

public class VengefulSpirit {
    private final Vector2 position;
    private final Rectangle bounds;
    private final boolean facingRight;
    private final float velocityX;

    private float lifeTimer;
    private float traveledDistance;
    private boolean expired;

    private final Set<Object> hitTargets = new HashSet<>();

    public VengefulSpirit(float x, float y, boolean facingRight) {
        this.facingRight = facingRight;
        this.velocityX = (facingRight ? 1f : -1f) * VengefulSpiritConstants.SPEED;

        this.position = new Vector2(x, y);
        this.bounds = new Rectangle(
            x + VengefulSpiritConstants.BOUNDS_OFFSET_X,
            y + VengefulSpiritConstants.BOUNDS_OFFSET_Y,
            VengefulSpiritConstants.BOUNDS_W,
            VengefulSpiritConstants.BOUNDS_H
        );

        lifeTimer = 0f;
        traveledDistance = 0f;
        expired = false;
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
        bounds.setPosition(x + VengefulSpiritConstants.BOUNDS_OFFSET_X, y + VengefulSpiritConstants.BOUNDS_OFFSET_Y);
    }

    public boolean hasHit(Object target) {
        return hitTargets.contains(target);
    }

    public void markHit(Object target) {
        hitTargets.add(target);
    }

    public Vector2 getPosition() { return position; }
    public Rectangle getBounds() { return bounds; }
    public boolean isFacingRight() { return facingRight; }
    public float getVelocityX() { return velocityX; }

    public float getLifeTimer() { return lifeTimer; }
    public void setLifeTimer(float lifeTimer) { this.lifeTimer = lifeTimer; }

    public float getTraveledDistance() { return traveledDistance; }
    public void setTraveledDistance(float traveledDistance) { this.traveledDistance = traveledDistance; }

    public boolean isExpired() { return expired; }
    public void setExpired(boolean expired) { this.expired = expired; }
}
