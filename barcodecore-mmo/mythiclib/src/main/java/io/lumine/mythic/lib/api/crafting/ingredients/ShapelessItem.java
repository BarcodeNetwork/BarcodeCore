package io.lumine.mythic.lib.api.crafting.ingredients;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.crafting.recipes.ShapelessRecipe;
import io.lumine.mythic.lib.api.util.ui.FFPMythicLib;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackProvider;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Actually <b>not</b> a {@link MythicIngredient} nor {@link MythicRecipeIngredient}.
 *
 * Instead it is a wrapper for {@link ItemStack} so that the items of a
 * shapeless recipe may request them before actually evaluating conflicts.
 *
 * And also the utils to evaluate such conflicts.
 *
 * @author Gunging
 */
public class ShapelessItem implements Cloneable {

    /**
     * Item Stack that is the actual thing being looked at
     */
    @NotNull ItemStack stack;

    /**
     * Item Stack that is the actual thing being looked at
     */
    @NotNull public ItemStack getStack() { return stack; }

    /**
     * Out of the total number of items here, how many
     * have been unambiguously claimed by requesters?
     */
    int taken = 0;
    /**
     * As a requester, indicate that these are yours now.
     */
    public void take(int amount) {
        taken += amount;

        // Bruh
        if (getTaken() > getOriginalAmount()) {

            MythicLib.plugin.getLogger().log(Level.SEVERE, FriendlyFeedbackProvider.quickForConsole(FFPMythicLib.get(),
                    "Error while evaluating a Shapeless Recipe: $fShapeless Item has overdue amount $r{0}$f out of $r{1}$f. ",
                    String.valueOf(getTaken()), String.valueOf(getOriginalAmount())));
        }
    }
    /**
     * Out of the total number of items here, how many
     * have been unambiguously claimed by requesters?
     */
    public int getTaken() { return taken; }
    /**
     * The number of this in here, minus those items that
     * have been claimed by requesters unambiguously.
     */
    public int getAmount() { return getStack().getAmount() - taken; }
    /**
     * The number of this in here, without thinking of those
     * items that have been taken by Requesters
     */
    public int getOriginalAmount() { return getStack().getAmount(); }

    /**
     * To be really sure of which item is which
     */
    final int operationalIdentifier;
    /**
     * To be really sure of which item is which
     */
    public int getOperationalIdentifier() { return operationalIdentifier; }

    /**
     * What was its original position? I might have forgotten bruh.
     * <p></p>
     * <b>Always positive</b>
     */
    final int originalWidthCoord;
    /**
     * What was its original position? I might have forgotten bruh
     * <p></p>
     * <b>Always positive</b>
     */
    public int getOriginalWidthCoord() { return originalWidthCoord; }

    /**
     * What was its original position? I might have forgotten bruh
     * <p></p>
     * <b>Always Negative</b>
     */
    final int originalHeightCoord;
    /**
     * What was its original position? I might have forgotten bruh
     * <p></p>
     * <b>Always Negative</b>
     */
    public int getOriginalHeightCoord() { return originalHeightCoord; }

    /**
     * Shapeless recipes do not support ItemStacks that cannot
     * be items, so keep AIR and FIRE out of here.
     *
     * @param shapelessItem A non-AIR valid ItemStack.
     *
     * @see SilentNumbers#isAir(ItemStack)
     */
    public ShapelessItem(int operationalID, @NotNull ItemStack shapelessItem, int originalW, int originalH) {
        Validate.isTrue(!SilentNumbers.isAir(shapelessItem), "Shapeless recipes do not support material " + shapelessItem.getType().toString() + " because it is not an item. ");

        // Wasn't that simple
        stack = shapelessItem;
        operationalIdentifier = operationalID;
        originalWidthCoord = originalW;
        originalHeightCoord = originalH;
    }

    /**
     * The first step in evaluating shapeless recipes is when the
     * {@link MythicIngredient}s registered onto the {@link ShapelessRecipe}
     * look at the whole item pool, check if the item contained matches,
     * and claim 'dibs' on it.
     *
     * We don't know how many of it they need yet, but we will know which
     * Ingredients seek this item for fulfillment.
     */
    @NotNull final HashMap<Integer, ShapelessRequester> unspecifiedAmountRequests = new HashMap<>();
    /**
     * This item would match this item (ignoring amount).
     *
     * Reserve it for me!
     *
     * @param me The ingredient that matches this ItemStack and would maybe potentially
     *           actually require it for the completion of the Shapeless Recipe.
     */
    public void claimDibs(@NotNull ShapelessRequester me) {

        // Cannot have duplicates yea no
        unspecifiedAmountRequests.put(me.getRequestIdentifier(), me);
    }

    /**
     * @return Those requesters that would like to use this item.
     */
    @NotNull public HashMap<Integer, ShapelessRequester> getRequesters() { return unspecifiedAmountRequests; }

    /**
     * @return A simple (non-deep) clone of this shapeless item.
     *         <p></p>
     *         The clone will know the ItemStack, the operational identifier,
     *         the original co-ordinates, and how much has been unambiguously
     *         taken.
     *         <p></p>
     *         The clone will have no dib claims though, no requesters.
     */
    @Override
    public ShapelessItem clone() {
        try { super.clone(); } catch (CloneNotSupportedException ignored) {}

        // Clone
        ShapelessItem ret = new ShapelessItem(getOperationalIdentifier(), getStack().clone(), getOriginalWidthCoord(), getOriginalHeightCoord());

        // Take
        ret.take(getTaken());

        // Yes
        return ret;
    }

    /**
     * @return A complete clone of this shapeless item.
     *         <p></p>
     *         The clone will know the ItemStack, the operational identifier,
     *         the original co-ordinates, and how much has been unambiguously
     *         taken.
     *         <p></p>
     *         The clone will preserve the original dibs claimed on the first.
     *         Note that these point to the same {@link ShapelessRequester},
     *         the ones in the original and the clone I mean.
     */
    public ShapelessItem completeClone() {

        // Clone
        ShapelessItem ret = this.clone();

        // Dib
        for (ShapelessRequester req : getRequesters().values()) {

            // Claim
            ret.claimDibs(req);
        }

        // Yes
        return ret;
    }

    /**
     * @return Also clones the Shapeless Items
     *         <p></p>
     *         The clones will remember their ItemStack, their operational identifier,
     *         their original co-ordinates, and how much has been unambiguously
     *         taken from them.
     *         <p></p>
     *         The clones will have no dib claims though, no requesters.
     */
    @NotNull public static ArrayList<ShapelessItem> deepCloneList(@NotNull ArrayList<ShapelessItem> list) {
        // Returning
        ArrayList<ShapelessItem> ret = new ArrayList<>();

        for (ShapelessItem tm : list) {

            // Clone and add
            ret.add(tm.clone());
        }

        // That's it
        return ret;
    }

    /**
     * @return Also clones the Shapeless Items, and <b>will</b> preserve dib information.
     *         <p></p>
     *         The clones will remember their ItemStack, their operational identifier,
     *         their original co-ordinates, and how much has been unambiguously
     *         taken from them.
     */
    @NotNull public static ArrayList<ShapelessItem> completeCloneList(@NotNull ArrayList<ShapelessItem> list) {
        // Returning
        ArrayList<ShapelessItem> ret = new ArrayList<>();

        for (ShapelessItem tm : list) {

            // Clone and add
            ret.add(tm.completeClone());
        }

        // That's it
        return ret;
    }
}
