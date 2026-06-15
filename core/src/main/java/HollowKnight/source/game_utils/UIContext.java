package HollowKnight.source.game_utils;

import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class UIContext {
    private static Viewport viewport;

    public static Viewport getViewport() {
        if (viewport == null)
            viewport = new ScreenViewport();
        return viewport;
    }
}
