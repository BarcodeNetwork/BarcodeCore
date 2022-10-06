package io.lumine.mythic.lib.api.crafting.uifilters;

import io.lumine.mythic.lib.api.crafting.uimanager.UIFilterManager;
import io.lumine.mythic.lib.api.util.ui.*;
import io.lumine.utils.items.ItemFactory;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 *  The filter to match an item by its enchantment levels.
 *
 *  @author Gunging
 */
public class EnchantmentUIFilter implements UIFilter {
    @NotNull @Override public String getIdentifier() { return "e"; }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean matches(@NotNull ItemStack item, @NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {

        // Check validity
        if (!isValid(argument, data, ffp)) { return false; }

        // Check counter matches
        if (cancelMatch(item, ffp)) { return false; }

        // Time to test
        Enchantment wench = getEnchantment(argument);
        QuickNumberRange expectedLevel = QuickNumberRange.getFromString(data);
        ItemMeta iMeta = item.getItemMeta();

        // Has enchantment?
        if (iMeta.hasEnchant(wench)) {

            // Get level
            int currentLevel = iMeta.getEnchantLevel(wench);

            // Well, level 0?
            if (expectedLevel.inRange(currentLevel)) {

                // Well isn't that funny
                FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.SUCCESS,
                        "This item had $r{0} {1}$b. $sPassed$b. ", wench.getKey().getKey().toLowerCase().replace("_", " "), SilentNumbers.toRomanNumerals(currentLevel));
                return true;

            } else {

                // Yea no
                FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.FAILURE,
                        "This had $r{0} {1}$b, expected $r{0} {2}$b. $fRejected$b. ", wench.getKey().getKey().toLowerCase().replace("_", " "), SilentNumbers.toRomanNumerals(currentLevel), getAsRomanNumeralRange(expectedLevel));
                return false;
            }

        } else {

            // Well, level 0?
            if (expectedLevel.inRange(0D)) {

                // Well isn't that funny
                FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.SUCCESS,
                        "This item didn't have the $r{0}$b enchantment. $sPassed$b. ", wench.getKey().getKey().toLowerCase().replace("_", " "));
                return true;

            } else {

                // Yea no
                FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.FAILURE,
                        "This item didn't have the $r{0}$b enchantment. $fRejected$b. ", wench.getKey().getKey().toLowerCase().replace("_", " "));
                return false;
            }
        }
    }

    @Override
    public boolean isValid(@NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {

        // Did it work?
        boolean failure = false;

        // Attempt to get enchantment
        if (getEnchantment(argument) == null) {

            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                    "Expected an enchantment like $efire_protection$b or $esmite$b instead of $u{0}$b. ", argument);
            failure = true;
        }

        // Attempt to parse range
        if (QuickNumberRange.getFromString(data, ffp) == null) {

            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                    "Enchantment level '$u{0}$b' is not an integer number or range (like $e3..4$b). ", data);
            failure = true;
        }

        return !failure;
    }

    @NotNull
    @Override
    public ArrayList<String> tabCompleteArgument(@NotNull String current) {
        return SilentNumbers.smartFilter(getEnchantmentNames(), current, true);
    }

    @NotNull
    @Override
    public ArrayList<String> tabCompleteData(@NotNull String argument, @NotNull String current) {
        return SilentNumbers.toArrayList("1", "2", "4", "6..", "..2", "3..5", "..");
    }

    @Override
    public boolean fullyDefinesItem() { return false; }

    @Nullable @Override public ItemStack getItemStack(@NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {

        // Appendable Message
        FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                "Enchantment UIFilter $fcannot generate an item. ");

        return null;
    }

    @NotNull
    @Override
    @SuppressWarnings("ConstantConditions")
    public ItemStack getDisplayStack(@NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {

        // Check that its valid
        if (!isValid(argument, data, ffp)) { return ItemFactory.of(Material.BARRIER).name("\u00a7cInvalid Enchantment \u00a7e" + argument + " " + data).build(); }

        // Read
        Enchantment wench = getEnchantment(argument);
        QuickNumberRange expectedLevel = QuickNumberRange.getFromString(data);

        // Return that ig
        return ItemFactory.of(Material.GOLDEN_APPLE).name("\u00a73Any Item").enchant(wench, SilentNumbers.round(expectedLevel.getAsDouble(1))).lore(getDescription(argument, data)).build();
    }

    @NotNull
    @Override
    @SuppressWarnings("ConstantConditions")
    public ArrayList<String> getDescription(@NotNull String argument, @NotNull String data) {

        // Check validity
        if (!isValid(argument, data, null)) { return SilentNumbers.toArrayList("This item is $finvalid$b."); }

        // Description is thus
        return SilentNumbers.toArrayList("This item must be enchanted with $r" +
                getEnchantment(argument).getKey().getKey().toLowerCase().replace("_", " ") + " " +
                getAsRomanNumeralRange(QuickNumberRange.getFromString(data)) + "$b.");
    }

    /**
     * Ever wanted to express a Quick Number Range in roman numerals?
     * <p></p>
     * No?
     * <p></p>
     * <p></p>
     * <b>Uses the {@link FriendlyFeedbackPalette} color codes system.</b>
     */
    @SuppressWarnings("ConstantConditions")
    @NotNull public String getAsRomanNumeralRange(@NotNull QuickNumberRange qnr) {

        // Simple?
        if (qnr.isSimple()) { return "$r" + SilentNumbers.toRomanNumerals(SilentNumbers.round(qnr.getMaximumInclusive())); }

        // Get
        String minn = "-∞"; String maxn = "∞";
        if (qnr.hasMin()) { minn = SilentNumbers.toRomanNumerals(SilentNumbers.round(qnr.getMinimumInclusive())); }
        if (qnr.hasMax()) { maxn = SilentNumbers.toRomanNumerals(SilentNumbers.round(qnr.getMaximumInclusive())); }

        // Build
        return "$r" + minn + " $bthrough$r " + maxn;
    }

    static ArrayList<String> enchantmentNames = null;
    static ArrayList<String> getEnchantmentNames() {

        // The one already loaded
        if (enchantmentNames != null) { return enchantmentNames; }

        // Generate
        enchantmentNames = new ArrayList<>();

        // Or generate the first time
        for (Enchantment m : Enchantment.values()) { enchantmentNames.add(m.toString());  }

        // Yes
        return enchantmentNames;
    }

    /*
     *  Tracking
     */

    @NotNull
    @Override
    public String getSourcePlugin() { return "MythicLib"; }

    @NotNull
    @Override
    public String getFilterName() { return "Enchantment"; }

    @NotNull
    @Override
    public String exampleArgument() { return "smite"; }

    @NotNull
    @Override
    public String exampleData() { return "2"; }

    public EnchantmentUIFilter() {}

    /**
     * Registers this filter onto the manager.
     */
    public static void register() {

        // Yes
        global = new EnchantmentUIFilter();
        UIFilterManager.registerUIFilter(global);
    }

    /**
     * @return The general instance of this MMOItem UIFilter.
     */
    @NotNull public static EnchantmentUIFilter get() { return global; }
    static EnchantmentUIFilter global;

    /**
     * Why is it so hard to get an enchantment? I cant be the only one who
     * is lazy about thinking if I should use the key or the name of the
     * namespace .-.
     */
    @Contract("null -> null")
    @Nullable
    private static Enchantment getEnchantment(@Nullable String name) {
        if (name == null) { return null; }

        // All right get by key in lowercase
        return Enchantment.getByKey(NamespacedKey.minecraft(name.toLowerCase().replace(" ", "_").replace("-","_")));
    }
}
