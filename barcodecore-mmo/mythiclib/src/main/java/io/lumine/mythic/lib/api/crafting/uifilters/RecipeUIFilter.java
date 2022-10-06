package io.lumine.mythic.lib.api.crafting.uifilters;

import io.lumine.mythic.lib.api.crafting.ingredients.MythicIngredient;
import io.lumine.mythic.lib.api.crafting.ingredients.MythicRecipeIngredient;
import io.lumine.mythic.lib.api.crafting.ingredients.MythicRecipeInventory;
import io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager;
import io.lumine.mythic.lib.api.crafting.recipes.MythicRecipe;
import io.lumine.mythic.lib.api.crafting.recipes.ShapelessRecipe;
import io.lumine.mythic.lib.api.crafting.uimanager.ProvidedUIFilter;
import io.lumine.mythic.lib.api.crafting.uimanager.UIFilterManager;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackProvider;
import io.lumine.mythic.lib.api.util.ui.QuickNumberRange;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import io.lumine.mythic.lib.version.VersionMaterial;
import io.lumine.utils.items.ItemFactory;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * A most advanced UIFilter, checks an inventory taking a target slot
 * as the TOP-LEFT corner of the cuboid in question, and examining an
 * inventory of a provided width and height for a match of this recipe.
 * <p></p>
 * May be shaped or shapeless or whatever actually. Only has capacity
 * of checking one inventory though, keep that in mind.
 *
 * @author Gunging
 */
public class RecipeUIFilter implements UIFilter {
    @NotNull
    @Override
    public String getIdentifier() {
        return "r";
    }

    @NotNull static ItemStack getFromInventory(@NotNull Inventory inven, int slot) {

        // Get that one
        ItemStack target = inven.getItem(slot);

        // Yes
        if (target == null) { target = VersionMaterial.CAULDRON.toItem(); }

        // That's it
        return target;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean matches(@NotNull Inventory inventory, int targetSlot, int width, int height, @NotNull String argument, @NotNull String data, @Nullable QuickNumberRange amount, @Nullable FriendlyFeedbackProvider ffp) {

        // Invalid?
        if (!isValid(argument, data, ffp)) { return false; }

        // Sure you want to cancel? I mean ok
        if (cancelMatch(getFromInventory(inventory, targetSlot), ffp)) { return false; }

        // Get recipe
        MythicRecipe recipe = MythicCraftingManager.getLoadedRecipe(argument);

        // Powerful summations
        MythicRecipeInventory extract = new MythicRecipeInventory();
        for (int h = 0; h < height; h++) {

            // Build row
            ItemStack[] row = new ItemStack[width];
            for (int w = 0; w < width; w++) {

                // Find current slot
                int current = targetSlot + w + (h * width);

                // If hasn't exceeded
                if (inventory.getSize() > current) {

                    // Add to array for row
                    ItemStack found = inventory.getItem(current);
                    if (found != null) { found = found.clone(); }

                    // Yes
                    row[w] = found;
                }
            }

            // Add rot
            extract.addRow(row);
        }

        // Does it match?
        return recipe.matches(extract, null) != null;
    }

    @Override
    public boolean matches(@NotNull ItemStack item, @NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {

        // Test for cancellation
        cancelMatch(item, ffp);

        // Must use inventory match
        return false;
    }

    @Override
    public boolean isValid(@NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {

        // Can you find this loaded recipe?
        return MythicCraftingManager.getLoadedRecipe(argument) != null;
    }

    @NotNull
    @Override
    public ArrayList<String> tabCompleteArgument(@NotNull String current) {
        return SilentNumbers.smartFilter(MythicCraftingManager.getLoadedRecipes(), current, true);
    }

    @NotNull
    @Override
    public ArrayList<String> tabCompleteData(@NotNull String argument, @NotNull String current) {
        return SilentNumbers.toArrayList("0", "(this_is_not_checked,_write_anything)");
    }

    @Override
    public boolean fullyDefinesItem() {
        return false;
    }

    @Override
    public boolean useInventoryMatch() {
        return true;
    }

    @Nullable
    @Override
    public ItemStack getItemStack(@NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {

        // This may only be used only as a reader
        return null;
    }

    @NotNull
    @Override
    public ItemStack getDisplayStack(@NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {

        return ItemFactory.of(Material.CRAFTING_TABLE).name("\u00a7aMythicRecipe \u00a7e " + argument).lore(getDescription(argument, data)).build(); }

    @NotNull
    @Override
    public ArrayList<String> getDescription(@NotNull String argument, @NotNull String data) {

        // Check validity
        if (!isValid(argument, data, null)) { return SilentNumbers.toArrayList("Recipe '$u" + argument + "$b' is $finvalid$b."); }

        return SilentNumbers.chop("Checks that the area contains the shaped recipe \u00a7b\u00a7o" + argument + "\u00a77\u00a7o in correct order and amounts.", 55, "\u00a77\u00a7o");
    }

    /**
     * @return <code>true</code> because this UIFilter is guaranteed
     *         to generate <code>null</code> ItemStacks 100% of the time.
     */
    @Override
    public boolean determinateGeneration() { return true; }

    @NotNull
    @Override
    public String getSourcePlugin() {
        return "MythicLib";
    }

    @NotNull
    @Override
    public String getFilterName() {
        return "MythicRecipe";
    }

    @NotNull
    @Override
    public String exampleArgument() {
        return "mysterious_sample_recipe";
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
        MythicIngredient ingredient = MythicIngredient.get("wood_planks");
        Validate.isTrue(ingredient != null, "RecipeUIFilter must enable after the IngredientUIFilter does, so that we can use that ingredient.");

        // Create OP recipe
        ShapelessRecipe recipe = new ShapelessRecipe("mysterious_sample_recipe", new MythicRecipeIngredient(ingredient));
        recipe.load();

        // Yes
        global = new RecipeUIFilter();
        UIFilterManager.registerUIFilter(global);

        // Remove that recipe plz
        recipe.unload();
    }

    /**
     * @return The general instance of this MMOItem UIFilter.
     */
    @NotNull public static RecipeUIFilter get() { return global; }
    static RecipeUIFilter global;
}
