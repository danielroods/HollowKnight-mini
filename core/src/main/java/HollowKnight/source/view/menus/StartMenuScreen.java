package HollowKnight.source.view.menus;

import HollowKnight.source.controller.menus.StartMenuController;
import HollowKnight.source.data.GameData;
import HollowKnight.source.data.SaveLoadManager;
import HollowKnight.source.model.asset.Assets;
import HollowKnight.source.model.map.Maps;
import HollowKnight.source.model.player.PlayerConstants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Scaling;

public class StartMenuScreen extends BaseMenuScreen {
    private static final int SLOT_COUNT = SaveLoadManager.SLOT_COUNT;

    private static final float BACKGROUND_WIDTH_SCALE = 2.00f;
    private static final float BACKGROUND_HEIGHT_SCALE = 1.75f;

    @Override
    public void show() {
        TextButton[] slotButtons = new TextButton[SLOT_COUNT];
        Image[] slotBackgrounds = new Image[SLOT_COUNT];

        for (int i = 0; i < SLOT_COUNT; i++) {
            GameData data = SaveLoadManager.load(i);
            slotButtons[i] = new TextButton(buildSlotLabel(i, data), Assets.getSkin());
            slotBackgrounds[i] = buildSlotBackground(data);
        }

        TextButton backBtn = new TextButton("Back", Assets.getSkin());
        StartMenuController.modifyComponents(slotButtons, backBtn);

        float slotWidth = 560f;
        float slotHeight = 70f;
        float spacing = 140f;
        float topY = 730f;
        float centerX = Gdx.graphics.getWidth() / 2f - slotWidth / 2f;

        for (int i = 0; i < SLOT_COUNT; i++) {
            float slotY = topY - i * spacing;

            if (slotBackgrounds[i] != null) {
                float bgWidth = slotWidth * BACKGROUND_WIDTH_SCALE;
                float bgHeight = slotHeight * BACKGROUND_HEIGHT_SCALE;

                slotBackgrounds[i].setSize(bgWidth, bgHeight);
                slotBackgrounds[i].setPosition(
                    centerX - (bgWidth - slotWidth) / 2f,
                    slotY - (bgHeight - slotHeight) / 2f
                );
                stage.addActor(slotBackgrounds[i]);
            }

            prepareButton(slotButtons[i], slotWidth, slotHeight);
            slotButtons[i].setPosition(centerX, slotY);
            stage.addActor(slotButtons[i]);
        }

        prepareButton(backBtn, 300f, 60f);
        backBtn.setPosition(Gdx.graphics.getWidth() / 2f - 150f, topY - SLOT_COUNT * spacing - 50f);
        stage.addActor(backBtn);
    }

    private Image buildSlotBackground(GameData data) {
        if (data == null) return null;

        String texturePath = backgroundPathForMap(Maps.fromId(data.getMapId()));
        if (texturePath == null) return null;

        Image image = new Image(new Texture(Gdx.files.internal(texturePath)));
        image.setScaling(Scaling.stretch);
        return image;
    }

    private String backgroundPathForMap(Maps map) {
        if (map == null) return null;

        switch (map) {
            case GREENPATH_ROOM_1:
            case GREENPATH_ROOM_2:
                return "menu/Area_Green_Path.png";
            case CRYSTAL_PEAK:
                return "menu/Area_Crystal_Peak.png";
            case BOSS_ROOM:
                return "menu/Area_Boss_Room.png";
            default:
                return null;
        }
    }

    private String buildSlotLabel(int slotIndex, GameData data) {
        if (data == null) {
            return "New Game";
        }

        Maps map = Maps.fromId(data.getMapId());
        String mapName = map != null ? mapName(map) : "Unknown Area";

        return String.format(
            "%s | %d Masks | %d Soul",
            mapName, data.getMasksCount(), data.getSoulsCount()
        );
    }

    private String mapName(Maps map) {
        String raw = map.name().replace('_', ' ').toLowerCase();
        return Character.toUpperCase(raw.charAt(0)) + raw.substring(1);
    }
}
