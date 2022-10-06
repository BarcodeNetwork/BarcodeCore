package net.Indyuce.mmoitems.gui.edition.recipe.recipes;

import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate;
import net.Indyuce.mmoitems.gui.edition.recipe.interpreters.RMGRI_BurningLegacy;
import net.Indyuce.mmoitems.gui.edition.recipe.interpreters.RMG_RecipeInterpreter;
import net.Indyuce.mmoitems.gui.edition.recipe.rba.RBA_CookingTime;
import net.Indyuce.mmoitems.gui.edition.recipe.rba.RBA_Experience;
import net.Indyuce.mmoitems.gui.edition.recipe.rba.RBA_HideFromBook;
import net.Indyuce.mmoitems.gui.edition.recipe.registry.RecipeRegistry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;


/**
 * The legacy recipes that are not supported by MythicLib that all happen to have to do
 * with burning stuff - furnaces, campfires, the other furnaces...
 *
 * @author Gunging
 */
public class RMG_BurningLegacy extends RecipeMakerGUI {

    @NotNull
    HashMap<Integer, Integer> inputLinks = new HashMap<>();

    /**
     * An editor for a Shaped Recipe. Because the recipe is loaded from the YML when this is created,
     * concurrent modifications of the same recipe are unsupported.
     *
     * @param player Player editing the recipe ig
     * @param template Template of which a recipe is being edited
     * @param recipeName Name of this recipe
     */
    public RMG_BurningLegacy(@NotNull Player player, @NotNull MMOItemTemplate template, @NotNull String recipeName, @NotNull RecipeRegistry recipeRegistry) {
        super(player, template, recipeName, recipeRegistry);
        addButton(new RBA_HideFromBook(this));
        addButton(new RBA_Experience(this));
        addButton(new RBA_CookingTime(this));

        // NO OUTPUT
        if (!isShowingInput()) { switchInput(); }

        // Get section and build interpreter
        interpreter = new RMGRI_BurningLegacy(getNameSection());

        // Bind inputs - Furnace only has which item to smelt
        inputLinks.put(40, 0);
    }

    @Override public int getButtonsRow() { return 2; }

    @Override
    public void putRecipe(@NotNull Inventory target) {

        // Fill inputs
        for (Integer s : inputLinks.keySet()) { target.setItem(s, getDisplay(isShowingInput(), inputLinks.get(s))); }
    }

    @Override
    int getInputSlot(int absolute) {

        // Not an input? Not our business
        @Nullable Integer found = inputLinks.get(absolute);

        // Found or negative
        return found != null ? found : -1;
    }

    @NotNull final RMGRI_BurningLegacy interpreter;
    @NotNull @Override public RMG_RecipeInterpreter getInterpreter() { return interpreter; }
}
