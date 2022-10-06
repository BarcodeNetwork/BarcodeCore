package io.lumine.mythic.lib.api.crafting.uimanager;

import io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager;
import io.lumine.mythic.lib.api.crafting.uifilters.UIFilter;
import io.lumine.mythic.lib.api.crafting.uifilters.VanillaUIFilter;
import io.lumine.mythic.lib.api.util.ui.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 *  When a user writes out a {@link UIFilter}, this class interprets
 *  what they mean (and also adds support for optional <i>amount</i>)
 *
 * @author Gunging
 */
@SuppressWarnings("unused")
public class ProvidedUIFilter implements Comparable<ProvidedUIFilter>, Cloneable {

    /**
     * Probably want to get this using {@link UIFilterManager#getUIFilter(String, FriendlyFeedbackProvider)}
     * rather than through this constructor tbh.
     *
     * @param matchOperator Which UIFilter to use to perform comparisons?
     * @param arg The Argument specified
     * @param dat The Data specified
     *
     * @see #getFromString(String, FriendlyFeedbackProvider)
     */
    public ProvidedUIFilter(@NotNull UIFilter matchOperator, @NotNull String arg, @NotNull String dat) { parent = matchOperator; argument = arg; data = dat; }

    /**
     * Probably want to get this using {@link UIFilterManager#getUIFilter(String, FriendlyFeedbackProvider)}
     * rather than through this constructor tbh.
     *
     * @param matchOperator Which UIFilter to use to perform comparisons?
     * @param arg The Argument specified
     * @param dat The Data specified
     * @param amount The Amount Specified
     *
     * @see #getFromString(String, FriendlyFeedbackProvider)
     */
    public ProvidedUIFilter(@NotNull UIFilter matchOperator, @NotNull String arg, @NotNull String dat, int amount) { parent = matchOperator; argument = arg; data = dat; setAmount(amount); }

    /**
     * Probably want to get this using {@link UIFilterManager#getUIFilter(String, FriendlyFeedbackProvider)}
     * rather than through this constructor tbh.
     *
     * @param matchOperator Which UIFilter to use to perform comparisons?
     * @param arg The Argument specified
     * @param dat The Data specified
     * @param amount The Amount Specified
     *
     * @see #getFromString(String, FriendlyFeedbackProvider)
     */
    public ProvidedUIFilter(@NotNull UIFilter matchOperator, @NotNull String arg, @NotNull String dat, @Nullable QuickNumberRange amount) { parent = matchOperator; argument = arg; data = dat; setAmountRange(amount); }

    /**
     * Commodity method to call {@link UIFilterManager#getUIFilter(String, FriendlyFeedbackProvider)}
     */
    @Nullable
    public static ProvidedUIFilter getFromString(@Nullable String compound, @Nullable FriendlyFeedbackProvider ffp) {

        // Yes
        return UIFilterManager.getUIFilter(compound, ffp);
    }


    /**
     * Quick way of testing if a filter is air or not.
     *
     * @return is this filter searching for vanilla air?
     */
    public boolean isAir() {

        // Not the Vanilla Filter, not AIR
        if(!(getParent() instanceof VanillaUIFilter)) { return false; }

        // Is it air?
        return getArgument().equalsIgnoreCase("AIR");
    }

    /*
     *  " Inherited Methods "
     */

    /**
     * Performs the comparison within using the parent filter.
     * @see UIFilter#matches(ItemStack, String, String, FriendlyFeedbackProvider)
     */
    @SuppressWarnings("ConstantConditions")
    public boolean matches(@NotNull ItemStack item, boolean ignoreAmount, @Nullable FriendlyFeedbackProvider ffp) {

        // Does amount check out?
        int amount = item.getAmount();
        //CHK// MythicCraftingManager.log("\u00a78PooF \u00a7eA\u00a77 Ignore? \u00a76" + ignoreAmount);

        // Amount checks out
        if (ignoreAmount || checkAmount(amount)) {

            // Amount checks out, lets perform a true comparison
            return getParent().matches(item, getArgument(), getData(), ffp);

        // This filter had an amount range defined, and this item did not meet it.
        } else {

            // Mention
            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.FAILURE,
                    "The amount of this item ($u{0}$b) was $fnot the desired $b({1}$b). ",
                    String.valueOf(amount), getAmountRange().toStringColored());
            return false;
        }
    }

    /**
     * Performs the comparison within using the parent filter.
     * @see UIFilter#matches(Inventory, int, int, int, String, String, QuickNumberRange, FriendlyFeedbackProvider)
     */
    public boolean matches(@NotNull Inventory inv, int slot, int w, int h, boolean ignoreAmount, @Nullable FriendlyFeedbackProvider ffp) {

        // What amount to pass
        QuickNumberRange amount = getAmountRange();
        if (ignoreAmount) { amount = null; }

        // Amount check handled within
        return getParent().matches(inv, slot, w, h, getArgument(), getData(), amount, ffp);
    }

    /**
     * Performs the comparison within using the parent filter.
     * @see UIFilter#isValid(String, String, FriendlyFeedbackProvider)
     */
    public boolean isValid(@Nullable FriendlyFeedbackProvider ffp) {

        // Check through parent
        return getParent().isValid(getArgument(), getData(), ffp);
    }

    /**
     * Performs the generation  within using the parent filter.
     * @see UIFilter#getItemStack(String, String, FriendlyFeedbackProvider)
     */
    @Nullable public ItemStack getItemStack(@Nullable FriendlyFeedbackProvider ffp) {

        // Attempt to generate
        ItemStack gen = getParent().getItemStack(getArgument(), getData(), ffp);

        // If null, null
        if (gen == null) { return null; }

        // Set correct amount
        gen.setAmount(getAmount(0));
        return gen;
    }

    /**
     * Generates an item that does not necessarily match this filter,
     * but contains its description in the lore in the worst case scenario.
     *
     * @see UIFilter#getItemStack(String, String, FriendlyFeedbackProvider)
     */
    @NotNull public ItemStack getDisplayStack(@Nullable FriendlyFeedbackProvider ffp) {

        // Attempt to generate
        ItemStack gen = getParent().getDisplayStack(getArgument(), getData(), ffp);

        // Set correct amount
        gen.setAmount(getAmount(1));

        ItemMeta itemMeta = gen.getItemMeta();
        if (itemMeta != null) {

            // Gen
            if (gen.getAmount() > 1) {
                itemMeta.setDisplayName(SilentNumbers.getItemName(gen));
            } else {
                itemMeta.setDisplayName(SilentNumbers.getItemName(gen, false) + "\u00a7\u02ab");
            }
            gen.setItemMeta(itemMeta);
        }

        return gen;
    }

    /**
     * Performs the comparison within using the parent filter.
     * @see UIFilter#getDescription(String, String)
     */
    @NotNull public ArrayList<String> getDescription() {

        // Check through parent
        return getParent().getDescription(getArgument(), getData());
    }

    /*
     *  Actual UIFilter Information
     */

    /**
     *  The item comparator that should be used.
     */
    @NotNull UIFilter parent;
    /**
     *  The item comparator that should be used.
     */
    @NotNull public UIFilter getParent() { return parent; }
    /**
     *  The item comparator that should be used.
     */
    public void setParent(@NotNull UIFilter filter) { parent = filter; }

    String asString = null;
    @Override
    public String toString() {
        if (asString != null) { return asString; }
        String amount = (getAmountRange() == null ? "1.." : getAmountRange().toString());
        asString = getParent().getIdentifier() + " " + getArgument() + " " + getData() + " " + amount;
        return asString;
    }

    /**
     *  The first string provided by the user
     */
    @NotNull String argument;
    /**
     *  The item comparator that should be used.
     */
    @NotNull public String getArgument() { return argument; }
    /**
     *  The item comparator that should be used.
     */
    public void setArgument(@NotNull String arg) { argument = arg; }

    /**
     *  The first string provided by the user
     */
    @NotNull String data;
    /**
     *  The item comparator that should be used.
     */
    @NotNull public String getData() { return data; }
    /**
     *  The item comparator that should be used.
     */
    public void setData(@NotNull String dat) { data = dat; }

    /*
     *  Amount Support
     */

    /**
     * The amount range specified by the user.
     * USUALLY it will be a simple number, not quite a range.
     */
    @Nullable
    QuickNumberRange amountRange;
    /**
     * The amount range specified by the user.
     * USUALLY it will be a simple number, not quite a range.
     */
    @Nullable public QuickNumberRange getAmountRange() { return amountRange; }
    /**
     * The amount range specified by the user.
     * USUALLY it will be a simple number, not quite a range.
     */
    public void setAmountRange(@Nullable QuickNumberRange range) { amountRange = range; }
    /**
     * The amount specified by the user.
     * @return <code>null</code> If it has no amount, or the amount is a range.
     * @see #getAmountRange()
     */
    @SuppressWarnings("ConstantConditions")
    @Nullable public Integer getAmount() {

        // No range no service
        if (getAmountRange() == null) { return null; }

        // Not a simple number no service
        if (!getAmountRange().isSimple()) { return null; }

        // If there is range, it is guaranteed to have a minimum
        return SilentNumbers.round(amountRange.getMinimumInclusive());
    }
    /**
     * Attempts to fit the {@link #getAmountRange()} into a single <code>int</code>,
     * if it fails, it will return def.
     *
     * @param def default in case it is impossible to fit the amount
     *            range into an <code>int</code>
     *
     * @return <code>null</code> If it has no amount, or the amount is a range.
     *
     * @see #getAmount()
     */
    @SuppressWarnings("ConstantConditions")
    public int getAmount(int def) {

        // No range no service
        if (getAmountRange() == null) { return def; }

        // Not a simple number no service
        if (!getAmountRange().isSimple()) {

            // Has min?
            if (getAmountRange().hasMin()) { return SilentNumbers.round(getAmountRange().getMinimumInclusive()); }

            // Maybe max?
            if (getAmountRange().hasMax()) { return SilentNumbers.round(getAmountRange().getMaximumInclusive()); }

            // Nope, return the default
            return def;
        }

        // If its not a range, the minimum equals the maximum so
        return SilentNumbers.round(getAmountRange().getMinimumInclusive());
    }
    /**
     * The amount specified by the user.
     */
    public void setAmount(@Nullable Integer range) {

        // Null?
        if (range == null) { setAmountRange(null); return; }

        // Not null
        setAmountRange(new QuickNumberRange((double) range, (double) range));
    }
    /**
     * Was there an amount specified?
     */
    public boolean hasAmount() { return getAmountRange() != null; }

    /**
     * Is this number a good amount?
     *
     * @param amount Number you are comparing
     *
     * @return <code>false</code> if this number does not fit
     *         in the specified amount range (if there was a
     *         range specified).
     */
    public boolean checkAmount(double amount) {

        // Air checks always succeed on amount
        if (isAir()) { return true; }

        // Delegate to general
        return checkAmount(getAmountRange(), amount);
    }

    /**
     * Utility method for checking this number, in the context of Provided UIFilters
     *
     * @param range Range the number must match, if <code>null</code>, this method returns <code>true</code>.
     * @param amount Number you are comparing
     *
     * @return <code>false</code> if this number does not fit
     *         in the specified amount range (if there was a
     *         range specified).
     */
    public static boolean checkAmount(@Nullable QuickNumberRange range, double amount) {

        // Any amount to compare?
        if (range != null) {

            // Only if range succeeds
            return range.inRange(amount);
        }

        //CHK// MythicCraftingManager.log("\u00a78PooF \u00a7eA\u00a77 Amount passed: \u00a7aNot Specified");
        return true;
    }

    @Override
    public int compareTo(@NotNull ProvidedUIFilter comparate) {

        /*
         * Must return:
         *
         * >0 if this is greater than 'comparate'
         * 0 if they are equal
         * <0 if this is less than 'comparate'
         */
        return getAmount(0) - comparate.getAmount(0);
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public ProvidedUIFilter clone() {
        QuickNumberRange amr = getAmountRange() != null ? getAmountRange().clone() : null;
        return new ProvidedUIFilter(getParent(), getArgument(), getData(), amr);
    }
}
