package HollowKnight.source.model.enemies.crystal_guardian;

public class CrystalGuardianConstants {

    public static final float WIDTH = 110f;
    public static final float HEIGHT = 110f;
    public static final float BOUNDS_OFFSET_X = 35f;
    public static final float BOUNDS_OFFSET_Y = 10f;
    public static final float BOUNDS_W = 60f;
    public static final float BOUNDS_H = 85f;

    public static final int MAX_HEALTH = 2;
    public static final float HURT_COOLDOWN = 0.4f;
    public static final float KNOCKBACK_SPEED_X = 300f;
    public static final float KNOCKBACK_SPEED_Y = 140f;
    public static final float KNOCKBACK_DURATION = 0.35f;
    public static final float GRAVITY = -850f;
    public static final float DETECTION_W = 400f;
    public static final float DETECTION_H = 30f;
    public static final float CHASE_SPEED = 150f;
    public static final float ENRAGE_DURATION = 2.5f;
    public static final float RETURN_SPEED = 110f;
    public static final float RETURN_ARRIVE_EPSILON = 6f;

    public static final float EVADE_TRIGGER_DISTANCE = 90f;
    public static final float EVADE_SPEED = 180f;
    public static final float EVADE_COOLDOWN = 2f;
    public static final float LASER_FIRE_DELAY = 0.55f;
    public static final float LASER_RANGE = 1250f;
    public static final float LASER_HEIGHT = 40f;
    public static final float LASER_ACTIVE_DUR = 0.18f;

    public static final int IDLE_FRAMES = 5;
    public static final int RUN_FRAMES = 6;
    public static final int SHOOT_FRAMES = 7;
    public static final int EVADE_FRAMES = 7;
    public static final int DEATH_FRAMES = 6;
    public static final int LASER_EFFECT_FRAMES = 16;

    public static final float IDLE_FRAME_DUR = 0.15f;
    public static final float RUN_FRAME_DUR = 0.09f;
    public static final float SHOOT_FRAME_DUR = 0.09f;
    public static final float EVADE_FRAME_DUR = 0.06f;
    public static final float DEATH_FRAME_DUR = 0.12f;
    public static final float LASER_EFFECT_FRAME_DUR = 0.035f;

    public static final float SHOOT_ANIM_DUR = SHOOT_FRAMES * SHOOT_FRAME_DUR;
    public static final float EVADE_ANIM_DUR = EVADE_FRAMES * EVADE_FRAME_DUR;
    public static final float DEATH_ANIM_DUR = DEATH_FRAMES * DEATH_FRAME_DUR;
    public static final float LASER_EFFECT_DUR = LASER_EFFECT_FRAMES * LASER_EFFECT_FRAME_DUR;
}
