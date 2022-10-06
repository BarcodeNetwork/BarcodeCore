package net.Indyuce.mmocore.api.event;

import net.Indyuce.mmocore.api.player.PlayerData;
import org.bukkit.event.HandlerList;

public class AsyncPlayerDataLoadEvent extends AsyncPlayerDataEvent {
    private static final HandlerList handlers = new HandlerList();

    /**
     * Called when a player data is being loaded into the game.
     * This event is called async.
     *
     * @param playerData Player data being loaded
     */
    public AsyncPlayerDataLoadEvent(PlayerData playerData) {
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
