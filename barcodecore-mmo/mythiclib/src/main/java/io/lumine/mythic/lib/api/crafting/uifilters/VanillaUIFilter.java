package io.lumine.mythic.lib.api.crafting.uifilters;

import io.lumine.mythic.lib.api.crafting.uimanager.UIFilterManager;
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
 *  The filter to match a common vanilla item by its material.
 *
 *  @author Gunging
 */
public class VanillaUIFilter implements UIFilter {
    @NotNull @Override public String getIdentifier() { return "v"; }

    @Override
    public boolean matches(@NotNull ItemStack item, @NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {

        // Check validity
        if (!isValid(argument, data, ffp)) { return false; }

        // Check counter matches
        if (cancelMatch(item, ffp)) { return false; }

        // Guaranteed to work
        Material mat = Material.valueOf(argument.toUpperCase().replace(" ", "_").replace("-", "_"));
        final String materialName = mat.toString().toLowerCase().replace("_", " ");

        // Same material?
        if (item.getType().equals(mat)) {

            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.SUCCESS,
                    "Item material matched $s{0}$b. ", materialName);
            return true;

        } else {

            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.FAILURE,
                    "Item material '$u{0}$b' failed to match expected material $f{0}$b. ", item.getType().toString().toLowerCase().replace("_"," "), materialName);
            return false;
        }
    }

    @Override
    public boolean isValid(@NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {

        // Vanilla filter only cares that the argument is a material
        try {

            // Can you determine a material about it?
            Material.valueOf(argument.toUpperCase().replace(" ", "_").replace("-", "_"));

            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.SUCCESS,
                    "Material found, $svalidated$b. ");
            return true;

        // Not a material L
        } catch (IllegalArgumentException ignored) {

            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
            "Expected a vanilla material instead of $u{0}$b. ", argument);
            return false;
        }
    }

    @NotNull
    @Override
    public ArrayList<String> tabCompleteArgument(@NotNull String current) {

        // Filtered
        return SilentNumbers.smartFilter(getMaterialNames(), current, true);
    }

    @NotNull
    @Override
    public ArrayList<String> tabCompleteData(@NotNull String argument, @NotNull String current) {

        // Data is not supported
        return SilentNumbers.toArrayList("0", "(this_is_not_checked,_write_anything)");
    }

    @Override
    public boolean fullyDefinesItem() { return true; }

    @Nullable
    @Override
    public ItemStack getItemStack(@NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {

        // Check that its valid
        if (!isValid(argument, data, ffp)) { return null; }

        // Alr get material
        Material mat = Material.valueOf(argument.toUpperCase().replace(" ", "_").replace("-", "_"));

        FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.SUCCESS,
                "Successfully generated $r{0}$b. ", mat.toString().toLowerCase().replace("_", " "));

        // Just simple like thay
        return new ItemStack(mat);
    }

    @NotNull
    @Override
    public ItemStack getDisplayStack(@NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {

        // Check that its valid
        if (!isValid(argument, data, ffp)) { return ItemFactory.of(Material.BARRIER).name("\u00a7cInvalid Material \u00a7e" + argument).build(); }

        // Alr get material
        Material mat = Material.valueOf(argument.toUpperCase().replace(" ", "_").replace("-", "_"));

        FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.SUCCESS,
                "Successfully generated $r{0}$b. ", mat.toString().toLowerCase().replace("_", " "));

        // Just simple like thay
        return new ItemStack(mat);
    }

    @NotNull
    @Override
    public ArrayList<String> getDescription(@NotNull String argument, @NotNull String data) {

        // Check validity
        if (!isValid(argument, data, null)) { return SilentNumbers.toArrayList("This material is $finvalid$b."); }

        // Guaranteed to work
        Material mat = Material.valueOf(argument.toUpperCase().replace(" ", "_").replace("-", "_"));

        // Description is thus
        return SilentNumbers.toArrayList("This item must be a $r" + mat.toString().toLowerCase().replace("_", " ") + "$b.");
    }

    @Override public boolean determinateGeneration() { return true; }

    static ArrayList<String> materialNames = null;
    static ArrayList<String> getMaterialNames() {

        // The one already loaded
        if (materialNames != null) { return materialNames; }

        // Generate
        materialNames = new ArrayList<>();

        // Or generate the first time
        for (Material m : Material.values()) { materialNames.add(m.toString());  }

        // Yes
        return materialNames;
    }

    /*
     *  Tracking
     */

    @NotNull
    @Override
    public String getSourcePlugin() { return "MythicLib"; }

    @NotNull
    @Override
    public String getFilterName() { return "Vanilla"; }

    @NotNull
    @Override
    public String exampleArgument() { return "carrot"; }

    @NotNull
    @Override
    public String exampleData() { return "0"; }

    /**
     * Registers this filter onto the manager.
     */
    public static void register() {

        // Yes
        global = new VanillaUIFilter();
        UIFilterManager.registerUIFilter(global);
    }

    /**
     * @return The general instance of this MMOItem UIFilter.
     */
    @NotNull public static VanillaUIFilter get() { return global; }
    static VanillaUIFilter global;
}
