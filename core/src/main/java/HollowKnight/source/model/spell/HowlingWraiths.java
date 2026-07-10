package HollowKnight.source.model.spell;

import HollowKnight.source.model.player.Player;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class HowlingWraiths {

    private final Vector2 position;
    private final Rectangle bounds;

    private float lifeTimer;
    private int ticksFired;
    private boolean expired;

    public HowlingWraiths(Player player) {
        position = new Vector2(0f, 0f);
        bounds = new Rectangle(0f, 0f, HowlingWraithsConstants.WIDTH - 110f, HowlingWraithsConstants.HEIGHT - 70f);
        lifeTimer = 0f;
        ticksFired = 0;
        expired = false;
        followPlayer(player);
    }

    public void followPlayer(Player player) {
        float centerX = player.getBounds().x + player.getBounds().width / 2f;
        float bottomY = player.getBounds().y + player.getBounds().height;
        setPosition(centerX - HowlingWraithsConstants.WIDTH / 2f, bottomY - 10f);
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
        bounds.setPosition(x + 55f, y + 35f);
    }

    public Vector2 getPosition() { return position; }
    public Rectangle getBounds() { return bounds; }

    public float getLifeTimer() { return lifeTimer; }
    public void setLifeTimer(float lifeTimer) { this.lifeTimer = lifeTimer; }

    public int getTicksFired() { return ticksFired; }
    public void setTicksFired(int ticksFired) { this.ticksFired = ticksFired; }

    public boolean isExpired() { return expired; }
    public void setExpired(boolean expired) { this.expired = expired; }
}
