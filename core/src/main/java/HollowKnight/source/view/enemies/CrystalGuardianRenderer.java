package HollowKnight.source.view.enemies;

import HollowKnight.source.model.asset.Assets;
import HollowKnight.source.model.enemies.crystal_guardian.CrystalGuardian;
import HollowKnight.source.model.enemies.crystal_guardian.CrystalGuardianConstants;
import HollowKnight.source.model.enemies.crystal_guardian.CrystalGuardianState;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;

public class CrystalGuardianRenderer {

    public void render(SpriteBatch batch, List<CrystalGuardian> guardianList) {
        for (CrystalGuardian guardian : guardianList) {
            renderOne(batch, guardian);
        }
    }

    private void renderOne(SpriteBatch batch, CrystalGuardian guardian) {
        CrystalGuardianState state = guardian.getState();

        Animation<TextureRegion> anim;
        float stateTime;
        boolean loops;

        switch (state) {
            case IDLE:
                anim = Assets.getCrystalGuardianIdleAnim();
                stateTime = guardian.getStateTimer();
                loops = true;
                break;

            case SHOOT:
                anim = Assets.getCrystalGuardianShootAnim();
                stateTime = guardian.getStateTimer();
                loops = false;
                break;

            case ENRAGED:
            case RETURN:
                anim = Assets.getCrystalGuardianRunAnim();
                stateTime = guardian.getStateTimer();
                loops = true;
                break;

            case EVADE:
                anim = Assets.getCrystalGuardianEvadeAnim();
                stateTime = guardian.getStateTimer();
                loops = false;
                break;

            case DEAD:
                anim = Assets.getCrystalGuardianDeathAnim();
                loops = false;
                stateTime = guardian.isOnGround() ? CrystalGuardianConstants.DEATH_ANIM_DUR : guardian.getStateTimer();
                break;

            default:
                return;
        }

        if (guardian.isInvincible()) {
            float blink = (float) Math.sin(guardian.getHurtTimer() * 30f);
            batch.setColor(1f, 1f, 1f, blink > 0f ? 1f : 0.2f);
        }

        TextureRegion frame = anim.getKeyFrame(stateTime, loops);

        boolean shouldFlip = guardian.isFacingRight();
        if (frame.isFlipX() != shouldFlip) {
            frame.flip(true, false);
        }

        batch.draw(frame, guardian.getPosition().x, guardian.getPosition().y, CrystalGuardianConstants.WIDTH, CrystalGuardianConstants.HEIGHT);

        batch.setColor(1f, 1f, 1f, 1f);

        if (guardian.isLaserActive()) {
            renderLaser(batch, guardian);
        }
    }

    private void renderLaser(SpriteBatch batch, CrystalGuardian guardian) {
        Animation<TextureRegion> laserAnim = Assets.getCrystalLaserAnim();
        TextureRegion frame = laserAnim.getKeyFrame(guardian.getLaserTimer(), false);

        boolean shouldFlip = guardian.isFacingRight();
        if (frame.isFlipX() != shouldFlip) {
            frame.flip(true, false);
        }

        float bx = guardian.getBounds().x;
        float by = guardian.getBounds().y;
        float bw = guardian.getBounds().width;
        float bh = guardian.getBounds().height;
        float range = CrystalGuardianConstants.LASER_RANGE;
        float height = CrystalGuardianConstants.LASER_HEIGHT;
        float y = by + bh / 2f - height / 2f;
        float x = guardian.isFacingRight() ? bx + bw : bx - range;

        batch.draw(frame, x, y, range, height);
    }
}
