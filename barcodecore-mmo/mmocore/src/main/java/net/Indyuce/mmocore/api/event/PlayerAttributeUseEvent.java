package net.Indyuce.mmocore.api.event;

import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.player.attribute.PlayerAttribute;
import org.bukkit.event.HandlerList;

public class PlayerAttributeUseEvent extends PlayerDataEvent {
    private static final HandlerList handlers = new HandlerList();

    private final PlayerAttribute attribute;

    /**
     * Called when a player increases an attribute using the attribute viewer GUI
     *
     * @param playerData PLayer increasing his attribute
     * @param attribute  Attribute being increased
     */
    public PlayerAttributeUseEvent(PlayerData playerData, PlayerAttribute attribute) {
        super(playerData);

        this.attribute = attribute;
    }

    public PlayerAttribute getAttribute() {
        return attribute;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
