package net.Indyuce.mmocore.api.event;

import net.Indyuce.mmocore.api.player.PlayerData;
import org.bukkit.event.HandlerList;

/**
 * @deprecated Use {@link AsyncPlayerDataLoadEvent} instead
 */
@Deprecated
public class PlayerDataLoadEvent extends PlayerDataEvent {
    private static final HandlerList handlers = new HandlerList();

    /**
     * Called when a player data is being loaded into the game.
     *
     * @param playerData Player data being loaded
     */
    public PlayerDataLoadEvent(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
