package HollowKnight.source.model.enemies.crystal_crawler;

import HollowKnight.source.model.enemies.Enemy;

public class CrystalCrawler extends Enemy {

    private CrystalCrawlerState state;

    public CrystalCrawler(float x, float y) {
        super(
            x, y,
            CrystalCrawlerConstants.MAX_HEALTH,
            CrystalCrawlerConstants.BOUNDS_OFFSET_X,
            CrystalCrawlerConstants.BOUNDS_OFFSET_Y,
            CrystalCrawlerConstants.BOUNDS_W,
            CrystalCrawlerConstants.BOUNDS_H
        );
        state = CrystalCrawlerState.WALK;
    }

    public CrystalCrawlerState getState() { return state; }
    public void setState(CrystalCrawlerState state) { this.state = state; }
}
