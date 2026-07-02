package HollowKnight.source.view.enemies;

import HollowKnight.source.model.asset.Assets;
import HollowKnight.source.model.enemies.husk_hornhead.HuskHornhead;
import HollowKnight.source.model.enemies.husk_hornhead.HuskHornheadConstants;
import HollowKnight.source.model.enemies.husk_hornhead.HuskHornheadState;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;

public class HuskHornheadRenderer {

    public void render(SpriteBatch batch, List<HuskHornhead> huskList) {
        for (HuskHornhead hh : huskList) {
            renderOne(batch, hh);
        }
    }

    private void renderOne(SpriteBatch batch, HuskHornhead hh) {
        HuskHornheadState state = hh.getState();

        Animation<TextureRegion> anim;
        float stateTime;
        boolean loops;

        switch (state) {
            case WALK:
                anim = Assets.getHuskHornheadWalkAnim();
                stateTime = hh.getStateTimer();
                loops = true;
                break;

            case REST:
                anim = Assets.getHuskHornheadIdleAnim();
                stateTime = hh.getStateTimer();
                loops = true;
                break;

            case CHARGE:
                anim = Assets.getHuskHornheadChargeAnim();
                stateTime = hh.getStateTimer();
                loops = true;
                break;

            case DEAD:
                anim = Assets.getHuskHornheadDeathAnim();
                loops = false;
                stateTime = hh.isOnGround() ? HuskHornheadConstants.DEATH_ANIM_DUR : hh.getStateTimer();
                break;

            default:
                return;
        }

        if (hh.isInvincible()) {
            float blink = (float) Math.sin(hh.getHurtTimer() * 30f);
            batch.setColor(1f, 1f, 1f, blink > 0f ? 1f : 0.2f);
        }

        TextureRegion frame = anim.getKeyFrame(stateTime, loops);

        boolean shouldFlip = hh.isFacingRight();
        if (frame.isFlipX() != shouldFlip) {
            frame.flip(true, false);
        }

        batch.draw(frame, hh.getPosition().x, hh.getPosition().y, HuskHornheadConstants.WIDTH, HuskHornheadConstants.HEIGHT);

        batch.setColor(1f, 1f, 1f, 1f);
    }
}
