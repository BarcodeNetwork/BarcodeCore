package net.Indyuce.mmocore.barcode.events;

import net.Indyuce.mmocore.api.event.PlayerDataEvent;
import net.Indyuce.mmocore.api.player.PlayerData;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerStatUpdateEvent extends PlayerDataEvent {
    private static final HandlerList handlers = new HandlerList();

    public PlayerStatUpdateEvent(PlayerData playerData) {
        super(playerData);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
