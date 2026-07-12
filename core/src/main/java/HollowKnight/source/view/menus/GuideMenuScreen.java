package HollowKnight.source.view.menus;

import HollowKnight.source.controller.menus.GuideMenuController;
import HollowKnight.source.model.asset.Assets;
import HollowKnight.source.model.player.PlayerConstants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;
import java.util.List;

public class GuideMenuScreen extends BaseMenuScreen {

    private static final float PANEL_WIDTH = 700f;
    private static final float CONTENT_TOP_Y = 860f;
    private static final float CONTROLS_SPACING = 56f;
    private static final float ABILITIES_SPACING = 140f;
    private static final float ABILITIES_VIEWPORT_BOTTOM = 200f;

    private static final String[][] CONTROLS = {
        {"Left/Right", "Move"},
        {"Space", "Jump - Double Jump - Wall Jump"},
        {"X", "Nail Attack"},
        {"Up + X", "Nail Attack (Upward)"},
        {"Down + X", "Nail Attack (Downward)"},
        {"C", "Dash"},
        {"A (Hold)", "Focus - spend Soul to heal"},
        {"Z", "Vengeful Spirit"},
        {"Down + Z", "Howling Wraiths"},
        {"I", "Open / Close Inventory"},
        {"E", "Interact / Talk"},
        {"Ctrl + S", "Save game"},
    };

    private static final String[][] CHEATS = {
        {"Ctrl + D", "God Mode"},
        {"Ctrl + A", "Boss Arena Teleport"},
        {"Ctrl + N", "Spectator"},
        {"Ctrl + I", "Charm Master"},
        {"Ctrl + E", "Emergency Heal"},
        {"Ctrl + L", "Refill Soul"},
    };

    private String[][] abilities() {
        return new String[][] {
            {"Health (Masks)", "You have " + PlayerConstants.MAX_HEALTH + " Masks. Taking a hit removes one; losing them all ends the run."},
            {"Soul", "Landing Nail hits fills your Soul meter, up to " + PlayerConstants.MAX_SOUL + "."},
            {"Focus (Healing)", "Hold Focus while grounded to spend " + PlayerConstants.SOUL_HEAL_COST + " Soul and restore a Mask."},
            {"Nail Combat", "Swing your Nail forward, or hold Up/Down while attacking to strike above or below you."},
            {"Vengeful Spirit", "Use " + PlayerConstants.SPELL_SOUL_COST + " souls to lunch a projectile spirit that go throw enemies & damage them."},
            {"Howling Wraiths", "Use " + PlayerConstants.SPELL_SOUL_COST + " souls to make a halo of wraiths above you and damage enemies."},
            {"Double Jump", "A second jump becomes available once per airtime after leaving the ground."},
            {"Dash", "A short burst of speed on a cooldown, usable a limited number of times in the air."},
            {"Wall Slide & Wall Jump", "Hold toward a wall while airborne to slide down slowly, then jump to launch off it."},
            {"Charms", "Equip passive Charms from the Inventory to customize your playstyle - each costs Notches."},
        };
    }

    private final List<Actor> contentActors = new ArrayList<>();

    private TextButton controlsTab;
    private TextButton abilitiesTab;
    private TextButton cheatsTab;
    private Image tabIndicator;

    private Texture scrollTrackTexture;
    private Texture scrollKnobTexture;
    private ScrollPane.ScrollPaneStyle scrollPaneStyle;

    @Override
    public void show() {
        setupCursor();
        Gdx.input.setInputProcessor(stage);

        float centerX = Gdx.graphics.getWidth() / 2f;

        controlsTab = new TextButton("Controls", Assets.getSkin());
        abilitiesTab = new TextButton("Abilities", Assets.getSkin());
        cheatsTab = new TextButton("Cheats", Assets.getSkin());

        prepareButton(controlsTab, 220, 55);
        prepareButton(abilitiesTab, 220, 55);
        prepareButton(cheatsTab, 220, 55);

        controlsTab.setPosition(centerX - 350, 900);
        abilitiesTab.setPosition(centerX - 110, 900);
        cheatsTab.setPosition(centerX + 130, 900);

        tabIndicator = new Image(Assets.getWhitePixel());
        tabIndicator.setColor(0.85f, 0.7f, 0.25f, 1f);
        tabIndicator.setSize(220, 4);

        GuideMenuController.modifyTabs(controlsTab, abilitiesTab, cheatsTab, this);

        stage.addActor(controlsTab);
        stage.addActor(abilitiesTab);
        stage.addActor(cheatsTab);
        stage.addActor(tabIndicator);

        buildScrollPaneStyle();

        showControls();

        TextButton backBtn = new TextButton("Back", Assets.getSkin());
        GuideMenuController.modifyComponents(backBtn);
        prepareButton(backBtn, 300, 60);
        backBtn.setPosition(centerX - 150, 120);
        stage.addActor(backBtn);
    }

    public void showControls() {
        highlightTab(controlsTab);
        clearContent();

        float x = (Gdx.graphics.getWidth() - PANEL_WIDTH) / 2f;
        float y = CONTENT_TOP_Y - 40f;

        Label.LabelStyle keyStyle = new Label.LabelStyle();
        keyStyle.font = Assets.getSkin().getFont("HollowfontGlow");
        keyStyle.fontColor = new Color(0.95f, 0.85f, 0.5f, 1f);

        Label.LabelStyle descStyle = new Label.LabelStyle();
        descStyle.font = Assets.getSkin().getFont("AchievementDescFont");
        descStyle.fontColor = new Color(0.85f, 0.85f, 0.85f, 1f);

        for (String[] row : CONTROLS) {
            Label key = new Label(row[0], keyStyle);
            key.setPosition(x - 5f, y);

            Label desc = new Label(row[1], descStyle);
            desc.setPosition(x + 390f, y);

            addContent(key);
            addContent(desc);

            y -= CONTROLS_SPACING;
        }
    }

    public void showAbilities() {
        highlightTab(abilitiesTab);
        clearContent();

        float x = (Gdx.graphics.getWidth() - PANEL_WIDTH) / 2f;
        float viewportHeight = CONTENT_TOP_Y - ABILITIES_VIEWPORT_BOTTOM;

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Assets.getSkin().getFont("HollowfontGlow");
        titleStyle.fontColor = new Color(0.95f, 0.85f, 0.5f, 1f);

        Label.LabelStyle descStyle = new Label.LabelStyle();
        descStyle.font = Assets.getSkin().getFont("AchievementDescFont");
        descStyle.fontColor = new Color(0.78f, 0.78f, 0.78f, 1f);

        float rowWidth = PANEL_WIDTH - 30f;

        Table table = new Table();
        table.top().left();

        String[][] rows = abilities();
        for (int i = 0; i < rows.length; i++) {
            Label title = new Label(rows[i][0], titleStyle);

            Label desc = new Label(rows[i][1], descStyle);
            desc.setWrap(true);

            table.add(title).width(rowWidth).left().padBottom(0f);
            table.row();
            table.add(desc).width(rowWidth).left().padBottom(30f);

            boolean isLast = i == rows.length - 1;
            table.row().padBottom(isLast ? 0f : ABILITIES_SPACING);
        }

        ScrollPane scrollPane = new ScrollPane(table, scrollPaneStyle);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsOnTop(false);
        scrollPane.setBounds(x, ABILITIES_VIEWPORT_BOTTOM, PANEL_WIDTH, viewportHeight);
        scrollPane.layout();
        scrollPane.setScrollY(0f);

        addContent(scrollPane);
    }

    public void showCheats() {
        highlightTab(cheatsTab);
        clearContent();

        float x = (Gdx.graphics.getWidth() - PANEL_WIDTH) / 2f;
        float y = CONTENT_TOP_Y - 50f;

        Label.LabelStyle keyStyle = new Label.LabelStyle();
        keyStyle.font = Assets.getSkin().getFont("HollowfontGlow");
        keyStyle.fontColor = new Color(0.95f, 0.85f, 0.5f, 1f);

        Label.LabelStyle descStyle = new Label.LabelStyle();
        descStyle.font = Assets.getSkin().getFont("AchievementDescFont");
        descStyle.fontColor = new Color(0.85f, 0.85f, 0.85f, 1f);

        for (String[] row : CHEATS) {
            Label key = new Label(row[0], keyStyle);
            key.setPosition(x + 130f , y - 80f);

            Label desc = new Label(row[1], descStyle);
            desc.setPosition(x + 390, y - 73f);

            addContent(key);
            addContent(desc);

            y -= CONTROLS_SPACING + 5f;
        }
    }

    private void buildScrollPaneStyle() {
        scrollTrackTexture = makeColorTexture(new Color(0.15f, 0.15f, 0.15f, 0.5f));
        scrollKnobTexture = makeColorTexture(new Color(0.85f, 0.7f, 0.25f, 0.9f));

        TextureRegionDrawable track = new TextureRegionDrawable(new TextureRegion(scrollTrackTexture));
        track.setMinWidth(10f);

        TextureRegionDrawable knob = new TextureRegionDrawable(new TextureRegion(scrollKnobTexture));
        knob.setMinWidth(10f);
        knob.setMinHeight(40f);

        scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.vScroll = track;
        scrollPaneStyle.vScrollKnob = knob;
    }

    private Texture makeColorTexture(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private void addContent(Actor actor) {
        stage.addActor(actor);
        contentActors.add(actor);
    }

    private void clearContent() {
        for (Actor actor : contentActors)
            actor.remove();
        contentActors.clear();
    }

    private void highlightTab(TextButton active) {
        tabIndicator.setPosition(active.getX(), active.getY() - 10);
    }

    @Override
    public void dispose() {
        if (scrollTrackTexture != null) scrollTrackTexture.dispose();
        if (scrollKnobTexture != null) scrollKnobTexture.dispose();
        super.dispose();
    }
}
