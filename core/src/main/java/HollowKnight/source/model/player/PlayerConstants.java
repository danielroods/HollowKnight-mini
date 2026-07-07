package HollowKnight.source.model.player;

public class PlayerConstants {
    public static final float WIDTH = 190f;
    public static final float HEIGHT = 100f;
    public static final float SPEED = 230f;
    public static final float JUMP_FORCE = 460f;
    public static final float GRAVITY= -950f;
    public static final float ATTACK_DUR = 0.25f;
    public static final float HITBOX_ACTIVE_END = ATTACK_DUR * 0.85f;
    public static final float HITBOX_ACTIVE_START = ATTACK_DUR * 0.20f;
    public static final float HURT_COOLDOWN = 1.5f;
    public static final int MAX_HEALTH = 8;
    public static final int MAX_SOUL = 99;
    public static final int SOUL_GAIN_PER_HIT = 5;
    public static final int SOUL_HEAL_COST = 33;
    public static final float FOCUS_DURATION = 1.75f;
    public static final float HEAL_ANIM_DUR = 0.80f;
    public static final float DEATH_ANIM_DUR = 1.2f;
    public static final float KNOCKBACK_DURATION = 0.40f;
    public static final float KNOCKBACK_VELOCITY_X = 320f;
    public static final float KNOCKBACK_VELOCITY_VY = 200f;
    public static final float DASH_SPEED = 730f;
    public static final float DASH_DURATION = 0.38f;
    public static final float DASH_COOLDOWN = 0.50f;
    public static final int MAX_DASH_IN_AIR = 2;
    public static final float WALL_SLIDE_SPEED = -120f;
    public static final float WALL_JUMP_SPEED_X = 250f;
    public static final float WALL_JUMP_LOCK_DURATION = 0.19f;
}
