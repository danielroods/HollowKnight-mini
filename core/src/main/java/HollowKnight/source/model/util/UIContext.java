package HollowKnight.source.model.util;

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
