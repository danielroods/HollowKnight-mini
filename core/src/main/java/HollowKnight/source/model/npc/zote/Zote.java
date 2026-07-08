package HollowKnight.source.model.npc.zote;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Zote {

    private final Vector2 position;
    private final Vector2 velocity;
    private final Rectangle bounds;
    private final Vector2 spawnPosition;

    private ZoteState state;
    private boolean facingRight;
    private boolean onGround;
    private float stateTimer;

    private boolean dialogueOpen;
    private boolean hasCompletedInitialDialogue;
    private String[] sessionLines;
    private int sessionLineIndex;
    private String currentLineText;
    private float typeTimer;

    public Zote(float x, float y) {
        spawnPosition = new Vector2(x, y);
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        bounds = new Rectangle(
            x + ZoteConstants.BOUNDS_OFFSET_X,
            y + ZoteConstants.BOUNDS_OFFSET_Y,
            ZoteConstants.BOUNDS_W,
            ZoteConstants.BOUNDS_H
        );

        state = ZoteState.IDLE;
        facingRight = false;
        onGround = false;
        stateTimer = 0f;

        dialogueOpen = false;
        hasCompletedInitialDialogue = false;
        sessionLines = new String[0];
        sessionLineIndex = 0;
        currentLineText = "";
        typeTimer = 0f;
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
        bounds.setPosition(x + ZoteConstants.BOUNDS_OFFSET_X, y + ZoteConstants.BOUNDS_OFFSET_Y);
    }

    public String getVisibleText() {
        int total = currentLineText.length();
        int visible = (int) (typeTimer / ZoteConstants.CHAR_REVEAL_INTERVAL);
        visible = Math.max(0, Math.min(total, visible));
        return currentLineText.substring(0, visible);
    }

    public boolean isLineFullyShown() {
        int total = currentLineText.length();
        int visible = (int) (typeTimer / ZoteConstants.CHAR_REVEAL_INTERVAL);
        return visible >= total;
    }

    public void revealFullLine() {
        int total = currentLineText.length();
        typeTimer = (total + 1) * ZoteConstants.CHAR_REVEAL_INTERVAL;
    }

    public Vector2 getPosition() { return position; }
    public Vector2 getVelocity() { return velocity; }
    public Rectangle getBounds() { return bounds; }
    public Vector2 getSpawnPosition() { return spawnPosition; }

    public ZoteState getState() { return state; }
    public void setState(ZoteState state) { this.state = state; }

    public boolean isFacingRight() { return facingRight; }
    public void setFacingRight(boolean facingRight) { this.facingRight = facingRight; }

    public boolean isOnGround() { return onGround; }
    public void setOnGround(boolean onGround) { this.onGround = onGround; }

    public float getStateTimer() { return stateTimer; }
    public void setStateTimer(float stateTimer) { this.stateTimer = stateTimer; }

    public boolean isDialogueOpen() { return dialogueOpen; }
    public void setDialogueOpen(boolean dialogueOpen) { this.dialogueOpen = dialogueOpen; }

    public boolean hasCompletedInitialDialogue() { return hasCompletedInitialDialogue; }
    public void setHasCompletedInitialDialogue(boolean v) { this.hasCompletedInitialDialogue = v; }

    public String[] getSessionLines() { return sessionLines; }
    public void setSessionLines(String[] sessionLines) {
        this.sessionLines = sessionLines == null ? new String[0] : sessionLines;
    }

    public int getSessionLineIndex() { return sessionLineIndex; }
    public void setSessionLineIndex(int sessionLineIndex) { this.sessionLineIndex = sessionLineIndex; }

    public String getCurrentLineText() { return currentLineText; }
    public void setCurrentLineText(String currentLineText) {
        this.currentLineText = currentLineText == null ? "" : currentLineText;
    }

    public float getTypeTimer() { return typeTimer; }
    public void setTypeTimer(float typeTimer) { this.typeTimer = typeTimer; }
}
