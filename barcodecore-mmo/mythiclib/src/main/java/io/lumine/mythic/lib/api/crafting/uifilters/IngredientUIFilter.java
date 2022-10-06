package io.lumine.mythic.lib.api.crafting.uifilters;

import io.lumine.mythic.lib.api.crafting.ingredients.MythicIngredient;
import io.lumine.mythic.lib.api.crafting.uimanager.ProvidedUIFilter;
import io.lumine.mythic.lib.api.crafting.uimanager.UIFilterManager;
import io.lumine.mythic.lib.api.util.AltChar;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackCategory;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackProvider;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import io.lumine.utils.items.ItemFactory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 *  The filter to match an item if it satisfies any of an ingredient's substitutes.
 *
 *  @author Gunging
 */
@Deprecated
public class IngredientUIFilter implements UIFilter {
    @NotNull
    @Override
    public String getIdentifier() {
        return "i";
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean matches(@NotNull ItemStack item, @NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {

        // Check validity
        if (!isValid(argument, data, ffp)) { return false; }

        // Check counter matches
        if (cancelMatch(item, ffp)) { return false; }

        // Guaranteed to work
        MythicIngredient ingredient = MythicIngredient.get(argument);

        // Does the ingredient match?
        return ingredient.matches(item, ffp);
    }

    @Override
    public boolean isValid(@NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {

        // Attempt to find mythic ingredient
        if (MythicIngredient.get(argument) != null) {

            // Found
            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.SUCCESS,
                    "Mythic Ingredient found, $svalidated$b. ");
            return true;

        // Was null?
        } else {

            // Not found
            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                    "Mythic Ingredient '$u{0}$b' not found, $frejected$b. ", argument);
            return false;
        }
    }

    @NotNull
    @Override
    public ArrayList<String> tabCompleteArgument(@NotNull String current) {
        return SilentNumbers.smartFilter(MythicIngredient.getEnabledIngredients(), current, true); }

    @NotNull
    @Override
    public ArrayList<String> tabCompleteData(@NotNull String argument, @NotNull String current) {
        return SilentNumbers.toArrayList("0", "(this_is_not_checked,_write_anything)"); }

    @Override
    public boolean fullyDefinesItem() { return true; }

    @Nullable
    @Override
    @SuppressWarnings("ConstantConditions")
    public ItemStack getItemStack(@NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {

        // Check that its valid
        if (!isValid(argument, data, ffp)) { return null; }

        // Get ingredient
        MythicIngredient ingredient = MythicIngredient.get(argument);

        return ingredient.getRandomSubstituteItem(null);
    }

    @SuppressWarnings("ConstantConditions")
    @NotNull
    @Override
    public ItemStack getDisplayStack(@NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {

        // Check that its valid
        if (!isValid(argument, data, ffp)) { return ItemFactory.of(Material.BARRIER).name("\u00a7cInvalid MythicIngredient").build(); }

        // Get ingredient
        MythicIngredient ingredient = MythicIngredient.get(argument);

        return ingredient.getRandomDisplayItem(null);
    }

    @NotNull
    @Override
    @SuppressWarnings("ConstantConditions")
    public ArrayList<String> getDescription(@NotNull String argument, @NotNull String data) {

        // Check validity
        if (!isValid(argument, data, null)) { return SilentNumbers.toArrayList("Ingredient '$u" + argument + "$b' is $finvalid$b."); }

        // Get ingredient
        MythicIngredient ingredient = MythicIngredient.get(argument);

        // Build result
        ArrayList<String> ret = new ArrayList<>();
        ret.add("$sAny of the following:");

        // Add every ingredient description
        for (ProvidedUIFilter puff : ingredient.getSubstitutes()) {

            // Build amount string?
            String prefix = " $r" + AltChar.listDash + " $b";
            if (puff.hasAmount()) { prefix = " " + puff.getAmountRange().toStringColored() + "x $b"; }
            StringBuilder tab = new StringBuilder();
            for (int s = 0; s < prefix.length(); s++) { tab.append(" "); }
            boolean first = true;
            String tabulation = tab.toString();

            // Add all the description things yes
            for (String str : puff.getDescription()) {

                // Append number amount
                if (first) {

                    // That yes
                    ret.add(prefix + str);
                    first = false;

                // Tab amount
                } else {

                    // That yes
                    ret.add(tabulation + str);
                }
            }
        }

        // Well that's it
        return ret;
    }

    @Override
    public boolean determinateGeneration() { return false; }

    /**
     * Its cool because players can then actually somehow
     * choose what ingredients they are going to get by
     * re-rolling.
     */
    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean partialDeterminateGeneration(@NotNull String argument, @NotNull String data) {
        if (!isValid(argument, data, null)) { return false; }

        // Find
        MythicIngredient ingredient = MythicIngredient.get(argument);

        // Determinate if it has only 1 substitute
        return ingredient.getSubstitutes().size() <= 1;
    }

    @NotNull
    @Override
    public String getSourcePlugin() { return "MythicLib"; }

    @NotNull
    @Override
    public String getFilterName() { return "MythicIngredient"; }

    @NotNull
    @Override
    public String exampleArgument() {
        return "wood_planks";
    }

    @NotNull
    @Override
    public String exampleData() {
        return "0";
    }

    /**
     * Registers this filter onto the manager.
     */
    public static void register() {

        // Create that supreme sample ingredient
        ArrayList<ProvidedUIFilter> planks = new ArrayList<>();
        planks.add(UIFilterManager.getUIFilter("v", Material.OAK_PLANKS.toString(), "0", null, null));
        planks.add(UIFilterManager.getUIFilter("v", Material.SPRUCE_PLANKS.toString(), "0", null, null));
        planks.add(UIFilterManager.getUIFilter("v", Material.BIRCH_PLANKS.toString(), "0", null, null));
        planks.add(UIFilterManager.getUIFilter("v", Material.JUNGLE_PLANKS.toString(), "0", null, null));
        planks.add(UIFilterManager.getUIFilter("v", Material.ACACIA_PLANKS.toString(), "0", null, null));
        planks.add(UIFilterManager.getUIFilter("v", Material.DARK_OAK_PLANKS.toString(), "0", null, null));
        MythicIngredient plank = new MythicIngredient("wood_planks", planks);
        MythicIngredient.enable(plank);

        // Yes
        global = new IngredientUIFilter();
        UIFilterManager.registerUIFilter(global);
    }

    /**
     * @return The general instance of this MMOItem UIFilter.
     */
    @NotNull public static IngredientUIFilter get() { return global; }
    static IngredientUIFilter global;
}
