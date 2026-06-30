package HollowKnight.source.game_utils;

import HollowKnight.source.controller.GameController;
import HollowKnight.source.model.achievement.AchievementType;
import HollowKnight.source.model.player.PlayerConstants;
import HollowKnight.source.model.player.PlayerState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PointMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;

import java.util.EnumMap;

public class Assets {

    private static final int IDLE_FRAMES = 9;
    private static final int RUN_FRAMES = 13;
    private static final int JUMP_FRAMES = 12;
    private static final int DOUBLE_JUMP_FRAMES = 8;
    private static final int FALL_FRAMES = 6;
    private static final int DEATH_FRAMES = 18;
    private static final int ATTACK_FRAMES = 5;
    private static final int ATTACK_EFFECT_FRAMES = 6;
    private static final int HURT_FRAMES = 12;
    private static final int FOCUS_FRAMES = 13;
    private static final int HEAL_FRAMES = 6;

    private static final int BREAK_HEALTH_FRAMES = 6;
    private static final int HEALTH_REFILL_FRAMES = 5;
    private static final int SOUL_ORB_FRAME_COUNT = 20;

    private static Skin skin;
    private static Texture lockIcon;
    private static EnumMap<AchievementType, Texture> achievementIcons;

    private static Texture idleSheet, runSheet, jumpSheet, doubleJumpSheet, fallSheet;
    private static Texture deathSheet, hurtSheet, focusSheet, healSheet, attackSheet;

    private static Texture attackHorizontalSheet, attackUpSheet, attackDownSheet;
    private static Animation<TextureRegion> attackHorizontalAnim, attackUpAnim, attackDownAnim;

    private static EnumMap<PlayerState, Animation<TextureRegion>> playerAnimations;

    private static Texture[] healthBarTextures;
    private static Texture filledHealthTex;
    private static Texture emptyHealthTex;
    private static Texture breakHealthSheet;
    private static Animation<TextureRegion> breakHealthAnim;
    private static Texture healthRefillSheet;
    private static Animation<TextureRegion> healthRefillAnim;
    private static Texture[] soulOrbFrames;
    private static Texture soulOrbEye;

    private static TiledMap[] maps;
    private static final int MAP_COUNT = 2;

    public static void load() {
        lockIcon = new Texture("icons/lock_icon.png");

        idleSheet = new Texture(Gdx.files.internal("player/Idle.png"));
        runSheet = new Texture(Gdx.files.internal("player/Run.png"));
        jumpSheet = new Texture(Gdx.files.internal("player/Jump.png"));
        doubleJumpSheet = new Texture(Gdx.files.internal("player/Double Jump.png"));
        fallSheet = new Texture(Gdx.files.internal("player/Fall.png"));
        deathSheet = new Texture(Gdx.files.internal("player/Death.png"));
        hurtSheet = new Texture(Gdx.files.internal("player/Idle Hurt.png"));
        focusSheet = new Texture(Gdx.files.internal("player/Focus.png"));
        healSheet = new Texture(Gdx.files.internal("player/Heal.png"));
        attackSheet = new Texture(Gdx.files.internal("player/Slash.png"));

        attackHorizontalSheet = new Texture(Gdx.files.internal("player/effects/SlashEffect.png"));
        attackUpSheet = new Texture(Gdx.files.internal("player/effects/UpSlashEffect.png"));
        attackDownSheet = new Texture(Gdx.files.internal("player/effects/DownSlashEffect.png"));

        achievementIcons = new EnumMap<>(AchievementType.class);
        for (AchievementType a : AchievementType.values())
            achievementIcons.put(a, new Texture(a.getIconPath()));

        loadPlayerAnimations();
        loadHUDAssets();
        loadMaps();
    }

    private static void loadPlayerAnimations() {
        playerAnimations = new EnumMap<>(PlayerState.class);

        playerAnimations.put(PlayerState.IDLE, makeAnim(1f / IDLE_FRAMES, idleSheet, IDLE_FRAMES, Animation.PlayMode.LOOP));
        playerAnimations.put(PlayerState.RUN, makeAnim(1f / RUN_FRAMES, runSheet, RUN_FRAMES, Animation.PlayMode.LOOP));
        playerAnimations.put(PlayerState.JUMP, makeAnim(1f / JUMP_FRAMES, jumpSheet, JUMP_FRAMES, Animation.PlayMode.NORMAL));
        playerAnimations.put(PlayerState.DOUBLE_JUMP, makeAnim(0.5f / DOUBLE_JUMP_FRAMES, doubleJumpSheet, DOUBLE_JUMP_FRAMES, Animation.PlayMode.NORMAL));
        playerAnimations.put(PlayerState.FALL, makeAnim(1f / FALL_FRAMES, fallSheet, FALL_FRAMES, Animation.PlayMode.LOOP));
        playerAnimations.put(PlayerState.DEAD, makeAnim(0.5f / DEATH_FRAMES, deathSheet, DEATH_FRAMES, Animation.PlayMode.NORMAL));
        playerAnimations.put(PlayerState.HURT, makeAnim(1f / HURT_FRAMES, hurtSheet, HURT_FRAMES, Animation.PlayMode.NORMAL));
        playerAnimations.put(PlayerState.FOCUS, makeAnim(PlayerConstants.FOCUS_DURATION / FOCUS_FRAMES,focusSheet, FOCUS_FRAMES, Animation.PlayMode.NORMAL));
        playerAnimations.put(PlayerState.HEAL, makeAnim(PlayerConstants.HEAL_ANIM_DUR / HEAL_FRAMES, healSheet, HEAL_FRAMES, Animation.PlayMode.NORMAL));
        playerAnimations.put(PlayerState.ATTACK, makeAnim(PlayerConstants.ATTACK_DUR / ATTACK_FRAMES, attackSheet, ATTACK_FRAMES, Animation.PlayMode.NORMAL));

        float attackFrameDur = PlayerConstants.ATTACK_DUR / ATTACK_EFFECT_FRAMES;
        attackHorizontalAnim = makeAnim(attackFrameDur, attackHorizontalSheet, ATTACK_EFFECT_FRAMES, Animation.PlayMode.NORMAL);
        attackUpAnim = makeAnim(attackFrameDur, attackUpSheet, ATTACK_EFFECT_FRAMES, Animation.PlayMode.NORMAL);
        attackDownAnim = makeAnim(attackFrameDur, attackDownSheet, ATTACK_EFFECT_FRAMES, Animation.PlayMode.NORMAL);
    }

    private static Animation<TextureRegion> makeAnim(float frameDur, Texture sheet, int frameCount, Animation.PlayMode mode) {
        int tileW = sheet.getWidth() / frameCount;
        int tileH = sheet.getHeight();
        TextureRegion[][] grid = TextureRegion.split(sheet, tileW, tileH);

        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < Math.min(frameCount, grid[0].length); i++)
            frames.add(grid[0][i]);

        return new Animation<>(frameDur, frames, mode);
    }

    private static void loadHUDAssets() {
        healthBarTextures = new Texture[5];
        for (int i = 0; i < 5; i++)
            healthBarTextures[i] = new Texture(Gdx.files.internal("hud/HealthBar" + (i + 1) + ".png"));

        filledHealthTex = new Texture(Gdx.files.internal("hud/FilledHealth.png"));
        emptyHealthTex = new Texture(Gdx.files.internal("hud/EmptyHealth.png"));

        breakHealthSheet = new Texture(Gdx.files.internal("hud/BreakHealth.png"));
        breakHealthAnim = makeAnim(0.06f, breakHealthSheet, BREAK_HEALTH_FRAMES, Animation.PlayMode.NORMAL);

        healthRefillSheet = new Texture(Gdx.files.internal("hud/HealthRefill.png"));
        healthRefillAnim = makeAnim(0.07f, healthRefillSheet, HEALTH_REFILL_FRAMES, Animation.PlayMode.NORMAL);

        soulOrbFrames = new Texture[SOUL_ORB_FRAME_COUNT];
        for (int i = 0; i < SOUL_ORB_FRAME_COUNT; i++) {
            soulOrbFrames[i] = new Texture(Gdx.files.internal("hud/HUD Cln_" + i + ".png"));
        }

        soulOrbEye = new Texture(Gdx.files.internal("hud/SoulOrb_Eye.png"));
    }

    private static void loadMaps() {
        TmxMapLoader loader = new TmxMapLoader();
        maps = new TiledMap[MAP_COUNT];
        maps[0] = loader.load("map/Greenpath-room1.tmx");
        maps[1] = loader.load("map/Greenpath-room2.tmx");
    }

    public static Vector2 getSpawnPosition(String spawnName) {
        MapLayer logicLayer = GameController.getInstance().getCurrentMap()
            .getLayers().get("logic");
        if (logicLayer == null) return new Vector2(220f, 220f);

        Vector2 pos = tryGetPoint(logicLayer, spawnName);
        if (pos != null) return pos;

        pos = tryGetPoint(logicLayer, "game_start_spawn");
        if (pos != null) return pos;

        return new Vector2(220f, 220f);
    }

    private static Vector2 tryGetPoint(MapLayer layer, String name) {
        MapObject obj = layer.getObjects().get(name);
        if (obj == null) return null;

        if (obj instanceof PointMapObject) {
            PointMapObject pt = (PointMapObject) obj;
            return new Vector2(pt.getPoint().x - 100f, pt.getPoint().y - 20f);
        }
        return null;
    }

    public static Skin getSkin() {
        if (skin == null) {
            skin = new Skin();
            TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("ui/HollowSkin.atlas"));
            skin.addRegions(atlas);
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/TrajanPro-Regular.ttf"));

            FreeTypeFontGenerator.FreeTypeFontParameter normal = new FreeTypeFontGenerator.FreeTypeFontParameter();
            normal.size = 34;
            BitmapFont normalFont = generator.generateFont(normal);
            skin.add("Hollowfont", normalFont);

            FreeTypeFontGenerator.FreeTypeFontParameter glow = new FreeTypeFontGenerator.FreeTypeFontParameter();
            glow.size = 36;
            glow.color = new Color(0.9f, 1.0f, 1.0f, 1f);
            glow.borderWidth = 1.4f;
            glow.borderColor = Color.WHITE;
            BitmapFont glowFont = generator.generateFont(glow);
            skin.add("HollowfontGlow", glowFont);

            FreeTypeFontGenerator.FreeTypeFontParameter desc = new FreeTypeFontGenerator.FreeTypeFontParameter();
            desc.size = 25;
            BitmapFont descFont = generator.generateFont(desc);
            skin.add("AchievementDescFont", descFont);
            generator.dispose();

            skin.load(Gdx.files.internal("ui/HollowSkin.json"));
            TextButton.TextButtonStyle style = skin.get("default", TextButton.TextButtonStyle.class);
            style.font = normalFont;
            style.fontColor = new Color(0.85f, 0.85f, 0.85f, 1f);
            style.overFontColor = Color.WHITE;
            style.downFontColor = Color.LIGHT_GRAY;
        }
        return skin;
    }

    public static TiledMap getMap(int index) { return maps[index]; }

    public static EnumMap<PlayerState, Animation<TextureRegion>> getPlayerAnimations() {
        return playerAnimations;
    }

    public static Animation<TextureRegion> getAttackHorizontalAnim() { return attackHorizontalAnim; }
    public static Animation<TextureRegion> getAttackUpAnim() { return attackUpAnim; }
    public static Animation<TextureRegion> getAttackDownAnim() { return attackDownAnim; }

    public static Texture getHealthBarTexture(int health) {
        int idx = MathUtils.clamp(health - 1, 0, 4);
        return healthBarTextures[idx];
    }
    public static Texture getFilledHealthTex() { return filledHealthTex; }
    public static Texture getEmptyHealthTex()   { return emptyHealthTex; }
    public static Animation<TextureRegion> getBreakHealthAnim() { return breakHealthAnim; }
    public static Animation<TextureRegion> getHealthRefillAnim() { return healthRefillAnim; }
    public static Texture getSoulOrbFrame(int soul) {
        int idx = Math.round((soul / (float) PlayerConstants.MAX_SOUL) * (SOUL_ORB_FRAME_COUNT - 1));
        idx = MathUtils.clamp(idx, 0, SOUL_ORB_FRAME_COUNT - 1);
        return soulOrbFrames[idx];
    }
    public static Texture getSoulOrbEye() { return soulOrbEye; }
    public static Texture getLockIcon() { return lockIcon; }
    public static Texture getAchievementIcon(AchievementType a) { return achievementIcons.get(a); }

    public static void dispose() {
        if (lockIcon != null) lockIcon.dispose();
        if (achievementIcons != null)
            for (Texture t : achievementIcons.values()) t.dispose();

        disposeIfNotNull(idleSheet); disposeIfNotNull(runSheet);
        disposeIfNotNull(jumpSheet); disposeIfNotNull(fallSheet);
        disposeIfNotNull(deathSheet); disposeIfNotNull(hurtSheet);
        disposeIfNotNull(focusSheet); disposeIfNotNull(healSheet);

        disposeIfNotNull(attackHorizontalSheet);
        disposeIfNotNull(attackUpSheet);
        disposeIfNotNull(attackDownSheet);
        disposeIfNotNull(attackSheet);

        if (healthBarTextures != null)
            for (Texture t : healthBarTextures) disposeIfNotNull(t);
        disposeIfNotNull(filledHealthTex);
        disposeIfNotNull(emptyHealthTex);
        disposeIfNotNull(breakHealthSheet);
        disposeIfNotNull(healthRefillSheet);
        if (soulOrbFrames != null)
            for (Texture t : soulOrbFrames) disposeIfNotNull(t);
        disposeIfNotNull(soulOrbEye);

        if (maps != null)
            for (TiledMap m : maps) if (m != null) m.dispose();

        if (skin != null) skin.dispose();
    }

    private static void disposeIfNotNull(Texture t) {
        if (t != null) t.dispose();
    }
}
