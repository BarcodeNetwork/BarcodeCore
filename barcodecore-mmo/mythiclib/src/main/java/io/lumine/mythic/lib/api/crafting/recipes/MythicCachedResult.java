package io.lumine.mythic.lib.api.crafting.recipes;

import io.lumine.mythic.lib.api.crafting.ingredients.MythicBlueprintInventory;
import io.lumine.mythic.lib.api.crafting.ingredients.MythicRecipeInventory;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.api.item.SupportedNBTTagValues;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

/**
 * Trying to read the recipe when preparing <b>and</b> when crafting is a bit
 * redundant, so that the prep method might cache if it makes sense.
 *
 * @author Gunging
 */
@SuppressWarnings("unused")
public class MythicCachedResult {

    /**
     * All the current loaded results.
     */
    @NotNull final static HashMap<UUID, MythicCachedResult> results = new HashMap<>();

    /**
     * Destroys this cached result, removes it from the loaded ones and its done for.
     */
    public void unload() { results.remove(operationIdentifier); }

    /**
     * Path of the Cached Tag
     */
    public static final String cacheNBTTagPath = "MYTHICLIB_CRAFTING";

    /**
     * To correctly link item to MythicCachedResult
     */
    @NotNull final UUID operationIdentifier;
    /**
     * To correctly link item to MythicCachedResult
     */
    @NotNull public UUID getOperationIdentifier() { return operationIdentifier; }

    /**
     * What was the result of the original operation?
     * <p></p>
     * Only fully essential when crafting once, as a multi-crafting
     * operation will just generate results over and over.
     */
    @NotNull final MythicBlueprintInventory resultOfOperation;
    /**
     * What was the result of the original operation?
     */
    @NotNull public MythicBlueprintInventory getResultOfOperation() { return resultOfOperation; }

    /**
     * What operation was carried out
     */
    @NotNull final MythicRecipeBlueprint operation;
    /**
     * What operation was carried out
     */
    @NotNull public MythicRecipeBlueprint getOperation() { return operation; }

    /**
     * Is there a link between this item and any MythicCachedResult?
     * Will destroy the item if it has the tag also (set amount to 0)
     *
     * @param item Item that you are testing
     *
     * @return a MythicCachedResult iff this item is linked to one.
     */
    @Nullable public static MythicCachedResult get(@Nullable ItemStack item) {

        // Yea no
        if (item == null) {
            //CSF//MythicCraftingManager.log("\u00a78Cache \u00a73F\u00a77 Item '\u00a7b" + SilentNumbers.getItemName(item) + "\u00a77' not existent");
            return null; }

        // Get NBT
        NBTItem asNBT = NBTItem.get(item);

        // Find tag
        ItemTag tag = ItemTag.getTagAtPath(cacheNBTTagPath, asNBT, SupportedNBTTagValues.STRING);

        // No tag?
        if (tag == null) {
            //CSF//MythicCraftingManager.log("\u00a78Cache \u00a73T\u00a77 Item '\u00a7b" + SilentNumbers.getItemName(item) + "\u00a77' has no tag");
            return null; }

        // All right destroy the item
        item.setAmount(0);

        // Build UUID
        UUID uuid = SilentNumbers.UUIDParse((String) tag.getValue());

        // L
        if (uuid == null) {
            //CSF//MythicCraftingManager.log("\u00a78Cache \u00a73U\u00a77 Item '\u00a7b" + SilentNumbers.getItemName(item) + "\u00a77' tag has invalid UUID");
            return null; }

        // Find
        MythicCachedResult ret = results.get(uuid);

        // No?
        if (ret == null) {
            //CSF//MythicCraftingManager.log("\u00a78Cache \u00a73E\u00a7c Item '\u00a7b" + SilentNumbers.getItemName(item) + "\u00a7c' UUID links to no Cache. \u00a77This is bad");
            return null;
        }

        //CSF//MythicCraftingManager.log("\u00a78Cache \u00a73S\u00a7a Found Cache for " + SilentNumbers.getItemName(item));
        results.remove(uuid);
        return ret;
    }

    /**
     * The number of times that the player will craft this recipe.
     *         <p></p>
     *         Generally, 1 if the player clicked normally. Otherwise (shift+click)
     *         it is the times the recipe can be crafted until the ingredients run out.
     */
    int times;
    /**
     * @return The number of times that the player will craft this recipe.
     *         <p></p>
     *         Generally, 1 if the player clicked normally. Otherwise (shift+click)
     *         it is the times the recipe can be crafted until the ingredients run out.
     */
    public int getTimes() { return times; }
    /**
     * @param t The number of times that the player will craft this recipe.
     *          <p></p>
     *          Generally, 1 if the player clicked normally. Otherwise (shift+click)
     *          it is the times the recipe can be crafted until the ingredients run out.
     */
    public void setTimes(int t) { times = t; }

    /**
     * Cache the result of correctly evaluating a recipe to display it.
     *
     * @param operationIdentifier Unique ID of this result so that it is unmistakable.
     * @param resultOfOperation The cached result of the operation. <br>
     *                          This contains information only on the final
     *                          amounts of each item upon crafting once, but
     *                          not on the output of the recipe, as the MythicRecipeOutput
     *                          has not been called.
     * @param operation The operation information. Just in case
     *
     * @param resultInventoryDisplay To correctly link this cached result, and to easily destroy
     *                               unwanted items if it even becomes necessary, cache will add
     *                               a tag to every display item.
     */
    public MythicCachedResult(@NotNull UUID operationIdentifier, @NotNull MythicBlueprintInventory resultOfOperation, @NotNull MythicRecipeBlueprint operation, @NotNull MythicRecipeInventory resultInventoryDisplay, int times) {
        this.operationIdentifier = operationIdentifier;
        this.resultOfOperation = resultOfOperation;
        this.operation = operation;
        this.times = times;

        //CSH//MythicCraftingManager.log("\u00a78Cache \u00a73C\u00a77 Created at UUID \u00a7b" + operationIdentifier.toString() + "\u00a79 ----|");
        //CSH//for (String str : resultOfOperation.getMainInventory().toStrings("\u00a78Cache \u00a79OM-")) { MythicCraftingManager.log(str); }
        //CSH//for (String str : resultOfOperation.getResultInventory().toStrings("\u00a78Cache \u00a79OR-")) { MythicCraftingManager.log(str); }

        // Store Reference
        results.put(operationIdentifier, this);

        // Distribute Tags
        for (int h = 0; h < resultInventoryDisplay.getHeight(); h++) {
            for (int w = 0; w < resultInventoryDisplay.getWidth(); w++) {

                // Get Item
                ItemStack item = resultInventoryDisplay.getItemAt(w, -h);

                // Found?
                if (item != null) {

                    // All right add tag
                    NBTItem nbt = NBTItem.get(item);

                    // Yes
                    nbt.addTag(new ItemTag(cacheNBTTagPath, operationIdentifier.toString()));

                    // Set item ig
                    resultInventoryDisplay.setItemAt(w, -h, nbt.toItem());
                    //CSH//MythicCraftingManager.log("\u00a78Cache \u00a73T\u00a77 Tagged \u00a7b" + SilentNumbers.getItemName(item));
                }
            }
        }
    }
}
