package HollowKnight.source.view.menus;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

//credit: Arvin Talebi
public class MenuBackground {

    private final Texture[] backgrounds;
    private Texture currentBg;
    private Texture nextBg;

    private int currentIndex = 0;

    private float timer = 0f;
    private boolean isTransitioning = false;
    private float transitionTimer = 0f;

    private static final float SWITCH_TIME = 10f;
    private static final float TRANSITION_DURATION = 2f;

    public MenuBackground() {
        backgrounds = new Texture[]{
            new Texture("menu/bg1.png"),
            new Texture("menu/bg2.png"),
            new Texture("menu/bg3.png"),
            new Texture("menu/bg4.png"),
            new Texture("menu/bg5.png")
        };

        currentBg = backgrounds[0];
    }

    public void updateAndRender(SpriteBatch batch, float delta, float virtualWidth, float virtualHeight) {
        if (!isTransitioning) {
            timer += delta;

            if (timer >= SWITCH_TIME) {
                currentIndex = (currentIndex + 1) % backgrounds.length;
                nextBg = backgrounds[currentIndex];

                isTransitioning = true;
                transitionTimer = 0f;
                timer = 0f;
            }
        }

        if (isTransitioning) {
            transitionTimer += delta;
            float progress = transitionTimer / TRANSITION_DURATION;

            if (progress >= 1f) {
                currentBg = nextBg;
                isTransitioning = false;

                batch.setColor(1, 1, 1, 1);
                batch.draw(currentBg, 0, 0, virtualWidth, virtualHeight);
            } else {
                batch.setColor(1, 1, 1, 1f - progress);
                batch.draw(currentBg, 0, 0, virtualWidth, virtualHeight);

                batch.setColor(1, 1, 1, progress);
                batch.draw(nextBg, 0, 0, virtualWidth, virtualHeight);
            }
        } else {
            batch.setColor(1, 1, 1, 1);
            batch.draw(currentBg, 0, 0, virtualWidth, virtualHeight);
        }

        batch.setColor(1, 1, 1, 1);
    }

    public void dispose() {
        for (Texture bg : backgrounds) {
            if (bg != null) {
                bg.dispose();
            }
        }
    }
}
