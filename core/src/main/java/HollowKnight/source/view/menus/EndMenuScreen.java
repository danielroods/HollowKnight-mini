package HollowKnight.source.view.menus;

import HollowKnight.source.controller.menus.EndMenuController;
import HollowKnight.source.model.asset.Assets;
import HollowKnight.source.model.data.GameStats;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class EndMenuScreen extends BaseMenuScreen {

    private static final float PANEL_WIDTH = 560f;

    @Override
    public void show() {
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        float centerX = Gdx.graphics.getWidth() / 2f;
        float y = 880f;

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Assets.getSkin().getFont("HollowfontGlow");
        titleStyle.fontColor = Color.WHITE;

        Label title = new Label("Journey's End", titleStyle);
        title.setFontScale(2.4f);
        title.setColor(Color.OLIVE);
        title.setPosition(centerX - title.getPrefWidth() / 2f, y);
        stage.addActor(title);
        y -= 195f;

        Label playTimeLabel = new Label("Total Play Time: " + formatPlayTime(GameStats.getTotalPlayTimeSeconds()), titleStyle);
        playTimeLabel.setPosition(centerX - 180, y);
        playTimeLabel.setFontScale(0.7f);
        playTimeLabel.setColor(Color.GOLD);
        stage.addActor(playTimeLabel);
        y -= 60f;

        Label enemiesLabel = new Label("Enemies Defeated: " + GameStats.getEnemiesDefeated(), titleStyle);
        enemiesLabel.setPosition(centerX - 153, y);
        enemiesLabel.setFontScale(0.7f);
        enemiesLabel.setColor(Color.GOLD);
        stage.addActor(enemiesLabel);
        y -= 60f;

        Label deathsLabel = new Label("Total Deaths: " + GameStats.getTotalDeaths(), titleStyle);
        deathsLabel.setPosition(centerX - 133, y);
        deathsLabel.setFontScale(0.7f);
        deathsLabel.setColor(Color.GOLD);
        stage.addActor(deathsLabel);
        y -= 290f;

        TextButton newGameBtn = new TextButton("Start New Game", Assets.getSkin());
        prepareButton(newGameBtn, 360, 70);
        newGameBtn.setPosition(centerX - 180, y);
        stage.addActor(newGameBtn);
        y -= 80f;

        TextButton mainMenuBtn = new TextButton("Back to Main Menu", Assets.getSkin());
        prepareButton(mainMenuBtn, 360, 70);
        mainMenuBtn.setPosition(centerX - 180, y);
        stage.addActor(mainMenuBtn);
        EndMenuController.modifyComponents(newGameBtn, mainMenuBtn);
    }

    private String formatPlayTime(float totalSeconds) {
        int seconds = (int) totalSeconds;
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        if (hours > 0) {
            return String.format("%dh %02dm %02ds", hours, minutes, secs);
        }
        return String.format("%dm %02ds", minutes, secs);
    }
}
