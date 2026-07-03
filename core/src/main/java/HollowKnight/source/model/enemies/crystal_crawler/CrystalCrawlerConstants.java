package HollowKnight.source.model.enemies.crystal_crawler;

public class CrystalCrawlerConstants {

    public static final float WIDTH = 80f;
    public static final float HEIGHT = 70f;
    public static final float BOUNDS_OFFSET_X = 15f;
    public static final float BOUNDS_OFFSET_Y = 10f;
    public static final float BOUNDS_W = 60f;
    public static final float BOUNDS_H = 50f;

    public static final int MAX_HEALTH = 2;
    public static final int DAMAGE_TO_PLAYER = 1;
    public static final float HURT_COOLDOWN = 0.2f;
    public static final float KNOCKBACK_SPEED_X = 330f;
    public static final float KNOCKBACK_SPEED_Y = 180f;
    public static final float KNOCKBACK_DURATION = 0.30f;
    public static final float PATROL_SPEED = 60f;
    public static final float GRAVITY = -850f;

    public static final int WALK_FRAMES = 4;
    public static final int DEATH_FRAMES = 5;

    public static final float WALK_FRAME_DUR = 0.12f;
    public static final float DEATH_FRAME_DUR = 0.08f;

    public static final float DEATH_ANIM_DUR = DEATH_FRAMES * DEATH_FRAME_DUR;
}
