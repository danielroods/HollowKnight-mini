package HollowKnight.source.view.enemies;

import HollowKnight.source.model.asset.Assets;
import HollowKnight.source.model.enemies.false_knight.FalseKnight;
import HollowKnight.source.model.enemies.false_knight.FalseKnightConstants;
import HollowKnight.source.model.enemies.false_knight.FalseKnightState;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import java.util.List;

public class FalseKnightRenderer {

    public void render(SpriteBatch batch, List<FalseKnight> falseKnightList) {
        for (FalseKnight falseKnight : falseKnightList) {
            renderOne(batch, falseKnight);
        }
    }

    private void renderOne(SpriteBatch batch, FalseKnight falseKnight) {
        FalseKnightState state = falseKnight.getState();

        Animation<TextureRegion> anim;
        float stateTime = falseKnight.getStateTimer();
        boolean loops;

        switch (state) {
            case IDLE:
                anim = Assets.getFalseKnightIdleAnim();
                loops = true;
                break;

            case CHARGE_RUN:
                anim = Assets.getFalseKnightRunAnim();
                loops = true;
                break;

            case JUMP:
                anim = Assets.getFalseKnightJumpAnim();
                loops = false;
                break;

            case SHOCKWAVE_SLAM:
                anim = Assets.getFalseKnightJumpAttackAnim();
                loops = false;
                break;

            case LAND:
                anim = Assets.getFalseKnightLandAnim();
                loops = false;
                break;

            case MACE_SLAM:
                anim = Assets.getFalseKnightAttackAnim();
                loops = false;
                break;

            case MACE_SLAM_RECOVER:
                anim = Assets.getFalseKnightAttackRecoverAnim();
                loops = false;
                break;

            case STUN_ENTER:
                anim = Assets.getFalseKnightDeathAnim();
                loops = false;
                break;

            case STUNNED:
                anim = Assets.getFalseKnightStunnedAnim();
                loops = true;
                break;

            case STUN_RECOVER:
                anim = Assets.getFalseKnightStunRecoverAnim();
                loops = false;
                break;

            case DEAD:
                anim = Assets.getFalseKnightDeathAnim();
                loops = false;
                break;

            default:
                return;
        }

        if (falseKnight.isInvincible()) {
            float blink = (float) Math.sin(falseKnight.getHurtTimer() * 30f);
            batch.setColor(1f, 1f, 1f, blink > 0f ? 1f : 0.2f);
        }

        TextureRegion frame = anim.getKeyFrame(stateTime, loops);

        boolean shouldFlip = falseKnight.isFacingRight();
        if (frame.isFlipX() != shouldFlip) {
            frame.flip(true, false);
        }

        batch.draw(frame, falseKnight.getPosition().x, falseKnight.getPosition().y, FalseKnightConstants.WIDTH, FalseKnightConstants.HEIGHT);

        batch.setColor(1f, 1f, 1f, 1f);

        if (falseKnight.isShockwaveActive()) {
            renderShockwave(batch, falseKnight);
        }
    }

    private void renderShockwave(SpriteBatch batch, FalseKnight falseKnight) {
        Animation<TextureRegion> waveAnim = Assets.getFalseKnightShockwaveAnim();
        TextureRegion frame = waveAnim.getKeyFrame(falseKnight.getShockwaveTimer(), true);

        boolean shouldFlip = falseKnight.getShockwaveDirection() < 0f;
        if (frame.isFlipX() != shouldFlip) {
            frame.flip(true, false);
        }

        float w = FalseKnightConstants.SHOCKWAVE_WIDTH;
        float h = FalseKnightConstants.SHOCKWAVE_HEIGHT;
        float x = falseKnight.getShockwaveDirection() > 0 ? falseKnight.getShockwaveX() : falseKnight.getShockwaveX() - w;
        float y = falseKnight.getShockwaveOriginY();

        batch.draw(frame, x, y + 4f , w, h);
    }
}
