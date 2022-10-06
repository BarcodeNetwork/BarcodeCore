package net.Indyuce.mmoitems.api.event.item;

import net.Indyuce.mmoitems.api.interaction.util.DurabilityItem;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CustomDurabilityDamage extends Event implements Cancellable {

    @NotNull DurabilityItem sourceItem;
    int durabilityDecrease;

    boolean cancelled = false;

    public CustomDurabilityDamage(@NotNull DurabilityItem item, int impendingDamage) {
        sourceItem = item;
        durabilityDecrease = impendingDamage; }

    public int getDurabilityDecrease() { return durabilityDecrease; }
    @NotNull public DurabilityItem getSourceItem() { return sourceItem; }

    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean b) { cancelled = b; }

    private static final HandlerList handlers = new HandlerList();
    @NotNull @Override public HandlerList getHandlers() { return handlers; }
}
