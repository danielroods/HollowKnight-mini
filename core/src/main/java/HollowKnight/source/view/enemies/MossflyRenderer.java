package HollowKnight.source.view.enemies;

import HollowKnight.source.model.asset.Assets;
import HollowKnight.source.model.enemies.mossfly.MossflyState;
import HollowKnight.source.model.enemies.mossfly.Mossfly;
import HollowKnight.source.model.enemies.mossfly.MossflyConstants;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;

public class MossflyRenderer {

    public void render(SpriteBatch batch, List<Mossfly> mossflyList) {
        for (Mossfly mossfly : mossflyList) {
            renderOneMossfly(batch, mossfly);
        }
    }

    private void renderOneMossfly(SpriteBatch batch, Mossfly mossfly) {
        MossflyState state = mossfly.getState();

        Animation<TextureRegion> anim;
        float stateTime;
        boolean loops;

        switch (state) {
            case HIDDEN:
                anim = Assets.getMossflyShakeAnim();
                stateTime = mossfly.getStateTimer();
                loops = true;
                break;

            case APPEAR:
                anim = Assets.getMossflyAppearAnim();
                stateTime = mossfly.getStateTimer();
                loops = false;
                break;

            case FLY:
                anim = Assets.getMossflyFlyAnim();
                stateTime = mossfly.getStateTimer();
                loops = true;
                break;

            case DEAD:
                anim = Assets.getMossflyDeathAnim();
                loops = false;
                stateTime = mossfly.isOnGround() ? MossflyConstants.DEATH_ANIM_DUR : mossfly.getStateTimer();
                break;

            default:
                return;
        }

        if (mossfly.isInvincible()) {
            float blink = (float) Math.sin(mossfly.getHurtTimer() * 30f);
            batch.setColor(1f, 1f, 1f, blink > 0f ? 1f : 0.2f);
        }

        TextureRegion frame = anim.getKeyFrame(stateTime, loops);

        boolean shouldFlip = mossfly.isFacingRight();
        if (frame.isFlipX() != shouldFlip) {
            frame.flip(true, false);
        }

        batch.draw(frame, mossfly.getPosition().x, mossfly.getPosition().y, MossflyConstants.WIDTH, MossflyConstants.HEIGHT);

        batch.setColor(1f, 1f, 1f, 1f);
    }
}
