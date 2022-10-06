package io.lumine.mythic.lib.api.crafting.recipes;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.crafting.ingredients.MythicBlueprintInventory;
import io.lumine.mythic.lib.api.crafting.ingredients.MythicRecipeInventory;
import io.lumine.mythic.lib.api.crafting.outputs.MythicRecipeOutput;
import io.lumine.mythic.lib.api.crafting.recipes.vmp.CustomInventoryCheck;
import io.lumine.mythic.lib.api.crafting.recipes.vmp.VanillaInventoryMapping;
import io.lumine.mythic.lib.api.crafting.uifilters.EnchantmentUIFilter;
import io.lumine.mythic.lib.api.crafting.uifilters.IngredientUIFilter;
import io.lumine.mythic.lib.api.crafting.uifilters.RecipeUIFilter;
import io.lumine.mythic.lib.api.crafting.uifilters.VanillaUIFilter;
import io.lumine.mythic.lib.api.util.Ref;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * The listener that orchestrates the recipes into doing what
 * they are supposed to do.
 *
 * @author Gunging
 */
public class MythicCraftingManager implements Listener {
    //region debug
    public static void log(String message) { MythicLib.plugin.getServer().getConsoleSender().sendMessage(MythicLib.plugin.parseColors(message)); }
    //endregion

    //region Initialization
    /**
     * Literally just an empty AIR ItemStack.
     */
    public static final ItemStack AIR = new ItemStack(Material.AIR);
    static boolean hasRegistered;
    public MythicCraftingManager() {

        if (!hasRegistered) {
            hasRegistered = true;

            // Register the UIFilters
            VanillaUIFilter.register();
            EnchantmentUIFilter.register();
            IngredientUIFilter.register();
            RecipeUIFilter.register();

            // Register
            VanillaInventoryMapping.registerAll();
        }
    }
    //endregion

    //region Recipes Headquarters
    /**
     * The link of name to recipe
     */
    @NotNull private static final HashMap<String, MythicRecipe> loadedRecipes = new HashMap<>();
    /**
     * Loads a recipe into the manager, allowing it to be used with the {@link RecipeUIFilter}.
     * Fails if there already is a recipe of that name.
     *
     * @param recipe This recipe you want to load.
     *
     * @return <code>true</code> If the recipe was loaded successfully
     */
    @SuppressWarnings("UnusedReturnValue")
    protected static boolean loadRecipe(@NotNull MythicRecipe recipe) {

        // Recipe already loaded?
        if (loadedRecipes.get(recipe.getName()) != null) { return false; }

        // Load it
        loadedRecipes.put(recipe.getName(), recipe);

        // Yea that's a success
        return true;
    }
    /**
     * Unlinks a recipe from the manager.
     * Does nothing if the recipe was not loaded
     *
     * @param recipe This recipe you want to unload.
     */
    protected static void unloadRecipe(@NotNull MythicRecipe recipe) {

        // Load it
        loadedRecipes.remove(recipe.getName());
    }
    /**
     * @return A Mythic Recipe of this name, if it is loaded.
     */
    @Nullable public static MythicRecipe getLoadedRecipe(@NotNull String name) { return loadedRecipes.get(name); }
    /**
     * @return The names of all loaded Mythic Recipes
     */
    @NotNull public static ArrayList<String> getLoadedRecipes() { return new ArrayList<>(loadedRecipes.keySet()); }
    //endregion

    //region Blueprints Headquarters
    public static void deployBlueprint(@NotNull MythicRecipeBlueprint blueprint, @NotNull MythicRecipeStation forStation) {

        // Get array
        deployBlueprint(blueprint, forStation, null);
    }
    public static void deployBlueprint(@NotNull MythicRecipeBlueprint blueprint, @NotNull MythicRecipeStation forStation, @Nullable String custom) {

        // Get array
        ArrayList<MythicRecipeBlueprint> found = getStationRecipes(forStation, custom);

        // Add
        found.add(blueprint);
        blueprint.registerAsDeployed(forStation);

        // Custom?
        if (forStation == MythicRecipeStation.CUSTOM && custom != null) {

            // Yes
            perCustomRecipes.put(custom, found);

        // Vanilla
        } else {

            // Put ig
            perStationRecipes.put(forStation, found);
        }

    }
    protected static void disableBlueprint(@NotNull MythicRecipeBlueprint blueprint, @NotNull MythicRecipeStation forStation) {

        // Get array
        disableBlueprint(blueprint, forStation, null);
    }
    protected static void disableBlueprint(@NotNull MythicRecipeBlueprint blueprint, @NotNull MythicRecipeStation forStation, @Nullable String custom) {

        // Get array
        ArrayList<MythicRecipeBlueprint> found = getStationRecipes(forStation, custom);

        // Add
        found.remove(blueprint);

        // Custom?
        if (forStation == MythicRecipeStation.CUSTOM && custom != null) {

            // Yes
            perCustomRecipes.put(custom, found);

        // Vanilla
        } else {

            // Put ig
            perStationRecipes.put(forStation, found);
        }
    }
    /**
     * The list of recipes checked by each station (they must be live)
     */
    @NotNull private static final HashMap<MythicRecipeStation, ArrayList<MythicRecipeBlueprint>> perStationRecipes = new HashMap<>();
    /**
     * The list of custom stations out there yeah
     */
    @NotNull private static final HashMap<String, ArrayList<MythicRecipeBlueprint>> perCustomRecipes = new HashMap<>();
    /**
     * Get the stations registered to this station.
     * @param station The station kind to check
     * @return an empty array at worst.
     */
    @NotNull public static ArrayList<MythicRecipeBlueprint> getStationRecipes(@NotNull MythicRecipeStation station, @Nullable String custom) {

        // If custom and specified
        if (station == MythicRecipeStation.CUSTOM && custom != null) {

            // Well do the normal finding
            return perCustomRecipes.computeIfAbsent(custom, k -> new ArrayList<>());

        } else {

            // Well do the normal finding
            return perStationRecipes.computeIfAbsent(station, k -> new ArrayList<>());
        }
    }
    //endregion

    //region Event Scouting
    @EventHandler(priority = EventPriority.LOWEST)
    public void onCraftingStationUse(InventoryClickEvent event) {
        //RDR//log("\u00a78RDR \u00a741\u00a77 Event Fired - Click");

        // Find the correct inventory
        Inventory inven = event.getView().getTopInventory();

        //EVENT//log("\u00a78>\u00a7a>\u00a77 Inventory Click Event");
        //EVENT//log("  \u00a78|\u00a7a|\u00a77 Event Name \u00a7f" + event.getEventName());
        //EVENT//log("  \u00a78|\u00a7a|\u00a77 View Type \u00a7f" + event.getView().getType().toString());
        //EVENT//log("  \u00a78|\u00a7a|\u00a77 Inven Type \u00a7f" + inven.getType().toString());
        //EVENT//log("  \u00a78|\u00a7a|\u00a77 Inven Size \u00a7f" + inven.getSize());
        //EVENT//log("  \u00a78|\u00a7a|\u00a77 Action \u00a7f" + event.getAction().toString());
        //EVENT//log("  \u00a78|\u00a7a|\u00a77 Current \u00a7f" + SilentNumbers.getItemName(event.getCurrentItem()));
        //EVENT//log("  \u00a78|\u00a7a|\u00a77 Cursor \u00a7f" + SilentNumbers.getItemName(event.getCursor()));
        //EVENT//log("  \u00a78|\u00a7a|\u00a77 Slot \u00a7f" + event.getSlot());
        //EVENT//log("  \u00a78>\u00a7a>\u00a77 Slot Contents: \u00a7f");
        //EVENT//for (int s = 0; s < event.getInventory().getSize(); s++) { log("     \u00a78:\u00a7a:\u00a77 #" + s + " \u00a7f" + SilentNumbers.getItemName(event.getInventory().getItem(s))); }

        // Find the mapping, and surely it must be intended for a vanilla station for this to work.
        final VanillaInventoryMapping mapping = VanillaInventoryMapping.getMappingFor(event.getView());
        if ((mapping == null) || (mapping.getIntendedStation() == null)) { return; }
        //CRAFT//log("\u00a78Click \u00a76M\u00a77 Found Mapping \u00a7f" + mapping.getClass().getSimpleName());

        //RDR//log("\u00a78RDR \u00a742\u00a77 Inventory Identified, supported - \u00a7c" + mapping.getClass().getName());
        //MPP//try { log("\u00a78Mapping Test Yes \u00a7bMain\u00a77 Is slot? \u00a7e" + mapping.getMainWidth(event.getSlot()) + ":" + (-mapping.getMainHeight(event.getSlot()))); } catch (IllegalArgumentException ignored) {}
        //MPP//try { log("\u00a78Mapping Test Yes \u00a7bResult\u00a77 Is slot? \u00a7e" + mapping.getResultWidth(event.getSlot()) + ":" + (-mapping.getResultHeight(event.getSlot()))); } catch (IllegalArgumentException ignored) {}

        // Crafting is disabled (no custom recipes), just nvm
        final ArrayList<MythicRecipeBlueprint> liveRecipes = getStationRecipes(mapping.getIntendedStation(), (mapping instanceof CustomInventoryCheck ? ((CustomInventoryCheck) mapping).getCustomStationKey() : null));
        if (liveRecipes.size() == 0) {
            //CRAFT//log("\u00a78Click \u00a76A\u00a7c No \u00a7e" + mapping.getIntendedStation().toString() + "\u00a7c Recipes Registered");
            return; }

        //RDR//log("\u00a78RDR \u00a743\u00a77 Recipes Found");

        // Compute
        computeCraftingAction(mapping, inven, liveRecipes, event.getViewers(), event);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onCraftingStationUse(InventoryDragEvent event) {

        //RDR//log("\u00a78RDR \u00a741\u00a77 Event Fired - Drag");
        //EVENT//log("\u00a78>\u00a7b>\u00a77 Inventory Drag Event");
        //EVENT//log("  \u00a78|\u00a7b|\u00a77 Event Name \u00a7f" + event.getEventName());
        //EVENT//log("  \u00a78|\u00a7b|\u00a77 View Type \u00a7f" + event.getView().getType().toString());
        //EVENT//log("  \u00a78|\u00a7b|\u00a77 Inven Type \u00a7f" + event.getInventory().getType().toString());
        //EVENT//log("  \u00a78|\u00a7b|\u00a77 Drag Type \u00a7f" + event.getType().toString());
        //EVENT//log("  \u00a78|\u00a7b|\u00a77 Cursor \u00a7f" + SilentNumbers.getItemName(event.getCursor()));
        //EVENT//log("  \u00a78|\u00a7b|\u00a77 Old Cur \u00a7f" + SilentNumbers.getItemName(event.getOldCursor()));
        //EVENT//log("  \u00a78|\u00a7b|\u00a77 Slots \u00a7e" + SilentNumbers.collapseList( SilentNumbers.transcribeList(new ArrayList<>(event.getInventorySlots()), (s) -> String.valueOf(((Integer) s))), "\u00a77, \u00a7e"));
        //EVENT//log("  \u00a78|\u00a7b|\u00a77 Raw Slots \u00a7a" + SilentNumbers.collapseList( SilentNumbers.transcribeList(new ArrayList<>(event.getRawSlots()), (s) -> String.valueOf(((Integer) s))), "\u00a77, \u00a7a"));
        //EVENT//log("  \u00a78>\u00a7a>\u00a77 Slot Contents: \u00a7f");
        //EVENT//for (int s = 0; s < event.getInventory().getSize(); s++) { log("     \u00a78:\u00a7a:\u00a77 #" + s + " \u00a7f" + SilentNumbers.getItemName(event.getInventory().getItem(s))); }

        final Inventory inven = event.getView().getTopInventory();

        // Find the mapping, and surely it must be intended for a vanilla station for this to work.
        final VanillaInventoryMapping mapping = VanillaInventoryMapping.getMappingFor(event.getView());
        if ((mapping == null) || (mapping.getIntendedStation() == null)) { return; }
        //CRAFT//log("\u00a78Drag \u00a76M\u00a77 Found Mapping \u00a7f" + mapping.getClass().getSimpleName());

        //RDR//log("\u00a78RDR \u00a742\u00a77 Inventory Identified, supported.");

        // Crafting is disabled (no custom recipes), just nvm
        final ArrayList<MythicRecipeBlueprint> liveRecipes = getStationRecipes(mapping.getIntendedStation(), (mapping instanceof CustomInventoryCheck ? ((CustomInventoryCheck) mapping).getCustomStationKey() : null));
        if (liveRecipes.size() == 0) {
            //CRAFT//log("\u00a78Drag \u00a76A\u00a7c No \u00a7e" + mapping.getIntendedStation().toString() + "\u00a7c Recipes Registered");
            return; }

        //RDR//log("\u00a78RDR \u00a743\u00a77 Recipes Found");

        // Did this action cover a result slot? Then its not supported. Cancel
        boolean clickedTop = false; int cSl = 0;
        for (Integer slot : event.getRawSlots()) {
            if (slot < inven.getSize()) {
                clickedTop = true;

                // Was any of the affected slots a result slot?
                if (mapping.isResultSlot(slot)) {

                    // Not supported
                    event.setCancelled(true);
                    return;
                } else {

                    // Yes
                    cSl = slot;
                }
            } }

        // Were there any changes in the top inventory?
        if (clickedTop) {

            //RDR//log("\u00a78RDR \u00a743.5\u00a77 Drag Click Supported");

            // Translate event
            InventoryClickEvent tEvent = new InventoryClickEvent(event.getView(), InventoryType.SlotType.CRAFTING, cSl, ClickType.LEFT, InventoryAction.PLACE_SOME);

            // Compute changes
            computeCraftingAction(mapping, inven, liveRecipes, event.getViewers(), tEvent);
        }
    }

    /**
     * @param mapping Vanilla mapping to use
     *
     * @param inven Inventory to apply to
     *
     * @param liveRecipes Recipes among which to check
     *
     * @param viewers Players who will need their inventories updated
     */
    public void computeCraftingAction(@NotNull VanillaInventoryMapping mapping, @NotNull Inventory inven, @NotNull ArrayList<MythicRecipeBlueprint> liveRecipes, @NotNull List<HumanEntity> viewers, @NotNull InventoryClickEvent event) {

        //RDR//log("\u00a78RDR \u00a744\u00a77 Computing Crafting Action...");
        //ISPM//for (int i = 0; i < inven.getSize(); i++) { MythicCraftingManager.log("\u00a78Pre Computation \u00a7f@" + i + " \u00a7f" + SilentNumbers.getItemName(inven.getItem(i))); }


        boolean cResultSlot = mapping.isResultSlot(event.getSlot());
        /*
         * Now that we know there is a reason for MythicLib's crafting system
         * to kick in (there are live recipes for this station), we must see
         * what the player is trying to do:
         *
         * 1 Are they providing the ingredients? (Clicks in the crafting area)
         *
         * 2 Are they pickup up the result? (Clicks in the result area).
         *
         * Some stations (furnace, brewing stand) trigger the recipe to consume
         * fuel before the player clicks the result button. We'll get to those
         * eventually.
         */
        if (cResultSlot) {

            /*
             * All right, since the result slot was clicked, there is no need to check
             * if the ingredients match the recipe (they still should, as they are not
             * being modified).
             *
             * So that the Cache recipe reader kicks in.
             *
             * Of course, if the player ran a 'craft all,' we'll have to run this process
             * up to completion, which will be handled by the Result.
             */
            //RDR//log("\u00a78RDR \u00a745\u00a77 Crafting Action: RESULT");

            //CRAFT//log("\u00a78Craft \u00a76C\u00a77 Obtaining result items to check for cache tag");
            // Read the result item actually
            MythicRecipeInventory resultInventory = mapping.getResultMythicInventory(inven);

            //CRAFT//log("\u00a78Craft \u00a76C\u00a77 Checking the first item of the result..");
            // Find one item bro
            MythicCachedResult cache = null;
            for (int h = 0; h < resultInventory.getHeight(); h++) {
                for (int w = 0; w < resultInventory.getWidth(); w++) {

                    // Find cache :flushed:
                    MythicCachedResult mcr = MythicCachedResult.get(resultInventory.getFirst());
                    if (mcr != null) { cache = mcr; }
                } }

            // Not found? Not our business
            if (cache == null) {
                //CRAFT//log("\u00a78Craft \u00a7cC\u00a77 Cache not found, \u00a74Ignoring.");

                return; }
            //CRAFT//log("\u00a78Craft \u00a76C\u00a77 Cache Found");

            // Identify the values
            MythicRecipeOutput output = cache.getOperation().getResult();

            //RDR//log("\u00a78RDR \u00a745.5\u00a77 Identifying Crafting times");
            int times = cache.getTimes();
            //CRAFT//log("\u00a78Craft \u00a76T\u00a77 Maximum Times to Completion: \u00a7e" + times);
            if (!isCraftToCompletion(event)) { times = 1; }
            //CRAFT//log("\u00a78Craft \u00a76T\u00a77 Player will Execute: \u00a7e" + times);

            // Apply the damn result
            output.applyResult(resultInventory, mapping.extractFrom(inven), cache, event, mapping, times);

        // The player is clicking the ingredients area of the station.
        }

        //RDR//log("\u00a78RDR \u00a745\u00a77 Crafting Action: RECIPE CHECK");

        /*
         * The ingredient quantities and positions have been moved as a result of
         * this click, we must check if the recipe is still matched.
         *
         * We do this of course, by making the mapper read this inventory, and
         * build a MythicRecipeInventory with this inventory translated into
         * the MythicCrafting format.
         *
         * However, we must wait 1 tick to correctly perform these operations,
         * since the current items are not the correct.
         *
         * As this operation is intended to read changes made by the player,
         * it is cancelled early if the action is doing nothing, which triggers
         * when the player clicks air, and has air in their cursor, effectively
         * leaving the ingredient layout unchanged.
         */
        if (event.getAction() == InventoryAction.NOTHING && !cResultSlot) { return; }

        /*
         * If custom inventory, might want to clear the result first rq
         */
        if (mapping.getIntendedInventory() == InventoryType.CHEST) {

            // Yeah, otherwise it may not be removed and the item may be DUPED
            for (int s = mapping.getResultInventoryStart(); s < (mapping.getResultInventorySize() + mapping.getResultInventoryStart()); s++) {

                // Introduce clear
                VanillaInventoryMapping.setInventoryItem(inven, s, AIR, false);
            }
        }

        (new BukkitRunnable() {
            public void run() {
                //RDR//log("\u00a78RDR \u00a746\u00a77 Tick waited, checking...");
                //ISPM//for (int i = 0; i < inven.getSize(); i++) { MythicCraftingManager.log("\u00a78After Tick \u00a7d@" + i + " \u00a7f" + SilentNumbers.getItemName(inven.getItem(i))); }

                ArrayList<Player> p = new ArrayList<>(); for (HumanEntity e : viewers) {if (e instanceof Player) { p.add((Player) e);}}
                boolean res = displayResult(mapping, inven, liveRecipes, p, event.getWhoClicked().getUniqueId(), event);

                //RDR//log("\u00a78RDR \u00a746.5\u00a77 Tick finished, result:\u00a7c " + res);

                //ISPM//for (int i = 0; i < inven.getSize(); i++) { MythicCraftingManager.log("\u00a78Post Display \u00a7c@" + i + " \u00a7f" + SilentNumbers.getItemName(inven.getItem(i))); }
            }
        }).runTaskLater(MythicLib.plugin, 1L);


        //ISPM//for (int i = 0; i < inven.getSize(); i++) { MythicCraftingManager.log("\u00a78Pre Tick \u00a73@" + i + " \u00a7f" + SilentNumbers.getItemName(inven.getItem(i))); }
    }

    /**
     * @param event The event you are testing
     *
     * @return If the player means, through this event, to craft all
     *         of the items they can craft until they either run out
     *         of inventory space or of ingredients.
     *         <p></p>
     *         Its about the key combinations they use, Shift+Click
     *         for crafting tables etc...
     */
    public static boolean isCraftToCompletion(@NotNull InventoryClickEvent event) {

        /*
         * Shift + click yes.
         *
         * There
         */
        return event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || (event.getAction() == InventoryAction.NOTHING && event.isShiftClick());
    }

    /**
     * @param mapping Vanilla mapping to use
     *
     * @param inven Inventory to apply to
     *
     * @param liveRecipes Recipes among which to check
     *
     * @param viewers Players who will need their inventories updated
     *
     * @return If any recipe was matched
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean displayResult(@NotNull VanillaInventoryMapping mapping, @NotNull Inventory inven, @NotNull ArrayList<MythicRecipeBlueprint> liveRecipes, @NotNull ArrayList<Player> viewers, @NotNull UUID uuid, @NotNull InventoryClickEvent eventTrigger) {

        /*
         * Read the contents, create the blueprint. Important that this happens
         * a tick afterwards to read with whatever changes the player did.
         */
        MythicBlueprintInventory inventory = mapping.extractFrom(inven);

        // All right we're good to go, lets see every recipe
        for (MythicRecipeBlueprint blueprint : liveRecipes) {
            //CRAFT//log("\u00a78Display \u00a76B\u00a77 Looking at Recipe \u00a7e" + ((io.lumine.mythic.lib.api.crafting.outputs.MRORecipe) blueprint.getResult()).getOutput().getName());

            /*
             * todo respect doLimitedCrafting Game Rule
            // The players has it unlocked right
            boolean limitedCrafted = false;
            for (Player viewer : viewers) { if (viewer != null) {

                World world = viewer.getLocation().getWorld();
                if (world == null) { continue; }

                // Prevent
                if (world.isGameRule("doLimitedCrafting")) {

                    // Counter limit
                    Boolean limit = world.getGameRuleValue(GameRule.DO_LIMITED_CRAFTING);

                    // No limit no service
                    if (limit == null || !limit) { continue; }

                    // Ok its limited, do the player have the recipe unlocked
                    boolean disc = viewer.undiscoverRecipe(blueprint.getNk());

                    // Newly discovered?
                    if (disc) {

                        // Cancel lmao
                        viewer.undiscoverRecipe(blueprint.getNk());

                        // Skip lma0
                        limitedCrafted = true;
                        break;
                    }
                }
            } }
            if (limitedCrafted) { continue; }
            */

            // Well, whats the modified version?
            Ref<Integer> timesCrafted = new Ref<>();
            MythicBlueprintInventory finalBlueprint = blueprint.matches(inventory, timesCrafted);

            // Success?
            if (finalBlueprint != null) {
                //CRAFT//log("\u00a78Display \u00a7aM!!\u00a77 Matched");

                // Get result
                MythicRecipeOutput output = blueprint.getResult();

                // Obtain what must be displayed (the hint of what you are crafting)
                MythicRecipeInventory finalResult = output.applyDisplay(finalBlueprint, eventTrigger, mapping);

                // Cache the result (To further evaluate when actually crafting)
                //CRAFT//log("\u00a78Display \u00a76C\u00a77 Creating cache");
                int craftTimes = timesCrafted.getValue(1);
                new MythicCachedResult(uuid, finalBlueprint, blueprint, finalResult, craftTimes);

                /*
                 * Don't want to overwrite...
                 *
                 * todo What if the main is the result?
                 *      If the cache is not saved like this, it wont be found
                 *      when the recipe is crafted.
                 *      -
                 *      Brewing stand and Enchantment table suffer from this.
                 */
                if (!mapping.mainIsResult()) {

                    // Actually show the display item to the player.
                    mapping.applyToResultInventory(inven, finalResult, false);
                }

                //ISPM//for (int i = 0; i < inven.getSize(); i++) { MythicCraftingManager.log("\u00a78Post Display \u00a76@" + i + " \u00a7f" + SilentNumbers.getItemName(inven.getItem(i))); }

                // Update inventory yay
                for (Player viewer : viewers) { if (viewer != null) {
                    //noinspection deprecation
                    viewer.updateInventory(); } }

                // A recipe matched!
                return true;
            }
        }

        // No recipe matched
        return false;
    }
    //endregion
}
