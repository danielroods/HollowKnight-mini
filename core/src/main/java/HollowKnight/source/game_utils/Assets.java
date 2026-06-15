package HollowKnight.source.game_utils;

import HollowKnight.source.model.achievement.AchievementType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.EnumMap;

public class Assets {
    private static Skin skin;

    private static Texture settingsMenuBG;
    private static Texture achievementsMenuBG;
    private static Texture lockIcon;
    private static EnumMap<AchievementType, Texture> achievementIcons;

    public static void load() {
        settingsMenuBG = new Texture("backgrounds/SettingsBG.png");
        achievementsMenuBG = new Texture("backgrounds/SettingsBG.png");
        lockIcon = new Texture("icons/lock_icon.png");

        achievementIcons = new EnumMap<>(AchievementType.class);
        for (AchievementType achievement : AchievementType.values()) {
            achievementIcons.put(achievement, new Texture(achievement.getIconPath()));
        }
    }

    public static Skin getSkin() {
        if (skin == null)
            skin = new Skin(Gdx.files.internal("ui/comic-ui.json"));
        return skin;
    }

    public static Texture getSettingsMenuBG() {
        return settingsMenuBG;
    }

    public static Texture getAchievementsMenuBG() {
        return achievementsMenuBG;
    }

    public static Texture getLockIcon() {
        return lockIcon;
    }

    public static Texture getAchievementIcon(AchievementType achievement) {
        return achievementIcons.get(achievement);
    }

    public static void dispose() {
        for (Texture texture : achievementIcons.values()) {
            texture.dispose();
        }

        lockIcon.dispose();
        settingsMenuBG.dispose();
        achievementsMenuBG.dispose();
    }
}
