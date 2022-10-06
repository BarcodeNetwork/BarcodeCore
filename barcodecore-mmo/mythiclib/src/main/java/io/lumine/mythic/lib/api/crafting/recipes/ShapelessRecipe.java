package io.lumine.mythic.lib.api.crafting.recipes;

import io.lumine.mythic.lib.api.crafting.ingredients.*;
import io.lumine.mythic.lib.api.crafting.uimanager.ProvidedUIFilter;
import io.lumine.mythic.lib.api.util.Ref;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackProvider;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Obviously the Mythic Recipe for workbench and the 2x2 inventory grid,
 * and any size of grid actually (up to 6x9?), but that does not care
 * for which order you put the ingredients as long as they are there.
 * <p></p>
 * Uses honestly any {@link MythicIngredient}
 * since it only needs the information required by them all.
 *
 * @author Gunging
 */
@SuppressWarnings("unused")
public class ShapelessRecipe extends MythicRecipe implements VanillaBookableRecipe {

    /**
     * A Mythic Recipe that checks for an specified amount of items
     * spread out patternless through a {@link MythicRecipeInventory}
     *
     * @param name Name of the recipe
     *
     * @param ingredients The list of ingredients
     */
    public ShapelessRecipe(@NotNull String name, @NotNull ArrayList<MythicRecipeIngredient> ingredients) {
        super(name, ingredients);
    }

    /**
     * A Mythic Recipe that checks for an specified amount of items
     * spread out patternless through a {@link MythicRecipeInventory}
     *
     * @param name Name of the recipe
     *
     * @param ingredients The list of ingredients
     */
    public ShapelessRecipe(@NotNull String name, @NotNull MythicRecipeIngredient... ingredients) {
        super(name, ingredients);
    }

    @Override
    public void addIngredient(@NotNull MythicRecipeIngredient ingredient) {

        // Yea vro just add it man
        mythicIngredients.add(ingredient);
    }

    @NotNull
    @Override
    public MythicRecipeStation getType() { return MythicRecipeStation.WORKBENCH; }

    @Override
    @Nullable public MythicRecipeInventory matches(@NotNull MythicRecipeInventory inventory, @Nullable Ref<Integer> timesToCompletion) {

        // Build ingredients
        ArrayList<MythicIngredient> ingredients = new ArrayList<>();
        for (MythicRecipeIngredient rIngredient : getIngredients()) {
            //CRAFT//MythicCraftingManager.log("\u00a78Match \u00a76C\u00a77 Seeking '\u00a7e" + rIngredient.getIngredient().getName() + "\u00a77' " + SilentNumbers.getItemName(rIngredient.getIngredient().getRandomSubstituteItem()));
            ingredients.add(rIngredient.getIngredient()); }

        // Well lets acquire the entire shapeless object pool (OG name I gave to this class while walking in circles trying to understand the thought process of distributing materials among several individuals)
        ArrayList<ShapelessItem> result = matches(pool(inventory), ingredients);

        // Was it successful?
        if (result != null) {
            //CRAFT//MythicCraftingManager.log("\u00a78Match \u00a7aC\u00a77 Success, Identifying Least Times ");

            // Build new Mythic Inventory with respective amounts
            MythicRecipeInventory ret = inventory.clone();
            int leastTimes = 32767;

            // Go through each item
            for (ShapelessItem resultItem : result) {
                //CRAFT//MythicCraftingManager.log("\u00a78Match {\u00a7b" + resultItem.getOriginalWidthCoord() + " " + (-resultItem.getOriginalHeightCoord()) + "\u00a78} \u00a76C\u00a77 " + SilentNumbers.getItemName(resultItem.getStack()));

                // Get stack at coords
                ItemStack stacc = ret.getItemAt(resultItem.getOriginalWidthCoord(), resultItem.getOriginalHeightCoord());
                //CRAFT//MythicCraftingManager.log("\u00a78Match----- \u00a76O\u00a77 Found Original " + SilentNumbers.getItemName(stacc));
                Validate.isTrue(stacc != null, "Original coordinates mismatch in shapeless recipe: Result item cannot point to an original null");

                if (timesToCompletion != null) {
                    //CRAFT//MythicCraftingManager.log("\u00a78Match----- \u00a76O\u00a77 Identifying Extent of Reaction ");
                    int originalAmount = stacc.getAmount();
                    int finalAmount = resultItem.getAmount();
                    int difference = originalAmount - finalAmount;
                    int times = SilentNumbers.floor(((double)originalAmount) / ((double)difference));
                    //CRAFT//MythicCraftingManager.log("\u00a78Match----- \u00a76E\u00a77 Original: \u00a79" + originalAmount + "\u00a77, Final: \u00a73" + finalAmount + "\u00a77, Difference: \u00a7b" + difference + "\u00a77, Extent: \u00a7e" + times);
                    if (leastTimes > times) {
                        //CRAFT//MythicCraftingManager.log("\u00a78Match----- \u00a76L\u00a77 New Least Times \u00a7e" + times);
                        leastTimes = times; }
                }

                // Set amount
                stacc.setAmount(resultItem.getAmount());
                //CRAFT//MythicCraftingManager.log("\u00a78Match----- \u00a76R\u00a77 Result: " + SilentNumbers.getItemName(ret.getItemAt(resultItem.getOriginalWidthCoord(), resultItem.getOriginalHeightCoord())));
            }

            // That's it
            Ref.setValue(timesToCompletion, leastTimes);
            return ret;
        }

        // Result unsuccessful
        //CRAFT//MythicCraftingManager.log("\u00a78Match \u00a7cC\u00a77 L");
        return null;
    }

    /**
     * Runs this scenario in which these ingredients are trying to match this pool.
     * <p></p>
     * <b>Consider passing in a deep-cloned pool</b> because it will suffer a lot of changes in here.
     *
     * @param pool The ItemStacks remaining in the pool, might have amount taken though.
     * @param unmatchedIngredients Ingredients to consider.
     *
     * @return <code>null</code> If no shapeless match was possible.
     *         <p></p>
     *         An array with the succeeding pool with information on how much
     *         of each ingredient was taken, check with {@link ShapelessItem#getTaken()}
     *
     * @see ShapelessItem#deepCloneList(ArrayList)
     * @see ShapelessItem#clone() This is the way the deepCloneList method clones ShaplessItems.
     */
    @SuppressWarnings("ConstantConditions")
    @Nullable public ArrayList<ShapelessItem> matches(@NotNull ArrayList<ShapelessItem> pool, @NotNull ArrayList<MythicIngredient> unmatchedIngredients) {

        // Which ingredients will claim dibs on the items?
        ArrayList<ShapelessRequester> dibbers = new ArrayList<>();
        int rOID = 0;

        /*
         * Make each ingredient claim dibs on all items of pool
         * that make it match (any of its substitutes)
         *
         * After this step, every ingredient will have an array of
         * ingredients trying to claim it, and the immediate logic is
         * the following:
         *
         * 1 If there is an item stack that matches no ingredient (no dibs),
         *   then the recipe is cancelled because the player is not trying
         *   to craft it I guess?
         *
         * 2 Items with exactly ONE dib attempt to fulfill their ingredient.
         *    * Ingredients fulfilled retract their dibs and have matched.
         *    * If all ingredients are fulfilled, its a MATCH.
         *
         * 3 If an item has two or more dibs, and multiple dibbing ingredients are
         *   still unfulfilled, it will attempt to split these so that it can be crafted.
         */
        //CRAFT//MythicCraftingManager.log("\u00a78Shapeless \u00a79C\u00a77 Claiming Shapeless Pool");
        // Does it match any filters?
        for (MythicIngredient checker : unmatchedIngredients) {
            //CRAFT//MythicCraftingManager.log("\u00a78Shapeless \u00a79-F>\u00a77 Checking Filter " + checker.getName());

            // Get it
            ShapelessRequester check = new ShapelessRequester(rOID, checker);
            rOID++;

            // Add it
            dibbers.add(check);

            for (ShapelessItem ingredient : pool) {
                //CRAFT//MythicCraftingManager.log("\u00a78Shapeless \u00a79--I>\u00a77 Found " + SilentNumbers.getItemName(ingredient.getStack()));

                // Gotta know thay first filter who match
                Ref<ArrayList<ProvidedUIFilter>> match = new Ref<>();

                FriendlyFeedbackProvider ffp = null;
                //FFP//FriendlyFeedbackProvider ffp = new FriendlyFeedbackProvider(FFPMythicLib.get());
                //FFP//ffp.activatePrefix(true, "\u00a78Ingredient \u00a78{\u00a79" + check.getIngredient().getName() + "\u00a78}");

                // Ignore amount tho
                if (check.getIngredient().matches(ingredient.getStack(), true, match, true, ffp)) {

                    // Sort to get the least amount one
                    Collections.sort(match.getValue());

                    //CRAFT//MythicCraftingManager.log("\u00a78Shapeless \u00a79--\u00a7aM\u00a79>\u00a77 Matched " + SilentNumbers.getItemName(ingredient.getStack()));
                    //CRAFT//for (String str : (SilentNumbers.transcribeList(match.getValue(), (s) -> ("\u00a78Shapeless \u00a79---M>\u00a77 " + ((ProvidedUIFilter) s).toString())))) { MythicCraftingManager.log(str); }

                    // Call dibs
                    check.dib(ingredient, match.getValue().get(0));
                    //CRAFT//MythicCraftingManager.log("\u00a78Shapeless \u00a79--D>\u00a77 Dibbed with \u00a7a" + (match.getValue().get(0)).toString());
                }

                //FFP//ffp.sendTo(FriendlyFeedbackCategory.ERROR, MythicLib.plugin.getServer().getConsoleSender());
                //FFP//ffp.sendTo(FriendlyFeedbackCategory.FAILURE, MythicLib.plugin.getServer().getConsoleSender());
            }
        }

        // #1 Cancel due to lack of dibs - the player is putting extra ingredients here ew
        for (ShapelessItem ingredient : pool) { if (ingredient.getRequesters().size() == 0) {
            //CRAFT//MythicCraftingManager.log("\u00a78Shapeless \u00a7cFail\u00a77 Item " + SilentNumbers.getItemName(ingredient.getStack()) + " had no Requesters");
            return null; } }

        // #2 Run first match
        //CRAFT//MythicCraftingManager.log("\u00a78Shapeless \u00a79C\u00a77 Running first claim");
        ArrayList<ShapelessRequester> ambiguouslyFulfilled = new ArrayList<>();
        for (ShapelessRequester requester : dibbers) {
            //CRAFT//MythicCraftingManager.log("\u00a78Shapeless \u00a79-D>\u00a77 Requester \u00a7e" + requester.getIngredient().getName());

            /*
             * Run the first claim checks, in which requesters will return FALSE
             * if there is straight up no way to complete this recipe.
             *
             * This method has two steps:
             * #1 Can this requester be fulfilled by grabbing only the items that
             *    no other requester wants? Then this is a TRUE TRUE
             *
             * #2 Else, can this requester be fulfilled supposing all resources
             *    were allocated to it (ignoring that other requesters might want
             *    the same resource and consume all of it)? Then this is TRUE FALSE
             *
             * #3 Otherwise, there is no way this recipe can succeed, FALSE FALSE.
             *
             *
             * First TRUE means 'is it possible for the recipe to succeed'
             * Its the result of firstClaim()
             *
             * Second TRUE means 'is this requester guaranteed to succeed'
             * Its the result of isFulfilled()
             *
             * In the case of #1, requester is removed from this array as it has
             * succeeded, no need to account for them anymore, and in #3, the whole
             * operation fails.
             */
            if (!requester.firstClaim()) {

                //CRAFT//MythicCraftingManager.log("\u00a78Shapeless \u00a7cFail\u00a77 Requester Impossible to Fulfill: \u00a7e" + requester.getIngredient().getName());
                return null; } else {

                // Was it not completely fulfilled?
                if (!requester.isFulfilled()) {

                    // Will have to evaluate it later.
                    ambiguouslyFulfilled.add(requester);
                }
            }
        }

        /*
         * There was no ambiguous requester? All must have succeeded.
         *
         * I guess that's it boys. The pool now has information on
         * how many of each item was taken, we'll take it from here.
         */
        if (ambiguouslyFulfilled.size() == 0) {
            //CRAFT//MythicCraftingManager.log("\u00a78Shapeless \u00a7aSuccess\u00a77 All requesters unambiguously fulfilled.");
            return pool; }

        /*
         * #3 Attempt to distribute the items among conflicting Requesters.
         *
         * The method is the following:
         *
         *   1 Pick any requester, what are the possible ways it can fulfill
         *     itself?
         *
         *   2 Run these scenarios by removing this requester and the ingredients
         *     it takes to be fulfilled, do any scenario succeeds?
         */
        ShapelessRequester chosen = ambiguouslyFulfilled.get(0);

        // Get all possible combinations in which the chosen is satisfied
        ArrayList<ArrayList<ShapelessItem>> alternativePools = chosen.secondClaim(ShapelessItem.completeCloneList(pool));
        Validate.isTrue(alternativePools.size() > 0, "Fatal Error when evaluating shapeless recipe: Alternative Pools Array Size Equals 0, which is ridiculous.");

        // Bake ingredients again
        ArrayList<MythicIngredient> alternativeIngredients = new ArrayList<>();

        // For those ambiguous
        for (int r = 1; r < ambiguouslyFulfilled.size(); r++) {

            // Get that Shapeless Requester
            ShapelessRequester observed = ambiguouslyFulfilled.get(r);

            // Include
            alternativeIngredients.add(observed.getIngredient());
        }

        // Well try each what are you waiting for
        for (ArrayList<ShapelessItem> alternativePool : alternativePools) {

            // Attempt
            ArrayList<ShapelessItem> ret = matches(alternativePool, alternativeIngredients);

            // Wait that attempt was successful? Lets goo
            if (ret != null) {

                // Merge taken from the alternative
                for (ShapelessItem alternative : ret) {

                    // Find in current
                    for (ShapelessItem original : pool) {

                        // Is it the one?
                        if (alternative.getOperationalIdentifier() == original.getOperationalIdentifier()) {

                            // WHats the last known amount
                            int currentA = original.getTaken();
                            int finalA = alternative.getTaken();

                            // Difference?
                            int deltaA = finalA - currentA;

                            // Take those
                            original.take(deltaA);
                        }
                    }
                }

                // That's the result
                //CRAFT//MythicCraftingManager.log("\u00a78Shapeless \u00a7aSuccess\u00a77 Shapelessception Passed.");
                return pool;
            }
        }

        // No attempts were successful
        //CRAFT//MythicCraftingManager.log("\u00a78Shapeless \u00a7cFailure\u00a77 Requesters could not share");
        return null;
    }

    /**
     * Since we don't care about the pattern, but about the amount
     */
    @NotNull public ArrayList<ShapelessItem> pool(@NotNull MythicRecipeInventory inventory) {

        // Yes
        ArrayList<ShapelessItem> ret = new ArrayList<>();
        int oid = 0;

        // Go through them all and just forget that they ever even had an arrangement lma0
        for (int w = 0; w < inventory.getWidth(); w++) {
            for (int h = 0; h < inventory.getHeight(); h++) {

                // Ah yes get
                ItemStack at = inventory.getItemAt(w, -h);

                // Found any?
                if (!SilentNumbers.isAir(at)) {

                    // Include
                    ret.add(new ShapelessItem(oid, at, w, -h));
                    oid++;
                }

            } }

        // That's eet
        return ret;
    }

    @NotNull
    @Override
    public Recipe getBukkitRecipe(@NotNull NamespacedKey recipeName, @NotNull ItemStack recipeResult) throws IllegalArgumentException {

        // Create bukkit recipe yay
        org.bukkit.inventory.ShapelessRecipe ret = new org.bukkit.inventory.ShapelessRecipe(recipeName, recipeResult);

        // Add all ingredients ig
        for (MythicRecipeIngredient ingredient : getIngredients()) {

            // Skip ig
            if (ingredient == null) { continue; }

            // Add
            ret.addIngredient(ingredient.toBukkit()); }

        return ret;
    }
}
