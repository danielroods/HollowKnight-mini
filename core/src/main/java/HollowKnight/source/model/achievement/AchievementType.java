package HollowKnight.source.model.achievement;

public enum AchievementType {

    COMPLETION(
        "Completion",
        "Finish the game",
        "achievements/completetion.png"
    ),

    SPEEDRUN(
        "Speedrun",
        "Finish the game under 20 minutes",
        "achievements/speedrun.png"
    ),

    TRUE_HUNTER(
        "True Hunter",
        "Kill all enemy types",
        "achievements/true_hunter.png"
    ),

    DEFEAT_FALSE_KNIGHT(
        "Defeat False Knight",
        "Defeat the False Knight boss",
        "achievements/defeat_false_knight.png"
    ),

    NO_DAMAGE_FALSE_KNIGHT(
        "Untouchable",
        "Defeat False Knight without taking damage",
        "achievements/no_damage_false_knight.png"
    );

    private final String title;
    private final String description;
    private final String iconPath;

    AchievementType(String title, String description, String iconPath) {
        this.title = title;
        this.description = description;
        this.iconPath = iconPath;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getIconPath() {
        return iconPath;
    }
}
