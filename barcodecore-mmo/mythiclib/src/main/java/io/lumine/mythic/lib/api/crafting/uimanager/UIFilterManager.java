package io.lumine.mythic.lib.api.crafting.uimanager;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.crafting.uifilters.UIFilter;
import io.lumine.mythic.lib.api.util.ui.FFPMythicLib;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackCategory;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackProvider;
import io.lumine.mythic.lib.api.util.ui.QuickNumberRange;
import io.lumine.mythic.lib.version.VersionMaterial;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * Any plugin may register their UIFilters onto here for
 * users to easily access with all the features of MythicLib!
 *
 * @see #registerUIFilter(UIFilter)
 * @author Gunging
 */
public class UIFilterManager {

    /*
     *  Register UIFilters
     */

    /**
     * The HashMap where the UIFilters are linked to their keys
     */
    private final static HashMap<String, UIFilter> loadedUIFilters = new HashMap<>();

    /**
     * Register your filter to work with every plugin.
     * <p></p>
     * This method will yell hell at the console if:
     * <p><code><b>*</b></code> Your filter has the same key as other filter
     * </p><code><b>*</b></code> Your filter does not check {@link UIFilter#cancelMatch(ItemStack, FriendlyFeedbackProvider)}
     *                           when calling {@link UIFilter#matches(ItemStack, String, String, FriendlyFeedbackProvider)}
     * <p><code><b>*</b></code> Your example arguments and data don't check out
     *
     * @param filter Filter you are registering
     */
    public static void registerUIFilter(@NotNull UIFilter filter) {
        FriendlyFeedbackProvider ffp = new FriendlyFeedbackProvider(FFPMythicLib.get());
        ffp.activatePrefix(true, "UIFilter Registry");

        /*
         *  Numero zero: Not before MythicLib loads!
         */
        if (MythicLib.plugin == null) {

            // Add
            ffp.log(FriendlyFeedbackCategory.ERROR,
                    "Filter '$u{0}$b' of $u{1}$b is being registered before MythicLib loads. $fDon't do that. ",
                    filter.getFilterName(), filter.getSourcePlugin());

            // Send
            ffp.sendAllTo(MythicLib.plugin.getServer().getConsoleSender());
            return;
        }

        /*
         *  Numero uno: Make sure there is no key conflict
         */
        if (loadedUIFilters.containsKey(filter.getIdentifier())) {

            // Find
            UIFilter aggravate = loadedUIFilters.get(filter.getIdentifier());

            // Add
            ffp.log(FriendlyFeedbackCategory.ERROR,
        "Filter '$u{0}$b' of $u{1}$b has a conflicting key with $e{2}$b's '$e{3}$b' filter. $fIt has been rejected. ",
                filter.getFilterName(), filter.getSourcePlugin(), aggravate.getSourcePlugin(), aggravate.getFilterName());

            // Send
            ffp.sendAllTo(MythicLib.plugin.getServer().getConsoleSender());
            return;
        }

        /*
         *  Numero dos: Example argument
         */
        if (!filter.isValid(filter.exampleArgument(), filter.exampleData(), ffp)) {

            // Add as 'success' because the ffp now contains ERROR information on why the filter failed.
            ffp.log(FriendlyFeedbackCategory.OTHER,
                    "Filter '$u{0}$b' of $u{1}$b has $finvalid example argument and data$b: ",
                    filter.getFilterName(), filter.getSourcePlugin());

            // Send that message first
            ffp.sendTo(FriendlyFeedbackCategory.OTHER, MythicLib.plugin.getServer().getConsoleSender());
            ffp.clearFeedbackOf(FriendlyFeedbackCategory.OTHER);

            // Send the errors
            ffp.sendAllTo(MythicLib.plugin.getServer().getConsoleSender());
            return;
        }

        /*
         *  Numero tres: Countermatch Check
         */
        UIFilterBasicCountermatch tester = new UIFilterBasicCountermatch();
        filter.addMatchOverride(tester);
        filter.matches(VersionMaterial.CAULDRON.toItem(), filter.exampleArgument(), filter.exampleData(), null);

        if (!tester.wasEvaluated()) {

            // Add as 'success' because the ffp now contains ERROR information on why the filter failed.
            ffp.log(FriendlyFeedbackCategory.ERROR,
                    "Filter '$u{0}$b' of $u{1}$b does not check for counter matches. $fRejected$b.",
                    filter.getFilterName(), filter.getSourcePlugin());

            // Send the errors
            ffp.sendTo(FriendlyFeedbackCategory.ERROR, MythicLib.plugin.getServer().getConsoleSender());
            return;
        }

        // It has now loaded :wazowskibruhmoment:
        tester.load();

        // Looks fine to me ngl
        loadedUIFilters.put(filter.getIdentifier(), filter);
    }

    /*
     *  Read UIFilters from user input
     */

    /**
     * Will read whatever UIFilter could be contained in this string
     * in the format <code><b>key argument data amount</b></code> where
     * only <code>amount</code> is optional.
     * @param compound String that may not be in the correct format
     * @param ffp Tell the user why their input is invalid with the help of this.
     * @return A built ProvidedUIFilter <i>iff</i> the compound is in correct
     *         syntax. <code>null</code> otherwise.
     *         Note that the argument and data may not make sense and this will
     *         still 'succeed,' that is checked elsewhere.
     */
    @Contract("null,_ -> null")
    @Nullable public static ProvidedUIFilter getUIFilter(@Nullable String compound, @Nullable FriendlyFeedbackProvider ffp) {

        // Filter does not exist to begin with
        if (compound == null) {

            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                    "No string was provided to even attempt to read a UIFilter from it. ");
            return null;
        }

        // Attempt to split
        String[] split = compound.split(" ");
        if (split.length < 3) {

            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                    "Provided string '$u{0}$b' was not in the expected format '$ekey argument data amount$b' (where amount is optional). ", compound);
            return null;
        }

        // Get amount?
        String amount = null; if (split.length >= 4) { amount = split[3]; }

        // Pass on
        return getUIFilter(split[0], split[1], split[2], amount, ffp);
    }

    /**
     * Will read whatever UIFilter could be contained in this string
     * in the format <code><b>key argument data amount</b></code> where
     * only <code>amount</code> is optional.
     * @param key Key to search among the loaded filters.
     * @param argument First string specified by the user.
     * @param data Second string specified by the user.
     * @param amount Optional string that should parse into a number or a {@link QuickNumberRange}
     * @param ffp Tell the user why their input is invalid with the help of this.
     * @return A built ProvidedUIFilter if no problems are encountered.
     *         Note that the argument and data may not make sense and this will
     *         still 'succeed,' that is checked elsewhere.
     */
    @Nullable public static ProvidedUIFilter getUIFilter(@NotNull String key, @NotNull String argument, @NotNull String data, @Nullable String amount, @Nullable FriendlyFeedbackProvider ffp) {

        // Loaded filter?
        UIFilter ofKey = loadedUIFilters.get(key);

        // Filter does not exist to begin with
        if (ofKey == null) {

            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                    "There is no UIFilter loaded with key $u{0}$b. ", key);
            return null;
        }

        // Should/does amount parse?
        QuickNumberRange amountRange = QuickNumberRange.getFromString(amount);
        if (amount != null && amountRange == null) {

            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                    "Expected a number or range like $e4..6$b instead of $u{0}$b as an amount. ", amount);
            return null;
        }

        // Build
        ProvidedUIFilter provided = new ProvidedUIFilter(ofKey, argument, data);
        provided.setAmountRange(amountRange);

        FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.SUCCESS,
                "Correctly built provided filter $r{0}$b from $u{1} {2} {3}$b. ", ofKey.getFilterName(), key, argument, data);

        if (amountRange != null) {
            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.SUCCESS,
                    "Amount range: {0}", amountRange.toStringColored()); }

        return provided;
    }

}
