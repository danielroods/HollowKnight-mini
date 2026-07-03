package HollowKnight.source.view.enemies;

import HollowKnight.source.model.asset.Assets;
import HollowKnight.source.model.enemies.crystal_crawler.CrystalCrawler;
import HollowKnight.source.model.enemies.crystal_crawler.CrystalCrawlerConstants;
import HollowKnight.source.model.enemies.crystal_crawler.CrystalCrawlerState;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;

public class CrystalCrawlerRenderer {

    public void render(SpriteBatch batch, List<CrystalCrawler> crawlerList) {
        for (CrystalCrawler crawler : crawlerList) {
            renderOne(batch, crawler);
        }
    }

    private void renderOne(SpriteBatch batch, CrystalCrawler crawler) {
        CrystalCrawlerState state = crawler.getState();

        Animation<TextureRegion> anim;
        float stateTime;
        boolean loops;

        switch (state) {
            case WALK:
                anim = Assets.getCrystalCrawlerWalkAnim();
                stateTime = crawler.getStateTimer();
                loops = true;
                break;

            case DEAD:
                anim = Assets.getCrystalCrawlerDeathAnim();
                loops = false;
                stateTime = crawler.isOnGround() ? CrystalCrawlerConstants.DEATH_ANIM_DUR : crawler.getStateTimer();
                break;

            default:
                return;
        }

        if (crawler.isInvincible()) {
            float blink = (float) Math.sin(crawler.getHurtTimer() * 30f);
            batch.setColor(1f, 1f, 1f, blink > 0f ? 1f : 0.2f);
        }

        TextureRegion frame = anim.getKeyFrame(stateTime, loops);

        boolean shouldFlip = crawler.isFacingRight();
        if (frame.isFlipX() != shouldFlip) {
            frame.flip(true, false);
        }

        batch.draw(frame, crawler.getPosition().x, crawler.getPosition().y, CrystalCrawlerConstants.WIDTH, CrystalCrawlerConstants.HEIGHT);

        batch.setColor(1f, 1f, 1f, 1f);
    }
}
