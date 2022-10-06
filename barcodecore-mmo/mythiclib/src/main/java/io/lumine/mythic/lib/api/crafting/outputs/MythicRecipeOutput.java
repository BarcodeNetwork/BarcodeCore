package io.lumine.mythic.lib.api.crafting.outputs;

import io.lumine.mythic.lib.api.crafting.ingredients.MythicBlueprintInventory;
import io.lumine.mythic.lib.api.crafting.ingredients.MythicRecipeInventory;
import io.lumine.mythic.lib.api.crafting.recipes.MythicCachedResult;
import io.lumine.mythic.lib.api.crafting.recipes.vmp.VanillaInventoryMapping;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * When talking about custom crafting, the intuitive output
 * is another item, made from items made past.
 * <p></p>
 * But I cannot help but think, could there be more kinds of
 * results to crafting than just an item in a slot?
 *
 * @author Gunging
 */
public abstract class MythicRecipeOutput {

    /**
     * When this recipe succeeds, what do display in the result inventory?
     * <p></p>
     * <b>Fill it slot by slot.</b>
     *
     * @param inventory Inventory of the result, carries information on what
     *                  is already there I suppose.
     *
     * @return The inventory how it should display instead.
     */
    @NotNull public abstract MythicRecipeInventory applyDisplay(@NotNull MythicBlueprintInventory inventory, @NotNull InventoryClickEvent eventTrigger, @NotNull VanillaInventoryMapping mapping);

    /**
     * When this recipe is actually crafted, what is the actual result of this recipe?
     * <p></p>
     * Note that the display items will have been destroyed (amount = 0), and the passed
     * inventory is more of a layout/size information wrapper.
     * <p></p>
     * The complete behaviour of applying is left to this method,
     * including subtracting the ingredients used to craft.
     * <p></p>
     * Other logic that this method must handle:
     * <p><code>*</code> Calculating how many times it actually gets crafted (supposing the extra times don't fit)
     * </p><code>*</code> Putting the items in the correct places (even cursor)
     * <p><code>*</code> Giving overflow items to the player directly
     *
     * @param resultInventory Inventory of the result, carries information on what
     *                  is already there I suppose. A bit redundant since you
     *                  can just get it from the event itself and through
     *                  {@link VanillaInventoryMapping}.
     *
     * @param otherInventories Also redundant since you could get them using the mappings,
     *                         but this just moves the trouble elsewhere.
     *
     * @param cache For an inventory craft event to get this far, it must have succeeded
     *              on a inventory prep procedure. This carries information on that success.
     *
     * @param eventTrigger The event itself, in the middle of running.
     *                     By default, at LOWEST priority.
     *
     * @param map Vanilla map used for this crafting table
     *
     * @param times The number of times the player is crafting this at once.
     *              <p></p>
     *              Basically, <code>1</code> if the player is clicking normally,
     *              or, if the player is Shift+Clicking, the amount of times the
     *              ingredients must be consumed to carry this out into completion.
     *              <p></p>
     *              Note that <b>this may exceed the inventory space of the player</b>
     *              and you must account for it in this method.
     *
     */
    public abstract void applyResult(@NotNull MythicRecipeInventory resultInventory, @NotNull MythicBlueprintInventory otherInventories, @NotNull MythicCachedResult cache, @NotNull InventoryClickEvent eventTrigger, @NotNull VanillaInventoryMapping map, int times);

    /**
     * Easy access, method to consume the ingredients of the recipe <b>once</b>. If the
     * player is crafting the recipe 5 times at once, that's what the <code>times</code>
     * parameter is for: Running this method 5 times (kind of).
     * <p></p>
     * Literally just call it from within {@link #applyResult(MythicRecipeInventory, MythicBlueprintInventory, MythicCachedResult, InventoryClickEvent, VanillaInventoryMapping, int)}
     * by adding the following code line:
     * <p><code><b>consumeIngredients(otherInventories, cache, eventTrigger.getInventory(), map, times);</b></code></p>
     *
     * @param originalInventories The inventories as they were before the operation
     * @param cache The cached result information.
     *              <br><br>
     *              The expectation is that ingredients have only been consumed, and
     *              any other output (like empty buckets when crafting a cake) that
     *              is given to the player in the ingredient section will be accounted
     *              for elsewhere.
     *              <br><br>
     *              This method CONSUMES INGREDIENTS
     * @param inven The actual inventory of the station that will reflect the changes
     * @param map The Vanilla Mapping to use
     * @param times The amount of times the player is crafting this recipe.
     */
    public void consumeIngredients(@NotNull MythicBlueprintInventory originalInventories, @NotNull MythicCachedResult cache, @NotNull Inventory inven, @NotNull VanillaInventoryMapping map, int times) {

        /*
         * Consume the crafted ingredients, we will perform the operation on the
         * "Other Inventories" blueprint (that has now been modified by the event)
         * and then copy them onto the true inventory.
         */
        MythicRecipeInventory main = originalInventories.getMainInventory();
        processInventory(main, cache.getResultOfOperation().getMainInventory(), times);
        map.applyToMainInventory(inven, main, false);

        // Now do all side inventories
        for (String sideName : originalInventories.getSideInventoryNames()) {

            // Find the original
            MythicRecipeInventory side = originalInventories.getSideInventory(sideName);

            // If contained
            if (cache.getResultOfOperation().hasSideInventories(sideName)) {

                // Get side result and process it
                MythicRecipeInventory sideResult = cache.getResultOfOperation().getSideInventory(sideName);
                processInventory(side, sideResult, times);

                // Apply
                map.applyToSideInventory(inven, side, sideName, false);
            }
        }
    }

    /**
     * Overwrites the contents of the original inventory by those in the overwrite inventory,
     * only where the overwrite inventory has something else than <code>null</code> specified.
     * <p></p>
     * This does not apply to the actual real vanilla inventory.
     *
     * @param original Original Mythic Inventory
     * @param overwrite Overwrite Mythic Inventory
     * @param multiplier Number of times you are crafting
     */
    public void processInventory(@NotNull MythicRecipeInventory original, @NotNull MythicRecipeInventory overwrite, int multiplier) {
        for (int h = 0; h < original.getHeight(); h++) {
            for (int w = 0; w < original.getWidth(); w++) {

                // Get cached version
                ItemStack stack = overwrite.getItemAt(w, -h);
                //RR//MythicCraftingManager.log("\u00a78Result \u00a74L\u00a77 At \u00a7e" + w + ":" + h + "\u00a77, \u00a7b" + SilentNumbers.getItemName(stack));

                // Null? ignore
                if (stack == null) { continue; }

                // Get current
                ItemStack current = original.getItemAt(w, -h);
                //RR//MythicCraftingManager.log("\u00a78Result \u00a74L\u00a77 Replacing \u00a7b" + SilentNumbers.getItemName(current));

                // Null? ignore (There should be no slot in this case lol)
                if (current == null) { continue; }

                // Calculate final amount
                int currentAmount = current.getAmount();
                int targetAmount = stack.getAmount();
                int difference = currentAmount - targetAmount;

                // Only amount?
                if (current.isSimilar(stack)) {

                    /*
                     * If the item stack is the same, we can consider the amount to
                     * be subtracted (or added?), so that multiple times of crafting
                     * will yield the difference over and over... To a limit of 0.
                     */
                    int finalAmount = Math.max(currentAmount - (difference * multiplier), 0);
                    current.setAmount(finalAmount);
                    //RR//MythicCraftingManager.log("\u00a78Result \u00a74A\u00a77 Similar, amount only \u00a7b" + SilentNumbers.getItemName(current));
                    //RR//MythicCraftingManager.log("\u00a78Result \u00a74A\u00a77 Pre Result: " + SilentNumbers.getItemName(original.getItemAt(w, -h)));

                    // Replace entirely
                } else {
                    /*
                     * When we are putting the amount its a little different, it makes
                     * no sense unless we are replacing the stuff, which means that
                     * the amount should not consider the original amount.
                     */
                    int finalAmount = targetAmount * multiplier;
                    current = stack;
                    current.setAmount(finalAmount);
                    //RR//MythicCraftingManager.log("\u00a78Result \u00a74A\u00a77 Different, overwriting \u00a7b" + SilentNumbers.getItemName(current));
                }

                // Set item
                original.setItemAt(w, -h, current);
                //RR//MythicCraftingManager.log("\u00a78Result \u00a74R\u00a77 Actual Result: " + SilentNumbers.getItemName(original.getItemAt(w, -h)));
            } }
    }
}
