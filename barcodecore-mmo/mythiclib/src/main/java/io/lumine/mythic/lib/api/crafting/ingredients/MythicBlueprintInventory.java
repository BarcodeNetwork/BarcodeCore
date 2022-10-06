package io.lumine.mythic.lib.api.crafting.ingredients;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This represents a collection of inventories, now with a meaning
 * that a blueprint can understand and compare.
 *
 * @author Gunging
 * @see io.lumine.mythic.lib.api.crafting.recipes.MythicRecipeBlueprint
 */
public class MythicBlueprintInventory {

    /**
     * The main, fully baked, collection of ItemStacks that lie
     * in the 'main inventory' concept of this recipe.
     */
    @NotNull final MythicRecipeInventory mainInventory;

    /**
     * The main, fully baked, collection of ItemStacks that lie
     * in the 'main inventory' concept of this recipe.
     */
    @NotNull public MythicRecipeInventory getMainInventory() { return mainInventory; }

    /**
     * The fully baked collection of ItemStacks that lie
     * in the 'result inventory' concept of this recipe.
     */
    @NotNull final MythicRecipeInventory resultInventory;

    /**
     * The main, fully baked, collection of ItemStacks that lie
     * in the 'main inventory' concept of this recipe.
     */
    @NotNull public MythicRecipeInventory getResultInventory() { return resultInventory; }

    /**
     * What's in the side inventories like the fuel slot (furnace)
     * or the netherite bar slot (smithing station)?
     */
    @NotNull final HashMap<String, MythicRecipeInventory> sideInventories = new HashMap<>();
    /**
     * <b>It is imperative that you know this name is indeed that of a contained side
     * inventory, use {@link #hasSideInventories(String)} to corroborate before calling this.</b>
     *
     * @return The side inventory associated to this string.
     */
    @NotNull public MythicRecipeInventory getSideInventory(@NotNull String ofName) {

        // Bruh
        Validate.isTrue(hasSideInventories(ofName), "You may not query for a side inventory that does not exist.");

        // Well was it?
        return sideInventories.get(ofName);
    }
    /**
     * Is there any side inventory associated to this name?
     * @param ofName What name
     * @return <code>true</code> if there is information for a side inventory of this name.
     */
    public boolean hasSideInventories(@NotNull String ofName) { return sideInventories.containsKey(ofName); }

    /**
     * Blueprint Inventories contain information on the contents of every
     * ItemStack of every Inventory, readily available for a blueprint
     * to read and assign to the correct recipe to evaluate.
     *
     * @param mainInventory The only required inventory input, the main one.
     *                      This would be the 3x3 grid of a crafting table,
     *                      or the potion slots of a brewing stand.
     *
     * @param resultInventory The output inventory, where the results will be displayed and picked up by the player.
     *                        <p></p>
     *                        If you really know what you are doing, you may just set it as a 0x0 empty MRI,
     *                        but only if your recipe does not have another recipe as an output I guess.
     */
    public MythicBlueprintInventory(@NotNull MythicRecipeInventory mainInventory, @NotNull MythicRecipeInventory resultInventory) {
        this.mainInventory = mainInventory;
        this.resultInventory = resultInventory;

        //BB//MythicCraftingManager.log("\u00a78Blueprint \u00a79B\u00a77 Building Blueprint: ");
        //BB//for (String str : mainInventory.toStrings("\u00a78Blueprint \u00a79BM-")) { MythicCraftingManager.log(str); }
        //BB//for (String str : resultInventory.toStrings("\u00a78Blueprint \u00a79BR-")) { MythicCraftingManager.log(str); }
    }

    /**
     * What are the side inventory names, of those of which this blueprint has information of?
     *
     * @return A new list, with a copy of every name of the side check inventories.
     */
    @NotNull public ArrayList<String> getSideInventoryNames() { return new ArrayList<>(sideInventories.keySet()); }

    /**
     * Include an inventory in the side-inventory-list.
     * Repeated keys will replace the old one.
     *
     * @param name Key associated to this inventory, like <code>fuel</code> or <code>spice</code>
     * @param inventory The item stack contents of it, the side inventory.
     *                  This would be the fuel slot of furnaces, or the lapis
     *                  slot of enchantment tables.
     */
    public void addSideInventory(@NotNull String name, @NotNull MythicRecipeInventory inventory) {

        // Include the inventory in the hashmap
        sideInventories.put(name, inventory);
    }

    /**
     * Why ever would you want to clear them I don't know, but here is how.
     */
    public void clearSideInventories() { sideInventories.clear(); }
}
