package io.lumine.mythic.lib.api.crafting.recipes;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;
import io.lumine.mythic.lib.api.crafting.outputs.MythicRecipeOutput;

/**
 * When a recipe is compatible with the vanilla book, it may
 * implement this class and you will provide the recipe to
 * display into the book, yeah.
 * <p></p>
 * Implement onto {@link MythicRecipe} to build the Bukkit one
 * on base of a result provided by a {@link MythicRecipeOutput}
 * implementing this.
 *
 * @author Gunging
 */
public interface VanillaBookableRecipe {

    /**
     * @return All you need is to provide this bukkit version of the recipe,
     *         and bukkit (and MythicLib) will take care of the displaying.
     *
     * @throws IllegalArgumentException if there is any problems generating the recipe.
     */
    @NotNull Recipe getBukkitRecipe(@NotNull NamespacedKey recipeName, @NotNull ItemStack recipeResult) throws IllegalArgumentException;
}
