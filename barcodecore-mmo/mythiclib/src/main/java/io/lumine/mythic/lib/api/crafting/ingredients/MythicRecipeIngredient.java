package io.lumine.mythic.lib.api.crafting.ingredients;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.crafting.uifilters.IngredientUIFilter;
import io.lumine.mythic.lib.api.crafting.uifilters.UIFilter;
import io.lumine.mythic.lib.api.crafting.uimanager.ProvidedUIFilter;
import io.lumine.mythic.lib.api.util.ui.FFPMythicLib;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackCategory;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackProvider;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Different kinds of recipes require different specifications in their ingredients,
 * though they all have a few methods in common (I guess).
 *
 * @author Gunging
 */
public class MythicRecipeIngredient {

    /**
     * Actual ingredient information
     */
    @NotNull final MythicIngredient ingredient;

    /**
     * Actual ingredient information
     */
    @NotNull public MythicIngredient getIngredient() { return ingredient; }

    public MythicRecipeIngredient(@NotNull MythicIngredient ingredient) {
        this.ingredient = ingredient;
    }

    /**
     *
     * Sometimes (most of the time, actually) one has a list of ProvidedUIFilters,
     * literally the things the user specified: <br>
     *     <p><code>- m CONSUMABLE MANGO 3</code>
     *     </p><code>- v SPRUCE_PLANKS 0 2</code> <br>
     *
     * Well, just pass one of these here and we'll take care of the rest
     *
     * @throws IllegalArgumentException If {@link ProvidedUIFilter#getParent()}'s {@link UIFilter#useInventoryMatch()} is <code>true</code>,
     *                                  or, in the case that this is a {@link IngredientUIFilter}, if the ingredient is not loaded.
     *
     * @param ingredient A single Provided UIFilter to encapsulate into a Mythic Ingredient
     */
    public MythicRecipeIngredient(@NotNull ProvidedUIFilter ingredient) throws IllegalArgumentException {

        // Is it the Mythic Ingredient itself?
        if (ingredient.getParent() instanceof IngredientUIFilter) {

            MythicIngredient mythicIngredient = MythicIngredient.get(ingredient.getArgument());
            if (mythicIngredient == null) { throw new IllegalArgumentException("$bIngredient is not loaded ($u" + ingredient.toString() + "$b)."); }

            // Attempt to get I guess
            this.ingredient = mythicIngredient;

        } else {

            // Nope
            if (ingredient.getParent().useInventoryMatch()) { throw new IllegalArgumentException("$bMythic Ingredients don't support filters with inventory match ($u" + ingredient.toString() + "$b)."); }

            // Create with only this filter
            this.ingredient = new MythicIngredient(ingredient.toString(), ingredient);
        }
    }

    /**
     * @return Transforms this ingredient into a bukkit ingredient to display in the book.
     */
    @NotNull public RecipeChoice toBukkit() {

        // Alr include
        ArrayList<ItemStack> choices = new ArrayList<>();
        for (ProvidedUIFilter substitute : getIngredient().getSubstitutes()) {

            //FriendlyFeedbackProvider ffp = null;
            FriendlyFeedbackProvider ffp = new FriendlyFeedbackProvider(FFPMythicLib.get());
            ffp.activatePrefix(true, "Ingredient \u00a7b" + getIngredient().getName());

            // Add display ItemStack
            choices.add(substitute.getDisplayStack(ffp));

            // Send errors to the console yea
            ffp.sendTo(FriendlyFeedbackCategory.ERROR, MythicLib.plugin.getServer().getConsoleSender());
            ffp.sendTo(FriendlyFeedbackCategory.FAILURE, MythicLib.plugin.getServer().getConsoleSender());
        }

        // Create recipe choice
        return new RecipeChoice.ExactChoice(choices);
    }
}
