package HollowKnight.source.game_utils;

import HollowKnight.source.data.GameSettings;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BrightnessRenderer {

    private static Texture overlay;

    public static void init() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        overlay = new Texture(pixmap);

        pixmap.dispose();
    }

    public static void render(SpriteBatch batch,
                              float width,
                              float height) {

        float darkness = 1f - GameSettings.getBrightness();

        if (darkness <= 0)
            return;

        batch.begin();

        batch.setColor(0, 0, 0, darkness);

        batch.draw(
            overlay,
            0,
            0,
            width,
            height
        );

        batch.setColor(Color.WHITE);

        batch.end();
    }

    public static void dispose() {
        overlay.dispose();
    }
}
