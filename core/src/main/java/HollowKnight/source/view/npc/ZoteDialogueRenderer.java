package HollowKnight.source.view.npc;

import HollowKnight.source.controller.npc.ZoteController;
import HollowKnight.source.model.asset.Assets;
import HollowKnight.source.model.npc.zote.Zote;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;

public class ZoteDialogueRenderer implements Disposable {

    private static final float BOX_MARGIN_X = 90f;
    private static final float BOX_HEIGHT = 150f;
    private static final float BOX_BOTTOM_Y = 40f;
    private static final float TEXT_PADDING = 30f;
    private static final float BORDER_THICKNESS = 3f;

    private final Texture panelTexture;
    private final GlyphLayout layout = new GlyphLayout();

    public ZoteDialogueRenderer() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        panelTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    public void render(SpriteBatch batch, OrthographicCamera uiCamera, ZoteController zoteController) {
        if (zoteController == null || !zoteController.isDialogueOpen()) return;

        Zote talkingZote = zoteController.getTalkingZote();
        if (talkingZote == null) return;

        float worldW = uiCamera.viewportWidth;

        float boxX = BOX_MARGIN_X;
        float boxW = worldW - BOX_MARGIN_X * 2f;
        float boxY = BOX_BOTTOM_Y;
        float boxH = BOX_HEIGHT;

        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();

        batch.setColor(0.05f, 0.05f, 0.05f, 0.85f);
        batch.draw(panelTexture, boxX, boxY, boxW, boxH);

        batch.setColor(0.75f, 0.65f, 0.4f, 0.9f);
        batch.draw(panelTexture, boxX, boxY + boxH - BORDER_THICKNESS, boxW, BORDER_THICKNESS);
        batch.draw(panelTexture, boxX, boxY, boxW, BORDER_THICKNESS);
        batch.setColor(Color.WHITE);

        BitmapFont dialogueFont = Assets.getSkin().getFont("AchievementDescFont");
        dialogueFont.setColor(Color.WHITE);

        String visibleText = talkingZote.getVisibleText();
        layout.setText(dialogueFont, visibleText, Color.WHITE, boxW - TEXT_PADDING * 2f, Align.left, true);
        dialogueFont.draw(batch, layout, boxX + TEXT_PADDING, boxY + boxH - TEXT_PADDING);

        if (talkingZote.isLineFullyShown()) {
            String hint = "Press Enter to continue";
            GlyphLayout hintLayout = new GlyphLayout(dialogueFont, hint);
            dialogueFont.setColor(1f, 1f, 1f, 0.7f);
            dialogueFont.draw(batch, hintLayout, boxX + boxW - hintLayout.width - TEXT_PADDING, boxY + TEXT_PADDING * 0.6f + hintLayout.height);
            dialogueFont.setColor(Color.WHITE);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        panelTexture.dispose();
    }
}
