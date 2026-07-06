package HollowKnight.source.game_utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class CameraShake {
    private static float remainingTime = 0f;
    private static float totalDuration = 0f;
    private static float magnitude = 0f;
    private static final Vector2 offset = new Vector2();

    private CameraShake() {}

    public static void trigger(float duration, float mag) {
        if (duration <= 0f || mag <= 0f) return;

        if (duration > remainingTime)
            remainingTime = duration;
        if (duration > totalDuration)
            totalDuration = duration;
        if (mag > magnitude)
            magnitude = mag;
    }

    public static Vector2 update(float delta) {
        if (remainingTime <= 0f) {
            offset.set(0f, 0f);
            return offset;
        }

        remainingTime -= delta;

        float falloff = totalDuration > 0f ? Math.max(0f, remainingTime / totalDuration) : 0f;
        float currentMagnitude = magnitude * falloff;

        offset.set(
            MathUtils.random(-1f, 1f) * currentMagnitude,
            MathUtils.random(-1f, 1f) * currentMagnitude
        );

        if (remainingTime <= 0f) {
            remainingTime = 0f;
            totalDuration = 0f;
            magnitude = 0f;
        }

        return offset;
    }
}
