package HollowKnight.source.model.enemies.false_knight;

public class FalseKnightConstants {
    public static final float WIDTH = 380f;
    public static final float HEIGHT = 290f;
    public static final float RIGHT_BOUNDS_OFFSET_X = 95f;
    public static final float LEFT_BOUNDS_OFFSET_X = 145f;
    public static final float BOUNDS_OFFSET_Y = 15f;
    public static final float BOUNDS_W = 140f;
    public static final float BOUNDS_H = 135f;
    public static final float INNER_HITBOX_W = 65f;
    public static final float INNER_HITBOX_H = 55f;

    public static final int MAX_HEALTH = 20;
    public static final int STUN_HP_THRESHOLD = MAX_HEALTH / 2;
    public static final float HURT_COOLDOWN = 0.45f;
    public static final float GRAVITY = -1450f;
    public static final float SPEED_MULTIPLIER_PHASE2 = 1.35f;
    public static final float ANIM_SPEED_MULTIPLIER_PHASE2 = 1.25f;
    public static final float DECISION_DELAY_PHASE1 = 0.50f;
    public static final float DECISION_DELAY_PHASE2 = 0.35f;

    public static final float CLOSE_RANGE = 140f;
    public static final float FAR_RANGE = 650f;

    public static final int MACE_SLAM_DAMAGE = 1;
    public static final float MACE_SLAM_HITBOX_W = 110f;
    public static final float MACE_SLAM_HITBOX_H = 120f;
    private static final float MACE_SLAM_TRIGGER_FRACTION = 0.99f;

    public static final float RUN_SPEED = 330f;
    public static final float RUN_MAX_DURATION = 3f;

    public static final float OFFENSIVE_JUMP_VX = 300f;
    public static final float OFFENSIVE_JUMP_VY = 550f;
    public static final float DEFENSIVE_JUMP_VX = 420f;
    public static final float DEFENSIVE_JUMP_VY = 420f;
    public static final float EVADE_TRIGGER_DISTANCE = 160f;
    public static final float SHOCKWAVE_JUMP_VX = 70f;
    public static final float SHOCKWAVE_JUMP_VY = 605f;
    public static final float SHOCKWAVE_BASE_SPEED = 450f;
    public static final float SHOCKWAVE_ACCEL = 160f;
    public static final float SHOCKWAVE_MAX_TRAVEL = 1000f;
    public static final float SHOCKWAVE_WIDTH = 90f;
    public static final float SHOCKWAVE_HEIGHT = 125f;
    public static final int SHOCKWAVE_SLAM_DAMAGE = 2;

    public static final float DEFENSIVE_JUMP_BASE_WEIGHT = 2f;
    public static final float DEFENSIVE_JUMP_CLOSE_WEIGHT = 20f;
    public static final float OFFENSIVE_JUMP_BASE_WEIGHT = 20f;
    public static final float OFFENSIVE_JUMP_RANGE_WEIGHT = 10f;
    public static final float MACE_SLAM_CLOSE_WEIGHT = 90f;
    public static final float MACE_SLAM_FAR_WEIGHT = 0f;
    public static final float CHARGE_RUN_FAR_WEIGHT = 95f;
    public static final float CHARGE_RUN_CLOSE_WEIGHT = 0f;
    public static final float SHOCKWAVE_SLAM_WEIGHT = 40f;

    public static final float FACING_DEADZONE = 20f;
    public static final float STUN_DURATION = 3.5f;
    public static final float SHAKE_DURATION_LAND = 0.22f;
    public static final float SHAKE_MAGNITUDE_LAND = 6f;
    public static final float SHAKE_DURATION_HEAVY = 0.35f;
    public static final float SHAKE_MAGNITUDE_HEAVY = 13f;

    public static final int ATTACK_RECOVER_FRAMES = 5;
    public static final int ATTACK_FRAMES = 9;
    public static final int DEATH_FRAMES = 13;
    public static final int IDLE_FRAMES = 5;
    public static final int JUMP_ATTACK_FRAMES = 8;
    public static final int JUMP_FRAMES = 4;
    public static final int LAND_FRAMES = 5;
    public static final int RUN_FRAMES = 5;
    public static final int STUN_RECOVER_FRAMES = 6;
    public static final int STUNNED_FRAMES = 5;
    public static final int SHOCKWAVE_EFFECT_FRAMES = 17;

    public static final float ATTACK_RECOVER_FRAME_DUR = 0.09f;
    public static final float ATTACK_FRAME_DUR = 0.065f;
    public static final float DEATH_FRAME_DUR = 0.09f;
    public static final float IDLE_FRAME_DUR = 0.11f;
    public static final float JUMP_ATTACK_FRAME_DUR = 0.18f;
    public static final float JUMP_FRAME_DUR = 0.12f;
    public static final float LAND_FRAME_DUR = 0.08f;
    public static final float RUN_FRAME_DUR = 0.07f;
    public static final float STUN_RECOVER_FRAME_DUR = 0.12f;
    public static final float STUNNED_FRAME_DUR = 0.20f;
    public static final float SHOCKWAVE_EFFECT_FRAME_DUR = 0.045f;

    public static final float ATTACK_RECOVER_ANIM_DUR = ATTACK_RECOVER_FRAMES * ATTACK_RECOVER_FRAME_DUR;
    public static final float ATTACK_ANIM_DUR = ATTACK_FRAMES * ATTACK_FRAME_DUR;
    public static final float DEATH_ANIM_DUR = DEATH_FRAMES * DEATH_FRAME_DUR;
    public static final float LAND_ANIM_DUR = LAND_FRAMES * LAND_FRAME_DUR;
    public static final float STUN_RECOVER_ANIM_DUR = STUN_RECOVER_FRAMES * STUN_RECOVER_FRAME_DUR;

    public static final float MACE_SLAM_TRIGGER_TIME = ATTACK_ANIM_DUR * MACE_SLAM_TRIGGER_FRACTION;
}
