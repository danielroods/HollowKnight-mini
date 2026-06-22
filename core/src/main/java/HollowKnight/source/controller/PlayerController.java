package HollowKnight.source.controller;

import HollowKnight.source.model.player.Player;
import HollowKnight.source.model.player.PlayerState;

public class PlayerController {
    private static PlayerController instance;

    private final Player player;
    public float hurtTimer = 0f;
    private PlayerController() {
        player = Player.getInstance();
    }

    public static PlayerController getInstance() {
        if (instance == null)
            instance = new PlayerController();
        return instance;
    }

    public void moveLeft() {
        player.setVelocityX(-Player.SPEED);
        player.setFacingRight(false);
    }
    public void moveRight() {
        player.setVelocityX(Player.SPEED);
        player.setFacingRight(true);
    }
    public void stopHorizontal() {
        player.setVelocityX(0f);
    }

    public void jump() {
        if (player.isOnGround()) {
            player.setVelocityY(Player.JUMP_FORCE);
            player.setOnGround(false);
        }
    }

    public void takeDamage(int amount) {
        player.setHealth(Math.max(0, player.getHealth() - amount));
        if (player.getHealth() > 0)
            player.setState(PlayerState.HURT);
    }

}
