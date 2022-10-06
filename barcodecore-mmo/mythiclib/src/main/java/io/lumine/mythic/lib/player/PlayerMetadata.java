package io.lumine.mythic.lib.player;

import io.lumine.mythic.lib.api.stat.StatMap;
import org.bukkit.entity.Player;

/**
 * A class containing the information about a player that can
 * be used to temporarily cache its statistics for instance
 * when attacking or casting a skill
 */
public abstract class PlayerMetadata {
    private final StatMap.CachedStatMap statMap;
    private final Player player;

    public PlayerMetadata(StatMap.CachedStatMap statMap) {
        this.statMap = statMap;
        this.player = statMap.getPlayer();
    }

    public StatMap.CachedStatMap getStats() {
        return statMap;
    }

    public Player getPlayer() {
        return player;
    }
}
