package HollowKnight.source.model.enemies.husk_hornhead;

import HollowKnight.source.model.enemies.Enemy;

public class HuskHornhead extends Enemy {

    private HuskHornheadState state;
    private float chargeDirectionX;

    public HuskHornhead(float x, float y) {
        super(
            x, y,
            HuskHornheadConstants.MAX_HEALTH,
            HuskHornheadConstants.RIGHT_BOUNDS_OFFSET_X,
            HuskHornheadConstants.BOUNDS_OFFSET_Y,
            HuskHornheadConstants.BOUNDS_W,
            HuskHornheadConstants.BOUNDS_H
        );
        state = HuskHornheadState.WALK;
        chargeDirectionX = 1f;
    }

    @Override
    public void setPosition(float x, float y) {
        position.set(x, y);
        float offsetX = facingRight ? HuskHornheadConstants.RIGHT_BOUNDS_OFFSET_X : HuskHornheadConstants.LEFT_BOUNDS_OFFSET_X;
        bounds.setPosition(x + offsetX, y + HuskHornheadConstants.BOUNDS_OFFSET_Y - 7f);
    }


    public HuskHornheadState getState() { return state; }
    public void setState(HuskHornheadState state) { this.state = state; }

    public float getChargeDirectionX() { return chargeDirectionX; }
    public void setChargeDirectionX(float dir) { this.chargeDirectionX = dir; }
}
