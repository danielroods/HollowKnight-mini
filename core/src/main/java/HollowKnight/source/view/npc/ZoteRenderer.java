package HollowKnight.source.view.npc;

import HollowKnight.source.controller.npc.ZoteController;
import HollowKnight.source.model.asset.Assets;
import HollowKnight.source.model.npc.zote.Zote;
import HollowKnight.source.model.npc.zote.ZoteConstants;
import HollowKnight.source.model.player.Player;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;

public class ZoteRenderer {
    private final GlyphLayout layout = new GlyphLayout();

    public void render(SpriteBatch batch, List<Zote> zoteList, ZoteController zoteController, Player player) {
        for (Zote zote : zoteList) {
            renderSprite(batch, zote);
        }

        if (zoteController != null && player != null) {
            Zote promptTarget = zoteController.getPromptTarget(player);
            if (promptTarget != null) {
                renderPrompt(batch, promptTarget);
            }
        }
    }

    private void renderSprite(SpriteBatch batch, Zote zote) {
        Animation<TextureRegion> anim;

        switch (zote.getState()) {
            case TALKING:
                anim = Assets.getZoteTalkAnim();
                break;
            case ANGRY:
                anim = Assets.getZoteAttackAnim();
                break;
            case IDLE:
            case RETURNING:
            default:
                anim = Assets.getZoteIdleAnim();
                break;
        }

        TextureRegion frame = anim.getKeyFrame(zote.getStateTimer(), true);

        boolean shouldFlip = zote.isFacingRight();
        if (frame.isFlipX() != shouldFlip) {
            frame.flip(true, false);
        }

        batch.draw(frame, zote.getPosition().x, zote.getPosition().y, ZoteConstants.WIDTH, ZoteConstants.HEIGHT);
    }

    private void renderPrompt(SpriteBatch batch, Zote zote) {
        BitmapFont font = Assets.getSkin().getFont("HollowfontGlow");

        float previousScaleX = font.getData().scaleX;
        float previousScaleY = font.getData().scaleY;
        font.getData().setScale(0.45f);

        layout.setText(font, "Press E to Talk");

        float x = zote.getPosition().x + ZoteConstants.WIDTH / 2f - layout.width / 2f;
        float y = zote.getPosition().y + ZoteConstants.HEIGHT + 10f;

        font.setColor(Color.WHITE);
        font.draw(batch, layout, x, y);

        font.getData().setScale(previousScaleX, previousScaleY);
    }
}
