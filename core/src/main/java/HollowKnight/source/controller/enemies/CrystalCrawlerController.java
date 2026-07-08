package HollowKnight.source.controller.enemies;

import HollowKnight.source.controller.PlayerController;
import HollowKnight.source.model.enemies.crystal_crawler.CrystalCrawler;
import HollowKnight.source.model.enemies.crystal_crawler.CrystalCrawlerConstants;
import HollowKnight.source.model.enemies.crystal_crawler.CrystalCrawlerState;
import HollowKnight.source.model.player.Player;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CrystalCrawlerController {
    private final PlayerController playerController;

    private final List<CrystalCrawler> crawlerList;
    private final Set<CrystalCrawler> hitCrawlers = new HashSet<>();

    public CrystalCrawlerController(List<CrystalCrawler> crawlerList) {
        this.crawlerList = crawlerList;
        this.playerController = PlayerController.getInstance();
    }

    public void update(float delta, Player player, MapLayer logicLayer) {
        if (!player.isAttacking()) {
            hitCrawlers.clear();
        }

        for (CrystalCrawler crawler : crawlerList) {
            updateOne(delta, crawler, player, logicLayer);
        }
    }

    public void checkSwordHits(Rectangle swordHitbox, Player player) {
        for (CrystalCrawler crawler : crawlerList) {
            if (crawler.getState() == CrystalCrawlerState.DEAD) continue;
            if (crawler.isInvincible()) continue;
            if (hitCrawlers.contains(crawler)) continue;

            if (Intersector.overlaps(swordHitbox, crawler.getBounds())) {
                playerController.gainSoul();
                hitCrawlers.add(crawler);
                applyHit(crawler, player);
            }
        }
    }

    public List<CrystalCrawler> getCrystalCrawlerList() { return crawlerList; }

    private void updateOne(float delta, CrystalCrawler crawler, Player player, MapLayer logicLayer) {

        tickTimers(crawler, delta);

        if (crawler.getKnockbackTimer() <= 0) {
            switch (crawler.getState()) {
                case WALK:
                    updateWalk(crawler);
                    break;
                case DEAD:
                    updateDead(crawler);
                    break;
            }
        }

        if (!crawler.isOnGround()) {
            crawler.getVelocity().y += CrystalCrawlerConstants.GRAVITY * delta;
        }
        else {
            if (crawler.getVelocity().y < 0f) {
                crawler.getVelocity().y = 0f;
            }
        }

        crawler.setPosition(crawler.getPosition().x + crawler.getVelocity().x * delta, crawler.getPosition().y + crawler.getVelocity().y * delta);

        resolveCollisions(crawler, logicLayer);

        if (crawler.getState() == CrystalCrawlerState.WALK && crawler.isOnGround()) {
            if (isCliffAhead(crawler, logicLayer)) {
                flip(crawler);
            }
        }

        checkPlayerContact(crawler, player);
    }

    private void updateWalk(CrystalCrawler crawler) {
        crawler.getVelocity().x = crawler.isFacingRight() ? CrystalCrawlerConstants.PATROL_SPEED : -CrystalCrawlerConstants.PATROL_SPEED;
    }

    private void updateDead(CrystalCrawler crawler) {
        if (crawler.isOnGround()) {
            crawler.getVelocity().set(0f, 0f);
        }
    }

    private void resolveCollisions(CrystalCrawler crawler, MapLayer logicLayer) {
        if (logicLayer == null) return;

        boolean grounded = false;
        Rectangle bounds = new Rectangle(crawler.getBounds());

        for (MapObject obj : logicLayer.getObjects()) {
            if (!(obj instanceof RectangleMapObject)) continue;
            if (!isSolid(obj.getName())) continue;

            Rectangle tile = ((RectangleMapObject) obj).getRectangle();
            Rectangle overlap = new Rectangle();
            if (!Intersector.intersectRectangles(bounds, tile, overlap)) continue;

            if (overlap.width < overlap.height) {
                boolean hitRight = bounds.x < tile.x;
                float push = hitRight ? -overlap.width : overlap.width;
                crawler.setPosition(crawler.getPosition().x + push, crawler.getPosition().y);

                onWallHit(crawler);
            }
            else {
                float bodyMidY = bounds.y + bounds.height / 2f;
                float tileMidY = tile.y + tile.height / 2f;

                if (bodyMidY > tileMidY) {
                    if (crawler.getVelocity().y <= 0f) {
                        float landedY = tile.y + tile.height - CrystalCrawlerConstants.BOUNDS_OFFSET_Y;
                        crawler.setPosition(crawler.getPosition().x, landedY);
                        crawler.getVelocity().y = 0f;
                        grounded = true;
                    }
                }
                else {
                    if (crawler.getVelocity().y > 0f) {
                        float ceilY = tile.y - CrystalCrawlerConstants.BOUNDS_H - CrystalCrawlerConstants.BOUNDS_OFFSET_Y;
                        crawler.setPosition(crawler.getPosition().x, ceilY);
                        crawler.getVelocity().y = 0f;
                    }
                }
            }

            bounds.set(crawler.getBounds());
        }

        crawler.setOnGround(grounded);
    }

    private void onWallHit(CrystalCrawler crawler) {
        crawler.getVelocity().x = 0f;

        if (crawler.getState() == CrystalCrawlerState.WALK) {
            flip(crawler);
        }
    }

    private boolean isCliffAhead(CrystalCrawler crawler, MapLayer logicLayer) {
        if (logicLayer == null) return false;

        Rectangle b = crawler.getBounds();

        float probeW = 6f;
        float probeH = 28f;
        float probeX = crawler.isFacingRight() ? b.x + b.width - probeW : b.x;
        float probeY = b.y - probeH;

        Rectangle probe = new Rectangle(probeX, probeY, probeW, probeH);

        for (MapObject obj : logicLayer.getObjects()) {
            if (!(obj instanceof RectangleMapObject))
                continue;
            if (!isSolid(obj.getName()))
                continue;
            if (Intersector.overlaps(probe, ((RectangleMapObject) obj).getRectangle()))
                return false;
        }
        return true;
    }

    private void checkPlayerContact(CrystalCrawler crawler, Player player) {
        if (crawler.getState() == CrystalCrawlerState.DEAD) return;
        if (!player.isAlive() || player.isInvincible()) return;

        if (Intersector.overlaps(crawler.getBounds(), player.getBounds())) {
            float enemyXCenter = crawler.getBounds().x + crawler.getBounds().width / 2f;
            float playerXCenter = player.getBounds().x + player.getBounds().width / 2f;
            float knockBackDirection = playerXCenter < enemyXCenter ? -1f : 1f;
            playerController.takeDamage(CrystalCrawlerConstants.DAMAGE_TO_PLAYER);
            playerController.applyKnockback(knockBackDirection);
        }
    }

    private void applyHit(CrystalCrawler crawler, Player player) {
        crawler.setHealth(crawler.getHealth() - playerController.getNailDamage());
        crawler.setHurtTimer(CrystalCrawlerConstants.HURT_COOLDOWN);
        crawler.setKnockbackTimer(CrystalCrawlerConstants.KNOCKBACK_DURATION);
        crawler.setOnGround(false);

        float knockBackDirectionX = player.isFacingRight() ? 1f : -1f;

        if (crawler.getHealth() <= 0) {
            enterState(crawler, CrystalCrawlerState.DEAD);
        }

        crawler.getVelocity().set(knockBackDirectionX * CrystalCrawlerConstants.KNOCKBACK_SPEED_X * playerController.getKnockbackMultiplier(),
            CrystalCrawlerConstants.KNOCKBACK_SPEED_Y * playerController.getKnockbackMultiplier());
    }

    private void tickTimers(CrystalCrawler crawler, float delta) {
        if (crawler.getHurtTimer() > 0)
            crawler.setHurtTimer(Math.max(0f, crawler.getHurtTimer() - delta));
        if (crawler.getKnockbackTimer() > 0)
            crawler.setKnockbackTimer(Math.max(0f, crawler.getKnockbackTimer() - delta));
        crawler.setStateTimer(crawler.getStateTimer() + delta);
    }

    private void enterState(CrystalCrawler crawler, CrystalCrawlerState newState) {
        crawler.setState(newState);
        crawler.setStateTimer(0f);
    }

    private void flip(CrystalCrawler crawler) {
        crawler.setFacingRight(!crawler.isFacingRight());
    }

    private boolean isSolid(String name) {
        if (name == null)
            return false;
        return name.equals("platform") || name.equals("wall") || name.equals("ceiling");
    }
}
