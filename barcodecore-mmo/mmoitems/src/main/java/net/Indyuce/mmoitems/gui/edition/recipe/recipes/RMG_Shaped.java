package net.Indyuce.mmoitems.gui.edition.recipe.recipes;

import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate;
import net.Indyuce.mmoitems.gui.edition.recipe.interpreters.RMGRI_Shaped;
import net.Indyuce.mmoitems.gui.edition.recipe.interpreters.RMG_RecipeInterpreter;
import net.Indyuce.mmoitems.gui.edition.recipe.rba.RBA_HideFromBook;
import net.Indyuce.mmoitems.gui.edition.recipe.rba.RBA_InputOutput;
import net.Indyuce.mmoitems.gui.edition.recipe.registry.RecipeRegistry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;

/**
 * Edits shaped recipes, very nice.
 *
 * @author Gunging
 */
public class RMG_Shaped extends RecipeMakerGUI {

    @NotNull HashMap<Integer, Integer> inputLinks = new HashMap<>();

    /**
     * An editor for a Shaped Recipe. Because the recipe is loaded from the YML when this is created,
     * concurrent modifications of the same recipe are unsupported.
     *
     * @param player Player editing the recipe ig
     * @param template Template of which a recipe is being edited
     * @param recipeName Name of this recipe
     */
    public RMG_Shaped(@NotNull Player player, @NotNull MMOItemTemplate template, @NotNull String recipeName, @NotNull RecipeRegistry recipeRegistry) {
        super(player, template, recipeName, recipeRegistry);
        addButton(new RBA_InputOutput(this));
        addButton(new RBA_HideFromBook(this));

        // Get section and build interpreter
        interpreter = new RMGRI_Shaped(getNameSection());

        // Bind inputs
        inputLinks.put(30, 0);
        inputLinks.put(31, 1);
        inputLinks.put(32, 2);

        inputLinks.put(39, 3);
        inputLinks.put(40, 4);
        inputLinks.put(41, 5);

        inputLinks.put(48, 6);
        inputLinks.put(49, 7);
        inputLinks.put(50, 8);
    }

    @Override public int getButtonsRow() { return 1; }

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

    @NotNull final RMGRI_Shaped interpreter;
    @NotNull
    @Override
    public RMG_RecipeInterpreter getInterpreter() { return interpreter; }
}
