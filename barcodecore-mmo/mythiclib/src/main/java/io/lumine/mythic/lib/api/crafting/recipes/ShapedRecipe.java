package io.lumine.mythic.lib.api.crafting.recipes;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.crafting.ingredients.MythicIngredient;
import io.lumine.mythic.lib.api.crafting.ingredients.MythicRecipeIngredient;
import io.lumine.mythic.lib.api.crafting.ingredients.MythicRecipeInventory;
import io.lumine.mythic.lib.api.crafting.ingredients.ShapedIngredient;
import io.lumine.mythic.lib.api.crafting.outputs.MRORecipe;
import io.lumine.mythic.lib.api.crafting.uimanager.ProvidedUIFilter;
import io.lumine.mythic.lib.api.util.Ref;
import io.lumine.mythic.lib.api.util.ui.FFPMythicLib;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackProvider;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Obviously the Mythic Recipe for workbench and the 2x2 inventory grid,
 * and any size of grid actually (up to 6x9?).
 * <p></p>
 * Uses {@link ShapedIngredient}s, with offsets information
 *
 * @author Gunging
 */
public class ShapedRecipe extends MythicRecipe implements VanillaBookableRecipe {

    /**
     * A Mythic Recipe that checks for an ordered pattern of items
     * spread out through a {@link MythicRecipeInventory}
     *
     * @param name Name of the recipe
     *
     * @param ingredients The list of ingredients with their offsets information.
     */
    public ShapedRecipe(@NotNull String name, @NotNull ArrayList<ShapedIngredient> ingredients) {
        super(name, new ArrayList<>(ingredients));
        recalculateSize();
    }

    /**
     * Simply enough, a 1x1 recipe with only one ingredient,
     * that accepts all these as substitutes.
     * <p></p>
     * For those times when you literally just want something
     * in the result slot of the crafting table or furnace,
     * as the {@link MRORecipe}
     * accepts only Shaped Recipes.
     *
     * @param poofs The many substitutes that can satisfy this recipe
     * @param name The name of this recipe.
     *
     * @return A 1x1 recipe with only 1 ingredient at 0,0.
     */
    public static ShapedRecipe single(@NotNull String name, @NotNull ProvidedUIFilter... poofs) {

        // Create Ingredient
        ShapedIngredient result = new ShapedIngredient(new MythicIngredient(name, poofs), 0, 0);

        // Create Recipe
        return new ShapedRecipe(name, result);
    }

    /**
     * Crops a recipe to leave out the exterior rows and columns of AIR out.
     * <p></p>
     * Example (A 4x5 recipe cuz why not:
     * <p><code>AIR AIR AIR AIR AIR</code>
     * </p><code>AIR <b>BOW</b> <b>EYE</b> AIR AIR</code>
     * <p><code>AIR <b>EGG</b> <b>RAY</b> AIR AIR</code>
     * </p><code>AIR <b>TAG</b> AIR AIR AIR</code>
     * <p></p>
     * WIll cut out the exterior rows and columns only,
     * resulting in:
     * <p><code>BOW EYE</code>
     * </p><code>EGG RAY</code>
     * <p><code>TAG AIR</code>
     *
     * @param sharpedRecipe Source recipe with as much air as you want.
     *
     * @return A recipe with no exterior columns made entirely of AIR.
     *         <p></p>
     *         Its not the same exact one you provide, this method kinda
     *         clones it.
     */
    @SuppressWarnings("unused")
    public static ShapedRecipe unsharpen(@NotNull ShapedRecipe sharpedRecipe) {
        //SHARP//MythicCraftingManager.log("\u00a78Sharpening \u00a7aC\u00a77 Sharpening \u00a7b" + sharpedRecipe.getName() + "\u00a77 (\u00a76" + sharpedRecipe.getWidth() + "x" + sharpedRecipe.getHeight() + "\u00a77)");

        /*
         * An internal joke with myself, because I'm somewhat of a comedian,
         * you see I used to think "sharped" crafting was in the workbench,
         * leaving the alternative 'not sharped' to the survival inventory.
         *
         * Eventually I read the word to be "shaped" instead but...
         */

        /*
         * Following the javadoc example, these original dimensions would have (respectively) been
         * 0            0           5           4
         *
         * But after processing, they would have identified to be
         * 1            1           2           4
         */
        int trueMinWidth = sharpedRecipe.getWidth(), trueMinHeight = sharpedRecipe.getHeight(), trueMaxWidth = 0, trueMaxHeight = 0;

        // Examine every ingredient
        for (MythicRecipeIngredient ingredient : sharpedRecipe.getIngredients()) {

            // Ignore
            if (ingredient == null) { continue; }

            /*
             * Find the item that would generate.
             *
             * This theoretically may incorrectly find an AIR filter
             * within a list of substitutes of which at least one
             * of them is not air... but for such a setup to exist
             * the user is doing nonsense.
             */
            ProvidedUIFilter poof = ingredient.getIngredient().getRandomSubstitute(false);
            if (poof == null) { continue; }
            //SHARP//MythicCraftingManager.log("\u00a78Sharpening \u00a7aI\u00a77 Found {\u00a7e" + ((ShapedIngredient) ingredient).getHorizontalOffset() + ":" + (-((ShapedIngredient) ingredient).getVerticalOffset()) + "\u00a77}" + "\u00a77 at \u00a7b" + poof.toString());

            // Applicable?
            if (poof.isAir()) { continue; }
            //SHARP//MythicCraftingManager.log("\u00a78Sharpening \u00a7aI\u00a72 Not AIR");

            // Get observed co-ordinates
            ShapedIngredient ing = (ShapedIngredient) ingredient;
            int cH = -ing.getVerticalOffset();
            int cW = ing.getHorizontalOffset();

            /*
             * Identify the smallest height and width,
             * as well as the largest of both.
             */
            if (trueMinWidth > cW) {
                //SHARP//MythicCraftingManager.log("\u00a78Sharpening \u00a7aA\u00a77 New min width:\u00a7e" + cW);
                trueMinWidth = cW; }
            if (trueMinHeight > cH) {
                //SHARP//MythicCraftingManager.log("\u00a78Sharpening \u00a7aA\u00a77 New min height:\u00a7e" + cH);
                trueMinHeight = cH; }
            if (trueMaxWidth < cW) {
                //SHARP//MythicCraftingManager.log("\u00a78Sharpening \u00a7aA\u00a77 New MAX width:\u00a7e" + cW);
                trueMaxWidth = cW; }
            if (trueMaxHeight < cH) {
                //SHARP//MythicCraftingManager.log("\u00a78Sharpening \u00a7aA\u00a77 New MAX height:\u00a7e" + cH);
                trueMaxHeight = cH; }
        }

        // Build new ingredients
        ArrayList<ShapedIngredient> accepted= new ArrayList<>();
        for (int w = trueMinWidth; w <= trueMaxWidth; w++) {
            for (int h = trueMinHeight; h <= trueMaxHeight; h++) {
                //SHARP//MythicCraftingManager.log("\u00a78Sharpening \u00a7aB\u00a77 Building \u00a72" + w + ":" + h);

                ShapedIngredient found = sharpedRecipe.getIngredientAt(w, -h);
                if (found == null) { continue; }
                //SHARP//MythicCraftingManager.log("\u00a78Sharpening \u00a7aB\u00a77 Found \u00a7f{\u00a7e" + found.getHorizontalOffset()+ " " + (-found.getVerticalOffset()) + "\u00a7f} \u00a7b" + found.getIngredient().getName());

                ShapedIngredient edited = found.clone();
                edited.setHorizontalOffset(edited.getHorizontalOffset() - trueMinWidth);
                edited.setVerticalOffset(edited.getVerticalOffset() + trueMinHeight);
                //SHARP//MythicCraftingManager.log("\u00a78Sharpening \u00a7aB\u00a77 Now \u00a7f{\u00a7e" + edited.getHorizontalOffset()+ " " + (-edited.getVerticalOffset()) + "\u00a7f}");


                // Get the ingredient found
                accepted.add(edited);
            }
        }

        // Yes
        //ShapedRecipe ret = new ShapedRecipe(sharpedRecipe.getName(), accepted);
        //SHARP//MythicCraftingManager.log("\u00a78Sharpening \u00a7aR\u00a77 Result: \u00a7a" + ret.getWidth() + "x" + ret.getHeight());
        //return ret;
        return new ShapedRecipe(sharpedRecipe.getName(), accepted);
    }

    /**
     * A Mythic Recipe that checks for an ordered pattern of items
     * spread out through a {@link MythicRecipeInventory}
     *
     * @param name Name of the recipe
     *
     * @param ingredients The list of ingredients with their offsets information.
     */
    public ShapedRecipe(@NotNull String name, @NotNull ShapedIngredient... ingredients) {
        super(name, ingredients);
    }

    @NotNull
    @Override
    public MythicRecipeStation getType() { return MythicRecipeStation.WORKBENCH; }

    @Override
    public void clearIngredients() {
        super.clearIngredients();
        width = 0;
        height = 0;
    }

    /**
     * Since shaped ingredients are ordered and such, attempting to put a second one in
     * an occupied slot will cause it to replace the old one.
     *
     * @param ingredient The {@link ShapedIngredient} to include.
     */
    @Override
    public void addIngredient(@NotNull MythicRecipeIngredient ingredient) {

        // Must be a Shaped Ingredient ffs
        Validate.isTrue(ingredient instanceof ShapedIngredient, "Shaped Mythic Recipes can only work with Shaped Ingredients.");

        // Is there any ingredient in these co-ords? replace it.
        ShapedIngredient replaced = getIngredientAt(((ShapedIngredient) ingredient).getHorizontalOffset(), ((ShapedIngredient) ingredient).getVerticalOffset());
        mythicIngredients.remove(replaced);

        // I guess add yo lol
        mythicIngredients.add(ingredient);
        ((ShapedIngredient) ingredient).linkToRecipe(this);
        int cH = (-((ShapedIngredient) ingredient).getVerticalOffset());
        int cW = ((ShapedIngredient) ingredient).getHorizontalOffset();

        //RSZ// MythicCraftingManager.log("\u00a78Resize \u00a74>\u00a77 Added Ingredient at \u00a73" + cH + ":" + cW + "\u00a78 (to this \u00a79" + getWidth() + "x" + getHeight() + "\u00a78) \u00a79" + ingredient.getIngredient().getName());

        // Greater width?
        if (getWidth() <= cW) {
            //RSZ// MythicCraftingManager.log("\u00a78Resize \u00a74>\u00a77 Max Width now \u00a76" + (cW + 1));
            width = cW + 1;}
        if (getHeight() <= cH) {
            //RSZ// MythicCraftingManager.log("\u00a78Resize \u00a74>\u00a77 Max Height now \u00a76" + (cH + 1));
            height = cH + 1;}
    }

    /**
     * Recalculates the size of the recipe.
     * <p></p>
     * Called by {@link ShapedIngredient#setHorizontalOffset(int)} and {@link ShapedIngredient#setVerticalOffset(int)}
     */
    public void recalculateSize() {
        //RSZ// MythicCraftingManager.log("\u00a78Resize \u00a7c>\u00a77 Recalculating size of this \u00a7e" + getWidth() + "x" + getHeight());

        // Yes
        int highestHeight = 0, widestWidth = 0;

        // Just look at the indexes of every ingredient and get the highest one
        for (MythicRecipeIngredient g : getIngredients()) {

            //Get the highest offsets
            int cH = -((ShapedIngredient) g).getVerticalOffset();
            int cW = ((ShapedIngredient) g).getHorizontalOffset();
        //RSZ// MythicCraftingManager.log("\u00a78Resize \u00a7c>\u00a77 Ingredient at \u00a73" + cH + ":" + cW + " \u00a79" + g.getIngredient().getName());
            if (cW > widestWidth) {
                //RSZ// MythicCraftingManager.log("\u00a78Resize \u00a7c>\u00a77 Max Width now \u00a76" + (cW));
                widestWidth = cW; }
            if (cH > highestHeight) {
                //RSZ// MythicCraftingManager.log("\u00a78Resize \u00a7c>\u00a77 Max Height now \u00a76" + (cH));
                highestHeight = cH; }
        }

        // Yes
        width = widestWidth + 1;
        height = highestHeight + 1;
        //RSZ// MythicCraftingManager.log("\u00a78Resize \u00a7c>\u00a77 Final size \u00a7e" + getWidth() + "x" + getHeight());
    }

    /**
     * These co-ordinates follow the same convention as {@link ShapedIngredient}.
     *
     * @param h The vertical shift in slots relative to the top left corner. <p>
     *               <b>Always Negative</b></p>
     * @param w The horizontal shift in slots relative to the top left corner. <p>
     *              <b>Always Positive</b></p>
     *
     * @return The ingredient expected at these co-ordinates.
     *         <p></p>
     *         May be <code>null</code> if the co-ordinates are
     *         out of range, or there is just no ingredient in there.
     */
    @Nullable public ShapedIngredient getIngredientAt(int w, int h) {

        // Find
        for (MythicRecipeIngredient ingredient : getIngredients()) {

            // Must be shaped
            ShapedIngredient shaped = (ShapedIngredient) ingredient;

            // Matches?
            if (shaped.getHorizontalOffset() == w && shaped.getVerticalOffset() == h) {

                // That's the one
                return shaped;
            }
        }

        // Not found
        return null;
    }
    /**
     * The horizontal size of a grid required to craft this recipe.
     * <p></p>
     * <b>Positive</b>, intuitively.
     */
    int width = 0;
    /**
     * @return The horizontal size of a grid required to craft this recipe.
     *         <p></p>
     *         <b>Positive</b>, intuitively.
     */
    public int getWidth() { return width; }
    /**
     * The vertical size of a grid required to craft this recipe.
     * <p></p>
     * <b>Positive</b>, intuitively.
     */
    int height = 0;
    /**
     * @return The vertical size of a grid required to craft this recipe.
     *         <p></p>
     *         <b>Positive</b>, intuitively.
     */
    public int getHeight() { return height; }

    /**
     * This method uses the list of {@link ShapedIngredient}s of this recipe, finds
     * all the ways these could fit in a rectangular grid of the provided sizes.
     * <p></p>
     * This allows for a simple double iteration to check the combinations, though
     * its arguably not the most optimized way (since the first combination may match
     * and the work of finding the remaining ones is gone to waste). If this does
     * present an actual annoying delay, it could be made so that the check for
     * inventory items happens at the same time as the combination finding.
     *
     * @param height Height of the inventory, obviously positive.
     * @param width Width of the inventory, obviously positive.
     *
     * @return An array of adapted ingredients for every possible combination of them that fits within these bounds.
     */
    @NotNull public ArrayList<ArrayList<ShapedIngredient>> permute(int width, int height) {
        //MUTE// MythicCraftingManager.log("\u00a78Permute \u00a75>\u00a77 Permuting this \u00a73" + getWidth() + "x" + getHeight() + "\u00a77 into \u00a7b" + width + "x" + height);

        // At which width and heights do the ingredients begin to overflow?
        Integer criticalWidth = null;
        Integer criticalHeight = null;

        // Yes
        ArrayList<ArrayList<ShapedIngredient>> ret = new ArrayList<>();

        // For every possible width
        for (int w0 = 0; w0 <= width; w0++) {

            // If it would overflow, skp
            if (criticalWidth != null) { if (w0 >= criticalWidth) {
                //MUTE// MythicCraftingManager.log("\u00a78-{\u00a75" + w0 + "\u00a78}\u00a75>\u00a77 Critical width reached. \u00a74Cancel.");
                continue; } }

            // For every possible height
            for (int h0 = 0; h0 <= height; h0++) {

                // If it would overflow, skp
                if (criticalHeight != null) { if (h0 >= criticalHeight) {
                    //MUTE// MythicCraftingManager.log("\u00a78-{\u00a75" + w0 + ":" + h0 + "\u00a78}\u00a75>\u00a77 Critical height reached. \u00a74Cancel.");
                    continue; } }

                // Build this array
                ArrayList<ShapedIngredient> iterationResult = new ArrayList<>();

                // Has the overflow happened?
                boolean cancelled = false;

                // For every Positional Ingredient
                for (MythicRecipeIngredient ingredient : getIngredients()) {
                    //MUTE// MythicCraftingManager.log("\u00a78-{\u00a75" + w0 + ":" + h0 + "\u00a78}\u00a75>\u00a77 Ingredient \u00a7b" + ingredient.getIngredient().getName());

                    // Attempt to adapt
                    ShapedIngredient adapted = ((ShapedIngredient) ingredient).adapt(width, height, w0, -h0);

                    // Valid?
                    if (adapted != null) {
                        //MUTE// MythicCraftingManager.log("\u00a78-{\u00a75" + w0 + ":" + h0 + "\u00a78}\u00a75>\u00a77 Successfully adapted \u00a73" + ((ShapedIngredient) ingredient).getHorizontalOffset() + ":" + (-((ShapedIngredient) ingredient).getVerticalOffset()) +"\u00a77 into \u00a7b" + adapted.getHorizontalOffset() + ":" + (-adapted.getVerticalOffset()));

                        // Add
                        iterationResult.add(adapted);

                        // Iteration failure, identify critical quantity
                    } else {
                        //MUTE// MythicCraftingManager.log("\u00a78-{\u00a75" + w0 + ":" + h0 + "\u00a78}\u00a75>\u00a74 Failed to adapt \u00a73" + ((ShapedIngredient) ingredient).getHorizontalOffset() + ":" + (-((ShapedIngredient) ingredient).getVerticalOffset()));

                        // Identify overflow
                        boolean ow = ((ShapedIngredient) ingredient).overflowsWidthFromOffset(width, w0);
                        boolean oh = ((ShapedIngredient) ingredient).overflowsHeightFromOffset(height, -h0);

                        // At this point, at least 1 ingredient overflows and such its impossible to be crafted.
                        if (ow) {
                            //MUTE// MythicCraftingManager.log("\u00a78-{\u00a75" + w0 + ":" + h0 + "\u00a78}\u00a75>\u00a77 Identified Critical Width as \u00a7c" + w0);
                            criticalWidth = w0; }
                        if (oh) {
                            //MUTE// MythicCraftingManager.log("\u00a78-{\u00a75" + w0 + ":" + h0 + "\u00a78}\u00a75>\u00a77 Identified Critical Height as \u00a7c" + h0);
                            criticalHeight = h0; }

                        // Cancel
                        cancelled = true;
                        break;
                    }
                }

                // Register possibility if successful
                if (!cancelled) {

                    //MUTE// MythicCraftingManager.log("\u00a78-{\u00a75" + w0 + ":" + h0 + "\u00a78}\u00a75>\u00a7a Included\u00a77, total now \u00a7a" + (ret.size() + 1));
                    ret.add(iterationResult);
                }
            }
        }

        // That's it
        return ret;
    }

    /**
     * In the case of shaped recipes, we specify a pattern of items.
     * There are a few things that make them fail:
     * <p></p>
     * <p><code>*</code> When an item is missing
     * </p><code>*</code> When an item is in the incorrect spot
     * <p><code>*</code> When there is extra items
     * </p>
     *
     * @param inventory Information on the location of every ItemStack, as
     *                  well as the dimensions of the inventory provided.
     *
     * @return Whether or not this MythicInventory exactly matches the requested
     *         recipe, with no extra items.
     */
    @Override
    @Nullable public MythicRecipeInventory matches(@NotNull MythicRecipeInventory inventory, @Nullable Ref<Integer> timesToCompletion) {

        /*
         * To know if there are any extra items, we must know which slots did succeed in the
         * recipe, and make sure the rest are completely empty.
         */
        ArrayList<CheckedSlot> slots = new ArrayList<>();

        // Too big? I sleep
        if (inventory.getWidth() < getWidth() || inventory.getHeight() < getHeight()) { return null; }

        //CRAFT//int i = 0;
        // All right lets do this
        for (ArrayList<ShapedIngredient> combination : permute(inventory.getWidth(), inventory.getHeight())) {
            //CRAFT//MythicCraftingManager.log("\u00a78M\u00a76C\u00a77 Checking Combination \u00a73#" + i++);
            slots.clear();

            // Check every item of this combination
            for (int c = 0; c < combination.size(); c++) {

                // Get target ingredient
                ShapedIngredient ingredient = combination.get(c);

                // What item stack is at these offsets
                ItemStack found = inventory.getItemAt(ingredient.getHorizontalOffset(), ingredient.getVerticalOffset());
                //CRAFT//MythicCraftingManager.log("\u00a78Matches \u00a76C\u00a78{\u00a7e" + ingredient.getHorizontalOffset() + "\u00a78,\u00a7e" + ingredient.getVerticalOffset() + "\u00a78}\u00a77 Ingredient \u00a7e#" + c + "\u00a77 '" + ingredient.getIngredient().getName() + "\u00a77', Comparing to '" + SilentNumbers.getItemName(found) + "\u00a77'");

                /*
                 * As there is a Ingredient being expected at this co-ordinates,
                 * it is kind of irrational that there is no slot in the inventory.
                 *
                 * I guess this could happen in an external plugin custom GUI misusing
                 * this system, or just being lazy at correctly building their recipes.
                 *
                 * Ex some plugin has an amorphous crafting grid that looks like this:
                 * [-][ ][-][-]
                 * [ ][ ][ ][ ]        where [ ] represents a slot of an inventory GUI
                 * [ ][-][ ][-]          and [-] is a slot where players cannot place items
                 *
                 * The recipe then seeks for an ingot in one of the slots marked as [-],
                 * which will cause the MythicRecipeInventory to give us a null ItemStack.
                 *
                 * In that case, I assume this recipe is just not for this crafting station,
                 * which means that this arrangement cannot match.
                 */
                if (found == null) { return null; }

                // If it does not match, this combination is invalid
                if (!ingredient.matches(found)) {
                    //CRAFT//MythicCraftingManager.log("\u00a78Matches \u00a76R\u00a7c No Match");
                    break; }

                /*
                 * The ingredient matched, and it was the LAST one of this combination?
                 *
                 * That can only mean that this is combination is satisfied. Success.
                 */
                else if ((c + 1) == combination.size()) {
                    //CRAFT//MythicCraftingManager.log("\u00a78Matches \u00a76R\u00a7e Complete Match");

                    // Add this final slot
                    slots.add(new CheckedSlot(ingredient.getHorizontalOffset(), ingredient.getVerticalOffset()));

                    /*
                     * So all the items of the recipe are there all right,
                     * but is there any extra items? We must now look
                     */
                    for (CheckedSlot oSlot : CheckedSlot.getFrom(inventory)) {

                        // Manual equals .-.
                        boolean contained = false;
                        for (CheckedSlot sl : slots) { if (oSlot.getWidth() == sl.getWidth() && oSlot.getHeight() == sl.getHeight()) { contained = true; break; } }

                        // Not in? That should be empty then
                        if (!contained) {

                            // Any equal in the stored array?
                            //CRAFT//MythicCraftingManager.log("\u00a78Matches \u00a76R\u00a7e Unchecked Slot \u00a7e" + oSlot.getWidth() + ":" + (-oSlot.getHeight()));

                            // Get item in there
                            ItemStack item = inventory.getItemAt(oSlot.getWidth(), oSlot.getHeight());
                            //CRAFT//MythicCraftingManager.log("\u00a78Matches \u00a76R\u00a7e Found \u00a7b" + SilentNumbers.getItemName(item));

                            // Must be null or air, else its extraneous
                            if (!SilentNumbers.isAir(item)) {

                                //CRAFT//MythicCraftingManager.log("\u00a78Matches \u00a76R\u00a7c Extraneous Item Found, \u00a74Cancelling.");
                                return null;
                            }
                        }
                    }

                    // Yea dupe
                    MythicRecipeInventory result = inventory.clone();
                    int lowestTimes = 32767;

                    // Edit the result inventory
                    for (ShapedIngredient item : combination) {

                        // Find at the result
                        ItemStack foundResult = result.getItemAt(item.getHorizontalOffset(), item.getVerticalOffset());

                        // That should really not have happened wth
                        if (foundResult == null) {
                            MythicLib.plugin.getLogger().log(Level.SEVERE, FriendlyFeedbackProvider.quickForConsole(FFPMythicLib.get(),
                                    "$fCustom recipe could not succeed: $bExpected item at offsets $r{0}h {1}w$b in order so subtract $u{2}$b. "
                            , String.valueOf(item.getVerticalOffset()), String.valueOf(item.getHorizontalOffset()), String.valueOf(item.getAmountOfSuccess())));

                            // No
                            return null; }

                        // Current item amount
                        int amount = foundResult.getAmount();

                        /*
                         * When it was tested if they match, these filters
                         * will now know the amount of success.
                         */
                        int newAmount = amount - item.getAmountOfSuccess();
                        int times = SilentNumbers.floor(((double) amount) / ((double) item.getAmountOfSuccess()));
                        //CRAFT//MythicCraftingManager.log("\u00a78Matches \u00a7b" + SilentNumbers.getItemName(foundResult) + " \u00a76AC\u00a77 Current: \u00a7e" + amount + "\u00a77, Success: \u00a7e" + item.getAmountOfSuccess() + "\u00a77, Result: \u00a76" + newAmount);
                        //CRAFT//MythicCraftingManager.log("\u00a78Matches \u00a7b" + SilentNumbers.getItemName(foundResult) + " \u00a76CM\u00a77 Difference: \u00a7e" + item.getAmountOfSuccess() + "\u00a77, Times: \u00a7c" + times);

                        // Set Amount yes
                        foundResult.setAmount(newAmount);
                        if (lowestTimes > times) {
                            //CRAFT//MythicCraftingManager.log("\u00a78Matches \u00a7b" + SilentNumbers.getItemName(foundResult) + " \u00a76CM\u00a77 New lowest times: \u00a7c" + times);
                            lowestTimes = times; }

                        //CRAFT//ItemStack parallel = result.getItemAt(item.getHorizontalOffset(), item.getVerticalOffset());
                        //CRAFT//MythicCraftingManager.log("\u00a78Matches \u00a76AC\u00a77 Final: \u00a7e" + parallel.getAmount());

                    }

                    Ref.setValue(timesToCompletion, lowestTimes);
                    return result;
                }
                //CRAFT//MythicCraftingManager.log("\u00a78Matches \u00a76P\u00a7a Match");

                // Add this slot to the checked
                slots.add(new CheckedSlot(ingredient.getHorizontalOffset(), ingredient.getVerticalOffset()));
            }
        }

        // Went through every combination and none matched.
        return null;
    }

    static class CheckedSlot {
        final int width;

        /**
         * @return The horizontal co-ordinate of this slot.
         *         <p></p>
         *         <b>Always Positive</b>
         */
        public int getWidth() {
            return width;
        }


        /**
         * @return The positive co-ordinate of this slot.
         *         <p></p>
         *         <b>Always Negative</b>
         */
        public int getHeight() {
            return height;
        }

        final int height;

        /**
         * Which slots have you checked?
         *
         * @param width Horizontal component,<b>positive</b>
         * @param height Horizontal component,<b>negative</b>
         */
        CheckedSlot(int width, int height) {
            this.width = width;
            this.height = height;
        }

        /**
         * @param inventory Inventory to extract the numbers from
         * @return A list with a CheckedSlot for every slot in this inventory
         */
        public static ArrayList<CheckedSlot> getFrom(@NotNull MythicRecipeInventory inventory) {

            // Result of operation
            ArrayList<CheckedSlot> ret = new ArrayList<>();

            // Go through every slot
            for (int w = 0; w < inventory.getWidth(); w++) {
                for (int h = 0; h < inventory.getHeight(); h++) {

                    // Create and add
                    ret.add(new CheckedSlot(w, -h));
                } }

            // That's it
            return ret;
        }
    }

    @NotNull
    @Override
    public Recipe getBukkitRecipe(@NotNull NamespacedKey recipeName, @NotNull ItemStack recipeResult) throws IllegalArgumentException {
        //BKT//io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager.log("Building bukkit recipe of \u00a7e" + getName());

        // Create bukkit recipe yay
        org.bukkit.inventory.ShapedRecipe ret = new org.bukkit.inventory.ShapedRecipe(recipeName, recipeResult);

        HashMap<Integer, HashMap<Integer, ShapedIngredient>> recipeContents = new HashMap<>();
        HashMap<Integer, String> rowStrings = new HashMap<>();
        int greatestH = 0;
        int greatestW = 0;

        for (int h = 0; h < getHeight() && h < 3; h++) {

            char[] chars;
            switch (h) {
                case 0:  chars = new char[] {'A', 'B', 'C'}; break;
                case 1:  chars = new char[] {'D', 'E', 'F'}; break;
                default: chars = new char[] {'G', 'H', 'I'}; break; }

            // Create hashmap of row
            HashMap<Integer, ShapedIngredient> rowContents = new HashMap<>();

            // String builder
            StringBuilder rowString = new StringBuilder();

            // Fill
            for (int w = 0; w < getWidth() && w < 3; w++) {

                // Get
                ShapedIngredient get = getIngredientAt(w, -h);

                // Fill ingredient (might be null)
                rowContents.put(w, get);
                greatestW = w;

                // AIR
                if (
                        // If its not null
                        get != null &&

                        // And its only component is not AIR (supposing it only has one)
                        !(get.getIngredient().getSubstitutes().size() == 1 &&
                        get.getIngredient().getSubstitutes().get(0).isAir())) {

                    // Store char of place
                    rowString.append(chars[w]);

                // Skip
                } else { rowString.append(" ");}

                //BKT//String foundName = get == null ? "null" : get.getIngredient().getName();
                //BKT//io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager.log("\u00a78{\u00a7b" + w + " " + h + "\u00a78}\u00a77 Found \u00a79" + foundName);
            }

            // Store
            String row = rowString.toString();
            boolean notBlanc = false;
            for (char c : row.toCharArray()) { if (c != ' ') { notBlanc = true; break; }
                //BKT//io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager.log("\u00a78-\u00a7c- \u00a77Row \u00a7b" + h + "\u00a77 is blanc. ");
                }

            // Include
            if (notBlanc) {
                rowStrings.put(h, rowString.toString());
                recipeContents.put(h, rowContents);
                greatestH = h;

                //BKT//io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager.log("\u00a78-\u00a7a- \u00a77Included row \u00a7b" + h + "\u00a77 as \u00a7e" + rowString.toString());
            } }

        // Bake
        String[] rowsBaked = new String[greatestH + 1];
        for (Integer r : rowStrings.keySet()) { rowsBaked[r] = rowStrings.get(r); }
        for (int s = 0; s < rowsBaked.length; s++) {
            if (rowsBaked[s] == null) {
                switch (greatestW) {
                    default:
                        rowsBaked[s] = " ";
                    case 1:
                        rowsBaked[s] = "  ";
                    case 2:
                        rowsBaked[s] = "   ";
                } } }

        // Insert shape yea
        ret.shape(rowsBaked);

        // Same thing yeet
        for (int h = 0; h < getHeight() && h < 3; h++) {

            char[] chars;
            switch (h) {
                case 0:  chars = new char[] {'A', 'B', 'C'}; break;
                case 1:  chars = new char[] {'D', 'E', 'F'}; break;
                default: chars = new char[] {'G', 'H', 'I'}; break; }

            // Get row
            HashMap<Integer, ShapedIngredient> rowContent = recipeContents.get(h);

            // Bruh
            if (rowContent == null) { continue; }

            // Continue
            for (int w = 0; w < getWidth() && w < 3; w++) {

                // Get ingredient
                ShapedIngredient ingredient = rowContent.get(w);
                if (ingredient == null) { continue; }

                // Insert but miss me with that 'missing char' trash
                try { ret.setIngredient(chars[w], ingredient.toBukkit()); } catch (IllegalArgumentException ignored) {}

            } }

        return ret;
    }
}
