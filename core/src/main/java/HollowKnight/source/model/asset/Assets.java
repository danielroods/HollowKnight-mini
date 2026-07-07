package HollowKnight.source.model.asset;

import HollowKnight.source.model.achievement.AchievementType;
import HollowKnight.source.model.enemies.crystal_crawler.CrystalCrawlerConstants;
import HollowKnight.source.model.enemies.crystal_guardian.CrystalGuardianConstants;
import HollowKnight.source.model.enemies.false_knight.FalseKnightConstants;
import HollowKnight.source.model.enemies.husk_hornhead.HuskHornheadConstants;
import HollowKnight.source.model.enemies.mossfly.MossflyConstants;
import HollowKnight.source.model.map.Maps;
import HollowKnight.source.model.player.PlayerConstants;
import HollowKnight.source.model.player.PlayerState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import java.util.EnumMap;

public class Assets {
    private static Skin skin;
    private static Texture lockIcon;
    private static EnumMap<AchievementType, Texture> achievementIcons;
    private static Texture idleSheet, runSheet, jumpSheet, doubleJumpSheet, fallSheet;
    private static Texture deathSheet, hurtSheet, focusSheet, healSheet, attackSheet;
    private static Texture attackHorizontalEffectSheet, attackUpEffectSheet, attackDownEffectSheet;
    private static Texture dashSheet;
    private static Texture dashEffectSheet;
    private static Texture wallSlideSheet;

    private static Texture[] healthBarTextures;
    private static Texture filledHealthTex;
    private static Texture emptyHealthTex;
    private static Texture breakHealthSheet;
    private static Texture healthRefillSheet;
    private static Texture[] soulOrbFrames;
    private static Texture soulOrbEye;

    private static Animation<TextureRegion> breakHealthAnim;
    private static Animation<TextureRegion> healthRefillAnim;
    private static Animation<TextureRegion> attackHorizontalEffectAnim, attackUpEffectAnim, attackDownEffectAnim;
    private static Animation<TextureRegion> dashEffectAnim;
    private static EnumMap<PlayerState, Animation<TextureRegion>> playerAnimations;

    private static Texture mossflyShakeSheet;
    private static Texture mossflyAppearSheet;
    private static Texture mossflyFlySheet;
    private static Texture mossflyDeathSheet;

    private static Animation<TextureRegion> mossflyShakeAnim;
    private static Animation<TextureRegion> mossflyAppearAnim;
    private static Animation<TextureRegion> mossflyFlyAnim;
    private static Animation<TextureRegion> mossflyDeathAnim;

    private static Texture huskHornheadWalkSheet;
    private static Texture huskHornheadIdleSheet;
    private static Texture huskHornheadChargeSheet;
    private static Texture huskHornheadDeathSheet;

    private static Animation<TextureRegion> huskHornheadWalkAnim;
    private static Animation<TextureRegion> huskHornheadIdleAnim;
    private static Animation<TextureRegion> huskHornheadChargeAnim;
    private static Animation<TextureRegion> huskHornheadDeathAnim;

    private static Texture crystalGuardianIdleSheet;
    private static Texture crystalGuardianRunSheet;
    private static Texture crystalGuardianShootSheet;
    private static Texture crystalGuardianEvadeSheet;
    private static Texture crystalGuardianDeathSheet;
    private static Texture crystalLaserSheet;

    private static Animation<TextureRegion> crystalGuardianIdleAnim;
    private static Animation<TextureRegion> crystalGuardianRunAnim;
    private static Animation<TextureRegion> crystalGuardianShootAnim;
    private static Animation<TextureRegion> crystalGuardianEvadeAnim;
    private static Animation<TextureRegion> crystalGuardianDeathAnim;
    private static Animation<TextureRegion> crystalLaserAnim;

    private static Texture crystalCrawlerWalkSheet;
    private static Texture crystalCrawlerDeathSheet;

    private static Animation<TextureRegion> crystalCrawlerWalkAnim;
    private static Animation<TextureRegion> crystalCrawlerDeathAnim;

    private static Texture falseKnightIdleSheet;
    private static Texture falseKnightRunSheet;
    private static Texture falseKnightAttackSheet;
    private static Texture falseKnightAttackRecoverSheet;
    private static Texture falseKnightDeathSheet;
    private static Texture falseKnightJumpSheet;
    private static Texture falseKnightJumpAttackSheet;
    private static Texture falseKnightLandSheet;
    private static Texture falseKnightStunnedSheet;
    private static Texture falseKnightStunRecoverSheet;
    private static Texture falseKnightShockwaveSheet;

    private static Animation<TextureRegion> falseKnightIdleAnim;
    private static Animation<TextureRegion> falseKnightRunAnim;
    private static Animation<TextureRegion> falseKnightAttackAnim;
    private static Animation<TextureRegion> falseKnightAttackRecoverAnim;
    private static Animation<TextureRegion> falseKnightDeathAnim;
    private static Animation<TextureRegion> falseKnightJumpAnim;
    private static Animation<TextureRegion> falseKnightJumpAttackAnim;
    private static Animation<TextureRegion> falseKnightLandAnim;
    private static Animation<TextureRegion> falseKnightStunnedAnim;
    private static Animation<TextureRegion> falseKnightStunRecoverAnim;
    private static Animation<TextureRegion> falseKnightShockwaveAnim;

    private static TiledMap[] maps;

    public static void load() {
        lockIcon = new Texture("icons/lock_icon.png");

        achievementIcons = new EnumMap<>(AchievementType.class);
        for (AchievementType a : AchievementType.values())
            achievementIcons.put(a, new Texture(a.getIconPath()));

        loadPlayerAssets();
        loadHUDAssets();
        loadMossflyAssets();
        loadHuskHornheadAssets();
        loadCrystalGuardianAssets();
        loadCrystalCrawlerAssets();
        loadFalseKnightAssets();
        loadMaps();
    }

    private static void loadPlayerAssets() {
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
        attackHorizontalEffectSheet = new Texture(Gdx.files.internal("player/effects/SlashEffect.png"));
        attackUpEffectSheet = new Texture(Gdx.files.internal("player/effects/UpSlashEffect.png"));
        attackDownEffectSheet = new Texture(Gdx.files.internal("player/effects/DownSlashEffect.png"));
        dashSheet = new Texture(Gdx.files.internal("player/Dash.png"));
        dashEffectSheet = new Texture(Gdx.files.internal("player/effects/Dash Effect.png"));
        wallSlideSheet = new Texture(Gdx.files.internal("player/Wall Slide.png"));

        playerAnimations = new EnumMap<>(PlayerState.class);
        playerAnimations.put(PlayerState.IDLE, makeAnim(1f / AssetConstants.IDLE_FRAMES, idleSheet, AssetConstants.IDLE_FRAMES, Animation.PlayMode.LOOP));
        playerAnimations.put(PlayerState.RUN, makeAnim(1f / AssetConstants.RUN_FRAMES, runSheet, AssetConstants.RUN_FRAMES, Animation.PlayMode.LOOP));
        playerAnimations.put(PlayerState.JUMP, makeAnim(1f / AssetConstants.JUMP_FRAMES, jumpSheet, AssetConstants.JUMP_FRAMES, Animation.PlayMode.NORMAL));
        playerAnimations.put(PlayerState.DOUBLE_JUMP, makeAnim(0.5f / AssetConstants.DOUBLE_JUMP_FRAMES, doubleJumpSheet, AssetConstants.DOUBLE_JUMP_FRAMES, Animation.PlayMode.NORMAL));
        playerAnimations.put(PlayerState.FALL, makeAnim(1f / AssetConstants.FALL_FRAMES, fallSheet, AssetConstants.FALL_FRAMES, Animation.PlayMode.LOOP));
        playerAnimations.put(PlayerState.DEAD, makeAnim(0.5f / AssetConstants.DEATH_FRAMES, deathSheet, AssetConstants.DEATH_FRAMES, Animation.PlayMode.NORMAL));
        playerAnimations.put(PlayerState.HURT, makeAnim(1f / AssetConstants.HURT_FRAMES, hurtSheet, AssetConstants.HURT_FRAMES, Animation.PlayMode.NORMAL));
        playerAnimations.put(PlayerState.FOCUS, makeAnim(PlayerConstants.FOCUS_DURATION / AssetConstants.FOCUS_FRAMES, focusSheet, AssetConstants.FOCUS_FRAMES, Animation.PlayMode.NORMAL));
        playerAnimations.put(PlayerState.HEAL, makeAnim(PlayerConstants.HEAL_ANIM_DUR / AssetConstants.HEAL_FRAMES, healSheet, AssetConstants.HEAL_FRAMES, Animation.PlayMode.NORMAL));
        playerAnimations.put(PlayerState.ATTACK, makeAnim(PlayerConstants.ATTACK_DUR / AssetConstants.ATTACK_FRAMES, attackSheet, AssetConstants.ATTACK_FRAMES, Animation.PlayMode.NORMAL));
        playerAnimations.put(PlayerState.DASH, makeAnim(PlayerConstants.DASH_DURATION / AssetConstants.DASH_FRAMES, dashSheet, AssetConstants.DASH_FRAMES, Animation.PlayMode.NORMAL));
        playerAnimations.put(PlayerState.WALL_SLIDE, makeAnim(0.12f, wallSlideSheet, AssetConstants.WALL_SLIDE_FRAMES, Animation.PlayMode.LOOP));

        float attackFrameDur = PlayerConstants.ATTACK_DUR / AssetConstants.ATTACK_EFFECT_FRAMES;
        attackHorizontalEffectAnim = makeAnim(attackFrameDur, attackHorizontalEffectSheet, AssetConstants.ATTACK_EFFECT_FRAMES, Animation.PlayMode.NORMAL);
        attackUpEffectAnim = makeAnim(attackFrameDur, attackUpEffectSheet, AssetConstants.ATTACK_EFFECT_FRAMES, Animation.PlayMode.NORMAL);
        attackDownEffectAnim = makeAnim(attackFrameDur, attackDownEffectSheet, AssetConstants.ATTACK_EFFECT_FRAMES, Animation.PlayMode.NORMAL);
        dashEffectAnim = makeAnim(PlayerConstants.DASH_DURATION / AssetConstants.DASH_EFFECT_FRAMES, dashEffectSheet, AssetConstants.DASH_EFFECT_FRAMES, Animation.PlayMode.NORMAL);
    }

    private static void loadMossflyAssets() {
        mossflyShakeSheet = new Texture(Gdx.files.internal("enemies/mossfly/Shake.png"));
        mossflyAppearSheet = new Texture(Gdx.files.internal("enemies/mossfly/Appear.png"));
        mossflyFlySheet = new Texture(Gdx.files.internal("enemies/mossfly/Fly.png"));
        mossflyDeathSheet = new Texture(Gdx.files.internal("enemies/mossfly/Death.png"));

        mossflyShakeAnim = makeAnim(MossflyConstants.SHAKE_FRAME_DUR, mossflyShakeSheet, MossflyConstants.SHAKE_FRAMES, Animation.PlayMode.LOOP);
        mossflyAppearAnim = makeAnim(MossflyConstants.APPEAR_FRAME_DUR, mossflyAppearSheet, MossflyConstants.APPEAR_FRAMES, Animation.PlayMode.NORMAL);
        mossflyFlyAnim = makeAnim(MossflyConstants.FLY_FRAME_DUR, mossflyFlySheet, MossflyConstants.FLY_FRAMES, Animation.PlayMode.LOOP);
        mossflyDeathAnim = makeAnim(MossflyConstants.DEATH_FRAME_DUR, mossflyDeathSheet, MossflyConstants.DEATH_FRAMES, Animation.PlayMode.NORMAL);
    }

    private static void loadHuskHornheadAssets() {
        huskHornheadWalkSheet = new Texture(Gdx.files.internal("enemies/husk_hornhead/Walk.png"));
        huskHornheadIdleSheet = new Texture(Gdx.files.internal("enemies/husk_hornhead/Idle.png"));
        huskHornheadChargeSheet = new Texture(Gdx.files.internal("enemies/husk_hornhead/Charge.png"));
        huskHornheadDeathSheet = new Texture(Gdx.files.internal("enemies/husk_hornhead/Death.png"));

        huskHornheadWalkAnim = makeAnim(HuskHornheadConstants.WALK_FRAME_DUR, huskHornheadWalkSheet, HuskHornheadConstants.WALK_FRAMES, Animation.PlayMode.LOOP);
        huskHornheadIdleAnim = makeAnim(HuskHornheadConstants.IDLE_FRAME_DUR, huskHornheadIdleSheet, HuskHornheadConstants.IDLE_FRAMES, Animation.PlayMode.LOOP);
        huskHornheadChargeAnim = makeAnim(HuskHornheadConstants.CHARGE_FRAME_DUR, huskHornheadChargeSheet, HuskHornheadConstants.CHARGE_FRAMES, Animation.PlayMode.LOOP);
        huskHornheadDeathAnim = makeAnim(HuskHornheadConstants.DEATH_FRAME_DUR, huskHornheadDeathSheet, HuskHornheadConstants.DEATH_FRAMES, Animation.PlayMode.NORMAL);
    }

    private static void loadCrystalGuardianAssets() {
        crystalGuardianIdleSheet = new Texture(Gdx.files.internal("enemies/crystal_guardian/Idle.png"));
        crystalGuardianRunSheet = new Texture(Gdx.files.internal("enemies/crystal_guardian/Run.png"));
        crystalGuardianShootSheet = new Texture(Gdx.files.internal("enemies/crystal_guardian/Shoot.png"));
        crystalGuardianEvadeSheet = new Texture(Gdx.files.internal("enemies/crystal_guardian/Evade.png"));
        crystalGuardianDeathSheet = new Texture(Gdx.files.internal("enemies/crystal_guardian/Death.png"));
        crystalLaserSheet = new Texture(Gdx.files.internal("enemies/crystal_guardian/CrystalLaser.png"));

        crystalGuardianIdleAnim = makeAnim(CrystalGuardianConstants.IDLE_FRAME_DUR, crystalGuardianIdleSheet, CrystalGuardianConstants.IDLE_FRAMES, Animation.PlayMode.LOOP);
        crystalGuardianRunAnim = makeAnim(CrystalGuardianConstants.RUN_FRAME_DUR, crystalGuardianRunSheet, CrystalGuardianConstants.RUN_FRAMES, Animation.PlayMode.LOOP);
        crystalGuardianShootAnim = makeAnim(CrystalGuardianConstants.SHOOT_FRAME_DUR, crystalGuardianShootSheet, CrystalGuardianConstants.SHOOT_FRAMES, Animation.PlayMode.NORMAL);
        crystalGuardianEvadeAnim = makeAnim(CrystalGuardianConstants.EVADE_FRAME_DUR, crystalGuardianEvadeSheet, CrystalGuardianConstants.EVADE_FRAMES, Animation.PlayMode.NORMAL);
        crystalGuardianDeathAnim = makeAnim(CrystalGuardianConstants.DEATH_FRAME_DUR, crystalGuardianDeathSheet, CrystalGuardianConstants.DEATH_FRAMES, Animation.PlayMode.NORMAL);
        crystalLaserAnim = makeAnim(CrystalGuardianConstants.LASER_EFFECT_FRAME_DUR, crystalLaserSheet, CrystalGuardianConstants.LASER_EFFECT_FRAMES, Animation.PlayMode.NORMAL);
    }

    private static void loadCrystalCrawlerAssets() {
        crystalCrawlerWalkSheet = new Texture(Gdx.files.internal("enemies/crystal_crawler/Walk.png"));
        crystalCrawlerDeathSheet = new Texture(Gdx.files.internal("enemies/crystal_crawler/Death.png"));

        crystalCrawlerWalkAnim = makeAnim(CrystalCrawlerConstants.WALK_FRAME_DUR, crystalCrawlerWalkSheet, CrystalCrawlerConstants.WALK_FRAMES, Animation.PlayMode.LOOP);
        crystalCrawlerDeathAnim = makeAnim(CrystalCrawlerConstants.DEATH_FRAME_DUR, crystalCrawlerDeathSheet, CrystalCrawlerConstants.DEATH_FRAMES, Animation.PlayMode.NORMAL);
    }

    private static void loadFalseKnightAssets() {
        falseKnightIdleSheet = new Texture(Gdx.files.internal("enemies/false_knight/Idle.png"));
        falseKnightRunSheet = new Texture(Gdx.files.internal("enemies/false_knight/Run.png"));
        falseKnightAttackSheet = new Texture(Gdx.files.internal("enemies/false_knight/Attack.png"));
        falseKnightAttackRecoverSheet = new Texture(Gdx.files.internal("enemies/false_knight/Attack Recover.png"));
        falseKnightDeathSheet = new Texture(Gdx.files.internal("enemies/false_knight/Death.png"));
        falseKnightJumpSheet = new Texture(Gdx.files.internal("enemies/false_knight/Jump.png"));
        falseKnightJumpAttackSheet = new Texture(Gdx.files.internal("enemies/false_knight/Jump Attack.png"));
        falseKnightLandSheet = new Texture(Gdx.files.internal("enemies/false_knight/Land.png"));
        falseKnightStunnedSheet = new Texture(Gdx.files.internal("enemies/false_knight/Stunned.png"));
        falseKnightStunRecoverSheet = new Texture(Gdx.files.internal("enemies/false_knight/Stun Recover.png"));
        falseKnightShockwaveSheet = new Texture(Gdx.files.internal("enemies/false_knight/Shockwave Effect.png"));

        falseKnightIdleAnim = makeAnim(FalseKnightConstants.IDLE_FRAME_DUR, falseKnightIdleSheet, FalseKnightConstants.IDLE_FRAMES, Animation.PlayMode.LOOP);
        falseKnightRunAnim = makeAnim(FalseKnightConstants.RUN_FRAME_DUR, falseKnightRunSheet, FalseKnightConstants.RUN_FRAMES, Animation.PlayMode.LOOP);
        falseKnightAttackAnim = makeAnim(FalseKnightConstants.ATTACK_FRAME_DUR, falseKnightAttackSheet, FalseKnightConstants.ATTACK_FRAMES, Animation.PlayMode.NORMAL);
        falseKnightAttackRecoverAnim = makeAnim(FalseKnightConstants.ATTACK_RECOVER_FRAME_DUR, falseKnightAttackRecoverSheet, FalseKnightConstants.ATTACK_RECOVER_FRAMES, Animation.PlayMode.NORMAL);
        falseKnightDeathAnim = makeAnim(FalseKnightConstants.DEATH_FRAME_DUR, falseKnightDeathSheet, FalseKnightConstants.DEATH_FRAMES, Animation.PlayMode.NORMAL);
        falseKnightJumpAnim = makeAnim(FalseKnightConstants.JUMP_FRAME_DUR, falseKnightJumpSheet, FalseKnightConstants.JUMP_FRAMES, Animation.PlayMode.NORMAL);
        falseKnightJumpAttackAnim = makeAnim(FalseKnightConstants.JUMP_ATTACK_FRAME_DUR, falseKnightJumpAttackSheet, FalseKnightConstants.JUMP_ATTACK_FRAMES, Animation.PlayMode.NORMAL);
        falseKnightLandAnim = makeAnim(FalseKnightConstants.LAND_FRAME_DUR, falseKnightLandSheet, FalseKnightConstants.LAND_FRAMES, Animation.PlayMode.NORMAL);
        falseKnightStunnedAnim = makeAnim(FalseKnightConstants.STUNNED_FRAME_DUR, falseKnightStunnedSheet, FalseKnightConstants.STUNNED_FRAMES, Animation.PlayMode.LOOP);
        falseKnightStunRecoverAnim = makeAnim(FalseKnightConstants.STUN_RECOVER_FRAME_DUR, falseKnightStunRecoverSheet, FalseKnightConstants.STUN_RECOVER_FRAMES, Animation.PlayMode.NORMAL);
        falseKnightShockwaveAnim = makeAnim(FalseKnightConstants.SHOCKWAVE_EFFECT_FRAME_DUR, falseKnightShockwaveSheet, FalseKnightConstants.SHOCKWAVE_EFFECT_FRAMES, Animation.PlayMode.LOOP);
    }

    private static void loadHUDAssets() {
        healthBarTextures = new Texture[5];
        for (int i = 0; i < 5; i++)
            healthBarTextures[i] = new Texture(Gdx.files.internal("hud/HealthBar" + (i + 1) + ".png"));

        filledHealthTex = new Texture(Gdx.files.internal("hud/FilledHealth.png"));
        emptyHealthTex = new Texture(Gdx.files.internal("hud/EmptyHealth.png"));

        breakHealthSheet = new Texture(Gdx.files.internal("hud/BreakHealth.png"));
        breakHealthAnim = makeAnim(0.06f, breakHealthSheet, AssetConstants.BREAK_HEALTH_FRAMES, Animation.PlayMode.NORMAL);

        healthRefillSheet = new Texture(Gdx.files.internal("hud/HealthRefill.png"));
        healthRefillAnim = makeAnim(0.07f, healthRefillSheet, AssetConstants.HEALTH_REFILL_FRAMES, Animation.PlayMode.NORMAL);

        soulOrbFrames = new Texture[AssetConstants.SOUL_ORB_FRAME_COUNT];
        for (int i = 0; i < AssetConstants.SOUL_ORB_FRAME_COUNT; i++)
            soulOrbFrames[i] = new Texture(Gdx.files.internal("hud/HUD Cln_" + i + ".png"));

        soulOrbEye = new Texture(Gdx.files.internal("hud/SoulOrb_Eye.png"));
    }

    private static void loadMaps() {
        TmxMapLoader loader = new TmxMapLoader();
        Maps[] allMaps = Maps.values();
        maps = new TiledMap[allMaps.length];
        for (int i = 0; i < allMaps.length; i++) {
            maps[i] = loader.load(allMaps[i].getPath());
        }
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

    public static Skin getSkin() {
        if (skin == null) {
            skin = new Skin();

            TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("ui/Hollow Knight skin.atlas"));
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

            Pixmap offPixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
            offPixmap.setColor(new Color(0.8f, 0.8f, 0.8f, 1f));
            offPixmap.drawRectangle(0, 0, 32, 32);
            offPixmap.setColor(new Color(0.08f, 0.08f, 0.08f, 0.85f));
            offPixmap.fillRectangle(2, 2, 28, 28);
            Texture offTexture = new Texture(offPixmap);
            skin.add("hk-checkbox-off", new TextureRegion(offTexture));

            Pixmap onPixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
            onPixmap.setColor(new Color(0.8f, 0.8f, 0.8f, 1f));
            onPixmap.drawRectangle(0, 0, 32, 32);
            onPixmap.setColor(new Color(0.08f, 0.08f, 0.08f, 0.85f));
            onPixmap.fillRectangle(2, 2, 28, 28);
            onPixmap.setColor(Color.WHITE);
            onPixmap.drawLine(8, 8, 24, 24);
            onPixmap.drawLine(8, 9, 23, 24);
            onPixmap.drawLine(8, 24, 24, 8);
            onPixmap.drawLine(8, 23, 23, 8);
            Texture onTexture = new Texture(onPixmap);
            skin.add("hk-checkbox-on", new TextureRegion(onTexture));

            offPixmap.dispose();
            onPixmap.dispose();

            skin.load(Gdx.files.internal("ui/Hollow Knight skin.json"));
        }
        return skin;
    }

    public static TiledMap getMap(int index) { return maps[index]; }
    public static EnumMap<PlayerState, Animation<TextureRegion>> getPlayerAnimations() { return playerAnimations; }
    public static Animation<TextureRegion> getAttackHorizontalEffectAnim() { return attackHorizontalEffectAnim; }
    public static Animation<TextureRegion> getAttackUpEffectAnim() { return attackUpEffectAnim; }
    public static Animation<TextureRegion> getAttackDownEffectAnim() { return attackDownEffectAnim; }
    public static Animation<TextureRegion> getDashEffectAnim() { return dashEffectAnim; }
    public static Texture getSoulOrbEye() { return soulOrbEye; }
    public static Texture getLockIcon() { return lockIcon; }
    public static Texture getAchievementIcon(AchievementType a) { return achievementIcons.get(a); }
    public static Texture getHealthBarTexture(int health) {
        int idx = MathUtils.clamp(health - 1, 0, 4);
        return healthBarTextures[idx];
    }
    public static Texture getFilledHealthTex() { return filledHealthTex; }
    public static Texture getEmptyHealthTex() { return emptyHealthTex; }
    public static Animation<TextureRegion> getBreakHealthAnim() { return breakHealthAnim; }
    public static Animation<TextureRegion> getHealthRefillAnim() { return healthRefillAnim; }
    public static Texture getSoulOrbFrame(int soul) {
        int idx = Math.round((soul / (float) PlayerConstants.MAX_SOUL) * (AssetConstants.SOUL_ORB_FRAME_COUNT - 1));
        idx = MathUtils.clamp(idx, 0, AssetConstants.SOUL_ORB_FRAME_COUNT - 1);
        return soulOrbFrames[idx];
    }

    public static Animation<TextureRegion> getMossflyShakeAnim() { return mossflyShakeAnim; }
    public static Animation<TextureRegion> getMossflyAppearAnim() { return mossflyAppearAnim; }
    public static Animation<TextureRegion> getMossflyFlyAnim() { return mossflyFlyAnim; }
    public static Animation<TextureRegion> getMossflyDeathAnim() { return mossflyDeathAnim; }

    public static Animation<TextureRegion> getHuskHornheadWalkAnim() { return huskHornheadWalkAnim; }
    public static Animation<TextureRegion> getHuskHornheadIdleAnim() { return huskHornheadIdleAnim; }
    public static Animation<TextureRegion> getHuskHornheadChargeAnim() { return huskHornheadChargeAnim; }
    public static Animation<TextureRegion> getHuskHornheadDeathAnim() { return huskHornheadDeathAnim; }

    public static Animation<TextureRegion> getCrystalGuardianIdleAnim() { return crystalGuardianIdleAnim; }
    public static Animation<TextureRegion> getCrystalGuardianRunAnim() { return crystalGuardianRunAnim; }
    public static Animation<TextureRegion> getCrystalGuardianShootAnim() { return crystalGuardianShootAnim; }
    public static Animation<TextureRegion> getCrystalGuardianEvadeAnim() { return crystalGuardianEvadeAnim; }
    public static Animation<TextureRegion> getCrystalGuardianDeathAnim() { return crystalGuardianDeathAnim; }
    public static Animation<TextureRegion> getCrystalLaserAnim() { return crystalLaserAnim; }

    public static Animation<TextureRegion> getCrystalCrawlerWalkAnim() { return crystalCrawlerWalkAnim; }
    public static Animation<TextureRegion> getCrystalCrawlerDeathAnim() { return crystalCrawlerDeathAnim; }

    public static Animation<TextureRegion> getFalseKnightIdleAnim() { return falseKnightIdleAnim; }
    public static Animation<TextureRegion> getFalseKnightRunAnim() { return falseKnightRunAnim; }
    public static Animation<TextureRegion> getFalseKnightAttackAnim() { return falseKnightAttackAnim; }
    public static Animation<TextureRegion> getFalseKnightAttackRecoverAnim() { return falseKnightAttackRecoverAnim; }
    public static Animation<TextureRegion> getFalseKnightDeathAnim() { return falseKnightDeathAnim; }
    public static Animation<TextureRegion> getFalseKnightJumpAnim() { return falseKnightJumpAnim; }
    public static Animation<TextureRegion> getFalseKnightJumpAttackAnim() { return falseKnightJumpAttackAnim; }
    public static Animation<TextureRegion> getFalseKnightLandAnim() { return falseKnightLandAnim; }
    public static Animation<TextureRegion> getFalseKnightStunnedAnim() { return falseKnightStunnedAnim; }
    public static Animation<TextureRegion> getFalseKnightStunRecoverAnim() { return falseKnightStunRecoverAnim; }
    public static Animation<TextureRegion> getFalseKnightShockwaveAnim() { return falseKnightShockwaveAnim; }

    public static void dispose() {
        if (lockIcon != null) lockIcon.dispose();
        if (achievementIcons != null)
            for (Texture t : achievementIcons.values()) t.dispose();

        disposeIfNotNull(idleSheet); disposeIfNotNull(runSheet);
        disposeIfNotNull(jumpSheet); disposeIfNotNull(fallSheet);
        disposeIfNotNull(deathSheet); disposeIfNotNull(hurtSheet);
        disposeIfNotNull(focusSheet); disposeIfNotNull(healSheet);
        disposeIfNotNull(attackHorizontalEffectSheet);
        disposeIfNotNull(attackUpEffectSheet);
        disposeIfNotNull(attackDownEffectSheet);
        disposeIfNotNull(attackSheet);
        disposeIfNotNull(dashSheet);
        disposeIfNotNull(dashEffectSheet);
        disposeIfNotNull(wallSlideSheet);

        if (healthBarTextures != null)
            for (Texture t : healthBarTextures) disposeIfNotNull(t);
        disposeIfNotNull(filledHealthTex);
        disposeIfNotNull(emptyHealthTex);
        disposeIfNotNull(breakHealthSheet);
        disposeIfNotNull(healthRefillSheet);
        if (soulOrbFrames != null)
            for (Texture t : soulOrbFrames) disposeIfNotNull(t);
        disposeIfNotNull(soulOrbEye);

        disposeIfNotNull(mossflyShakeSheet);
        disposeIfNotNull(mossflyAppearSheet);
        disposeIfNotNull(mossflyFlySheet);
        disposeIfNotNull(mossflyDeathSheet);

        disposeIfNotNull(huskHornheadWalkSheet);
        disposeIfNotNull(huskHornheadIdleSheet);
        disposeIfNotNull(huskHornheadChargeSheet);
        disposeIfNotNull(huskHornheadDeathSheet);

        disposeIfNotNull(crystalGuardianIdleSheet);
        disposeIfNotNull(crystalGuardianRunSheet);
        disposeIfNotNull(crystalGuardianShootSheet);
        disposeIfNotNull(crystalGuardianEvadeSheet);
        disposeIfNotNull(crystalGuardianDeathSheet);
        disposeIfNotNull(crystalLaserSheet);

        disposeIfNotNull(crystalCrawlerWalkSheet);
        disposeIfNotNull(crystalCrawlerDeathSheet);

        disposeIfNotNull(falseKnightIdleSheet);
        disposeIfNotNull(falseKnightRunSheet);
        disposeIfNotNull(falseKnightAttackSheet);
        disposeIfNotNull(falseKnightAttackRecoverSheet);
        disposeIfNotNull(falseKnightDeathSheet);
        disposeIfNotNull(falseKnightJumpSheet);
        disposeIfNotNull(falseKnightJumpAttackSheet);
        disposeIfNotNull(falseKnightLandSheet);
        disposeIfNotNull(falseKnightStunnedSheet);
        disposeIfNotNull(falseKnightStunRecoverSheet);
        disposeIfNotNull(falseKnightShockwaveSheet);

        if (maps != null)
            for (TiledMap m : maps) if (m != null) m.dispose();

        if (skin != null) skin.dispose();
    }

    private static void disposeIfNotNull(Texture t) {
        if (t != null) t.dispose();
    }
}
