package HollowKnight.source.view.spell;

import HollowKnight.source.model.asset.Assets;
import HollowKnight.source.model.spell.HowlingWraiths;
import HollowKnight.source.model.spell.HowlingWraithsConstants;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;

public class HowlingWraithsRenderer {

    public void render(SpriteBatch batch, List<HowlingWraiths> effects) {
        for (HowlingWraiths effect : effects) {
            renderOne(batch, effect);
        }
    }

    private void renderOne(SpriteBatch batch, HowlingWraiths effect) {
        Animation<TextureRegion> anim = Assets.getHowlingWraithsAnim();
        if (anim == null) return;

        TextureRegion frame = anim.getKeyFrame(effect.getLifeTimer(), false);

        batch.draw(frame, effect.getPosition().x, effect.getPosition().y, HowlingWraithsConstants.WIDTH, HowlingWraithsConstants.HEIGHT);
    }
}
