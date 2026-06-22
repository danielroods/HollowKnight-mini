package HollowKnight.source.game_utils;

import HollowKnight.source.controller.GameController;
import HollowKnight.source.model.achievement.AchievementType;
import HollowKnight.source.model.player.PlayerState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PointMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import java.util.EnumMap;

public class Assets {

    private static final int IDLE_FRAMES = 9;
    private static final int RUN_FRAMES = 13;
    private static final int JUMP_FRAMES = 8;
    private static final int FALL_FRAMES = 6;
    private static final int DEATH_FRAMES = 18;

    private static Skin skin;
    private static Texture settingsMenuBG;
    private static Texture achievementsMenuBG;
    private static Texture lockIcon;
    private static EnumMap<AchievementType, Texture> achievementIcons;

    private static Texture idleSheet;
    private static Texture runSheet;
    private static Texture jumpSheet;
    private static Texture fallSheet;
    private static Texture deatSheet;
    private static EnumMap<PlayerState, Animation<TextureRegion>> playerAnimations;

    private static TiledMap[] maps;
    private static final int  MAP_COUNT = 2;

    public static void load() {
        settingsMenuBG = new Texture("backgrounds/SettingsBG.png");
        achievementsMenuBG = new Texture("backgrounds/SettingsBG.png");
        lockIcon = new Texture("icons/lock_icon.png");

        achievementIcons = new EnumMap<>(AchievementType.class);
        for (AchievementType a : AchievementType.values()) {
            achievementIcons.put(a, new Texture(a.getIconPath()));
        }

        loadPlayerAnimations();
        loadMaps();
    }

    private static void loadPlayerAnimations() {
        idleSheet = new Texture(Gdx.files.internal("player/Idle.png"));
        runSheet = new Texture(Gdx.files.internal("player/Run.png"));
        jumpSheet = new Texture(Gdx.files.internal("player/Double Jump.png"));
        fallSheet = new Texture(Gdx.files.internal("player/Fall.png"));
        deatSheet = new Texture(Gdx.files.internal("player/Death.png"));

        playerAnimations = new EnumMap<>(PlayerState.class);
        playerAnimations.put(PlayerState.IDLE, makeAnim(1f/IDLE_FRAMES, idleSheet, IDLE_FRAMES));
        playerAnimations.put(PlayerState.RUN, makeAnim(1f/RUN_FRAMES, runSheet, RUN_FRAMES));
        playerAnimations.put(PlayerState.JUMP, makeAnim(1f/JUMP_FRAMES, jumpSheet, JUMP_FRAMES));
        playerAnimations.put(PlayerState.FALL, makeAnim(1f/FALL_FRAMES, fallSheet, FALL_FRAMES));
        playerAnimations.put(PlayerState.DEAD, makeAnim(1f/DEATH_FRAMES, deatSheet, DEATH_FRAMES));
    }

    private static Animation<TextureRegion> makeAnim(float spd, Texture sheet, int frameCount) {
        int tileW = sheet.getWidth() / frameCount;
        int tileH = sheet.getHeight();
        TextureRegion[][] grid = TextureRegion.split(sheet, tileW, tileH);

        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < frameCount; i++) {
            frames.add(grid[0][i]);
        }
        return new Animation<>(spd, frames, Animation.PlayMode.LOOP);
    }

    private static void loadMaps() {
        TmxMapLoader mapLoader = new TmxMapLoader();
        maps = new TiledMap[MAP_COUNT];
        maps[0] = mapLoader.load("map/Greenpath-room1.tmx");
        maps[1] = mapLoader.load("map/Greenpath-room2.tmx");
    }

    public static Skin getSkin() {
        if (skin == null)
            skin = new Skin(Gdx.files.internal("ui/comic-ui.json"));
        return skin;
    }

    public static TiledMap getMap(int index) { return maps[index]; }

    public static Vector2 getSpawnPosition(String spawnName) {
        MapLayer logicLayer = GameController.getInstance().getCurrentMap().getLayers().get("logic");
        if (logicLayer == null) return new Vector2(200f, 200f);

        Vector2 pos = tryGetPoint(logicLayer, spawnName);
        if (pos != null) return pos;

        pos = tryGetPoint(logicLayer, "game_start_spawn");
        if (pos != null) return pos;

        return new Vector2(200f, 200f);
    }
    private static Vector2 tryGetPoint(MapLayer layer, String name) {
        MapObject obj = layer.getObjects().get(name);
        if (obj == null) return null;

        if (obj instanceof PointMapObject) {
            PointMapObject pt = (PointMapObject) obj;
            return new Vector2(pt.getPoint().x, pt.getPoint().y);
        }

        Float x = obj.getProperties().get("x", Float.class);
        Float y = obj.getProperties().get("y", Float.class);
        if (x != null && y != null) return new Vector2(x, y);

        return null;
    }

    public static EnumMap<PlayerState, Animation<TextureRegion>> getPlayerAnimations() {
        return playerAnimations;
    }

    public static Texture getSettingsMenuBG() { return settingsMenuBG; }
    public static Texture getAchievementsMenuBG() { return achievementsMenuBG; }
    public static Texture getLockIcon() { return lockIcon; }
    public static Texture getAchievementIcon(AchievementType a) { return achievementIcons.get(a); }

    public static void dispose() {
        settingsMenuBG.dispose();
        achievementsMenuBG.dispose();
        lockIcon.dispose();
        for (Texture t : achievementIcons.values()) t.dispose();

        if (idleSheet != null) idleSheet.dispose();
        if (runSheet != null) runSheet.dispose();
        if (jumpSheet != null) jumpSheet.dispose();
        if (fallSheet != null) fallSheet.dispose();

        if (maps != null)
            for (TiledMap m : maps)
                if (m != null)
                    m.dispose();

        if (skin != null) skin.dispose();
    }
}
