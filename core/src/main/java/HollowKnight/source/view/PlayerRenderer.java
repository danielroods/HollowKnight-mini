package HollowKnight.source.view;

import HollowKnight.source.model.player.Player;
import HollowKnight.source.model.player.PlayerState;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.EnumMap;

public class PlayerRenderer {

    private final EnumMap<PlayerState, Animation<TextureRegion>> animations;
    private float stateTime = 0f;

    public PlayerRenderer(EnumMap<PlayerState, Animation<TextureRegion>> animations) {
        this.animations = animations;
    }

    public void render(SpriteBatch batch, Player player, float delta) {
        stateTime += delta;

        Animation<TextureRegion> anim = animations.getOrDefault(player.getState(), animations.get(PlayerState.IDLE));
        TextureRegion frame = anim.getKeyFrame(stateTime, true);

        boolean shouldFlip = player.isFacingRight();
        if (frame.isFlipX() != shouldFlip) {
            frame.flip(true, false);
        }

        float x = player.getPosition().x;
        float y = player.getPosition().y;

        batch.draw(frame, x, y, Player.WIDTH, Player.HEIGHT);
    }
}
