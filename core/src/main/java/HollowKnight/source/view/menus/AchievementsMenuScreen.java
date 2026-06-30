package HollowKnight.source.view.menus;

import HollowKnight.source.controller.menus.AchievementsMenuController;
import HollowKnight.source.game_utils.Assets;
import HollowKnight.source.model.achievement.AchievementManager;
import HollowKnight.source.model.achievement.AchievementType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class AchievementsMenuScreen extends BaseMenuScreen {

    @Override
    public void show() {
        float panelWidth = 700;
        float x = (Gdx.graphics.getWidth() - panelWidth) / 2f;
        float y = 730;
        float spacing = 95;

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Assets.getSkin().getFont("HollowfontGlow");
        titleStyle.fontColor = Color.WHITE;

        Label.LabelStyle descStyle = new Label.LabelStyle();
        descStyle.font = Assets.getSkin().getFont("AchievementDescFont");
        descStyle.fontColor = Color.WHITE;

        for (AchievementType achievement : AchievementType.values()) {

            Image icon = new Image(Assets.getAchievementIcon(achievement));

            Stack stack = new Stack();

            if (!AchievementManager.isUnlocked(achievement)) {
                icon.setColor(0.25f, 0.25f, 0.25f, 0.55f);

                Image lock = new Image(Assets.getLockIcon());

                stack.add(icon);
                stack.add(lock);
            } else {
                stack.add(icon);
            }

            Label title = new Label(achievement.getTitle(), titleStyle);

            title.setColor(0.95f, 0.92f, 0.78f, 1f);

            Label desc = new Label(achievement.getDescription(), descStyle);
            desc.setColor(0.72f, 0.72f, 0.72f, 1f);

            stack.setBounds(x, y - 6, 72, 72);

            title.setPosition(x + 95, y + 20);

            desc.setPosition(x + 95, y - 8);

            stage.addActor(stack);
            stage.addActor(title);
            stage.addActor(desc);

            y -= spacing;
        }

        TextButton backBtn = new TextButton("Back", Assets.getSkin());

        AchievementsMenuController.modifyComponents(backBtn);

        prepareButton(backBtn, 300, 60);

        backBtn.setPosition(
            Gdx.graphics.getWidth() / 2f - 150,
            120
        );

        stage.addActor(backBtn);
    }
}
