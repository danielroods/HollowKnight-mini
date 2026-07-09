package HollowKnight.source.view.spell;

import HollowKnight.source.model.asset.Assets;
import HollowKnight.source.model.spell.VengefulSpirit;
import HollowKnight.source.model.spell.VengefulSpiritConstants;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;

public class VengefulSpiritRenderer {

    public void render(SpriteBatch batch, List<VengefulSpirit> projectiles) {
        for (VengefulSpirit spirit : projectiles) {
            renderOne(batch, spirit);
        }
    }

    private void renderOne(SpriteBatch batch, VengefulSpirit spirit) {
        Animation<TextureRegion> anim = Assets.getVengefulSpiritAnim();
        if (anim == null) return;

        TextureRegion frame = anim.getKeyFrame(spirit.getLifeTimer(), true);

        boolean shouldFlip = spirit.isFacingRight();
        if (frame.isFlipX() == shouldFlip) {
            frame.flip(true, false);
        }

        batch.draw(frame, spirit.getPosition().x, spirit.getPosition().y + 10, VengefulSpiritConstants.WIDTH, VengefulSpiritConstants.HEIGHT);
    }
}
