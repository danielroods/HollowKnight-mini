package HollowKnight.source.controller.npc;

import HollowKnight.source.controller.PlayerController;
import HollowKnight.source.data.GameSettings;
import HollowKnight.source.model.asset.Assets;
import HollowKnight.source.model.npc.zote.Zote;
import HollowKnight.source.model.npc.zote.ZoteConstants;
import HollowKnight.source.model.npc.zote.ZoteDialogueLines;
import HollowKnight.source.model.npc.zote.ZoteState;
import HollowKnight.source.model.player.Player;
import HollowKnight.source.model.spell.VengefulSpirit;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

public class ZoteController {
    private final List<Zote> zoteList;
    private final PlayerController playerController;

    public ZoteController(List<Zote> zoteList) {
        this.zoteList = zoteList;
        this.playerController = PlayerController.getInstance();
    }

    public void update(float delta, Player player, MapLayer logicLayer) {
        for (Zote zote : zoteList) {
            updateOne(delta, zote, player, logicLayer);
        }
    }

    private void updateOne(float delta, Zote zote, Player player, MapLayer logicLayer) {
        zote.setStateTimer(zote.getStateTimer() + delta);

        switch (zote.getState()) {
            case IDLE:
                zote.getVelocity().x = 0f;
                break;

            case TALKING:
                zote.getVelocity().x = 0f;
                if (zote.isDialogueOpen()) {
                    zote.setTypeTimer(zote.getTypeTimer() + delta);
                }
                break;

            case ANGRY:
                updateAngry(zote, player);
                if (zote.getStateTimer() >= ZoteConstants.ANGER_DURATION) {
                    enterState(zote, ZoteState.RETURNING);
                }
                break;

            case RETURNING:
                updateReturning(zote);
                break;
        }

        if (!zote.isOnGround()) {
            zote.getVelocity().y += ZoteConstants.GRAVITY * delta;
        }
        else if (zote.getVelocity().y < 0f) {
            zote.getVelocity().y = 0f;
        }

        zote.setPosition(
            zote.getPosition().x + zote.getVelocity().x * delta,
            zote.getPosition().y + zote.getVelocity().y * delta
        );

        resolveCollisions(zote, logicLayer);
    }

    public void checkSwordHits(Rectangle swordHitbox, Player player) {
        for (Zote zote : zoteList) {
            if (zote.getState() == ZoteState.TALKING || zote.getState() == ZoteState.ANGRY) continue;
            if (!Intersector.overlaps(swordHitbox, zote.getBounds())) continue;

            playAngryVoiceSfx();
            enterState(zote, ZoteState.ANGRY);
        }
    }

    public void checkVengefulSpiritHit(VengefulSpirit spirit, Player player) {
        for (Zote zote : zoteList) {
            if (zote.getState() == ZoteState.TALKING || zote.getState() == ZoteState.ANGRY) continue;
            if (spirit.hasHit(zote)) continue;
            if (!Intersector.overlaps(spirit.getBounds(), zote.getBounds())) continue;

            spirit.markHit(zote);
            playAngryVoiceSfx();
            enterState(zote, ZoteState.ANGRY);
        }
    }

    public boolean tryInteract(Player player) {
        if (isDialogueOpen())
            return false;

        for (Zote zote : zoteList) {
            if (zote.getState() != ZoteState.IDLE) continue;
            if (!isPlayerInRange(zote, player)) continue;

            startDialogue(zote);
            return true;
        }
        return false;
    }

    public void controlDialogue() {
        Zote zote = getTalkingZote();
        if (zote == null) return;

        if (!zote.isLineFullyShown()) {
            zote.revealFullLine();
            return;
        }

        String[] lines = zote.getSessionLines();
        int nextIndex = zote.getSessionLineIndex() + 1;

        if (nextIndex < lines.length) {
            zote.setSessionLineIndex(nextIndex);
            showLine(zote, lines[nextIndex]);
        }
        else {
            closeDialogue(zote);
        }
    }

    public boolean isDialogueOpen() {
        return getTalkingZote() != null;
    }

    public Zote getTalkingZote() {
        for (Zote zote : zoteList) {
            if (zote.isDialogueOpen()) return zote;
        }
        return null;
    }

    public Zote getPromptTarget(Player player) {
        for (Zote zote : zoteList) {
            if (zote.getState() != ZoteState.IDLE)
                continue;
            if (isPlayerInRange(zote, player))
                return zote;
        }
        return null;
    }

    public List<Zote> getZoteList() { return zoteList; }

    private void updateAngry(Zote zote, Player player) {
        float zoteCX = zote.getBounds().x + zote.getBounds().width / 2f;
        float playerCX = player.getBounds().x + player.getBounds().width / 2f;
        float dx = playerCX - zoteCX;
        boolean shouldStop = dx <= 23f && dx >= -28f;
        boolean goRight = dx > 0f;
        zote.setFacingRight(goRight);
        if (!shouldStop) {
            zote.getVelocity().x = (goRight ? 1f : -1f) * ZoteConstants.ANGER_CHASE_SPEED;
        }
        else {
            zote.getVelocity().x = 0f;
        }
    }

    private void updateReturning(Zote zote) {
        float dx = zote.getSpawnPosition().x - zote.getPosition().x;
        if (Math.abs(dx) <= ZoteConstants.REACH_SPAWN_EPSILON) {
            zote.getVelocity().x = 0f;
            zote.setPosition(zote.getSpawnPosition().x, zote.getPosition().y);
            enterState(zote, ZoteState.IDLE);
            return;
        }
        boolean goRight = dx > 0f;
        zote.setFacingRight(goRight);
        zote.getVelocity().x = (goRight ? 1f : -1f) * ZoteConstants.RETURN_SPEED;
    }

    private void resolveCollisions(Zote zote, MapLayer logicLayer) {
        if (logicLayer == null) return;

        boolean grounded = false;
        Rectangle bounds = new Rectangle(zote.getBounds());

        for (MapObject obj : logicLayer.getObjects()) {
            if (!(obj instanceof RectangleMapObject)) continue;
            if (!isSolid(obj.getName())) continue;

            Rectangle tile = ((RectangleMapObject) obj).getRectangle();
            Rectangle overlap = new Rectangle();
            if (!Intersector.intersectRectangles(bounds, tile, overlap)) continue;

            if (overlap.width < overlap.height) {
                boolean hitRight = bounds.x < tile.x;
                float push = hitRight ? -overlap.width : overlap.width;
                zote.setPosition(zote.getPosition().x + push, zote.getPosition().y);
                zote.getVelocity().x = 0f;
            }
            else {
                float bodyMidY = bounds.y + bounds.height / 2f;
                float tileMidY = tile.y + tile.height / 2f;

                if (bodyMidY > tileMidY) {
                    if (zote.getVelocity().y <= 0f) {
                        float landedY = tile.y + tile.height - ZoteConstants.BOUNDS_OFFSET_Y;
                        zote.setPosition(zote.getPosition().x, landedY);
                        zote.getVelocity().y = 0f;
                        grounded = true;
                    }
                }
                else {
                    if (zote.getVelocity().y > 0f) {
                        float ceilY = tile.y - ZoteConstants.BOUNDS_H - ZoteConstants.BOUNDS_OFFSET_Y;
                        zote.setPosition(zote.getPosition().x, ceilY);
                        zote.getVelocity().y = 0f;
                    }
                }
            }
            bounds.set(zote.getBounds());
        }

        zote.setOnGround(grounded);
    }

    private boolean isSolid(String name) {
        if (name == null) return false;
        return name.equals("platform") || name.equals("wall") || name.equals("ceiling");
    }

    private void startDialogue(Zote zote) {
        String[] lines = zote.hasCompletedInitialDialogue() ? new String[]{ randomSentences() } : ZoteDialogueLines.INITIAL_LINES;

        zote.setSessionLines(lines);
        zote.setSessionLineIndex(0);
        zote.setDialogueOpen(true);
        enterState(zote, ZoteState.TALKING);
        showLine(zote, lines[0]);
    }

    private void showLine(Zote zote, String line) {
        zote.setCurrentLineText(line);
        zote.setTypeTimer(0f);
        playRandomVoiceSfx();
    }

    private void closeDialogue(Zote zote) {
        zote.setDialogueOpen(false);
        if (!zote.hasCompletedInitialDialogue()) {
            zote.setHasCompletedInitialDialogue(true);
        }
        zote.setSessionLines(new String[0]);
        zote.setSessionLineIndex(0);
        zote.setCurrentLineText("");
        zote.setTypeTimer(0f);
        enterState(zote, ZoteState.IDLE);
    }

    private String randomSentences() {
        String[] sentences = ZoteDialogueLines.SENTENCES_OF_ZOTE;
        return sentences[MathUtils.random(sentences.length - 1)];
    }

    private void playRandomVoiceSfx() {
        Sound[] sfx = Assets.getZoteVoiceSfx();
        if (sfx == null || sfx.length == 0) return;
        int idx = MathUtils.random(sfx.length - 1);
        sfx[idx].play(GameSettings.getVolume());
    }

    private void playAngryVoiceSfx() {
        Sound sfx = Assets.getZoteAngryVoiceSfx();
        if (sfx == null) return;
        sfx.play(GameSettings.getVolume());
    }

    private boolean isPlayerInRange(Zote zote, Player player) {
        float zoteCX = zote.getBounds().x + zote.getBounds().width / 2f;
        float zoteCY = zote.getBounds().y + zote.getBounds().height / 2f;
        float playerCX = player.getBounds().x + player.getBounds().width / 2f;
        float playerCY = player.getBounds().y + player.getBounds().height / 2f;
        float dx = playerCX - zoteCX;
        float dy = playerCY - zoteCY;
        float r = ZoteConstants.INTERACTION_RANGE;
        return (dx * dx + dy * dy) <= (r * r);
    }

    private void enterState(Zote zote, ZoteState newState) {
        zote.setState(newState);
        zote.setStateTimer(0f);
    }
}
