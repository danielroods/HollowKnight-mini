package HollowKnight.source.view;

import HollowKnight.source.game_utils.Assets;
import HollowKnight.source.model.player.Player;
import HollowKnight.source.model.player.PlayerConstants;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

public class HUDRenderer implements Disposable {

    private static final float HEAD_X = 36f;
    private static final float HEAD_Y = 571f;
    private static final float HEAD_W = 210f;
    private static final float HEAD_H = 135f;

    private static final float PIP_X0 = 142f;
    private static final float PIP_Y = 605f;
    private static final float PIP_W = 70f;
    private static final float PIP_H = 90f;
    private static final float PIP_SPACING = 67f;

    private static final float ORB_X = 8f;
    private static final float ORB_Y = 533f;
    private static final float ORB_W = 175f;
    private static final float ORB_H = 188f;

    private static final float BREAK_DUR = 0.43f;
    private static final float REFILL_DUR = 0.63f;

    private final float[] breakAnimationTimePassed = new float[PlayerConstants.MAX_HEALTH];
    private final float[] refillAnimationTimePassed = new float[PlayerConstants.MAX_HEALTH];
    private int lastHealth = PlayerConstants.MAX_HEALTH;

    private float displayedSoul = 0f;

    private final GlyphLayout layout;

    public HUDRenderer() {
        layout = new GlyphLayout();
    }

    public void update(float delta) {
        Player player = Player.getInstance();
        int currentHealth = player.getHealth();

        if (currentHealth < lastHealth) {
            for (int i = currentHealth; i < lastHealth; i++)
                breakAnimationTimePassed[i] = 0f;
        }
        if (currentHealth > lastHealth) {
            for (int i = lastHealth; i < currentHealth; i++)
                refillAnimationTimePassed[i] = 0f;
        }
        lastHealth = currentHealth;

        for (int i = 0; i < PlayerConstants.MAX_HEALTH; i++) {
            if (breakAnimationTimePassed[i] >= 0 && breakAnimationTimePassed[i] < BREAK_DUR)
                breakAnimationTimePassed[i] += delta;
            if (refillAnimationTimePassed[i] >= 0 && refillAnimationTimePassed[i] < REFILL_DUR)
                refillAnimationTimePassed[i] += delta;
        }

        displayedSoul = MathUtils.lerp(displayedSoul, player.getSoul(), delta * 5f);
        if (Math.abs(displayedSoul - player.getSoul()) < 0.5f)
            displayedSoul = player.getSoul();
    }

    public void render(SpriteBatch batch, OrthographicCamera uiCamera, Player player) {
        batch.setProjectionMatrix(uiCamera.combined);

        batch.begin();

        drawHead(batch, player);
        drawPips(batch, player);
        drawSoulOrb(batch, player);

        batch.end();
    }

    private void drawHead(SpriteBatch batch, Player player) {
        int headState = Math.max(1, player.getHealth());
        batch.draw(Assets.getHealthBarTexture(headState), HEAD_X, HEAD_Y, HEAD_W, HEAD_H);
    }

    private void drawPips(SpriteBatch batch, Player player) {
        for (int i = 0; i < PlayerConstants.MAX_HEALTH; i++) {
            float x = PIP_X0 + i * PIP_SPACING;

            if (i < player.getHealth()) {
                if (refillAnimationTimePassed[i] >= 0 && refillAnimationTimePassed[i] < REFILL_DUR) {
                    TextureRegion frame = Assets.getHealthRefillAnim().getKeyFrame(refillAnimationTimePassed[i], false);
                    batch.draw(frame, x, PIP_Y, PIP_W, PIP_H);
                }
                else {
                    batch.draw(Assets.getFilledHealthTex(), x, PIP_Y, PIP_W, PIP_H);
                }
            }
            else {
                if (breakAnimationTimePassed[i] >= 0 && breakAnimationTimePassed[i] < BREAK_DUR) {
                    TextureRegion frame = Assets.getBreakHealthAnim().getKeyFrame(breakAnimationTimePassed[i], false);
                    batch.draw(frame, x, PIP_Y, PIP_W, PIP_H);
                }
                else {
                    batch.draw(Assets.getEmptyHealthTex(), x, PIP_Y, PIP_W, PIP_H);
                }
            }
        }
    }

    private void drawSoulOrb(SpriteBatch batch, Player player) {

        batch.draw(Assets.getSoulOrbFrame((int) displayedSoul), ORB_X, ORB_Y, ORB_W, ORB_H);

        float eyeOffset = ORB_W * 0.34f;
        batch.draw(Assets.getSoulOrbEye(), ORB_X + eyeOffset - 2.5f, ORB_Y + eyeOffset + 5f, 65f, 30f);
    }

    @Override
    public void dispose() {}
}
