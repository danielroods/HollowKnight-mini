package HollowKnight.source.view.menus;

import HollowKnight.source.Main;
import HollowKnight.source.controller.menus.InventoryController;
import HollowKnight.source.model.util.UIContext;
import HollowKnight.source.model.asset.Assets;
import HollowKnight.source.model.charm.CharmManager;
import HollowKnight.source.model.charm.CharmType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;

public class InventoryScreen {
    private static final float PANEL_WIDTH = 760f;
    private static final float ROW_SPACING = 110f;
    private static final float INVENTORY_Y = 770f;

    private final SpriteBatch batch;
    private final Viewport viewport;
    private final Stage stage;

    private boolean open;
    private static Cursor inventoryCursor;

    public InventoryScreen() {
        batch = Main.getGameInstance().getBatch();
        viewport = UIContext.getViewport();
        stage = new Stage(viewport, batch);
    }

    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
        build();
        Gdx.input.setCursorCatched(false);
        setupInventoryCursor();
        Gdx.input.setInputProcessor(stage);
    }

    public void close() {
        open = false;
        Gdx.input.setInputProcessor(null);
        Gdx.input.setCursorCatched(true);
    }

    public void refresh() {
        build();
    }

    private void setupInventoryCursor() {
        if (inventoryCursor == null) {
            Pixmap cursorPixmap = new Pixmap(Gdx.files.internal("icons/Cursor.png"));
            inventoryCursor = Gdx.graphics.newCursor(cursorPixmap, 0, 0);
            cursorPixmap.dispose();
        }
        Gdx.graphics.setCursor(inventoryCursor);
    }

    private void build() {
        stage.clear();

        float x = (Gdx.graphics.getWidth() - PANEL_WIDTH) / 2f - 130f;
        float y = INVENTORY_Y;

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Assets.getSkin().getFont("HollowfontGlow");
        titleStyle.fontColor = Color.WHITE;

        Label.LabelStyle descStyle = new Label.LabelStyle();
        descStyle.font = Assets.getSkin().getFont("AchievementDescFont");
        descStyle.fontColor = Color.WHITE;

        Label header = new Label("Charms", titleStyle);
        header.setPosition(x - 12, y + 95);
        header.setColor(Color.CYAN);
        stage.addActor(header);

        Label notches = new Label("Notches: " + CharmManager.getUsedNotches() + " / " + CharmManager.getTotalNotches(), descStyle);
        notches.setColor(Color.CYAN);
        notches.setPosition(x + PANEL_WIDTH - 250f, y + 65);
        stage.addActor(notches);

        for (CharmType charm : CharmType.values()) {
            if (!CharmManager.isCollected(charm)) continue;

            boolean equipped = CharmManager.isEquipped(charm);

            Image icon = new Image(Assets.getCharmIcon(charm));
            icon.setScale(icon.getScaleX() * 1.25f, icon.getScaleY() * 1.25f);
            if (!equipped)
                icon.setColor(0.55f, 0.55f, 0.55f, 0.6f);

            Stack stack = new Stack();
            stack.add(icon);
            stack.setBounds(x - 15, y - 15, 72, 72);

            InventoryController.modifyCharmSlot(stack, charm, this);

            Label title = new Label(charm.getTitle(), titleStyle);
            title.setColor(equipped ?  new Color(0.89f, 0.45f, 1f, 1f) : new Color(0.7f, 0.7f, 0.7f, 0.6f));
            title.setPosition(x + 95, y + 20);

            Label desc = new Label(charm.getDescription(), descStyle);
            desc.setColor(equipped ? new Color(0.85f, 0.8f, 0.94f, 1f) : new Color(0.72f, 0.72f, 0.72f, 0.65f));
            desc.setPosition(x + 95, y - 8);

            stage.addActor(stack);
            stage.addActor(title);
            stage.addActor(desc);

            y -= ROW_SPACING;
        }

        TextButton closeBtn = new TextButton("Close", Assets.getSkin());
        BaseMenuScreen.prepareButton(closeBtn, 300, 60);
        closeBtn.setPosition(Gdx.graphics.getWidth() / 2f - 150, 110);

        InventoryController.modifyCloseButton(closeBtn, this);

        stage.addActor(closeBtn);
    }

    public void render(float delta) {
        batch.begin();
        batch.setColor(0f, 0f, 0f, 0.85f);
        batch.draw(Assets.getWhitePixel(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setColor(Color.WHITE);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
    }
}
