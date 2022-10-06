package net.Indyuce.mmoitems.gui.edition.recipe;

import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import io.lumine.mythic.utils.items.ItemFactory;
import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate;
import net.Indyuce.mmoitems.api.util.message.FFPMMOItems;
import net.Indyuce.mmoitems.gui.edition.EditionInventory;
import net.Indyuce.mmoitems.gui.edition.recipe.registry.*;
import net.Indyuce.mmoitems.gui.edition.recipe.registry.burninglegacy.RMGRR_LBBlast;
import net.Indyuce.mmoitems.gui.edition.recipe.registry.burninglegacy.RMGRR_LBCampfire;
import net.Indyuce.mmoitems.gui.edition.recipe.registry.burninglegacy.RMGRR_LBFurnace;
import net.Indyuce.mmoitems.gui.edition.recipe.registry.burninglegacy.RMGRR_LBSmoker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Set;

/**
 * The first menu when a player clicks the 'Crafting' stat is the
 * Recipe Browser GUI. It shows all the loaded types of recipes.
 *      <br> <br>
 * From there, the user will choose a type of recipe, and see the
 * list of recipes of that type through a {@link RecipeListGUI}.
 *      <br> <br>
 * Finally, the user will choose one of the recipes in the list to
 * edit or delete or whatever.
 *
 * @author Gunging
 */
public class RecipeBrowserGUI extends EditionInventory {

    /*
     * Item Stacks used in this inven
     */
    @NotNull final ItemStack nextPage = ItemFactory.of(Material.ARROW).name(FFPMMOItems.get().getExampleFormat() + "Next Page").build();
    @NotNull final ItemStack prevPage = ItemFactory.of(Material.ARROW).name(FFPMMOItems.get().getExampleFormat() + "Previous Page").build();
    @NotNull final ItemStack noRecipe = ItemFactory.of(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name("").build();

    int currentPage;

    /**
     * Not gonna lie, I think this class doesnt support concurrent edition of
     * the same template. Lets just hope no two users decide to do crafting
     * stuff for the same item at the same time. Its untested.
     *
     * @param player Player that is editing recipes
     * @param template Template being edited
     */
    public RecipeBrowserGUI(@NotNull Player player, @NotNull MMOItemTemplate template) {
        super(player, template);

        // Start with defaults
        currentPage = 0;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        // Create and prepare
        Inventory inv = Bukkit.createInventory(this, 54, "Choose Recipe Type");

        // Put buttons
        addEditionInventoryItems(inv, true);

        // Arrange yes
        arrangeInventory(inv);

        // That's it lets GOOOO
        return inv;
    }

    /**
     * A map containing a link between Recipe Type and Absolute Inventory Slot
     * (to know that, when a player clicks the inventory thing, which recipe
     * they are meaning)
     */
    @NotNull final HashMap<Integer, RecipeRegistry> recipeTypeMap = new HashMap<>();

    /**
     * Updates the inventory, refreshes the page number whatever.
     *
     * @param inv Inventory object to edit
     */
    void arrangeInventory(@NotNull Inventory inv) {

        // Start fresh
        recipeTypeMap.clear();

        // Include page buttons
        if (currentPage > 0) { inv.setItem(27, prevPage); }
        if (registeredRecipes.size() >= ((currentPage + 1) * 21)) { inv.setItem(36, nextPage); }

        // Well order them I guess
        HashMap<Integer, RecipeRegistry> reg = new HashMap<>(); int op = 0;
        for (RecipeRegistry r : registeredRecipes.values()) { reg.put(op, r); op++; }

        // Fill the space I guess
        for (int p = 21 * currentPage; p < (21 * (currentPage + 1)); p++) {

            //CNT//MMOItems.log("\u00a77Running \u00a73" + p);

            /*
             * The job of this is to identify which slots of this
             * inventory will trigger which action.
             *
             * If the slot has a recipe to edit, a connection will
             * be made between clicking this and which recipe to
             * edit via the HashMap 'recipeMap'
             *
             * But for that we must calculate which absolute slot
             * of this inventory are we talking about...
             */
            int absolute = page(p);

            /*
             * Going through the whole page, first thing
             * to check is that there is a recipe here.
             *
             * Note that clicking the very next glass pane
             * creates a new recipe.
             */
            if (p >= registeredRecipes.size()) {

                // Just snooze
                inv.setItem(absolute, noRecipe);

                // There exists a recipe for this slot
            } else {

                // Which will it be?
                RecipeRegistry rr = reg.get(p);

                // Display name
                inv.setItem(absolute, rr.getDisplayListItem());

                // Store
                recipeTypeMap.put(absolute, rr);
            }
        }
    }
    public static int page(int p) {

        // Remove multiples of 21
        int red = SilentNumbers.floor(p / 21.00D);
        p -= red * 21;

        //CNT//MMOItems.log("\u00a73+\u00a77 Reduced to \u00a79" + p);

        /*
         * A page is the third, fourth, and fifth rows, excluding the first and last column.
         *
         * #1 Obtain the relative column, and relative row
         *
         * #2 Convert to absolute inventory positions
         */
        int relRow = SilentNumbers.floor(p / 7.00D);
        int relCol = p - (7 * relRow);

        //CNT//MMOItems.log("\u00a73+\u00a77 Row \u00a79" + relRow + "\u00a77, Col\u00a79 " + relCol);

        // Starting at the third row, each row adds 9 slots.
        int rowAdditive = 18 + (relRow * 9);
        int columnAdditive = relCol + 1;

        // Sum to obtain final

        //CNT//MMOItems.log("\u00a7a+\u00a77 Out \u00a7b" + (rowAdditive + columnAdditive));
        return rowAdditive + columnAdditive;
    }

    @Override
    public void whenClicked(InventoryClickEvent event) {

        // Clicked inventory was not the observed inventory? Not our business
        if ((event.getView().getTopInventory() != event.getClickedInventory())) { return; }

        // Disallow any clicking.
        event.setCancelled(true);

        // Selecting a recipe type to browse
        if (event.getAction() == InventoryAction.PICKUP_ALL) {

            // Previous page
            if (event.getSlot() == 27) {

                // Retreat page
                currentPage--;
                arrangeInventory(event.getView().getTopInventory());

                // Next Page
            } else if (event.getSlot() == 36) {

                // Advance page
                currentPage++;
                arrangeInventory(event.getView().getTopInventory());

                // Create a new recipe
            } else if (event.getSlot() > 18) {

                // A recipe exists of this name?
                RecipeRegistry recipeType = recipeTypeMap.get(event.getSlot());

                // Well, found anything?
                if (recipeType != null) {

                    // Open that menu for the player
                    new RecipeListGUI(player, template, recipeType).open(getPreviousPage());
                }
            }
        }
    }

    //region As Recipe Type Manager
    /**
     * This is called when MMOItems loads and registers the recipes that
     * come with the plugin.
     *
     * There is no reason to call it again thereafter so I wont even attempt
     * to prevent duplicate registrations of these.
     */
    public static void registerNativeRecipes() {

        registerRecipe(new RMGRR_Smithing());
        registerRecipe(new RMGRR_Shapeless());
        registerRecipe(new RMGRR_Shaped());
        registerRecipe(new RMGRR_SuperShaped());
        registerRecipe(new RMGRR_MegaShaped());

        /*
         * These don't go through mythiclib, I, gunging, merely
         * transcribed them to fit in the new crafting recipe
         * code but don't really know how they work.
         *
         * After the RMGRR_LegacyBurning.sendToMythicLib method (that
         * sends the recipes back to RecipeManager.registerBurningRecipe)
         * I have no clue what happens; barely even read that far.
         */
        registerRecipe(new RMGRR_LBFurnace());
        registerRecipe(new RMGRR_LBBlast());
        registerRecipe(new RMGRR_LBSmoker());
        registerRecipe(new RMGRR_LBCampfire());
    }

    @NotNull
    static final HashMap<String, RecipeRegistry> registeredRecipes = new HashMap<>();
    public static void registerRecipe(@NotNull RecipeRegistry recipe) {

        // Register that yes
        registeredRecipes.put(recipe.getRecipeConfigPath(), recipe);
    }

    @NotNull public static Set<String> getRegisteredRecipes() { return registeredRecipes.keySet(); }

    /**
     * @param name This is only guaranteed to be NotNull if you got it from {@link #getRegisteredRecipes()}.
     *
     * @return The loaded recipe attached to this path. Must be loaded.
     */
    @NotNull public static RecipeRegistry getRegisteredRecipe(@NotNull String name) { return registeredRecipes.get(name); }
    //endregion
}
