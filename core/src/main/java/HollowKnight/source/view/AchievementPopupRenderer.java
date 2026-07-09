package HollowKnight.source.view;

import HollowKnight.source.model.achievement.AchievementManager;
import HollowKnight.source.model.achievement.AchievementType;
import HollowKnight.source.model.asset.Assets;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

public class AchievementPopupRenderer implements Disposable {

    private static final float SLIDE_IN_DUR = 0.35f;
    private static final float HOLD_DUR = 3f;
    private static final float FADE_OUT_DUR = 0.45f;
    private static final float TOTAL_DUR = SLIDE_IN_DUR + HOLD_DUR + FADE_OUT_DUR;

    private static final float BANNER_W = 640f;
    private static final float BANNER_H = 105f;
    private static final float TOP_MARGIN = 39f;
    private static final float ICON_PADDING = 12f;

    private AchievementType current;
    private float timer;

    public void update(float delta) {
        if (current == null) {
            current = AchievementManager.pollPendingPopup();
            timer = 0f;
            return;
        }

        timer += delta;
        if (timer >= TOTAL_DUR) {
            current = AchievementManager.pollPendingPopup();
            timer = 0f;
        }
    }

    public void render(SpriteBatch batch, OrthographicCamera uiCamera) {
        if (current == null) return;

        float alpha = computeAlpha();

        float slideProgress = Math.min(1f, timer / SLIDE_IN_DUR);
        float eased = Interpolation.swingOut.apply(slideProgress);

        float screenW = uiCamera.viewportWidth;
        float screenH = uiCamera.viewportHeight;

        float x = (screenW - BANNER_W) / 2f;
        float restY = screenH - BANNER_H - TOP_MARGIN;
        float startY = screenH + 10f;
        float y = MathUtils.lerp(startY, restY, eased);

        BitmapFont titleFont = Assets.getSkin().getFont("HollowfontGlow");
        BitmapFont descFont = Assets.getSkin().getFont("AchievementDescFont");

        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();

        batch.setColor(0.08f, 0.08f, 0.08f, alpha);
        batch.draw(Assets.getWhitePixel(), x, y, BANNER_W, BANNER_H);

        batch.setColor(1f, 1f, 1f, alpha);
        float iconSize = BANNER_H - ICON_PADDING * 2f;
        batch.draw(Assets.getAchievementIcon(current), x + ICON_PADDING, y + ICON_PADDING, iconSize, iconSize);

        titleFont.setColor(0.95f, 0.85f, 0.5f, alpha);
        titleFont.draw(batch, "Achievement Unlocked", x + BANNER_H, y + BANNER_H - 17f);

        descFont.setColor(1f, 1f, 1f, alpha);
        descFont.draw(batch, current.getTitle(), x + BANNER_H, y + BANNER_H - 55f);

        batch.end();

        titleFont.setColor(1f, 1f, 1f, 1f);
        descFont.setColor(1f, 1f, 1f, 1f);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    private float computeAlpha() {
        if (timer < SLIDE_IN_DUR)
            return MathUtils.clamp(timer / SLIDE_IN_DUR, 0f, 1f);
        if (timer < SLIDE_IN_DUR + HOLD_DUR)
            return 1f;
        float fadeT = timer - SLIDE_IN_DUR - HOLD_DUR;
        return 1f - MathUtils.clamp(fadeT / FADE_OUT_DUR, 0f, 1f);
    }

    @Override
    public void dispose() {
    }
}
