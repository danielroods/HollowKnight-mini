package HollowKnight.source.model.enemies.mossfly;

public class MossflyConstants {
    public static final float WIDTH = 80f;
    public static final float HEIGHT = 80f;
    public static final float BOUNDS_OFFSET_X = 15f;
    public static final float BOUNDS_OFFSET_Y = 15f;
    public static final float BOUNDS_W = 50f;
    public static final float BOUNDS_H = 50f;

    public static final int MAX_HEALTH = 3;
    public static final int DAMAGE_TO_PLAYER = 1;
    public static final float HURT_COOLDOWN = 0.4f;
    public static final float KNOCKBACK_SPEED_X = 210f;
    public static final float KNOCKBACK_SPEED_Y = 210f;
    public static final float KNOCKBACK_DURATION = 0.30f;
    public static final float DETECTION_RADIUS = 300f;
    public static final float FLY_SPEED = 170f;
    public static final float GRAVITY = -580f;

    public static final int SHAKE_FRAMES = 3;
    public static final int APPEAR_FRAMES = 6;
    public static final int FLY_FRAMES = 4;
    public static final int DEATH_FRAMES = 4;

    public static final float SHAKE_FRAME_DUR = 0.4f;
    public static final float APPEAR_FRAME_DUR = 0.1f;
    public static final float FLY_FRAME_DUR = 0.08f;
    public static final float DEATH_FRAME_DUR = 0.1f;

    public static final float APPEAR_ANIM_DUR = APPEAR_FRAMES * APPEAR_FRAME_DUR;
    public static final float DEATH_ANIM_DUR = DEATH_FRAMES * DEATH_FRAME_DUR;
}
