package HollowKnight.source.view.menus;

import HollowKnight.source.controller.menus.AchievementsMenuController;
import HollowKnight.source.game_utils.Assets;
import HollowKnight.source.model.achievement.AchievementManager;
import HollowKnight.source.model.achievement.AchievementType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class AchievementsMenuScreen extends BaseMenuScreen {
    @Override
    public void show() {

        setBackground(Assets.getAchievementsMenuBG());

        rootTable.defaults().pad(10);

        for (AchievementType achievement : AchievementType.values()) {
            Image icon = new Image(Assets.getAchievementIcon(achievement));

            Stack iconStack = new Stack();

            if (!AchievementManager.isUnlocked(achievement)) {
                icon.setColor(0.3f, 0.3f, 0.3f, 0.6f);

                Image lockIcon = new Image(Assets.getLockIcon());

                iconStack.add(icon);
                iconStack.add(lockIcon);
            } else {
                iconStack.add(icon);
            }

            Label title = new Label(achievement.getTitle(), Assets.getSkin());

            Label description = new Label(achievement.getDescription(), Assets.getSkin());

            rootTable.add(iconStack).size(64);

            rootTable.add(title).left();

            rootTable.add(description).left();

            rootTable.row();
        }

        TextButton backBtn = new TextButton("Back", Assets.getSkin());

        AchievementsMenuController.modifyComponents(backBtn);

        rootTable.row();

        rootTable.add(backBtn).colspan(3);

        stage.addActor(rootTable);
    }
}
