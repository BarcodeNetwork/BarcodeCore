package net.Indyuce.mmoitems.api.event;

import net.Indyuce.mmoitems.api.ReforgeOptions;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.api.util.MMOItemReforger;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when an item is being reforged by {@link MMOItemReforger}.
 *
 * @author Gunging
 */
public class MMOItemReforgeEvent extends Event implements Cancellable {

    /**
     * The reforger class with most of the information.
     */
    @NotNull final MMOItemReforger reforger;
    /**
     * @return The reforger class with most of the information.
     */
    @NotNull public MMOItemReforger getReforger() { return reforger; }

    /**
     * The options for this reforging taking place.
     */
    @NotNull final ReforgeOptions options;
    /**
     * @return The options for this reforging taking place.
     */
    @NotNull public ReforgeOptions getOptions() { return options; }

    /**
     * @param reforger Reforger with the item stuff and all that
     *
     * @param options Options by which to reforge yes
     */
    public MMOItemReforgeEvent(@NotNull MMOItemReforger reforger, @NotNull ReforgeOptions options) {
        this.reforger = reforger;
        this.options = options;
    }

    //region API Shortcuts

    /**
     * @return The player that has this ItemStack in their inventory,
     *         if updated that way (items may be reforged from other
     *         sources).
     */
    @Nullable public Player getPlayer() {
        if (getReforger().getPlayer() == null) { return null; }

        // That's that
        return getReforger().getPlayer().getPlayer();
    }

    /**
     * @return MMOItems Type we are working with
     */
    @NotNull public Type getType() { return getReforger().getTemplate().getType(); }

    /**
     * @return MMOItems Type we are working with
     */
    @NotNull public String getTypeName() { return getReforger().getTemplate().getType().getId(); }

    /**
     * @return MMOItems ID we are working with
     */
    @NotNull public String getID() { return getReforger().getTemplate().getId(); }

    /**
     * @return Old MMOItem, getting revised.
     *
     * @see #getNewMMOItem()
     */
    @NotNull public LiveMMOItem getOldMMOItem() { return getReforger().getOldMMOItem(); }

    /**
     * @return Fresh, polished, updated, revised version of {@link #getOldMMOItem()}.
     *         <br><br>
     *         <b>The point of all this is to edit this one.</b> Transfer all the
     *         relevant information from the old one.
     *
     * @see #getOldMMOItem()
     */
    @NotNull public MMOItem getNewMMOItem() { return getReforger().getFreshMMOItem(); }
    //endregion

    //region Event Standard
    private static final HandlerList handlers = new HandlerList();
    @NotNull @Override public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }
    //endregion

    //region Cancellable Standard
    boolean cancelled;
    @Override public boolean isCancelled() {
        return cancelled;
    }
    @Override public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
    //endregion
}
