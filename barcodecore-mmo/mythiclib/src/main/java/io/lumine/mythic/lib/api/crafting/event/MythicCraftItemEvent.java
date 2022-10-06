package io.lumine.mythic.lib.api.crafting.event;

import io.lumine.mythic.lib.api.crafting.ingredients.MythicBlueprintInventory;
import io.lumine.mythic.lib.api.crafting.ingredients.MythicRecipeInventory;
import io.lumine.mythic.lib.api.crafting.recipes.MythicCachedResult;
import io.lumine.mythic.lib.api.crafting.recipes.vmp.VanillaInventoryMapping;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An extended {@link org.bukkit.event.inventory.CraftItemEvent} that
 * has processed information of MythicLib's Crafting System.
 *
 * @author Gunging
 */
@SuppressWarnings("unused")
public class MythicCraftItemEvent extends Event implements Cancellable {
    /**
     * @return What is currently in the result slots.
     *         <p></p>
     *         If interested in the input slots, their data is
     *         contained within {@link #getOtherInventories()}
     */
    @NotNull public MythicRecipeInventory getResultInventory() { return resultInventory; }
    /**
     * What is currently in the result slots.
     * <p></p>
     * If interested in the input slots, their data is
     * contained within {@link #getOtherInventories()}
     */
    @NotNull final MythicRecipeInventory resultInventory;

    /**
     * @return The current layout, immediately before the player crafts their item.
     *         <p></p>
     *         If interested in the result, output slots, their data is
     *         contained within {@link #getResultInventory()}
     */
    @NotNull public MythicBlueprintInventory getOtherInventories() { return otherInventories; }
    /**
     * The current layout, immediately before the player crafts their item.
     * <p></p>
     * If interested in the result, output slots, their data is
     * contained within {@link #getResultInventory()}
     */
    @NotNull final MythicBlueprintInventory otherInventories;

    /**
     * When the recipe first succeeded on the craft prep event,
     * it stored all the information in this cached result.
     * <p></p>
     * Stuff like:
     * <p><code>  * How will the final amounts look, of the ingredients?
     * </code></p>* Which was the recipe that succeeded?
     * <p></p>
     * This is important because the result items will be copied over on top
     * of the original ones. <code>null</code> items are ignored though, so
     * if you want to remove an item, either set its amount to 0 or make
     * it an AIR item stack.
     *
     * @return The information of the recipe's first success
     *         when the CraftPrep event ran.
     *
     * @see MythicCachedResult#getOperation() Which recipe succeeded
     * @see MythicCachedResult#getResultOfOperation() Final amounts of ingredients
     * @see io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager#AIR Sample AIR item stack
     */
    @NotNull public MythicCachedResult getCache() { return cache; }
    /**
     * When the recipe first succeeded on the craft prep event,
     * it stored all the information in this cached result.
     * <p></p>
     * Stuff like:
     * <p><code>  * How will the final amounts look, of the ingredients?
     * </code></p>* Which was the recipe that succeeded?
     * <p></p>
     * This is important because the result items will be copied over on top
     * of the original ones. <code>null</code> items are ignored though, so
     * if you want to remove an item, either set its amount to 0 or make
     * it an AIR item stack.
     *
     * @see MythicCachedResult#getOperation() Which recipe succeeded
     * @see MythicCachedResult#getResultOfOperation() Final amounts of ingredients
     * @see io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager#AIR Sample AIR item stack
     */
    @NotNull final MythicCachedResult cache;

    /**
     * This map stores information on how to actually apply the result of
     * a MythicCraftingInventory onto an actual Inventory.
     *
     * @return The mapping that will be used
     */
    @NotNull public VanillaInventoryMapping getMapping() { return mapping; }
    /**
     * This map stores information on how to actually apply the result of
     * a MythicCraftingInventory onto an actual Inventory.
     *
     * @param map The mapping to be used
     */
    public void setMapping(@NotNull VanillaInventoryMapping map) { mapping = map; }
    /**
     * This map stores information on how to actually apply the result of
     * a MythicCraftingInventory onto an actual Inventory.
     */
    @NotNull VanillaInventoryMapping mapping;

    @NotNull final InventoryClickEvent trigger;

    /**
     * @return The original event that triggered this MythicCraftItemEvent
     */
    @NotNull
    public InventoryClickEvent getTrigger() { return trigger; }

    public MythicCraftItemEvent(@NotNull InventoryClickEvent reason, @NotNull MythicRecipeInventory resultInventory, @NotNull VanillaInventoryMapping mapping, @NotNull MythicBlueprintInventory otherInventories, @NotNull MythicCachedResult cache) {
        this.trigger = reason;
        this.resultInventory = resultInventory;
        this.mapping = mapping;
        this.otherInventories = otherInventories;
        this.cache = cache;
    }

    @NotNull static final HandlerList handlers = new HandlerList();
    @NotNull public HandlerList getHandlers() { return handlers; }
    @NotNull public static HandlerList getHandlerList() { return handlers; }

    boolean cancelled = false;
    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancel) { cancelled = cancel; }
}
