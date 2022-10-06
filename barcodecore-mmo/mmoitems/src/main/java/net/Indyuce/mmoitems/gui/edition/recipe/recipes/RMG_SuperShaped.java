package net.Indyuce.mmoitems.gui.edition.recipe.recipes;

import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate;
import net.Indyuce.mmoitems.gui.edition.recipe.interpreters.RMGRI_SuperShaped;
import net.Indyuce.mmoitems.gui.edition.recipe.interpreters.RMG_RecipeInterpreter;
import net.Indyuce.mmoitems.gui.edition.recipe.rba.RBA_InputOutput;
import net.Indyuce.mmoitems.gui.edition.recipe.registry.RecipeRegistry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * Edits super shaped recipes, very nice.
 *
 * @author Gunging
 */
public class RMG_SuperShaped extends RecipeMakerGUI {

    @NotNull HashMap<Integer, Integer> inputLinks = new HashMap<>();

    /**
     * An editor for a Super Shaped Recipe. Because the recipe is loaded from the YML when this is created,
     * concurrent modifications of the same recipe are unsupported.
     *
     * @param player Player editing the recipe ig
     * @param template Template of which a recipe is being edited
     * @param recipeName Name of this recipe
     */
    public RMG_SuperShaped(@NotNull Player player, @NotNull MMOItemTemplate template, @NotNull String recipeName, @NotNull RecipeRegistry recipeRegistry) {
        super(player, template, recipeName, recipeRegistry);
        addButton(new RBA_InputOutput(this));

        // Get section and build interpreter
        interpreter = new RMGRI_SuperShaped(getNameSection());

        // Bind inputs
        inputLinks.put(11, 0);
        inputLinks.put(12, 1);
        inputLinks.put(13, 2);
        inputLinks.put(14, 3);
        inputLinks.put(15, 4);

        inputLinks.put(20, 5);
        inputLinks.put(21, 6);
        inputLinks.put(22, 7);
        inputLinks.put(23, 8);
        inputLinks.put(24, 9);

        inputLinks.put(29, 10);
        inputLinks.put(30, 11);
        inputLinks.put(31, 12);
        inputLinks.put(32, 13);
        inputLinks.put(33, 14);

        inputLinks.put(38, 15);
        inputLinks.put(39, 16);
        inputLinks.put(40, 17);
        inputLinks.put(41, 18);
        inputLinks.put(42, 19);

        inputLinks.put(47, 20);
        inputLinks.put(48, 21);
        inputLinks.put(49, 22);
        inputLinks.put(50, 23);
        inputLinks.put(51, 24);
    }

    @Override public int getButtonsRow() { return 0; }

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

    @NotNull final RMGRI_SuperShaped interpreter;
    @NotNull
    @Override
    public RMG_RecipeInterpreter getInterpreter() { return interpreter; }
}
