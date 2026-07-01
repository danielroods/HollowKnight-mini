package HollowKnight.source.model.enemies.mossfly;

import HollowKnight.source.model.enemies.Enemy;

public class Mossfly extends Enemy {

    private MossflyState state;

    public Mossfly(float x, float y) {
        super(x, y,
              MossflyConstants.MAX_HEALTH,
              MossflyConstants.BOUNDS_OFFSET_X,
              MossflyConstants.BOUNDS_OFFSET_Y,
              MossflyConstants.BOUNDS_W,
              MossflyConstants.BOUNDS_H);

        state = MossflyState.HIDDEN;
    }

    public MossflyState getState() {
        return state;
    }

    public void setState(MossflyState state) {
        this.state = state;
    }
}
