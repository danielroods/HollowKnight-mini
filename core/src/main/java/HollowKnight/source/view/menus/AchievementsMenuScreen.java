package HollowKnight.source.view.menus;

import HollowKnight.source.controller.menus.AchievementsMenuController;
import HollowKnight.source.model.asset.Assets;
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
        Gdx.input.setInputProcessor(stage);

        float panelWidth = 700;
        float x = (Gdx.graphics.getWidth() - panelWidth) / 2f;
        float y = 760;
        float spacing = 122;

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Assets.getSkin().getFont("HollowfontGlow");
        titleStyle.fontColor = Color.WHITE;

        Label.LabelStyle descStyle = new Label.LabelStyle();
        descStyle.font = Assets.getSkin().getFont("AchievementDescFont");
        descStyle.fontColor = Color.WHITE;

        Label menuTitle = new Label("Achievements", titleStyle);
        menuTitle.setFontScale(2.2f);
        menuTitle.setColor(Color.WHITE);
        menuTitle.setPosition(x + 20f , y + 170f);

        for (AchievementType achievement : AchievementType.values()) {

            Image icon = new Image(Assets.getAchievementIcon(achievement));

            Stack stack = new Stack();

            if (!AchievementManager.isUnlocked(achievement)) {
                icon.setScale(icon.getScaleX() * 1.2f, icon.getScaleY() * 1.2f);
                icon.setColor(0.25f, 0.25f, 0.25f, 0.55f);

                Image lock = new Image(Assets.getLockIcon());
                lock.setScale(lock.getScaleX() * 1.2f, lock.getScaleY() * 1.2f);

                stack.add(icon);
                stack.add(lock);
            } else {
                icon.setScale(icon.getScaleX() * 1.2f, icon.getScaleY() * 1.2f);
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

        backBtn.setPosition(Gdx.graphics.getWidth() / 2f - 150, 120);

        stage.addActor(menuTitle);
        stage.addActor(backBtn);
    }
}
