package io.lumine.mythic.lib.api.crafting.recipes;

import io.lumine.mythic.lib.api.crafting.ingredients.MythicRecipeIngredient;
import io.lumine.mythic.lib.api.crafting.ingredients.MythicRecipeInventory;
import io.lumine.mythic.lib.api.crafting.uifilters.RecipeUIFilter;
import io.lumine.mythic.lib.api.util.Ref;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * A recipe for the MythicRecipes system.
 * <p></p>
 * Allows a much more powerful crafting system, designed for cross-compatibility
 * of plugins as well as a few utilities for support of RPGs and MMOs
 *
 * @author Gunging
 */
@SuppressWarnings("unused")
public abstract class MythicRecipe {

    /**
     * The ingredients registered to this recipe
     */
    @NotNull final ArrayList<MythicRecipeIngredient> mythicIngredients;
    /**
     * Add an ingredient to this recipe.
     * <p></p>
     * Notice that different recipes will accept different types of ingredients.
     */
    public abstract void addIngredient(@NotNull MythicRecipeIngredient ingredient);
    /**
     * The ingredients registered to this recipe.
     * <p></p>
     * This is the array itself.
     */
    @NotNull public ArrayList<MythicRecipeIngredient> getIngredients() { return mythicIngredients; }
    /**
     * Clear t ingredients registered to this recipe
     */
    public void clearIngredients() { mythicIngredients.clear(); }

    /**
     * The name of this recipe
     */
    @NotNull final String name;
    /**
     * The name of this recipe
     */
    @NotNull public String getName() { return name; }

    /**
     * @return The kind of station this recipe is intended for.
     */
    @NotNull public abstract MythicRecipeStation getType();

    /**
     * Most basic creation of a mythic recipe but... This recipe wont work for anything!
     */
    public MythicRecipe(@NotNull String name, @NotNull ArrayList<MythicRecipeIngredient> ingredients) {
        this.name = name;

        // Get ingredients
        mythicIngredients = ingredients;
    }
    /**
     * Most basic creation of a mythic recipe but... This recipe wont work for anything!
     */
    public MythicRecipe(@NotNull String name, @NotNull MythicRecipeIngredient... ingredients) {
        this.name = name;

        // Get ingredients
        mythicIngredients = new ArrayList<>();
        for (MythicRecipeIngredient ing : ingredients) {

            // Ignore if null
            if (ing == null) { continue; }

            // Add
            mythicIngredients.add(ing);
        }
    }

    /**
     * Is this inventory fulfilling of this recipe?
     *
     * @param inventory Information on the location of every ItemStack, as
     *                  well as the dimensions of the inventory provided.
     *
     * @param timesToCompletion How many times must this recipe run to reach completion.
     *                          That is, when the first ingredient will run out first.
     *
     * @return If every item stack was in the correct order, so that the recipe was met,
     *         it builds a MythicRecipeInventory with the recipe carried out (consuming
     *         ingredients, basically).
     *         <p></p>
     *         <code>null</code> if it did not match.
     */
    @Nullable public abstract MythicRecipeInventory matches(@NotNull MythicRecipeInventory inventory, @Nullable Ref<Integer> timesToCompletion);

    /**
     * Load this recipe to be used with the {@link RecipeUIFilter} filter.
     */
    public void load() { MythicCraftingManager.loadRecipe(this); }

    /**
     * This recipe may no longer be used with the {@link RecipeUIFilter} filter.
     */
    public void unload() { MythicCraftingManager.unloadRecipe(this); }

    /**
     * Is this recipe enabled to be detected by workbenches and furnaces?
     */
    boolean live = false;
    /**
     * Is this recipe enabled to be detected by workbenches and furnaces?
     */
    public boolean isLive() { return live; }
    /**
     * Is this recipe enabled to be detected by workbenches and furnaces?
     */
    protected void setLive(boolean lv) { live = lv; }
}
