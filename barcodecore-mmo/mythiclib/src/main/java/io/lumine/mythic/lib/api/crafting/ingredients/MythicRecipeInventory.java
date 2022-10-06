package io.lumine.mythic.lib.api.crafting.ingredients;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * This represents a grid of item stacks, as well as provides utilities
 * to translate normal inventories into this format that can be unambiguously
 * checked by {@link io.lumine.mythic.lib.api.crafting.recipes.MythicRecipe}s.
 *
 * <p></p>
 * <b>Note that <code>null</code> and <code>Material.AIR</code> ItemStacks
 * mean two very different things for this class:</b>
 * <p><code>null</code> Means that the inventory does not have a slot there.
 * </p><code>AIR</code> Means that there is no item in that slot.
 *
 * <p></p>
 * This is because crafting grids that are not rectangles are supported by this
 * system (vanilla grids, say the workbench -- a 3x3 square -- wont ever have
 * <code>null</code> ItemStacks, this is meant for custom crafting GUIs idk).
 *
 * @author Gunging
 */
@SuppressWarnings("unused")
public class MythicRecipeInventory implements Cloneable {

    /**
     * The baked matrix of ItemStacks.
     * <p></p>
     * <code>null</code> elements are allowed, and mean a
     * literal gap in the slots. Not that it occurs in any
     * vanilla station though.
     * <p></p>
     * An <code>AIR</code> item stack indicates that there
     * is a slot there, but it has nothing in it.
     * <p></p>
     * The sizes of the arrays are guaranteed to be the same,
     * so that this is a square matrix of a height {@link #getHeight()}
     * and width
     */
    @NotNull private final HashMap<Integer, ArrayList<ItemStack>> contents = new HashMap<>();

    /**
     * Note that this does <b>not</b> use the {@link ShapedIngredient} convention.
     *
     * Thus this is <b>always positive</b>.
     *
     * @return Height of this inventory representation
     */
    public int getHeight() { return contents.size(); }

    /**
     * Note that this does <b>not</b> use the {@link ShapedIngredient} convention.
     *
     * Thus this is <b>always positive</b>.
     *
     * @return Width of this inventory representation.
     */
    public int getWidth() {

        // Does content have size?
        if (getHeight() == 0) { return 0; }

        // Return the size of the very first one
        return contents.get(0).size();
    }

    /**
     * These co-ordinates follow the same convention as {@link ShapedIngredient}.
     *
     * @param width The horizontal shift in slots relative to the top left corner. <p>
     *              <b>Always Positive</b></p>
     *
     * @param height The vertical shift in slots relative to the top left corner. <p>
     *               <b>Always Negative</b></p>
     *
     * @return The item stack at these co-ordinates.
     *         <p></p>
     *         Will return an <code>AIR</code> ItemStack sometimes,
     *         not to be confused with a <code>null</code> ItemStack:
     *         <p></p>
     *         <code>Null</code> item stacks happen when the inventory has no
     *         slot at those coordinates:
     *         <p>1- The indexes are out of range
     *         </p>2- The index represents an invalid position for an item to be.
     *         <p>3- Doesn't really happen in vanilla mc stations</p>
     *
     * @see MythicRecipeIngredient The general description of this class has more information
     *                             on the difference between <code>AIR</code> and <code>null</code>.
     */
    @Nullable public ItemStack getItemAt(int width, int height) {

        // Negate lol
        height *= -1;

        // Out of range?
        if (height < 0 || width < 0 || height >= getHeight() || width >= getWidth()) {
            if (height < 0) { MythicLib.plugin.getLogger().log(Level.WARNING, "Positive height provided, which is WRONG: " + height); }
            return null; }

        // All right, get that row
        ArrayList<ItemStack> targetRow = contents.get(height);

        // That's it
        return targetRow.get(width);
    }

    /**
     * Puts an item at these co-ordinates in the matrix. However,
     * this method <b>cannot</b> increase width or height, and
     * will return <code>false</code> if you specify co-ordinates
     * out of range.
     * <p></p>
     * These co-ordinates follow the same convention as {@link ShapedIngredient}.
     * <p></p>
     * Remember that setting an item to <code>null</code> is not the same
     * as to <code>new ItemStack(Material.AIR)</code>:
     * <p><code>AIR</code> means this slot is empty.
     * </p><code>NULL</code> means this slot does not exist.
     *
     * @param height The vertical shift in slots relative to the top left corner. <p>
     *               <b>Always Negative</b></p>
     * @param width The horizontal shift in slots relative to the top left corner. <p>
     *              <b>Always Positive</b></p>
     *
     * @return If the placing was successful.
     *
     * @see MythicRecipeIngredient The general description of this class has more information
     *                             on the difference between <code>AIR</code> and <code>null</code>.
     *
     * @see MythicCraftingManager#AIR
     */
     public boolean setItemAt(int width, int height, @Nullable ItemStack item) {

        // Negate lol
        height *= -1;

        // Out of range?
        if (height < 0 || width < 0 || height >= getHeight() || width >= getWidth()) { return false; }

        // All right, get that row
        ArrayList<ItemStack> targetRow = contents.get(height);

        // Remove old and insert
        targetRow.remove(width);
        targetRow.add(width, item);
        return true;
    }

    /**
     * Sets the information of a row of ItemStacks
     * <p></p>
     * <code>null</code> elements are allowed, and mean a
     * literal gap in the slots. Not that it occurs in any
     * vanilla station though.
     * <p></p>
     * An <code>AIR</code> item stack indicates that there
     * is a slot there, but it has nothing in it.
     *
     * @param contents The Item Stacks, in order, to put.
     */
    public void addRow(@Nullable ItemStack... contents) {

        // Set the row at the max height of this.
        setRow(getHeight(), contents);
    }

    /**
     * Sets the information of a row of ItemStacks
     * <p></p>
     * <code>null</code> elements are allowed, and mean a
     * literal gap in the slots. Not that it occurs in any
     * vanilla station though.
     * <p></p>
     * An <code>AIR</code> item stack indicates that there
     * is a slot there, but it has nothing in it.
     *
     * @param contents The Item Stacks, in order, to put.
     * @param row The row number, <b>must be positive.</b>
     *            <p></p>
     *            Empty rows may be created, all full of null ItemStacks.
     */
    public void setRow(int row, @Nullable ItemStack... contents) {
        // No
        Validate.isTrue(row >= 0, "Mythic Recipe Inventories cannot have a negative number of rows.");

        // Create required rows, if they are not defined
        createRowsUntil(row);

        /*
         * Bake contents and register them onto thay row.
         */
        ArrayList<ItemStack> fill;

        // No contents information? An array full of null elements then
        if (contents == null) { fill = fill(new ArrayList<>()); }

        // Contents information existed, lets see
        else {

            /*
             * Transform into a list, and make sure its of the appropriate size.
             *
             * If these contents are greater than the current width, every array registered
             * will be updated to this size to preserve the uniform row size expectation.
             */
            fill = fill(SilentNumbers.toArrayList(contents));

        }

        // Register
        this.contents.put(row, fill);
        //CRAFT// MythicCraftingManager.log("\u00a78Inventory \u00a76R\u00a77 Setting Row \u00a7f" + row +"\u00a77 as " + SilentNumbers.collapseList( SilentNumbers.transcribeList(fill, (o) -> SilentNumbers.getItemName(((ItemStack) o))), "\u00a77,\u00a7f "));
    }

    /**
     * Modifies the contents so that they are continuous at least up to this row number.
     * <p></p>
     * Created rows will have <code>non-null</code> arrays of the required width, but
     * full of <code>null</code> entries.
     *
     * @param row Row number (inclusive) up to which the contents must be defined.
     *            <p></p>
     *            <b>Must be positive</b>
     */
    void createRowsUntil(int row) {
        // No
        Validate.isTrue(row >= 0, "Mythic Recipe Inventories cannot have a negative number of rows.");

        // For each
        for (Integer i = 0; i <= row; i++) {

            // Does the row exist?
            if (contents.get(i) != null) { continue; }

            // Register
            contents.put(i, fill(new ArrayList<>()));
        }
    }

    /**
     * Makes an array of the appropriate size, according to the current {@link #getWidth()}.
     *
     * @param list Array to Resize.
     *             <p></p>
     *             <b>This list, if registered into <code>contents</code>, is assumed to not
     *             have a size greater than {@link #getWidth()}, and that if a list does have
     *             a size greater than {@link #getWidth()}, it is not in <code>contents</code>
     *             <i>yet</i>;</b> This latter case prompts a {@link #updateRowSizesTo(int)} call.
     *
     * @return An array of size {@link #getWidth()}, full of <code>null</code> ItemStacks
     *         if necessary.
     */
    ArrayList<ItemStack> fill(@NotNull ArrayList<ItemStack> list) {

        // Is this list greater than getWidth()? Prompt a resize.
        if (list.size() >= getWidth()) {

            /*
             * Update all contents to match this larger list.
             *
             * This method cancels itself if the number equals getWidth().
             */
            updateRowSizesTo(list.size());

            // The list width is fine now.
            return new ArrayList<>(list);
        }

        // Ret
        ArrayList<ItemStack> ret = new ArrayList<>();

        // Add or copy
        for (int j = 0; j < getWidth(); j++) {

            // There is still an entry corresponding
            if (list.size() > j) {

                // Copy it over (may be null)
                ret.add(list.get(j));

            // Original array is too short
            } else {

                // Add new null entry
                ret.add(null);
            }
        }

        // Yes
        return ret;
    }

    /**
     * Changes the size of all rows to having this size.
     * <p></p>
     * <b>It should never be called with a number equal
     * or less than {@link #getWidth()}.</b> Wont throw
     * exceptions but it is not expected and may have
     * unintended consequences.
     *
     * @param size New row size.
     */
    void updateRowSizesTo(int size) {

        // Nothing to do to begin with
        if (getHeight() == 0) { return; }

        // If size equals width, huh?
        if (size == getWidth()) { return; }

        /*
         * To use the fill(ArrayList<ItemStack>) method,
         * we only need the very first content array with
         * us so that getWidth() now returns the correct
         * updated number.
         */
        ArrayList<ItemStack> first = contents.get(0);

        // Fill it
        for (int j = 0; j < size; j++) {

            /*
             * When the size equals j, add a new (null) entry.
             * This will cause the size to be greater than j, by 1.
             *
             * Next iteration, same thing will happen, until j == size.
             * At this point, the size of first will have been size since
             * last iteration, which is what we want.
             */
            if (first.size() == j) {

                // Add new null entry
                first.add(null);
            }
        }

        // Put back, not sure if necessary but eh
        contents.put(0, first);

        // Now go through the rest of the lists
        for (int l = 1; l < getHeight(); l++) {

            // Get list at
            ArrayList<ItemStack> lst = contents.get(l);

            // Replace
            contents.put(l, fill(lst));
        }
    }

    /**
     * Clears the information contained of this inventory representation
     */
    public void clearContents() { contents.clear(); }

    /**
     * Set all non-null contents to AIR, keeping null contents null.
     * <p></p>
     * Basically provides a clean inventory of the same size, but basically empty.
     */
    public void clean() {

        // Make everything air
        for (int w = 0; w < getWidth(); w++) {
            for (int h = 0; h < getHeight(); h++) {

                // Set as air
                if (getItemAt(w, -h) != null) {

                    setItemAt(w, -h, MythicCraftingManager.AIR);
                }
            } }
    }

    /**
     * Clones deeply so that the ItemStack information contained
     * is independent of the original.
     *
     * @return A mirror of this inventory, ready to be edited
     *         without conflicting with the original.
     */
    @Override public MythicRecipeInventory clone() {
        try { super.clone(); } catch (CloneNotSupportedException ignored) {}

        // New
        MythicRecipeInventory ret = new MythicRecipeInventory();

        for (Map.Entry<Integer, ArrayList<ItemStack>> row : contents.entrySet()) {

            // Get array there
            ItemStack[] newRow =new ItemStack[getWidth()];

            // Clone each item
            for (int w = 0; w < getWidth(); w++) {

                // Get at width
                ItemStack stacc = row.getValue().get(w);

                // Build
                if (stacc != null) {

                    // Put
                    newRow[w] = stacc.clone();

                } else {

                    // Add null yes
                    newRow[w] = null;
                }
            }

            // Add row
            ret.setRow(row.getKey(), newRow);
        }

        // That's it
        return ret;
    }

    /**
     * Attempts to get the first valid ItemStack it finds.
     *
     * @return <code>null</code> if empty (all entries are <code>AIR</code> or <code>null</code>)
     */
    @Nullable
    public ItemStack getFirst() {

        // Iterate all items
        for (ArrayList<ItemStack> cont : contents.values()) { for (ItemStack itm : cont) {

                // Non null and a valid item? This is the one
                if (!SilentNumbers.isAir(itm)) { return itm; }

            } }

        // None found
        return null;
    }

    /**
     * @return To easily log the contents
     */
    @NotNull public ArrayList<String> toStrings(@NotNull String prefix) {

        // Go through every row and add the contents of it
        ArrayList<String> ret = new ArrayList<>();
        for (int r = 0; r < contents.size(); r++) {

            // Find row of this index
            ArrayList<ItemStack> fill = contents.get(r);
            if (fill == null) { continue; }

            // Add the contents of this row
            ret.add(prefix + SilentNumbers.collapseList( SilentNumbers.transcribeList(fill, (o) -> SilentNumbers.getItemName(((ItemStack) o))), "\u00a77,\u00a7f "));
        }
        return ret;
    }
}
