package HollowKnight.source.game_utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class MenuParticleLayer {
    private final Texture particleTexture;
    private final Array<Particle> particles;
    private final float virtualWidth;
    private final float virtualHeight;
    private final Color calculatedColor = new Color();
    private final Array<Color> colors;

    private float stateTimer = 0f;
    private float transitionTimer = 0f;

    private final float TIME_BETWEEN_CHANGES = 10f;
    private final float TRANSITION_DURATION = 2f;
    private boolean isTransitioning = false;

    private int currentIndex = 0;
    private int nextIndex = 0;

    private static class Particle {
        float x, y;
        float speedX, speedY;
        float size;
        float alpha;
        float alphaSpeed;
    }

    public MenuParticleLayer(float virtualWidth, float virtualHeight) {
        this.virtualWidth = virtualWidth;
        this.virtualHeight = virtualHeight;
        colors = new Array<>();

        colors.add(new Color(0.35f, 1.00f, 0.80f, 1.0f));
        colors.add(new Color(0.70f, 0.30f, 0.90f, 1.0f));
        colors.add(new Color(1.00f, 1.00f, 1.00f, 1.0f));
        colors.add(new Color(0.55f, 0.80f, 1.00f, 1.0f));
        colors.add(new Color(0.85f, 0.88f, 0.90f, 1.0f));

        particleTexture = new Texture("menu/radient.png");

        particles = new Array<>();

        for (int i = 0; i < 15; i++) {
            Particle p = new Particle();
            resetParticle(p, true);
            particles.add(p);
        }
    }

    private void resetParticle(Particle p, boolean isInitialSpawn) {
        p.size = MathUtils.random(15f, 45f);

        p.x = MathUtils.random(-50f, virtualWidth);

        p.y = isInitialSpawn ? MathUtils.random(0f, virtualHeight) : -p.size;

        p.speedY = MathUtils.random(30f, 65f);
        p.speedX = MathUtils.random(19f, 33f);

        p.alpha = MathUtils.random(0.15f, 0.65f);
        p.alphaSpeed = MathUtils.random(0.3f, 0.9f);
    }

    public void updateAndRender(SpriteBatch batch, float delta) {
        if (!isTransitioning) {
            stateTimer += delta;
            if (stateTimer >= TIME_BETWEEN_CHANGES) {
                isTransitioning = true;
                transitionTimer = 0f;
                nextIndex = (currentIndex + 1) % colors.size;
            }
        } else {
            transitionTimer += delta;
            if (transitionTimer >= TRANSITION_DURATION) {
                isTransitioning = false;
                currentIndex = nextIndex;
                stateTimer = 0f;
            }
        }

        if (isTransitioning) {
            float progress = transitionTimer / TRANSITION_DURATION;
            calculatedColor.set(colors.get(currentIndex));
            calculatedColor.lerp(colors.get(nextIndex), progress);
        } else {
            calculatedColor.set(colors.get(currentIndex));
        }

        for (Particle p : particles) {
            p.x += p.speedX * delta;
            p.y += p.speedY * delta;

            p.alpha += p.alphaSpeed * delta;
            if (p.alpha > 0.7f || p.alpha < 0.15f) {
                p.alphaSpeed = -p.alphaSpeed;
            }

            if (p.y > virtualHeight || p.x > virtualWidth + p.size) {
                resetParticle(p, false);
            }
            batch.setColor(calculatedColor.r, calculatedColor.g, calculatedColor.b, MathUtils.clamp(p.alpha, 0f, 1f));

            batch.draw(particleTexture, p.x, p.y, p.size, p.size);
        }

        batch.setColor(1, 1, 1, 1);
    }

    public void dispose() {
        if (particleTexture != null) {
            particleTexture.dispose();
        }
    }
}
