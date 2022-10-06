package io.lumine.mythic.lib.api.crafting.recipes.vmp;

import io.lumine.mythic.lib.api.crafting.ingredients.MythicBlueprintInventory;
import io.lumine.mythic.lib.api.crafting.ingredients.MythicRecipeInventory;
import io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager;
import io.lumine.mythic.lib.api.crafting.recipes.MythicRecipeStation;
import io.lumine.utils.version.ServerVersion;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * To easily perform operations to and fro vanilla inventories,
 * these mappings contain the "translation keys" from a normal
 * inventory to a {@link MythicRecipeInventory}.
 * <p></p>
 * <b>These really only account for vanilla crafting stations</b>
 *
 * @author Gunging
 */
@SuppressWarnings("unused")
public abstract class VanillaInventoryMapping {

    //region Main Inventory
    /**
     * Interestingly, custom inventories might have gaps between chest pieces.
     * In that case, please return a negative number and the count will ignore it.
     *
     * @param slot What slot you trying to read.
     *
     * @return For the Main inventory, this slot number,
     *         to what height does it translate to?
     *         <p></p>
     *         <b>Always Positive</b>
     *
     * @throws IllegalArgumentException when the slot is not part of the inventory.
     */
    public abstract int getMainWidth(int slot) throws IllegalArgumentException;
    /**
     * Interestingly, custom inventories might have gaps between chest pieces.
     * In that case, please return a positive number and the count will ignore it.
     *
     * @param slot What slot you trying to read.
     *
     * @return For the Main inventory, this slot number,
     *         to what height does it translate to?
     *         <p></p>
     *         <b>Always negative</b>
     *
     * @throws IllegalArgumentException when the slot is not part of the inventory.
     */
    public abstract int getMainHeight(int slot) throws IllegalArgumentException;
    /**
     * @param width The Horizontal co-ordinate in the mythic inventory
     *               <p></p>
     *               <b>Always Positive</b>
     *
     * @param height The Vertical co-ordinate in the mythic inventory
     *               <p></p>
     *               <b>Always Negative</b>
     *
     * @return The inventory slot that these offsets correspond to
     *
     * @throws IllegalArgumentException If the width and height are out of bounds.
     */
    public abstract int getMainSlot(int width, int height) throws IllegalArgumentException;
    /**
     * @return Inventories may not start at the 0 slot;
     *         <p>Easiest example is crafting table's main inventory:
     *         </p> Slots 1 through 10 (for a size of 9); In that
     *         case, this number returns '<code>1</code>'
     */
    public abstract int getMainInventoryStart();
    /**
     * @return How many slots are there in the vanilla inventory?
     *         <p></p>
     *         This behaves like array or list sizes, so that the first
     *         slot is <code>'0'</code> and the last slot is <code>'inventorySize() - 1'</code>
     */
    public abstract int getMainInventorySize();
    /**
     * @return How wide is the Main inventory?
     *         <p></p>
     *         <b>Positive</b>
     */
    public abstract int getMainInventoryWidth();
    /**
     * @return How tall is the Main inventory?
     *         <p></p>
     *         <b>Positive</b>
     */
    public abstract int getMainInventoryHeight();
    /**
     * Returns a mythic recipe inventory from the provided inventory.
     * <p></p>
     * The one defined as the <b>main</b> inventory.
     *
     * @param inventory Inventory to extract ItemStacks from
     *
     * @return A built Mythic Recipe Inventory
     */
    @NotNull public MythicRecipeInventory getMainMythicInventory(@NotNull Inventory inventory) {

        // Create
        MythicRecipeInventory main = new MythicRecipeInventory();

        // For each Main size
        for (int h = 0; h < getMainInventoryHeight(); h++) {

            // Make row
            ItemStack[] row = new ItemStack[getMainInventoryWidth()];

            for (int w = 0; w < getMainInventoryWidth(); w++) {

                // Fill row
                row[w] = get(inventory, getMainSlot(w, -h));
            }

            // Add row
            main.addRow(row);
        }

        // That's it
        return main;
    }
    //endregion
    
    //region Result Inventory
    /**
     * @param slot What slot you trying to read.
     *
     * @return For the result inventory, this slot number,
     *         to what height does it translate to?
     *         <p></p>
     *         <b>Always Positive</b>
     *
     * @throws IllegalArgumentException when the slot is not part of the inventory.
     */
    public abstract int getResultWidth(int slot) throws IllegalArgumentException;
    /**
     * @param slot What slot you trying to read.
     *
     * @return For the result inventory, this slot number,
     *         to what height does it translate to?
     *         <p></p>
     *         <b>Always negative</b>
     *
     * @throws IllegalArgumentException when the slot is not part of the inventory.
     */
    public abstract int getResultHeight(int slot) throws IllegalArgumentException;
    /**
     * @param width The Horizontal co-ordinate in the mythic inventory
     *               <p></p>
     *               <b>Always Positive</b>
     *
     * @param height The Vertical co-ordinate in the mythic inventory
     *               <p></p>
     *               <b>Always Negative</b>
     *
     * @return The inventory slot that these offsets correspond to
     *
     * @throws IllegalArgumentException If the width and height are out of bounds.
     */
    public abstract int getResultSlot(int width, int height) throws IllegalArgumentException;
    /**
     * @return How many slots are there in the vanilla inventory?
     *         <p></p>
     *         This behaves like array or list sizes, so that the first
     *         slot is <code>'0'</code> and the last slot is <code>'inventorySize() - 1'</code>
     */
    public abstract int getResultInventorySize();
    /**
     * @return Inventories may not start at the 0 slot;
     *         <p>Easiest example is crafting table's main inventory:
     *         </p> Slots 1 through 10 (for a size of 9); In that
     *         case, this number returns '<code>1</code>'
     */
    public abstract int getResultInventoryStart();
    /**
     * @return How wide is the result inventory?
     *         <p></p>
     *         <b>Positive</b>
     */
    public abstract int getResultInventoryWidth();
    /**
     * @return How tall is the result inventory?
     *         <p></p>
     *         <b>Positive</b>
     */
    public abstract int getResultInventoryHeight();
    /**
     * Returns a mythic recipe inventory from the provided inventory.
     * <p></p>
     * The one defined as the <b>result</b> inventory.
     *
     * @param inventory Inventory to extract ItemStacks from
     *
     * @return A built Mythic Recipe Inventory
     */
    @NotNull public MythicRecipeInventory getResultMythicInventory(@NotNull Inventory inventory) {

        // Create
        MythicRecipeInventory result = new MythicRecipeInventory();

        // For each result size
        for (int h = 0; h < getResultInventoryHeight(); h++) {

            // Make row
            ItemStack[] row = new ItemStack[getResultInventoryWidth()];

            for (int w = 0; w < getResultInventoryWidth(); w++) {

                // Fill row
                row[w] = get(inventory, getResultSlot(w, -h));
            }

            // Add row
            result.addRow(row);
        }

        // That's it
        return result;
    }
    //endregion

    //region Side Inventories
    /**
     * @param slot What slot you trying to read.
     *
     * @return For the Side inventory, this slot number,
     *         to what height does it translate to?
     *         <p></p>
     *         <b>Always Positive</b>
     *
     * @param side The name of the side inventory
     *
     * @throws IllegalArgumentException when the slot is not part of the inventory,
     *                                  or the side inventory of that name does not exist.
     */
    public abstract int getSideWidth(@NotNull String side, int slot) throws IllegalArgumentException;
    /**
     * @param slot What slot you trying to read.
     *
     * @return For the Side inventory, this slot number,
     *         to what height does it translate to?
     *         <p></p>
     *         <b>Always negative</b>
     *
     * @param side The name of the side inventory
     *
     * @throws IllegalArgumentException when the slot is not part of the inventory,
     *                                  or the side inventory of that name does not exist.
     */
    public abstract int getSideHeight(@NotNull String side,int slot) throws IllegalArgumentException;
    /**
     * @param width The Horizontal co-ordinate in the mythic inventory
     *               <p></p>
     *               <b>Always Positive</b>
     *
     * @param height The Vertical co-ordinate in the mythic inventory
     *               <p></p>
     *               <b>Always Negative</b>
     *
     * @return The inventory slot that these offsets correspond to
     *
     * @param side The name of the side inventory
     *
     * @throws IllegalArgumentException when the slot is not part of the inventory,
     *                                  or the side inventory of that name does not exist.
     */
    public abstract int getSideSlot(@NotNull String side,int width, int height) throws IllegalArgumentException;
    /**
     * @return Inventories may not start at the 0 slot;
     *         <p>Easiest example is crafting table's main inventory:
     *         </p> Slots 1 through 10 (for a size of 9); In that
     *         case, this number returns '<code>1</code>'
     */
    public abstract int getSideInventoryStart(@NotNull String side) throws IllegalArgumentException;
    /**
     * @return How many slots are there in the vanilla inventory?
     *         <p></p>
     *         This behaves like array or list sizes, so that the first
     *         slot is <code>'0'</code> and the last slot is <code>'inventorySize() - 1'</code>
     *
     * @param side The name of the side inventory
     *
     * @throws IllegalArgumentException when the slot is not part of the inventory,
     *                                  or the side inventory of that name does not exist.
     */
    public abstract int getSideInventorySize(@NotNull String side) throws IllegalArgumentException;
    /**
     * @return How wide is the Side inventory?
     *         <p></p>
     *         <b>Positive</b>
     *
     * @param side The name of the side inventory
     *
     * @throws IllegalArgumentException when the slot is not part of the inventory,
     *                                  or the side inventory of that name does not exist.
     */
    public abstract int getSideInventoryWidth(@NotNull String side) throws IllegalArgumentException;
    /**
     * @return How tall is the Side inventory?
     *         <p></p>
     *         <b>Positive</b>
     *
     * @param side The name of the side inventory
     *
     * @throws IllegalArgumentException when the slot is not part of the inventory,
     *                                  or the side inventory of that name does not exist.
     */
    public abstract int getSideInventoryHeight(@NotNull String side) throws IllegalArgumentException;
    /**
     * Returns a mythic recipe inventory from the provided inventory.
     *
     * @param inventory Inventory to extract ItemStacks from
     *
     * @return A built Mythic Recipe Inventory
     *
     * @param side The name of the side inventory
     *
     * @throws IllegalArgumentException when the slot is not part of the inventory,
     *                                  or the side inventory of that name does not exist.
     */
    @NotNull public MythicRecipeInventory getSideMythicInventory(@NotNull String side, @NotNull Inventory inventory) throws IllegalArgumentException {

        // No?
        if (!getSideInventoryNames().contains(side)) { throwSideInventoryException(side); }

        // Create
        MythicRecipeInventory sideInven = new MythicRecipeInventory();

        // For each result size
        for (int h = 0; h < getSideInventoryHeight(side); h++) {

            // Make row
            ItemStack[] row = new ItemStack[getSideInventoryWidth(side)];

            for (int w = 0; w < getSideInventoryWidth(side); w++) {

                // Fill row
                row[w] = get(inventory, getSideSlot(side, w, -h));
            }

            // Add row
            sideInven.addRow(row);
        }

        // That's it
        return sideInven;
    }
    /**
     * @return The names of all the side inventories of this station.
     */
    @NotNull public abstract ArrayList<String> getSideInventoryNames();
    /**
     * Returns a mythic recipe inventory from the provided inventory.
     * <p></p>
     * All those that we can consider a <b>side</b> inventories, like
     * the fuel slot of furnace or lapis slot of enchantment table.
     * <p></p>
     * Might be empty.
     *
     * @param inventory Inventory to extract ItemStacks from
     *
     * @return A built Mythic Recipe Inventory
     */
    @NotNull public HashMap<String, MythicRecipeInventory> getSideMythicInventories(@NotNull Inventory inventory) {

        // Build hash
        HashMap<String, MythicRecipeInventory> sides = new HashMap<>();

        for (String side : getSideInventoryNames()) {

            // Get and add
            sides.put(side, getSideMythicInventory(side, inventory));
        }

        // Yes
        return sides;
    }
    /**
     * Shorthand to throw the exception that says "HEY! Theres no such side inventory"
     *
     * @param name Name of the side inventory that didn't exist
     *
     * @throws IllegalArgumentException Always
     */
    public void throwSideInventoryException(@NotNull String name) throws IllegalArgumentException { throw new IllegalArgumentException("No such side inventory of name '" + name + "' for mapping " + getClass().getSimpleName()); }
    /**
     * Throws the 'Side Inventory not Found' Exception if the side inventory is, well if its not found.
     *
     * @param side The side inventory being requested.
     */
    public void considerThrowingSideException(@NotNull String side) { if (!getSideInventoryNames().contains(side)) { throwSideInventoryException(side); } }
    //endregion

    //region Apply to inventory
    /**
     * Applies the contents of a Mythic Recipe Inventory to this Inventory.
     * <p></p>
     * Targets the main inventory.
     *
     * @param inventory Inventory to dump the items to.
     *
     * @param finalMain Mythic Inventory to get Side from
     *
     * @param amountOnly Sometimes one knows for sure that the new and old inventories differ
     *                     only in the amount of items, in which case, it is more optimized to
     *                     decrease the amounts rather than replacing the items.
     */
    public void applyToMainInventory(@NotNull Inventory inventory, @NotNull MythicRecipeInventory finalMain, boolean amountOnly) {

        // Skips
        int z = 0;

        // For every main slot
        for (int s = getMainInventoryStart(); s < (getMainInventorySize() + getMainInventoryStart() + z); s++) {

            // Read
            int w = getMainWidth(s);
            int h = getMainHeight(s);

            // Any of them extraneous?
            if (w < 0 || h > 0) { z++; continue; }

            setInventoryItem(inventory, s, finalMain.getItemAt(w, h), amountOnly);
        }

        //APY//MythicCraftingManager.log("\u00a78Apply \u00a76M\u00a77 Applying to main inventory");
        //APY//for (int i = 1; i <= 9; i++) { MythicCraftingManager.log("\u00a78Apply \u00a76M\u00a77 Result: \u00a7b@" + i + " \u00a7f" + SilentNumbers.getItemName(inventory.getItem(i))); }
    }
    /**
     * Applies the contents of a Mythic Recipe Inventory to this Inventory.
     * <p></p>
     * Targets the result inventory.
     *
     * @param inventory Inventory to dump the items to.
     *
     * @param finalResult Mythic Inventory to get result from
     *
     * @param amountOnly Sometimes one knows for sure that the new and old inventories differ
     *                     only in the amount of items, in which case, it is more optimized to
     *                     decrease the amounts rather than replacing the items.
     */
    public void applyToResultInventory(@NotNull Inventory inventory, @NotNull MythicRecipeInventory finalResult, boolean amountOnly) {

        // For every main slot
        for (int s = getResultInventoryStart(); s < (getResultInventorySize() + getResultInventoryStart()); s++) { setInventoryItem(inventory, s, finalResult.getItemAt(getResultWidth(s), getResultHeight(s)), amountOnly); }

        //APY//MythicCraftingManager.log("\u00a78Apply \u00a76R\u00a77 Applying to result inventory");
        //APY//for (int i = 1; i <= 9; i++) { MythicCraftingManager.log("\u00a78Apply \u00a76R\u00a77 Result: \u00a7b@" + i + " \u00a7f" + SilentNumbers.getItemName(inventory.getItem(i))); }
    }
    /**
     * Applies the contents of a Mythic Recipe Inventory to this Inventory.
     * <p></p>
     * Targets a specific side inventory.
     *
     * @param inventory Inventory to dump the items to.
     *
     * @param finalSide Mythic Inventory to get result from
     *
     * @param sideKeyName The name of this side inventory. If it doesn't exist, this
     *                    method returns <code><b>false</b></code>.
     *
     * @param amountOnly Sometimes one knows for sure that the new and old inventories differ
     *                     only in the amount of items, in which case, it is more optimized to
     *                     decrease the amounts rather than replacing the items.
     */
    @SuppressWarnings("UnusedReturnValue") public boolean applyToSideInventory(@NotNull Inventory inventory, @NotNull MythicRecipeInventory finalSide, @NotNull String sideKeyName, boolean amountOnly) {
        if (!getSideInventoryNames().contains(sideKeyName)) { return false; }

        // For every main slot
        for (int s = getSideInventoryStart(sideKeyName); s < (getSideInventorySize(sideKeyName) + getSideInventoryStart(sideKeyName)); s++) { setInventoryItem(inventory, s, finalSide.getItemAt(getSideWidth(sideKeyName, s), getSideHeight(sideKeyName, s)), amountOnly); }

        //APY//MythicCraftingManager.log("\u00a78Apply \u00a76S\u00a77 Applying to side inventory");
        //APY//for (int i = 1; i <= 9; i++) { MythicCraftingManager.log("\u00a78Apply \u00a76S\u00a77 Result: \u00a7b@" + i + " \u00a7f" + SilentNumbers.getItemName(inventory.getItem(i))); }
        return true;
    }
    //endregion
    
    //region Mapping Instance
    /**
     * Makes use of all the get inventory methods to
     * build a MythicBlueprintInventory.
     * <p></p>
     * <b>Make sure the inventory is correct</b> to assimilate
     * with this inventory mapping, as it is untested what would
     * happen if you try to read, say a furnace inventory, with
     * the Crafting Table Mapping.
     *
     * @param inven The inventory you want to transcribe the contents of
     *
     * @return A built MythicBlueprintInventory.
     *
     * @see #getMainMythicInventory(Inventory)
     * @see #getResultMythicInventory(Inventory)
     * @see #getSideMythicInventories(Inventory)
     */
    @NotNull public MythicBlueprintInventory extractFrom(@NotNull Inventory inven) {

        //CRAFT//MythicCraftingManager.log("\u00a78Extract \u00a76I\u00a77 Getting Main Inventory...");
        MythicRecipeInventory main = getMainMythicInventory(inven);
        //CRAFT//MythicCraftingManager.log("\u00a78Extract\u00a76R\u00a77 Getting Result Inventory...");
        MythicRecipeInventory result = getResultMythicInventory(inven);
        //CRAFT//MythicCraftingManager.log("\u00a78Extract\u00a76S\u00a77 Getting Side Inventories...");
        HashMap<String, MythicRecipeInventory> sideInventories = getSideMythicInventories(inven);

        // Create blueprint alv
        MythicBlueprintInventory inventory = new MythicBlueprintInventory(main, result);
        for (String side : sideInventories.keySet()) { inventory.addSideInventory(side, sideInventories.get(side)); }

        // That's it
        return inventory;
    }
    /**
     * @param slot The slot the player clicked
     *
     * @return Is this slot one of the result slots?
     *         <p></p>
     *         Making this as <code>true</code> will indicate the
     *         event system that, when a player clicks this slot,
     *         they are trying to trigger the recipe result.
     */
    public abstract boolean isResultSlot(int slot);
    /**
     * Some crafting stations' result slot is the same
     * as the main one, in which this will return true,
     * and calling the result methods just redirects you
     * to the main methods.
     * <p></p>
     * However, you might want to treat the same MythicRecipeInventory
     * as both the main and result inventories in case it suffers changes
     * so that they remain the same.
     *
     * @return Is this crafting station result slots the same as the main slots?
     *         Brewing Stand, Enchantment Table, for example.
     */
    public abstract boolean mainIsResult();
    /**
     * @return The inventory this mapping is for.
     */
    @NotNull public abstract InventoryType getIntendedInventory();
    //endregion

    //region Utility Functions
    /**
     * Very specific to the functioning of Mythic Recipes,
     * honestly it just modifies the item amount except it
     * is fool-proof (nulls wont throw it off)
     *
     * @param inven Inventory you're looking at.
     * @param slot Slot where the item in question is.
     * @param item Item stack holding the amount information, 0 if <code>null</code>.
     */
    public static void setInventoryItemAmount(@Nullable Inventory inven, int slot, @Nullable ItemStack item) {
        if (inven == null) { return; }

        ItemStack target = inven.getItem(slot);
        if (target == null) { return; }

        int amount = 0;
        if (item != null) { amount = item.getAmount(); }

        target.setAmount(amount);
    }
    /**
     * Very specific to the functioning of Mythic Recipes,
     * honestly it just sets the following item except it
     * is fool-proof (nulls wont throw it off)
     *
     * @param inven Inventory you're looking at.
     * @param slot Slot where the item in question is.
     * @param item The item you are going to replace the current with.
     */
    public static void setInventoryItem(@Nullable Inventory inven, int slot, @Nullable ItemStack item) {
        if (inven == null) { return; }

        inven.setItem(slot, item);
    }
    /**
     * Redistributes to the appropriate method, for amount or for set.
     *
     * @param inven Inventory you're looking at.
     * @param slot Slot where the item in question is.
     * @param item The item you are going to replace the current with.
     * @param asAmountInstead Should we only modify amounts.
     *
     * @see #setInventoryItem(Inventory, int, ItemStack, boolean)
     * @see #setInventoryItemAmount(Inventory, int, ItemStack)
     */
    public static void setInventoryItem(@Nullable Inventory inven, int slot, @Nullable ItemStack item, boolean asAmountInstead) {
        if (inven == null) { return; }
        //CRAFT//MythicCraftingManager.log("\u00a78Write \u00a76S\u00a77 Putting to \u00a7e" + slot + "\u00a77 of \u00a7b" + inven.getType().toString() + "\u00a77 a \u00a7f" + SilentNumbers.getItemName(item));

        // As amount, set amounts
        if (asAmountInstead) {
            //CRAFT//MythicCraftingManager.log("\u00a78Write \u00a76A\u00a77 Only amount, to \u00a7f" + SilentNumbers.getItemName(inven.getItem(slot)));
            setInventoryItemAmount(inven, slot, item);

        // Not as amount, set item
        } else {
            //CRAFT//MythicCraftingManager.log("\u00a78Write \u00a76A\u00a77 Rewriting Item... \u00a7f" + SilentNumbers.getItemName(inven.getItem(slot)));
            setInventoryItem(inven, slot, item);
        }

        //CRAFT//MythicCraftingManager.log("\u00a78Write \u00a76R\u00a77 Result: \u00a7b@" + slot + " \u00a7f" + SilentNumbers.getItemName(inven.getItem(slot)));
    }
    /**
     * In {@link MythicRecipeInventory}ies, the definition of a <code>null</code>
     * ItemStack is very different from that of an <code>AIR</code> ItemStack.
     * <p></p>
     * Thus, this method will return an <code>new ItemStack(Material.AIR)</code> if
     * the slot is empty, which is the definition of air.
     * <p></p>
     * You must not use it for co-ordinates that point to no slot, because that
     * is the definition of <code>null</code> - that there is no slot to begin with.
     * <p></p>
     * Thus, this method throws an exception if the slot exceeds inventory size.
     *
     * @param inven Inventory to get item from
     * @param slot Slot to get item from
     *
     * @return The item contained, or an AIR item stack.
     */
    @NotNull public static ItemStack get(@NotNull Inventory inven, int slot) {

        // Ew
        if (inven.getSize() <= slot) { throw new IndexOutOfBoundsException("Attempted to get Non-Null item from a slot that does not exist."); }

        // All right get it
        ItemStack ret = inven.getItem(slot);

        // Return it or air
        return ret == null ? MythicCraftingManager.AIR : ret;
    }
    /**
     * Shorthand to throw the exception that says "HEY! Theres no such slot"
     *
     * @param out Slot number that caused the exception
     *
     * @throws IllegalArgumentException Always
     */
    void throwOutOfBounds(int out) throws IllegalArgumentException { throw new IllegalArgumentException("Mapping " + getClass().getSimpleName() + " has no data for slot '" + out + "'"); }
    /**
     * Shorthand to throw the exception that says "HEY! Theres no such slot"
     *
     * @param w Horizontal number that caused the exception
     * @param h Vertical number that caused the exception
     *
     * @throws IllegalArgumentException Always
     */
    void throwOutOfBounds(int w, int h) throws IllegalArgumentException { throw new IllegalArgumentException("Mapping " + getClass().getSimpleName() + " has no data for slot at width '" + w + "' and height '" + h + "'"); }
    //endregion

    //region Managing Section
    @NotNull static HashMap<InventoryType, VanillaInventoryMapping> vanillaMappings = new HashMap<>();
    @NotNull static ArrayList<VanillaInventoryMapping> customInventoryMappings = new ArrayList<>();
    /**
     * Registers all vanilla mappings so that they can be found anywhere.
     */
    public static void registerAll() {

        // Clears the mappings
        vanillaMappings.clear();

        // Register
        vanillaMappings.put(InventoryType.WORKBENCH, new WorkbenchMapping());
        vanillaMappings.put(InventoryType.CRAFTING, new CraftingMapping());
        vanillaMappings.put(InventoryType.FURNACE, new FurnaceMapping());
        registerCustomMapping(SuperWorkbenchMapping.SWB);
        registerCustomMapping(MegaWorkbenchMapping.MWB);
        if (ServerVersion.get().getMinor() >= 16) { vanillaMappings.put(InventoryType.valueOf("SMITHING"), new SmithingStationMapping()); }
    }

    /**
     * Registers a custom crafting station, hooked to a custom inventory yes.
     *
     * @param vmp Vanilla inventory mapping intended for {@link InventoryType#CHEST}
     *            and for {@link MythicRecipeStation#CUSTOM} that you wish to load
     *            and be detected. It must implement {@link CustomInventoryCheck}
     */
    public static void registerCustomMapping(@NotNull VanillaInventoryMapping vmp) {

        // Cancel that shit
        if (!(vmp instanceof CustomInventoryCheck)) { return; }
        if (vmp.getIntendedInventory() != InventoryType.CHEST) { return; }

        // Add it I guess
        customInventoryMappings.add(vmp);
    }
    /**
     * Get the mapping pertaining to this crafting station.
     *
     * @param inven The inventory type of the station.
     *
     * @return The correct mapping, if found.
     */
    @Nullable public static VanillaInventoryMapping getMappingFor(@NotNull InventoryView inven) {

        // Is it a chest?
        if (inven.getType() == InventoryType.CHEST && customInventoryMappings.size() > 0) {

            // Might be custom, check through all mappings for the inventory view
            for (VanillaInventoryMapping vmp : customInventoryMappings) {

                // Good Inventory View?
                if (((CustomInventoryCheck) vmp).IsTargetInventory(inven)) {

                    // Success this is it boys
                    return vmp;
                }
            }

            // :gruno:
            return null;

        // Not a chest, treat normally
        } else {

            return vanillaMappings.get(inven.getType());
        }
    }
    /**
     * @return Which live recipe type will this mapping be used with?
     *         <p></p>
     *         Allows to easily identify the list of recipes to use
     *         at the same time as one reads the inventory of this
     *         station.
     */
    @Nullable public abstract MythicRecipeStation getIntendedStation();
    //endregion
}
