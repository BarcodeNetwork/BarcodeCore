package io.lumine.mythic.lib.api.crafting.ingredients;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.crafting.uimanager.ProvidedUIFilter;
import io.lumine.mythic.lib.api.util.Ref;
import io.lumine.mythic.lib.api.util.ui.FFPMythicLib;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackProvider;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * The way items are fairly distributed among ingredients
 * when it comes to Shapeless Recipes requires this auxiliary
 * class to make decisions.
 *
 * @author Gunging
 */
@SuppressWarnings("unused")
public class ShapelessRequester {

    /**
     * A class to keep track what items this requester has dibbed on.
     *
     * @param ingredient Ingredient information
     */
    public ShapelessRequester(int id, @NotNull MythicIngredient ingredient) { requestIdentifier = id; this.ingredient = ingredient; }
    /**
     * To be really sure of which ingredient is which
     */
    final int requestIdentifier;
    /**
     * To be really sure of which ingredient is which
     */
    public int getRequestIdentifier() { return requestIdentifier; }

    /**
     * Has this ingredient been unambiguously matched?
     */
    boolean fulfilled = false;
    /**
     * Has this ingredient been unambiguously matched?
     */
    public boolean isFulfilled() { return fulfilled; }

    /**
     * The dibbing ingredient
     */
    @NotNull final MythicIngredient ingredient;
    /**
     * The dibbing ingredient
     */
    @NotNull public MythicIngredient getIngredient() { return ingredient; }

    /**
     * The items this ingredient has matched.
     */
    @NotNull final HashMap<Integer, ShapelessItem> dibbed = new HashMap<>();
    /**
     * The items this ingredient has matched.
     */
    @NotNull public HashMap<Integer, ShapelessItem> getDibbed() { return dibbed; }
    /**
     * The items this ingredient has matched.
     */
    @Nullable public ShapelessItem getDibbed(int id) { return dibbed.get(id); }
    /**
     * Which filter was used to match each dibbed
     */
    @NotNull final HashMap<Integer, ProvidedUIFilter> matched = new HashMap<>();
    /**
     * Which filter was used to match each dibbed
     */
    @NotNull public HashMap<Integer, ProvidedUIFilter> getMatched() { return matched; }
    /**
     * Which filter was used to match each dibbed
     */
    @Nullable public ProvidedUIFilter getMatched(int id) { return matched.get(id); }
    /**
     * Indicate that this item may fulfill this ingredient,
     * if the amount were right.
     *
     * @param item The Item Stack that <b>matched</b> {@link #getIngredient()}'s
     *             {@link MythicIngredient#matches(ItemStack, boolean, Ref, boolean, FriendlyFeedbackProvider)}
     *             with ignore amount in true.
     *
     * @param filter To know the amount later in the future, we want the provided UIFilter also
     */
    public void dib(@NotNull ShapelessItem item, @NotNull ProvidedUIFilter filter) {

        // Call
        item.claimDibs(this);

        // Store
        dibbed.put(item.getOperationalIdentifier(), item);
        matched.put(item.getOperationalIdentifier(), filter);
        //CRAFT//MythicCraftingManager.log("\u00a78Dibbed \u00a7e---\u00a7bE\u00a77 As \u00a73" + getIngredient().getName() + "\u00a77, claimed [\u00a7b" + item.getOperationalIdentifier() + "\u00a77] " + SilentNumbers.getItemName(item.getStack()) + "\u00a77 (\u00a7f#" + dibbed.size() + "\u00a77), through \u00a7e" + filter.toString() + "\u00a77 (\u00a7e#" + matched.size() +  "\u00a77)");

        // Decide where to put the item operational id
        boolean reg = false;
        for (int i = 0; i < dibAmountOrder.size(); i++) {

            // Get p, which is the ith entry in the dib order
            Integer p = dibAmountOrder.get(i);

            // Get the filter there
            ProvidedUIFilter dib = getMatched(p);

            // Found?
            if (dib == null) { continue; }
            //CRAFT//MythicCraftingManager.log("\u00a78Dibbed \u00a7e---\u00a7bE\u00a77 Amount Order \u00a73#" + i + "\u00a77, Identifier [\u00a7b" + item.getOperationalIdentifier() + "\u00a77] \u00a7e" + dib.toString() + "; Filter: \u00a7a" + filter.getAmount(0) + " \u00a72<=? \u00a77Dib \u00a7a" + dib.getAmount(0));

            /*
             * Whatever matched is in this position is now greater,
             * push it back one entry and put this one in its place.
             *
             * Or well, if they are equal it doesn't matter anyway.
             */
            if (filter.getAmount(0) <= dib.getAmount(0)) {
                //CRAFT//MythicCraftingManager.log("\u00a78Dibbed \u00a7e---\u00a7bE\u00a77 Amount Order Identified: \u00a7e" + i);

                // Yes, insert.
                dibAmountOrder.add(i, item.getOperationalIdentifier());
                reg = true;
                break;
            }
        }

        // Include as the last entry.
        if (!reg) { dibAmountOrder.add(item.getOperationalIdentifier()); }

        //CRAFT//MythicCraftingManager.log("\u00a78Dibbed \u00a7e---\u00a7bE\u00a77 New Order:");
        //CRAFT//for (Integer inF : dibAmountOrder) { MythicCraftingManager.log("\u00a78Dibbed \u00a7e---\u00a7bE\u00a77 [\u00a73" + inF + "\u00a77] \u00a73" + getMatched(inF) ); }
    }

    /**
     * Order from LEASt to MOST amount required of the dibbed.
     */
    @NotNull ArrayList<Integer> dibAmountOrder = new ArrayList<>();

    /**
     * The first step consists in going through all of {@link #getDibbed()}
     * and see if any are dibbed exclusively by this very requester.
     *
     * If so, we know no conflicts may arise by using this one requester to
     * grab as much as he needs from them.
     *
     * @return <code>true</code> If, within the dibbed, it is <b>possible</b> for this
     *         ingredient to be fulfilled. This includes those dibbed by two or more
     *         requesters, so that a <code>false</code> indicates that the whole recipe cannot
     *         match, while a <code>true</code> does not mean it will, it just means that it
     *         is possible to fulfill this ingredient if all resources were available
     *         to it alone.
     *         <p></p>
     *         To know if it was actually fulfilled within this method, check {@link #isFulfilled()}
     *         afterwards, which will be true <b>if</b> this item completely, unambiguously matched.
     */
    public boolean firstClaim() {

        //CRAFT//MythicCraftingManager.log("\u00a78First Claim \u00a7c1\u00a77 Checking single claims first... {\u00a7e" + getIngredient().getName() + "\u00a77}");
        /*
         * Round #1: See if theres any requested item of which only
         * this requester wants of. This can happen:
         *
         * 1 The first item is not enough to fulfill this, which is
         *   a player error kind of and we must stop recipe crafting
         *   until they remove this extra input; Since it does not
         *   fulfill any other ingredient, and is not enough to fill
         *   this one, it is basically extraneous.
         *
         * 2 The first item can fulfill this, which means this is an
         *   automatic success for this whole ingredient.
         *
         * 3 No single item is only requested by this requester, in
         *   which case we move to round #2.
         */
        for (Integer dib : dibAmountOrder) {

            // Find the shapeless item and filter in question
            ShapelessItem item = getDibbed(dib);
            ProvidedUIFilter filter = getMatched(dib);
            Validate.isTrue(filter != null && item != null, "Mismatch occurred between the order in which shapeless items are evaluated. ");
            //CRAFT//MythicCraftingManager.log("\u00a78First Claim \u00a7c-F>\u00a77 Found dibbing filter \u00a7e" + filter.toString() + "\u00a77 claiming " + SilentNumbers.getItemName(item.getStack()));

            // Dibbed only by us?
            if (item.getRequesters().size() == 1) {
                //CRAFT//MythicCraftingManager.log("\u00a78First Claim \u00a7a-F>\u00a77 Requested Only Once");

                // Does the item meet the filter amount
                if (item.getAmount() < filter.getAmount(0)) {
                    //CRAFT//MythicCraftingManager.log("\u00a78First Claim \u00a7c-F>\u00a77 Not enough amount to fulfill, denominated 'extraneous' and cancelled");

                    // Ok so what are you doing bruh?
                    /*
                     *  todo verify that this is necessary and feels right.
                     *       -
                     *       This cancels a recipe completely if one of the items
                     *       matches only one ingredient, but does not meet the amount
                     *       requirement.
                     */
                    return false;

                // Hey then it matches!
                } else {
                    //CRAFT//MythicCraftingManager.log("\u00a78First Claim \u00a7a-F>\u00a77 Amount fulfilled. \u00a7aDetermined Unambiguously");

                    // I have been fulfilled
                    fulfilled = true;

                    // I take those
                    item.take(filter.getAmount(0));

                    // Yes
                    return true;
                }
            }
        }

        //CRAFT//MythicCraftingManager.log("\u00a78First Claim \u00a7c2\u00a77 Multiclaim finesse: Suppose everything was for me.");
        /*
         * Round #2: Can I be filled if all resources was for me?
         * Checks items of shared resources to see if any could
         * fulfill this ingredient wholly.
         *
         * 1 Some items may not fulfill this ingredient, but well,
         *   they might be for the other requesters, who knows?
         *
         * 2 Requested item can fulfill this, which means this, if
         *   the other requester is fulfilled with other items, can
         *   be used to fulfill this requester, anyway that's a TRUE
         *   but we cant guarantee the recipe will work.
         *
         * 3 No ingredient can fulfill, that's a full failure.
         */
        for (Integer dib : dibAmountOrder) {

            // Find the shapeless item and filter in question
            ShapelessItem item = getDibbed(dib);
            ProvidedUIFilter filter = getMatched(dib);
            Validate.isTrue(filter != null && item != null, "Mismatch occurred between the order in which shapeless items are evaluated. ");
            //CRAFT//MythicCraftingManager.log("\u00a78First Claim \u00a7c-F>\u00a77 Found dibbing filter \u00a7e" + filter.toString() + "\u00a77 claiming " + SilentNumbers.getItemName(item.getStack()));

            // Does the item meet the filter amount
            if (item.getAmount() >= filter.getAmount(0)) {
                //CRAFT//MythicCraftingManager.log("\u00a78First Claim \u00a7a-F>\u00a77 Amount fulfilled. \u00a7eDetermined Ambiguously");

                // Yes, if shared, this recipe might work
                return true; }
        }

        // No item could have fulfilled this, even if all resources were for me.
        //CRAFT//MythicCraftingManager.log("\u00a78First Claim \u00a7cU\u00a77 Unmet, no amount of ingredients can fill");
        return false;
    }

    /**
     * This requester has been chosen to perform the second step.
     *
     * This consists of simulating all the combinations in which this requester is fulfilled,
     * doing this way by removing this Requester and the ingredients that fulfilled it from
     * the operation.
     *
     * One requester less, some ingredients less, eventually only one will be left to choose
     * from the last remaining ingredients... will it fulfill itself?
     *
     * @return All the combinations of the item pool in which this requester has been fulfilled,
     *         and can thus be ignored and kicked from the operation.
     */
    @NotNull public ArrayList<ArrayList<ShapelessItem>> secondClaim(@NotNull ArrayList<ShapelessItem> originalPool) {

        /*
         * At this point, the original pool:
         *  + Has ALL the items from the beginning of this comparison
         *  + Those items have been dibbed, and underwent the first claim
         *    * This means some will have been taken, and completely fulfilled some ingredients.
         *
         * Anyway, since we wont be including the fulfilled ingredients (that succeeded in the firstClaim())
         * we can assume these are all the original pool items with only one dib, so lets clear those.
         */
        ArrayList<ShapelessItem> relevantPool = new ArrayList<>();

        // Exclude the items with only one requester
        for (ShapelessItem item : originalPool) {

            // Does it have more than one dib?
            if (item.getRequesters().size() > 1) {

                // Include
                relevantPool.add(item);

            // This is a concern because it means that the recipe is not cancelling when an item is unwanted
            } else if (item.getRequesters().size() == 0) {

                // Throw exception
                MythicLib.plugin.getLogger().log(Level.WARNING, FriendlyFeedbackProvider.quickForConsole(FFPMythicLib.get(),
                        "Undibbed item has bypassed onto the $rsecondClaim$b area: $u{0}$b. $fThis should be impossible, contact Gunging.",
                        SilentNumbers.getItemName(item.getStack())));
            }
        }

        /*
         * Now we must generate a new simulation per possible combination.
         */
        ArrayList<ArrayList<ShapelessItem>> ret = new ArrayList<>();

        // One possibility per item that can be fulfilled
        for (ShapelessItem path : getDibbed().values()) {

            // Dupe the relevant pool
            ArrayList<ShapelessItem> pathPool = ShapelessItem.deepCloneList(relevantPool);

            // Is this a successful possibility?
            boolean success = false;

            // Which of the shapeless items is this
            for (ShapelessItem observed : pathPool) {

                // Is it this possibility?
                if (path.getOperationalIdentifier() == observed.getOperationalIdentifier()) {

                    // Take the objects from them
                    ProvidedUIFilter filter = getMatched(path.getOperationalIdentifier());
                    Validate.isTrue(filter != null, "Fatal Error when evaluating Second Claim of Shapeless Recipe: Actually found matching ShapelessItem, but the filter was lost somehow.");

                    // Could this item have fulfilled this?
                    if (observed.getAmount() >= filter.getAmount(0)) {

                        // Take
                        observed.take(filter.getAmount(0));

                        // No need to continue
                        success = true;
                        break;
                    }
                }
            }

            // This item could fulfill this, that makes this one way.
            if (success) {

                // All right that's one possibility
                ret.add(pathPool);
            }
        }

        // That's it
        return ret;
    }
}
