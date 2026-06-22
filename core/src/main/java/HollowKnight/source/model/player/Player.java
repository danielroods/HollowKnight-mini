package HollowKnight.source.model.player;

import HollowKnight.source.game_utils.Assets;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private static Player instance;

    public static final float WIDTH = 190f;
    public static final float HEIGHT = 100f;
    public static final float SPEED = 210f;
    public static final float JUMP_FORCE = 520f;
    public static final float GRAVITY = -950f;
    public static final int MAX_HEALTH = 5;
    public static final float ATTACK_DUR = 0.30f;
    public static final float HURT_COOLDOWN = 1.0f;

    private final Vector2 position;
    private final Vector2 velocity;
    private final Rectangle bounds;

    private PlayerState state;

    private int health;

    private boolean facingRight;
    private boolean onGround;
    private boolean isAttacking;
    private float attackTimer;

    private Player(float x, float y) {
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        bounds = new Rectangle(x + 85, y, WIDTH - 170, HEIGHT - 48);
        state = PlayerState.IDLE;
        facingRight = true;
        onGround = false;
        health = MAX_HEALTH;
    }

    public static Player getInstance() {
        Vector2 startSpawn = Assets.getSpawnPosition("game_start_spawn");
        if (instance == null)
            instance = new Player(startSpawn.x, startSpawn.y);
        return instance;
    }

    public void update(float delta) {
        velocity.y += GRAVITY * delta;

        position.x += velocity.x * delta;
        position.y += velocity.y * delta;

        bounds.setPosition(position.x + 85, position.y);

        if (!isAlive()) {
            state = PlayerState.DEAD;
        }
        else if (isAttacking) {
            state = PlayerState.ATTACK;
            attackTimer -= delta;
            if (attackTimer <= 0)
                isAttacking = false;
        }
        else if (!onGround) {
            state = velocity.y > 0 ? PlayerState.JUMP : PlayerState.FALL;
        }
        else if (Math.abs(velocity.x) > 0f) {
            state = PlayerState.RUN;
        }
        else {
            state = PlayerState.IDLE;
        }
    }

    public void setVelocityX(float velocityX) { velocity.x = velocityX;}
    public void setVelocityY(float velocityY) { velocity.y = velocityY;}
    public void setFacingRight(boolean facingRight) { this.facingRight = facingRight;}
    public void setHealth(int health) { this.health = health;}
    public void setState(PlayerState state) { this.state = state;}
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
        if (onGround) velocity.y = 0;
    }
    public void setPosition(float x, float y) {
        position.set(x, y);
        bounds.setPosition(x + 85, y);
    }

    public Vector2 getPosition() { return position; }
    public Vector2 getVelocity() { return velocity; }
    public Rectangle getBounds() { return bounds; }
    public PlayerState getState() { return state; }
    public boolean isFacingRight() { return facingRight; }
    public boolean isOnGround() { return onGround; }
    public int getHealth() { return health; }
    public boolean isAlive() { return health > 0; }
}
