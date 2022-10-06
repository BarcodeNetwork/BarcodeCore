package io.lumine.mythic.lib.api.crafting.recipes;

import io.lumine.mythic.lib.api.crafting.outputs.MythicRecipeOutput;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

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
public interface VanillaBookableOutput {

    /**
     * @return The item to display as the bukkit recipe result.
     *
     * @throws IllegalArgumentException If anything goes wrong.
     */
    @NotNull ItemStack getBukkitRecipeResult() throws IllegalArgumentException;
}