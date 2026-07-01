package HollowKnight.source.view;

import HollowKnight.source.model.asset.Assets;
import HollowKnight.source.model.player.Player;
import HollowKnight.source.model.player.PlayerConstants;
import HollowKnight.source.model.player.PlayerState;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.EnumMap;

public class PlayerRenderer {

    private final EnumMap<PlayerState, Animation<TextureRegion>> animations;

    private float stateTime = 0f;
    private PlayerState lastState = null;

    public PlayerRenderer(EnumMap<PlayerState, Animation<TextureRegion>> animations) {
        this.animations = animations;
    }

    public void render(SpriteBatch batch, Player player, float delta) {
        PlayerState currentState = player.getState();

        if (lastState == null || currentState != lastState) {
            stateTime = 0f;
            lastState = currentState;
        }
        stateTime += delta;

        if (player.isInvincible() && player.isAlive()) {
            float blink = (float) Math.sin(player.getHurtTimer() * 30f);
            float alpha = blink > 0f ? 1f : 0.2f;
            batch.setColor(1f, 1f, 1f, alpha);
        }

        Animation<TextureRegion> playerAnim = animations.get(currentState);

        boolean doesLoop = !(currentState == PlayerState.ATTACK
            || currentState == PlayerState.HURT
            || currentState == PlayerState.HEAL
            || currentState == PlayerState.DEAD);

        TextureRegion frame = playerAnim.getKeyFrame(stateTime, doesLoop);

        boolean shouldFlip = player.isFacingRight();
        if (frame.isFlipX() != shouldFlip)
            frame.flip(true, false);

        float x = player.getPosition().x;
        float y = player.getPosition().y;

        batch.draw(frame, x, y, PlayerConstants.WIDTH, PlayerConstants.HEIGHT);

        if (player.isAttacking()) {
            float attackStateTime = PlayerConstants.ATTACK_DUR - player.getAttackTimer();
            Animation<TextureRegion> attackAnim;


            switch (player.getAttackDirection()) {
                case UP:
                    attackAnim = Assets.getAttackUpAnim();
                    break;
                case DOWN:
                    attackAnim = Assets.getAttackDownAnim();
                    break;
                case LEFT:
                case RIGHT:
                default:
                    attackAnim = Assets.getAttackHorizontalAnim();
                    break;
            }


            if (attackAnim != null) {
                TextureRegion slashFrame = attackAnim.getKeyFrame(attackStateTime, false);
                if (slashFrame.isFlipX() != shouldFlip) {
                    slashFrame.flip(true, false);
                }

                if (attackAnim.equals(Assets.getAttackUpAnim()))
                    batch.draw(slashFrame, x+56 , y+30 , PlayerConstants.WIDTH * 0.45f, PlayerConstants.HEIGHT * 0.75f);
                else if (attackAnim.equals(Assets.getAttackDownAnim()))
                    batch.draw(slashFrame, x+56 , y-32 , PlayerConstants.WIDTH * 0.4f, PlayerConstants.HEIGHT * 0.7f);
                else
                    batch.draw(slashFrame, x, y, PlayerConstants.WIDTH, PlayerConstants.HEIGHT);
            }
        }

        batch.setColor(1f, 1f, 1f, 1f);
    }
}
