package HollowKnight.source.model.charm;

public enum CharmType {

    SOUL_CATCHER(
        "Soul Catcher",
        "Increases the amount of Soul gained when the Nail strikes a foe.",
        "charms/Soul Catcher.png"
    ),

    DASHMASTER(
        "Dashmaster",
        "Reduces the cooldown between dashes, allowing you to dash more frequently.",
        "charms/Dashmaster.png"
    ),

    UNBREAKABLE_STRENGTH(
        "Unbreakable Strength",
        "Deals additional Nail damage to enemies.",
        "charms/Unbreakable Strength.png"
    ),

    QUICK_SLASH(
        "Quick Slash",
        "Attacks faster by shortening the delay between Nail swings.",
        "charms/Quick Slash.png"
    ),

    QUICK_FOCUS(
        "Quick Focus",
        "Focus completes faster, letting you heal in less time.",
        "charms/Quick Focus.png"
    ),

    HEAVY_BLOW(
        "Heavy Blow",
        "Increases the knockback dealt to enemies struck by the Nail.",
        "charms/Heavy Blow.png"
    );

    private final String title;
    private final String description;
    private final String iconPath;

    CharmType(String title, String description, String iconPath) {
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
