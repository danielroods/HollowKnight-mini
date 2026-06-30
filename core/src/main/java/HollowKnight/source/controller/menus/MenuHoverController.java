package HollowKnight.source.controller.menus;

import HollowKnight.source.model.asset.Assets;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuHoverController {

    public static void addHoverEffect(TextButton button) {

        button.setTransform(true);

        BitmapFont normalFont = Assets.getSkin().getFont("Hollowfont");

        BitmapFont glowFont = Assets.getSkin().getFont("HollowfontGlow");

        TextButton.TextButtonStyle normalStyle = new TextButton.TextButtonStyle(button.getStyle());

        normalStyle.font = normalFont;
        normalStyle.fontColor = new Color(0.95f, 0.95f, 0.95f, 1f);

        TextButton.TextButtonStyle hoverStyle = new TextButton.TextButtonStyle(button.getStyle());

        hoverStyle.font = glowFont;
        hoverStyle.fontColor = Color.WHITE;

        button.setStyle(normalStyle);

        button.addListener(new ClickListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {

                button.clearActions();

                button.setStyle(hoverStyle);

                button.addAction(
                    Actions.parallel(
                        Actions.scaleTo(
                            1.08f,
                            1.08f,
                            0.12f
                        ),
                        Actions.color(
                            Color.WHITE,
                            0.12f
                        )
                    )
                );
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {

                button.clearActions();

                button.setStyle(normalStyle);

                button.addAction(
                    Actions.parallel(
                        Actions.scaleTo(
                            1f,
                            1f,
                            0.12f
                        ),
                        Actions.color(
                            Color.LIGHT_GRAY,
                            0.12f
                        )
                    )
                );
            }
        });
    }
}
