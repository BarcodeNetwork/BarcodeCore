package io.lumine.mythic.lib.api.crafting.uifilters;

import io.lumine.mythic.lib.api.crafting.uimanager.ProvidedUIFilter;
import io.lumine.mythic.lib.api.crafting.uimanager.UIFilterCountermatch;
import io.lumine.mythic.lib.api.util.ui.*;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *  When an user wants to detect a plugin item, they can easily
 *  use one of these to specify the machine what to look for!
 *  <p></p>
 *  Item filters are <b>always</b> specified as three words
 *  separated by spaces; the format is thus:
 *  <p><code><b>Identifier Argument Data</b></code></p>
 *  <p> </p>
 *  The most important is the first one, it will tell MythicLib
 *  to use your UIFilter to perform operations, the other two
 *  you may handle however you want :)
 *  <p></p>
 *  <b>By convention, never expect bars <code>|</code> in arguments
 *  or data</b>. This character shall only be used as separator in
 *  lists of UIFilters.
 *
 *  @author Gunging
 */
@SuppressWarnings("unused")
public interface UIFilter {

    /*
     *  Prime Functionality
     */

    /**
     * The short key by which users indicate they are using your plugin's
     * UIFilter to detect their item.
     * <p></p>
     * Example: <code><b>m</b></code> for MMOItems
     */
    @NotNull String getIdentifier();

    /**
     * A rather advanced form of {@link #matches(ItemStack, String, String, FriendlyFeedbackProvider)}
     * that allows to support matching items even <i>around</i> the target item itself (which you
     * should be able to find in the target slot).
     *
     * @param inventory The inventory containing thay item
     * @param targetSlot A slot, what it means depends on the filter
     * @param width Probably how wide the inventory is (3 for crafting tables, 9 for chests...)
     * @param height Probably how tall the inventory is (3 for crafting tables, 6 for double chests...)
     * @param argument First user-specified String of information
     * @param data Second user-specified String of information
     * @param amount User-specified amount, usually checked through {@link ProvidedUIFilter}, but
     *               if you're doing this advanced operation, the amount checking is up to you.
     * @param ffp Tell the user why their item did not match, or if it did,
     *            with this interface!
     * @return <code>true</code> if the item matches the user-specified strings.
     *
     * @see #useInventoryMatch() If you override this method, you will want to override this to true too.
     */
    default boolean matches(@NotNull Inventory inventory, int targetSlot, int width, int height, @NotNull String argument, @NotNull String data, @Nullable QuickNumberRange amount, @Nullable FriendlyFeedbackProvider ffp) {

        // Item valid?
        ItemStack item = inventory.getItem(targetSlot);

        // No lol
        if (item == null) { item = new ItemStack(Material.AIR); }

        // Get Amount
        int itemAmount = item.getAmount();

        if (!ProvidedUIFilter.checkAmount(amount, itemAmount)) {

            // Mention
            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.FAILURE,
                    "The amount of this item ($u{0}$b) was $fnot the desired $b({1}$b). ",
                    String.valueOf(itemAmount), amount.toStringColored());
            return false;
        }

        // Just passes onto matches
        return matches(item, argument, data, ffp);
    }

    /**
     * Is this ItemStack what the user meant to target with this
     * <code>Argument-Data</code> combination of input?
     * <p></p>
     * <p></p>
     * <p><i>Note for devs implementing this class:</i></p>
     * <b><i>It is very important that the {@link #cancelMatch(ItemStack, FriendlyFeedbackProvider)}
     * method is used here in case any other plugin has compatibility issues with your UIFilter</i></b>
     *
     * @param item The item being compared
     * @param argument First user-specified String of information
     * @param data Second user-specified String of information
     * @param ffp Tell the user why their item did not match, or if it did,
     *            with this interface!
     * @return <code>true</code> if the item matches the user-specified strings.
     * @see #cancelMatch(ItemStack, FriendlyFeedbackProvider)
     */
    boolean matches(@NotNull ItemStack item, @NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp);

    /**
     * This method is to quickly check user input, tells if the two strings provided
     * are in the correct syntax and could actually work when used.
     * <p></p>
     * Basically, if the user is required to provide a number, you'd check that the
     * user did provide a number here, and return <code>false</code> if they didn't.
     *
     * @param argument The first user-specified string
     * @param data The second user-specified string
     * @param ffp Tell the user why their stuff doesn't make sense with this.
     * @return <code>true</code> if these Strings make sense to use with this UIFilter
     */
    boolean isValid(@NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp);

    /*
     *  Tab Completer
     */

    /**
     * We have no information what the second string will be, but
     * we know the user is using your UIFilter. What do you suggest
     * them to write as the first string?
     *
     * @param current What the user has written so far.
     *
     * @return A list of valid suggestions to display as the first
     *         user-specified String.
     */
    @NotNull ArrayList<String> tabCompleteArgument(@NotNull String current);

    /**
     * The user has fully specified the first string (the argument),
     * knowing this, what will you suggest the user to write next?
     *
     * @param argument The first user-specified string.
     * @param current What the user has written so far.
     *
     * @return A list of valid suggestions to display as the second
     *         user-specified String.
     */
    @NotNull ArrayList<String> tabCompleteData(@NotNull String argument, @NotNull String current);

    /*
     *  Item Generation
     */

    /**
     * With only the two strings specified, can your plugin generate a complete item?
     * <p></p>
     * Example: <code><b>v STONE 0</b></code> for a stone block
     *
     * @see #getItemStack(String, String, FriendlyFeedbackProvider)
     * @see #determinateGeneration()
     */
    boolean fullyDefinesItem();

    /**
     * This method indicates if you can check
     * {@link #matches(ItemStack, String, String, FriendlyFeedbackProvider)} or are required to use
     * {@link #matches(Inventory, int, int, int, String, String, QuickNumberRange, FriendlyFeedbackProvider)}
     * instead
     * <p></p>
     * Most filters will not require the inventory one, but it should work always.
     */
    default boolean useInventoryMatch() { return false; }

    /**
     * Generate an ItemStack from these two strings. Remember to check {@link #fullyDefinesItem()}
     * to see if its even possible when using this UIFilter.
     *
     * @param argument First string that the user will provide
     * @param data Second string that the user will provide
     * @param ffp Tell the user why this could not generate an item using this.
     * @return <code>null</code> if anything goes wrong. Please do not throw exceptions.
     * @see #fullyDefinesItem()
     * @see #determinateGeneration()
     */
    @Nullable ItemStack getItemStack(@NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp);

    /**
     * Generate an ItemStack to display in the recipe book or before an item
     * is crafted, this shall not be used for items that the player will obtain.
     *
     * @param argument First string that the user will provide
     * @param data Second string that the user will provide
     * @param ffp Tell the user why this could not generate an item using this.
     * @return <code>null</code> if anything goes wrong. Please do not throw exceptions.
     *
     * @see #fullyDefinesItem()
     * @see #determinateGeneration()
     */
    @NotNull ItemStack getDisplayStack(@NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp);

    /**
     * Describe the meaning of the user providing these two strings.
     * <p>Think of it as, the user asks
     * </p>'<code>what does <b>v EMERALD 0</b> mean?</code>'
     * <p>and you answer
     * </p>'<i><code>This item must be a vanilla emerald.</code></i>'
     * <p></p>
     * You may indicate that their arguments do not make sense if its the case.
     * <p></p>
     * This method shall parse no color codes of any kind.
     * <b>Probably uses the {@link FriendlyFeedbackPalette}
     * color codes system</b> rather than normal color codes.
     */
    @NotNull ArrayList<String> getDescription(@NotNull String argument, @NotNull String data);

    /**
     * For optimization, some places may want to generate an item when loading, and no more thereafter.
     * This is only an option if there is randomness involved when generating, in which case this
     * should return <code>false</code>.
     *
     * @return <code>true</code> if the result of {@link #getItemStack(String, String, FriendlyFeedbackProvider)} is the
     * same no matter what.
     *
     * @see #getItemStack(String, String, FriendlyFeedbackProvider)
     * @see #fullyDefinesItem()
     * @see #partialDeterminateGeneration(String, String)
     */
    default boolean determinateGeneration() { return true; }

    /**
     * Maybe {@link #determinateGeneration()} is false because not all your items generate
     * the same thing every time they run, though there may be some who do, perhaps during
     * runtime itself and not when loading.
     * <p></p>
     * <code>If {@link #determinateGeneration()} == false</code> and this method returns
     * true, it means that you are safe to call {@link #getItemStack(String, String, FriendlyFeedbackProvider)}
     * once and then just set to the amount you need.
     * <p></p>
     * If this method returns <code>false</code>, you must generate one new item for
     * each up to the amount you require, unless you really don't want randomness.
     * <p></p>
     * If {@link #determinateGeneration()} is true, this method should always be
     * true as well.
     *
     * @return <code>true</code> if the result of {@link #getItemStack(String, String, FriendlyFeedbackProvider)} is the
     * same, at least during a short time (usually one tick) for these argument and data combinations.
     *
     * @see #getItemStack(String, String, FriendlyFeedbackProvider)
     * @see #determinateGeneration()
     * @see #fullyDefinesItem()
     */
    default boolean partialDeterminateGeneration(@NotNull String argument, @NotNull String data) { return determinateGeneration(); }

    /*
     *  Counter Matching
     */

    /**
     * A plugin may register one of these to prevent its own items from matching those of other plugins.
     */
    @NotNull HashMap<String, ArrayList<UIFilterCountermatch>> matchOverrides = new HashMap<>();

    /**
     * A plugin may register one of these to prevent its own items from matching those of other plugins.
     */
    default void addMatchOverride(@NotNull UIFilterCountermatch countermatch) { matchOverrides.put(getIdentifier(), SilentNumbers.addAll(matchOverrides.get(getIdentifier()), countermatch)); }

    /**
     * Supposing your UIFilter is wrongly matching the items of another plugin, in
     * that case, the other plugin may insert a Match Override into your UIFilter
     * so that you are not required to do anything.
     *
     * @return <code>true</code> if the item must <b>not</b> match.
     * @see #matches(ItemStack, String, String, FriendlyFeedbackProvider)
     */
    default boolean cancelMatch(@NotNull ItemStack item, @Nullable FriendlyFeedbackProvider ffp) {
        matchOverrides.computeIfAbsent(getIdentifier(), k -> new ArrayList<>());

        // Only one is required
        boolean failure = false;

        // Check every override
        for (UIFilterCountermatch ufc : matchOverrides.get(getIdentifier())) {

            // Logs
            int e = 0;
            if (ffp != null) { ffp.messagesTotal(); }

            // Check
            boolean thisPrevent = ufc.preventsMatching(item, ffp);
            failure = thisPrevent || failure;

            if (thisPrevent && ffp != null) {

                // No new messages? Fine, Ill do it myself-
                if (ffp.messagesTotal() == e) {

                    // Log
                    ffp.log(FriendlyFeedbackCategory.ERROR, "Item match was cancelled by $f{0}$b for an unspecified reason.", ufc.getClass().getPackage().getName() + "." + ufc.getClass().getSimpleName());
                }
            }
        }

        // Well what was it?
        return failure;
    }

    /*
     *  Author Stuff
     */

    /**
     * @return Name of the plugin this filter comes from.
     */
    @NotNull String getSourcePlugin();

    /**
     * @return Name of this filter :p
     */
    @NotNull String getFilterName();

    /**
     * @return An example of an argument that will, in combination with {@link #exampleData()},
     *         make {@link #isValid(String, String, FriendlyFeedbackProvider)} return <code>true</code>.
     */
    @NotNull String exampleArgument();

    /**
     * @return An example of a data string that will, in combination with {@link #exampleArgument()},
     *         make {@link #isValid(String, String, FriendlyFeedbackProvider)} return <code>true</code>.
     */
    @NotNull String exampleData();
}
