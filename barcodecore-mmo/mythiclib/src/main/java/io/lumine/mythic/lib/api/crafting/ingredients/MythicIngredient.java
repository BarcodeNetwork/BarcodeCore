package io.lumine.mythic.lib.api.crafting.ingredients;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.crafting.uifilters.IngredientUIFilter;
import io.lumine.mythic.lib.api.crafting.uimanager.ProvidedUIFilter;
import io.lumine.mythic.lib.api.crafting.uifilters.UIFilter;
import io.lumine.mythic.lib.api.util.Ref;
import io.lumine.mythic.lib.api.util.ui.*;
import io.lumine.utils.items.ItemFactory;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Used for the MythicRecipes system.
 * <p></p>
 * Since this system is completely customizable, the information we
 * need is just a {@link ProvidedUIFilter}.
 * <p></p>
 * There may be 'replacements' as well (say oak/spruce planks)
 * so that it is a list of {@link ProvidedUIFilter} instead.
 * <p></p>
 * MythicIngredients may be written in YML format and loaded through
 * {@link #deserializeFrom(ArrayList, FriendlyFeedbackProvider)}.
 * <p></p>
 * Finally, MythicIngredients may be <b>enabled</b> so that they can
 * be targeted by the {@link IngredientUIFilter}, or just obtained from
 * anywhere by their {@link #getName()} using {@link #get(String)}.
 *
 * @see #enable(MythicIngredient) 
 *
 * @author Gunging
 */
@SuppressWarnings("unused")
public class MythicIngredient implements Cloneable {

    /**
     * Some internal name to identify this ingredient.
     */
    @NotNull final String name;
    /**
     * Some internal name to identify this ingredient.
     * <p></p>
     * Must only be unique if you are going to load it to
     * be used with the {@link IngredientUIFilter}.
     */
    @NotNull public String getName() { return name; }

    /**
     * An ingredient that can be used in Mythic Recipes
     *
     * @param name A name to give to this ingredient, for use with the Ingredient UIFilter
     *
     * @param ingredient A list of all valid ProvidedUIFilters that act for this ingredient.
     *                   simplest example is a list with one entry, which means this ingredient
     *                   has no substitutes (Spruce planks vs Oak planks)
     *
     * @see #get(String)
     */
    public MythicIngredient(@NotNull String name, @NotNull ArrayList<ProvidedUIFilter> ingredient) {

        // Set those
        substitutes = ingredient;
        this.name = name;

        // It makes no sense that a UIFilter of an ingredient doesn't have an amount defined
        quantifySubstitutes();
    }
    /**
     * An ingredient that can be used in Mythic Recipes
     *
     * @param name A name to give to this ingredient, for use with the Ingredient UIFilter
     *
     * @param ingredient The provided UIFilter that encodes for the item this
     *                   ingredient represents.
     *
     * @see #get(String)
     */
    public MythicIngredient(@NotNull String name, @NotNull ProvidedUIFilter... ingredient) {

        // Set those
        substitutes = new ArrayList<>();
        for (ProvidedUIFilter puff : ingredient) { if (puff != null) { substitutes.add(puff); }}
        this.name = name;

        // It makes no sense that a UIFilter of an ingredient doesn't have an amount defined
        quantifySubstitutes();
    }

    /**
     * The list of items this ingredient will accept.
     * <p></p>
     * Ex, to accept multiple types of wood:
     * <p><code>v OAK_PLANKS 0</code>
     * </p><code>v SPRUCE_PLANKS 0</code>
     * <p><code>v JUNGLE_PLANKS 0</code>
     * </p><code>v BIRCH_PLANKS 0</code>
     */
    @NotNull final ArrayList<ProvidedUIFilter> substitutes;
    /**
     * The list of items this ingredient will accept.
     * <p></p>
     * Ex, to accept multiple types of wood:
     * <p><code>v OAK_PLANKS 0</code>
     * </p><code>v SPRUCE_PLANKS 0</code>
     * <p><code>v JUNGLE_PLANKS 0</code>
     * </p><code>v BIRCH_PLANKS 0</code>
     * <p></p>
     * <b>Fails silently if</b> you try to add a filter that requires
     * inventory match or an ingredient filter.
     *
     * @see UIFilter#useInventoryMatch()
     * @see IngredientUIFilter
     */
    public void addSubstitute(@NotNull ProvidedUIFilter substitute) {

        // Does it have amount? (May be a range)
        if (!substitute.hasAmount()) {

            // Set amount to 1, the default
            substitute.setAmount(1);
        }

        // Is it one of those weird filters?
        if (substitute.getParent().useInventoryMatch()) {

            // ew no
            return;
        }

        // No ingredientception AHHH
        else if (substitute.getParent() instanceof IngredientUIFilter) {

            // ew no
            return;
        }

        // Does it define?
        if (substitute.getParent().fullyDefinesItem()) { definesItem = true; }

        // Must have count
        substitutes.add(substitute);
    }
    /**
     * The list of items this ingredient will accept.
     * <p></p>
     * Ex, to accept multiple types of wood:
     * <p><code>v OAK_PLANKS 0</code>
     * </p><code>v SPRUCE_PLANKS 0</code>
     * <p><code>v JUNGLE_PLANKS 0</code>
     * </p><code>v BIRCH_PLANKS 0</code>
     *
     * @return A clone of the substitutes list
     */
    @NotNull public ArrayList<ProvidedUIFilter> getSubstitutes() { return new ArrayList<>(substitutes); }
    /**
     * The list of items this ingredient will accept.
     * <p></p>
     * Ex, to accept multiple types of wood:
     * <p><code>v OAK_PLANKS 0</code>
     * </p><code>v SPRUCE_PLANKS 0</code>
     * <p><code>v JUNGLE_PLANKS 0</code>
     * </p><code>v BIRCH_PLANKS 0</code>
     */
    public void clearSubstitutes() { substitutes.clear(); definesItem = false; }
    /**
     * Recipes forcefully require an amount, it cannot be unspecified.
     */
    void quantifySubstitutes() {
        definesItem = false;

        // Log
        FriendlyFeedbackProvider ffp = new FriendlyFeedbackProvider(FFPMythicLib.get());
        ffp.activatePrefix(true, "Mythic Ingredient $f" + getName());

        /*
         *  Go through each ingredient and check that it has an amount defined.
         */
        for (int n = 0; n < substitutes.size(); n++) {

            // Get
            ProvidedUIFilter nbt = substitutes.get(n);
            if (nbt == null) {

                // ew no
                substitutes.remove(n);
                n--;

                // Add as 'success' because the ffp now contains ERROR information on why the filter failed.
                ffp.log(FriendlyFeedbackCategory.ERROR,
                        "$fNull ingredient at {0}. ", getName());

                continue;
            }

            if (nbt.getParent().fullyDefinesItem()) { definesItem = true; }

            // Does it have amount? (May be a range)
            if (!nbt.hasAmount()) {

                // Set amount to 1, the default
                nbt.setAmount(1);
            }

            // Is it one of those weird filters?
            if (nbt.getParent().useInventoryMatch()) {

                // ew no
                substitutes.remove(nbt);
                n--;

                // Add as 'success' because the ffp now contains ERROR information on why the filter failed.
                ffp.log(FriendlyFeedbackCategory.ERROR,
                        "You may not use the $u{0}$b ($r{1}$b) UIFilter key to define ingredients: $fInventory-match filters are not supported for ingredients. ",
                        nbt.getParent().getIdentifier(), nbt.getParent().getFilterName(), getName());
            }

            // No ingredientception AHHH
            else if (nbt.getParent() instanceof IngredientUIFilter) {

                // ew no
                substitutes.remove(nbt);
                n--;

                // Add as 'success' because the ffp now contains ERROR information on why the filter failed.
                ffp.log(FriendlyFeedbackCategory.ERROR,
                        "You may not use the $u{0}$b ($r{1}$b) UIFilter key to define ingredients: $fIngredient filter cannot be used to define ingredients. ",
                        nbt.getParent().getIdentifier(), nbt.getParent().getFilterName(), getName());
            }
        }

        // Send all
        ffp.sendTo(FriendlyFeedbackCategory.ERROR, MythicLib.plugin.getServer().getConsoleSender());
    }

    /**
     * Is such an item suitable to fulfill this ingredient?
     * @param what Item Stack you want to test that matches any
     *             of the substitutes to fulfill this ingredient.
     *
     * @param ffp Tell the user why their ItemStack isn't this ingredient.
     *            Note that it may stack the failure logs and end up succeeding
     *            in the last ProvidedUIFilter of the list, in which case you may
     *            not want to log the <code>FAILURE</code> messages.
     *
     * @return <code>true</code> If <code><i>what</i></code> matches
     *         correctly even a single UIFilter (note that the amount
     *         is considered too).
     */
    public boolean matches(@NotNull ItemStack what, @Nullable FriendlyFeedbackProvider ffp) {

        // Try every substitute
        for (ProvidedUIFilter fil : substitutes) {

            // Does it match?
            if (fil.matches(what, false, ffp)) { return true; }
        }

        // Nope
        return false;
    }

    /**
     * Is such an item suitable to fulfill this ingredient?
     * A more advanced form of {@link #matches(ItemStack, FriendlyFeedbackProvider)}
     *
     * @param what Item Stack you want to test that matches any
     *             of the substitutes to fulfill this ingredient.
     *
     * @param ignoreAmount Should amount prevent matching of the
     *                     Provided UI Filters?
     *
     * @param matchers Outputs the first {@link ProvidedUIFilter} that matched
     *                 this ItemStack, if not null, so that its information can
     *                 be read outside this method.
     *
     * @param deepMatch Rather than only the first match, it will go through every
     *                  substitute and tell you which matched in the array 'matchers'.
     *
     * @param ffp Tell the user why their ItemStack isn't this ingredient.
     *            Note that it may stack the failure logs and end up succeeding
     *            in the last ProvidedUIFilter of the list, in which case you may
     *            not want to log the <code>FAILURE</code> messages.
     *
     * @return <code>true</code> If <code><i>what</i></code> matches
     *         correctly even a single UIFilter (note that the amount
     *         is considered too).
     */
    public boolean matches(@NotNull ItemStack what, boolean ignoreAmount, @Nullable Ref<ArrayList<ProvidedUIFilter>> matchers, boolean deepMatch, @Nullable FriendlyFeedbackProvider ffp) {

        // Thay array
        ArrayList<ProvidedUIFilter> ref = new ArrayList<>();
        boolean oneMatch = false;

        // Try every substitute
        for (ProvidedUIFilter fil : substitutes) {

            //CRAFT// MythicCraftingManager.log("\u00a78Filter \u00a73M\u00a77 Is it '\u00a7b" + fil.getParent().getIdentifier() + " " + fil.getArgument() + " " + fil.getData() + " " + fil.getAmountRange() + "\u00a77' ?");

            // Does it match?
            if (fil.matches(what, ignoreAmount, ffp)) {
                //CRAFT// MythicCraftingManager.log("\u00a78Filter \u00a73M\u00a77 \u00a7aYes");

                // Output the ProvidedUIFilter information
                ref.add(fil);

                // Success
                if (!deepMatch) {

                    // That's it
                    Ref.setValue(matchers, ref);
                    return true;  }
                oneMatch = true;
            }

            //CRAFT// else { MythicCraftingManager.log("\u00a78Filter \u00a73M\u00a77 \u00a7cNo"); }
        }

        // Store matchers
        Ref.setValue(matchers, ref);

        // Nope
        return oneMatch;
    }

    /**
     * List of ingredients that can be used with the {@link IngredientUIFilter} filter.
     */
    final static HashMap<String, MythicIngredient> enabledIngredients = new HashMap<>();
    /**
     * Enables a MythicIngredient to be used and found with ease globally.
     * <p></p>
     * MythicIngredients may not be loaded to be used, but if they are to
     * be loaded, they must also have unique names, otherwise this operation
     * fails silently.
     *
     * @param ing MythicIngredient you're loading.
     */
    public static void enable(@NotNull MythicIngredient ing) {

        // If invalid
        if (get(ing.getName()) == null) {

            // Load :B
            enabledIngredients.put(ing.getName(), ing);
        }
    }
    /**
     * Get a enabled MythicIngredient of this name
     *
     * @param name Name you are searching for
     * @return <code>null</code> If there is no MythicIngredient of this name loaded.
     *
     * @see #enable(MythicIngredient)
     */
    @Nullable public static MythicIngredient get(@NotNull String name) {

        // Loaded?
        return enabledIngredients.get(name);
    }
    /**
     * Gives you a list of the names of all loaded ingredients.
     */
    @NotNull public static ArrayList<String> getEnabledIngredients() { return new ArrayList<>(enabledIngredients.keySet());}

    /**
     * Loads all the ingredients encoded in the provided configuration sections
     * into being available with the {@link IngredientUIFilter} filter.
     * <p></p>
     * Duplicate names will be ignored.
     *
     * @param configurationSections Sections you are sure encode for MythicIngredients
     *
     * @param ffp Information on why a file doesn't parse (like syntax mistakes).
     */
    public static void deserializeFrom(@NotNull ArrayList<ConfigurationSection> configurationSections, @Nullable FriendlyFeedbackProvider ffp) {

        // Evaluate very loaded ingredient
        for (ConfigurationSection csConfig : configurationSections) {

            // Yea no
            if (csConfig == null) {

                FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                        "Found a null config in the provided list. ");
                continue;
            }

            // Examine Every Entry
            for(Map.Entry<String, Object> val : (csConfig.getValues(false)).entrySet()) {

                // Get Template Name
                String tName = val.getKey();

                // Ignore duplicates
                if (get(tName) != null) {

                    FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                            "There is already an ingredient of name '$u{0}$b' defined. $fIgnored. ", tName);
                    continue;
                }

                // List of UIFilters yes
                ArrayList<String> cont = new ArrayList<>();
                if (csConfig.contains(tName + ".Items")) { cont = new ArrayList<>(csConfig.getStringList(tName + ".Items")); }

                // Register if any
                if (cont.size() > 0) {

                    // Deserialize thay mythic ingredient
                    MythicIngredient deserializedIngredient = deserialize(tName, cont, ffp);

                    // Has any ingredients?
                    if (deserializedIngredient.getSubstitutes().size() > 0) {

                        // Actually load
                        enable(deserializedIngredient);

                    } else {

                        // Um cant have no ingredient with no contents
                        FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                                "Ingredient of name '$u{0}$b' has no items to match. $fIgnored. ", tName);
                    }

                } else {

                    // Um cant have no ingredient with no contents
                    FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                            "Ingredient of name '$u{0}$b' has no items to match. $fIgnored. ", tName);
                }
            }
        }
    }
    /**
     * Interpret a list of strings as a list of Provided UIFilters for a Mythic Ingredient.
     * <p></p>
     * Meant to deserialize a compound ingredient. <i>Not ingredients in recipes.</i>
     * <p></p>
     * Example:
     * <p>- 'm CONSUMABLE MANGO'
     * </p>- 'm CONSUMABLE GRAPES'
     * <p>- 'v apple 0'
     * </p>- 'm CONSUMABLE ORANGE'
     *
     * @param ingredientName Internal name of the MythicIngredient. Must only be unique if you plan to {@link #enable(MythicIngredient)} it.
     * @param serializedContents List of Strings encoding for Provided UIFilters.
     * @param ffp Feedback on the syntax of the strings.
     *
     * @return A MythicIngredient that, at worst (if no String in the list was a valid UIFilter), will match no items.
     */
    @NotNull public static MythicIngredient deserialize(@NotNull String ingredientName, @NotNull ArrayList<String> serializedContents, @Nullable FriendlyFeedbackProvider ffp) {

        // Array of ingredients yes
        ArrayList<ProvidedUIFilter> ret = new ArrayList<>();

        // Well
        for (String ing : serializedContents) {

            // Uh bruh
            if (ing == null || ing.isEmpty()) {

                FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                        "Expected a UIFilter in the format $ekey argument data amount$b (where amount is optional) instead of a $fnull/empty$b entry.");
                continue;
            }

            // Well attempt to generate lma0
            ProvidedUIFilter puff = ProvidedUIFilter.getFromString(ing, ffp);

            // FFP Will have information on the failure
            if (puff == null) { continue; }

            // Actually that's it :pogYOO:
            ret.add(puff);
        }

        // Proc ig
        return new MythicIngredient(ingredientName, ret);
    }

    @Override
    public MythicIngredient clone() {
        try { super.clone(); } catch (CloneNotSupportedException ignored) {}

        // Clone
        return new MythicIngredient(getName(), getSubstitutes());
    }
    
    /**
     * Does at least one of the substitutes fully defines an item?
     */
    boolean definesItem = false;
    /**
     * Does at least one of the substitutes fully defines an item?
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isDefinesItem() { return definesItem; }
    /**
     * Among the many substitutes that satisfy this
     * ingredient, get one at random.
     * <p></p>
     * This might be null if there is no substitute
     * that can produce an item.
     *
     * @return One random item that would satisfy this recipe
     */
    @Nullable public ItemStack getRandomSubstituteItem(@Nullable FriendlyFeedbackProvider ffp) {

        // No fully defined item? L
        if (!isDefinesItem()) { return null; }

        // What will it be?
        ItemStack result = null;

        // An editable array of disposable ingredients
        ProvidedUIFilter chosen = getRandomSubstitute(true);

        // Nope
        if (chosen == null) { return null; }

        // Generate Item Stack yes
        return chosen.getItemStack(ffp);
    }
    @NotNull public ItemStack getRandomDisplayItem(@Nullable FriendlyFeedbackProvider ffp) {

        // What will it be?
        ItemStack result = null;

        // An editable array of disposable ingredients
        ProvidedUIFilter chosen = getRandomSubstitute(false);
        if (chosen == null) { return ItemFactory.of(Material.STRUCTURE_VOID).name("\u00a74Internal Error:\u00a73 No Substitute").lore("\u00a76MythicLib \u00a7eio.lumine.mythic.lib.api.crafting.ingredients.MythicIngredient.getRandomDisplayItem()").build(); }

        // Generate Item Stack yes
        return chosen.getDisplayStack(ffp);
    }
    /**
     * @return One random item that would satisfy this recipe, as a provided UI filter.
     */
    @Nullable public ProvidedUIFilter getRandomSubstitute(boolean requireItemFullyDefined) {

        // To return
        ProvidedUIFilter ret = null;

        // An editable array of disposable ingredients
        ArrayList<ProvidedUIFilter> poof = getSubstitutes();

        // While the result is null
        while (ret == null && poof.size() > 0) {

            // Get random index of
            int chosenIndex = SilentNumbers.floor(SilentNumbers.randomRange(0, poof.size() - 0.25));

            // Attempt to generate item at that index
            ProvidedUIFilter puff = poof.get(chosenIndex);

            // Can it generate items?
            if (!requireItemFullyDefined || puff.getParent().fullyDefinesItem()) {

                // Generate?
                ret = puff;
            }

            // Remove puff
            poof.remove(puff);
        }

        return ret;
    }
}