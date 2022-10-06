package io.lumine.mythic.lib.api.crafting.recipes;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.crafting.ingredients.*;
import io.lumine.mythic.lib.api.crafting.outputs.MythicRecipeOutput;
import io.lumine.mythic.lib.api.util.Ref;
import io.lumine.mythic.lib.api.util.ui.FFPMythicLib;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackProvider;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * In the most general sense of crafting, what makes a recipe a recipe?
 * <p></p>
 * When a player puts ingredients in specific slots, requiring order sometimes,
 * the result is given as output 'ingredients' in specific slots.
 * <p></p>
 * In the most intuitive senses, the output and input 'ingredients' (where, clearly,
 * ingredients mean any item at all) have different spaces dedicated:
 * <p>In furnaces, workbench, anvil, and so on, the result slot is separate.
 * </p>However, this is not true for the enchantment table and brewing stand, where
 *     the result slot is in the same place as the input.
 *
 * <p></p>
 * In other intuitive senses, input is not all the same. Notice how basically
 * every crafting station has a secondary input space:
 * <p>Furnace has the fuel slot,
 * </p>Smithing Table has the netherite ingot slot thing,
 * <p>Enchantment table has the lapis slot,
 * </p>and so on.
 *
 * Thus, apart from the main input area, there may be side-input stuff that
 * affects the output somehow.
 *
 * <p></p>
 * Sure, we are used to one result slot, one fuel slot, and such, but why not go ahead
 * and treat each of these as 'inventories' of one slot, so that then we can allow
 * for fuel inventories and multiple results... now deconstructing into 3x3 components
 * is possible.
 *
 * <p></p>
 * In such a way, the Mythic Blueprint is the ACTUAL recipe, a recipe of recipes, with
 * information on what to detect as input and what to spit out as output.
 *
 * @author Gunging
 */
@SuppressWarnings("unused")
public class MythicRecipeBlueprint {

    /**
     * A Blueprint has input recipes that must be matched to produce the output.
     *
     * The output may be produced for two different scenarios:
     * <p>As a preview
     * </p>As an actual result
     *
     * @param mainCheck The main recipe to check, required.
     * @param result The result that will happen if the player accepts the recipe,
     *               which may also have information on how to display it for preview.
     */
    public MythicRecipeBlueprint(@NotNull MythicRecipe mainCheck, @NotNull MythicRecipeOutput result) {
        this.mainCheck = mainCheck;
        this.result = result;
        nk = new NamespacedKey(MythicLib.plugin, getMainCheck().getName());
        canDisplayInRecipeBook = getMainCheck() instanceof VanillaBookableRecipe && getResult() instanceof VanillaBookableOutput;
    }

    /**
     * A Blueprint has input recipes that must be matched to produce the output.
     *
     * The output may be produced for two different scenarios:
     * <p>As a preview
     * </p>As an actual result
     *
     * @param mainCheck The main recipe to check, required.
     * @param result The result that will happen if the player accepts the recipe,
     *               which may also have information on how to display it for preview.
     */
    public MythicRecipeBlueprint(@NotNull MythicRecipe mainCheck, @NotNull MythicRecipeOutput result, @NotNull NamespacedKey key) {
        this.mainCheck = mainCheck;
        this.result = result;
        nk = key;
        canDisplayInRecipeBook = getMainCheck() instanceof VanillaBookableRecipe && getResult() instanceof VanillaBookableOutput;
    }

    /**
     * Namespace under which this recipe may register onto the Recipe Book
     */
    @NotNull final NamespacedKey nk;

    /**
     * Namespace under which this recipe may register onto the Recipe Book
     */
    @NotNull public NamespacedKey getNk() {
        return nk;
    }

    /**
     * Can this be displayed to players in the recipe book?
     */
    public boolean canDisplayInRecipeBook() {
        return canDisplayInRecipeBook;
    }
    /**
     * Can this be displayed to players in the recipe book?
     */
    final boolean canDisplayInRecipeBook;

    /**
     * What will happen if all the checks return <code>true</code>?
     * <p></p>
     * There are two scenarios for this though, one is when they
     * return <code>true</code> during the preparation, another
     * is when they match upon the player actually 'crafting;' upon
     * completing the operation.
     */
    @NotNull final MythicRecipeOutput result;
    /**
     * What will happen if all the checks return <code>true</code>?
     * <p></p>
     * There are two scenarios for this though, one is when they
     * return <code>true</code> during the preparation, another
     * is when they match upon the player actually 'crafting;' upon
     * completing the operation.
     */
    @NotNull
    public MythicRecipeOutput getResult() { return result; }

    /**
     * For ease of implementation, It is required that there be at last one Non-null
     * main recipe to check an inventory. This is that one.
     */
    @NotNull final MythicRecipe mainCheck;
    /**
     * For ease of implementation, It is required that there be at last one Non-null
     * main recipe to check an inventory. This is that one.
     */
    @NotNull public MythicRecipe getMainCheck() { return mainCheck; }

    /**
     * Optional, any mount of 'fuel' recipes. Each recipe will contain
     * information on it being required or whatever.
     */
    @NotNull final HashMap<String, MythicRecipe> sideChecks = new HashMap<>();
    /**
     * What are the expected side inventory names?
     *
     * @return A new list, with a copy of every name of the side check inventories.
     */
    @NotNull public ArrayList<String> getSideInventoryNames() { return new ArrayList<>(sideChecks.keySet()); }
    /**
     * <b>It is imperative that you know this name is indeed that of a contained side
     * check, use {@link #hasSideInventory(String)} to corroborate before calling this.</b>
     *
     * @return The side check associated to this string.
     */
    @NotNull public MythicRecipe getSideCheck(@NotNull String ofName) {

        // Bruh
        Validate.isTrue(hasSideInventory(ofName), "You may not query for a side recipe that does not exist.");

        // Well was it?
        return sideChecks.get(ofName);
    }
    /**
     * Is there any side recipe associated to this inventory name?
     * @param ofName What name
     * @return <code>true</code> if there is a side inventory expected of this name.
     */
    public boolean hasSideInventory(@NotNull String ofName) { return sideChecks.containsKey(ofName); }
    /**
     * Registers a check that must be fulfilled
     *
     * @param ofName The name of the side inventory, 'fuel' for furnace for example
     * @param recipe The recipe that will check this side inventory
     */
    public void addSideCheck(@NotNull String ofName, @NotNull MythicRecipe recipe) {

        // Put
        sideChecks.put(ofName, recipe);
    }

    /**
     * Do all the checks return true upon inspecting this collection of inventories?
     *
     * @return Will return <code>null</code> if the recipe does not match, and
     *         a new MythicBlueprintInventory with the recipe executed for you
     *         to put the items where they are supposed to go.
     *
     * @param inventories The inventory layout you are testing with this blueprint
     * @param maxTimes To know how many times this recipe can be carried out before
     *                 the ingredients run out.
     */
    @Nullable public MythicBlueprintInventory matches(@NotNull MythicBlueprintInventory inventories, @Nullable Ref<Integer> maxTimes) {

        // Does the blueprint have information to check with the side checks. That's an automatic no
        if (!SilentNumbers.hasAll(inventories.getSideInventoryNames(), getSideInventoryNames())) {
            //MCH//MythicCraftingManager.log("\u00a78Matching \u00a7cS\u00a77 Incorrect side inventory amount. \u00a74No match.");
            return null;
        }

        // Get main inventory result ig
        Ref<Integer> mainTimes = new Ref<>();
        MythicRecipeInventory mainResult = getMainCheck().matches(inventories.getMainInventory(), mainTimes);

        // Does the main recipe accept the main inventory?
        if (mainResult == null) {
            //MCH// MythicCraftingManager.log("\u00a78Matching \u00a7cM\u00a74 Main Check Failed");
            return null; }
        //MCH// MythicCraftingManager.log("\u00a78Matching \u00a7cM\u00a77 Main check passed, building result.");

        // That's a success huh, build the results then.
        MythicBlueprintInventory ret = new MythicBlueprintInventory(mainResult, inventories.getResultInventory());

        // Check every side inventory
        Integer limitingSideReactions = null;
        for (String side : getSideInventoryNames()) {

            // Get side result
            Ref<Integer> sideTimes = new Ref<>();
            MythicRecipeInventory sideResult = getSideCheck(side).matches(inventories.getSideInventory(side), sideTimes);

            // First failure and you're out
            if (sideResult == null) {
                //MCH// MythicCraftingManager.log("\u00a78Matching \u00a7cS\u00a74 Side Check '\u00a7e" + getSideCheck(side).getName()  + "\u00a74' Failed");
                return null; }

            // Include
            ret.addSideInventory(side, sideResult);
            if (limitingSideReactions == null) { limitingSideReactions = sideTimes.getValue(); } else { if (limitingSideReactions > sideTimes.getValue(32767)) { limitingSideReactions = sideTimes.getValue(); } }
        }

        if (maxTimes != null) {
            //MCH// MythicCraftingManager.log("\u00a78Matching \u00a7cT\u00a74 Main Times: \u00a73" + mainTimes.getValue());
            //MCH// MythicCraftingManager.log("\u00a78Matching \u00a7cT\u00a74 Side Times: \u00a7b" + limitingSideReactions);

            // Which one is the least?
            int limitingTimes;
            if (limitingSideReactions != null) {

                // Whichever is lower
                limitingTimes = Math.min(limitingSideReactions, mainTimes.getValue(1));
            } else {

                // Whatever value the main had
                limitingTimes = mainTimes.getValue(1);
            }

            // The max yes
            maxTimes.setValue(limitingTimes);
            //MCH// MythicCraftingManager.log("\u00a78Matching \u00a7cT\u00a74 Total Times: \u00a79" + maxTimes.getValue());
        }

        // So this actually met every input requirement, lit.
        return ret;
    }

    /**
     * Players may access this in vanilla stations.
     *
     * @param forStation For which station you are activating this. Will not register the recipe onto the recipe book.
     *
     * @see #disable()
     */
    public void deploy(@NotNull MythicRecipeStation forStation) { deploy(forStation, null); }
    /**
     * Players may access this in vanilla stations. Also sets the recipe book live if possible
     *
     * @param forStation For which station you are activating this
     *
     * @param recipeName The Namespaced Key (if it was set live onto the recipe book) so you can remove
     *                   the recipe when your plugin reloads these kinds of things.
     *                   <p></p>
     *                   If <code>null</code>, the recipe wont be included in the crafting book.
     *
     * @see #disable()
     */
    public void deploy(@NotNull MythicRecipeStation forStation, @Nullable Ref<NamespacedKey> recipeName) {
        MythicCraftingManager.deployBlueprint(this, forStation);

        //DPY//MythicCraftingManager.log("\u00a78Deploy \u00a7eA\u00a77 Recipe now live: \u00a76" + getMainCheck().getName() + "\u00a7e/\u00a76" + ((io.lumine.mythic.lib.api.crafting.outputs.MRORecipe) getResult()).getOutput().getName());
        //DPY//for (MythicRecipeIngredient ingredient : getMainCheck().getIngredients()) {  String loc = ""; if (ingredient instanceof ShapedIngredient) { loc = "\u00a73" + ((ShapedIngredient) ingredient).getHorizontalOffset() + ":" + (-((ShapedIngredient) ingredient).getVerticalOffset());} MythicCraftingManager.log("\u00a78Deploy " + loc + " \u00a7e-I\u00a77 " + ingredient.getIngredient().getName()); }
        //DPY//for (MythicRecipeIngredient ingredient : ((io.lumine.mythic.lib.api.crafting.outputs.MRORecipe) getResult()).getOutput().getIngredients()) {  String loc = ""; if (ingredient instanceof ShapedIngredient) { loc = "\u00a73" + ((ShapedIngredient) ingredient).getHorizontalOffset() + ":" + (-((ShapedIngredient) ingredient).getVerticalOffset());} MythicCraftingManager.log("\u00a78Deploy " + loc + " \u00a7e-R\u00a77 " + ingredient.getIngredient().getName()); }

        // Main and result are vanilla bookable?
        if (canDisplayInRecipeBook() && recipeName != null) {

            //DPY//MythicCraftingManager.log("\u00a78Deploy \u00a7eB\u00a77 Booked under namespace\u00a79 " + nk.toString());

            try {

                // The name of the main check is used as namespace key
                Bukkit.addRecipe(((VanillaBookableRecipe) getMainCheck()).getBukkitRecipe(getNk(),
                        ((VanillaBookableOutput) getResult()).getBukkitRecipeResult()));

                recipeName.setValue(nk);

            } catch (IllegalArgumentException | IllegalStateException runtimeException) {

                if (runtimeException instanceof IllegalStateException) {

                    // Log duplicate recipe
                    MythicLib.plugin.getServer().getConsoleSender().sendMessage(FriendlyFeedbackProvider.quickForConsole(FFPMythicLib.get(),
                            "Could not register recipe for crafting book: Recipe of name '$u{0}$b' already registered.", getNk().getKey()));
                } else {

                    // Log error
                    MythicLib.plugin.getServer().getConsoleSender().sendMessage(FriendlyFeedbackProvider.quickForConsole(FFPMythicLib.get(),
                            "Could not register recipe for crafting book: ", ((IllegalArgumentException) runtimeException).getMessage()));
                }
            }
        } }

    /**
     * @return For which stations has this blueprint been set live, which vanilla
     * stations can players use to interact with it?
     */
    @NotNull public ArrayList<MythicRecipeStation> getDeployedFor() { return deployedFor; }
    /**
     * @param st Station its being deployed to.
     */
    protected void registerAsDeployed(@NotNull MythicRecipeStation st) { getDeployedFor().add(st); }
    /**
     * For which stations has this blueprint been set live, which vanilla
     * stations can players use to interact with it?
     */
    @NotNull final ArrayList<MythicRecipeStation> deployedFor = new ArrayList<>();

    /**
     * Players may no longer access this in vanilla stations.
     *
     * @see #deploy(MythicRecipeStation,Ref)
     */
    public void disable() {  for (MythicRecipeStation st : getDeployedFor()) { MythicCraftingManager.disableBlueprint(this, st); } deployedFor.clear(); }
}
