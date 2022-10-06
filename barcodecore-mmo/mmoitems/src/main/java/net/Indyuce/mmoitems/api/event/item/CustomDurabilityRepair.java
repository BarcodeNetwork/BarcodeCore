package net.Indyuce.mmoitems.api.event.item;

import net.Indyuce.mmoitems.api.interaction.util.DurabilityItem;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CustomDurabilityRepair extends Event implements Cancellable {

    @NotNull
    private final DurabilityItem sourceItem;
    private final int durabilityIncrease;

    private boolean cancelled;

    private static final HandlerList handlers = new HandlerList();

    public CustomDurabilityRepair(@NotNull DurabilityItem item, int impendingRepair) {
        sourceItem = item;
        durabilityIncrease = impendingRepair;
    }

    /**
     * MMOItems always provides a player when using DurabilityItems. However
     * external plugins may provide a null player instance to manipulate
     * the item's durability WITHOUT having to provide a player.
     *
     * @return Checks if the item is being used by a player.
     */
    public boolean hasPlayer() {
        return sourceItem.getPlayer() != null;
    }

    public Player getPlayer() {
        return sourceItem.getPlayer();
    }

    public int getDurabilityIncrease() {
        return durabilityIncrease;
    }

    @NotNull
    public DurabilityItem getSourceItem() {
        return sourceItem;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
