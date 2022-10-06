package net.Indyuce.mmocore.api.event;

import net.Indyuce.mmocore.api.player.PlayerData;
import org.bukkit.event.HandlerList;

public class PlayerCombatEvent extends PlayerDataEvent {
    private static final HandlerList handlers = new HandlerList();

    private final boolean enter;

    /**
     * Called when a player either enters or leaves combat
     * by dealing damage to, or being hit by another entity
     *
     * @param playerData Player interacting
     * @param enter      If the player is entering combt
     */
    public PlayerCombatEvent(PlayerData playerData, boolean enter) {
        super(playerData);

        this.enter = enter;
    }

    public boolean entersCombat() {
        return enter;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
