package net.Indyuce.mmoitems.api.item.build;

import java.util.*;

import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.ItemTier;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate;
import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate.TemplateOption;
import net.Indyuce.mmoitems.api.item.template.NameModifier;
import net.Indyuce.mmoitems.api.item.template.NameModifier.ModifierType;
import net.Indyuce.mmoitems.api.item.template.TemplateModifier;
import net.Indyuce.mmoitems.stat.data.DoubleData;
import net.Indyuce.mmoitems.stat.data.StringData;
import net.Indyuce.mmoitems.stat.data.type.Mergeable;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import net.Indyuce.mmoitems.stat.type.NameData;
import net.Indyuce.mmoitems.stat.type.StatHistory;
import org.jetbrains.annotations.NotNull;

public class MMOItemBuilder {
    private final MMOItem mmoitem;
    private final int level;
    private final ItemTier tier;

    /**
     * Name modifiers, prefixes or suffixes, with priorities. They are saved
     * because they must be applied after the modifier selection process
     */
    private final HashMap<UUID, NameModifier> nameModifiers = new HashMap<>();

    /**
     * Instance which is created everytime an mmoitem is being randomly
     * generated
     *
     * @param template
     *            The mmoitem template used to generate an item.
     * @param level
     *            Specified item level.
     * @param tier
     *            Specified item tier which determines how many capacity it will
     *            have. If no tier is given, item uses the default capacity
     *            formula given in the main config file
     */
    public MMOItemBuilder(MMOItemTemplate template, int level, ItemTier tier) {
        this.level = level;
        this.tier = tier;

        /*
         * Capacity is not final as it keeps lowering as modifiers are selected and
         * applied
         */
        double capacity = (tier != null && tier.hasCapacity() ? tier.getModifierCapacity() : MMOItems.plugin.getLanguage().defaultItemCapacity).calculate(level);
        mmoitem = new MMOItem(template.getType(), template.getId());

        // apply base item data
        template.getBaseItemData().forEach((stat, random) -> applyData(stat, random.randomize(this)));

        if (tier != null)
            mmoitem.setData(ItemStats.TIER, new StringData(tier.getId()));
        if (level > 0)
            mmoitem.setData(ItemStats.ITEM_LEVEL, new DoubleData(level));

        // roll item gen modifiers
        for (TemplateModifier modifier : rollModifiers(template)) {
            // roll modifier chance
            // only apply if enough item weight
            if (!modifier.rollChance() || modifier.getWeight() > capacity)
                continue;

            UUID modUUID = UUID.randomUUID();

            capacity -= modifier.getWeight();
            if (modifier.hasNameModifier()) { addModifier(modifier.getNameModifier(), modUUID); }

            for (ItemStat stat : modifier.getItemData().keySet())
                addModifierData(stat, modifier.getItemData().get(stat).randomize(this), modUUID);
        }
    }

    public int getLevel() {
        return level;
    }

    public ItemTier getTier() {
        return tier;
    }

    /**
     * Calculates the item display name after applying name modifiers. If name
     * modifiers are specified but the item has no display name, MMOItems uses
     * "Item"
     *
     * @return Built MMOItem instance
     */
    public MMOItem build() {

        if (!nameModifiers.isEmpty()) {

            // Get name data
            StatHistory hist = StatHistory.from(mmoitem, ItemStats.NAME);
            if (!mmoitem.hasData(ItemStats.NAME)) { mmoitem.setData(ItemStats.NAME, new NameData("Item")); }

            for (UUID obs : nameModifiers.keySet()) {

                // Create new Name Data
                NameModifier mod = nameModifiers.get(obs);
                NameData modName = new NameData("");

                // Include modifier information
                if (mod.getType() == ModifierType.PREFIX) { modName.addPrefix(mod.getFormat()); }
                if (mod.getType() == ModifierType.SUFFIX) { modName.addSuffix(mod.getFormat()); }

                // Register onto SH
                hist.registerModifierBonus(obs, modName);
            }

            // Recalculate name
            mmoitem.setData(ItemStats.NAME, hist.recalculate(mmoitem.getUpgradeLevel()));
        }

        return mmoitem;
    }

    /**
     * Applies statData to the builder, either merges it if statData is
     * mergeable like lore, abilities.. or entirely replaces current data
     *
     * @param stat
     *            Stat owning the data
     * @param data
     *            StatData to apply
     */
    public void applyData(ItemStat stat, StatData data) {

        // Is the data mergeable? Apply as External SH
        if (mmoitem.hasData(stat) && data instanceof Mergeable) {

            ((Mergeable) mmoitem.getData(stat)).merge(data);

        } else {

            // Set, there is no more.
            mmoitem.setData(stat, data);
        }
    }

    /**
     * Registers the modifier onto the item
     *
     * @param stat
     *            Stat owning the data
     * @param data
     *            StatData to apply
     */
    public void addModifierData(@NotNull ItemStat stat, @NotNull StatData data, @NotNull UUID uuid) {

        // Is this mergeable?
        if (stat.getClearStatData() instanceof Mergeable) {


            // Apply onto Stat History
            StatHistory hist = StatHistory.from(mmoitem, stat);
            //MOD//MMOItems.log("\u00a7c+---------->\u00a77 Modifying Item");
            //MOD//hist.log();

            // Apply
            hist.registerModifierBonus(uuid, data);

        } else {

            // Set, there is no more.
            mmoitem.setData(stat, data);
        }
    }

    /**
     * Adds a modifier only if there aren't already modifier of the same type
     * with strictly higher priority. If there are none, adds modifier and
     * clears less priority modifiers
     *
     * @param modifier
     *            Name modifier which needs to be added
     *
     * @param mod
     * 			  UUID of storage into the Stat History of name
     */
    public void addModifier(@NotNull NameModifier modifier, @NotNull UUID mod) {

        // Might overwrite a modifier yes
        ArrayList<UUID> removedObs = new ArrayList<>();
        for (UUID cUID : nameModifiers.keySet()) {

            // Remove obs?
            NameModifier obs = nameModifiers.get(cUID);

            // Are they the same type?
            if (obs.getType() == modifier.getType()) {

                // Choose greater priority
                if (obs.getPriority() > modifier.getPriority()) {

                    // Keep old one
                    return;

                } else if (obs.getPriority() < modifier.getPriority()) {

                    // Remove old one and add new one
                    removedObs.add(cUID);
                }
            }
        }

        // Remove
        for (UUID ro : removedObs) { nameModifiers.remove(ro); }
        nameModifiers.put(mod, modifier);
    }

    /**
     * @param template
     *            The template to list modifiers from
     * @return A sorted (or unsorted depending on the template options) list of
     *         modifiers that can be later rolled and applied to the builder
     */
    @NotNull public static Collection<TemplateModifier> rollModifiers(@NotNull MMOItemTemplate template) {
        if (!template.hasOption(TemplateOption.ROLL_MODIFIER_CHECK_ORDER))
            return template.getModifiers().values();

        List<TemplateModifier> modifiers = new ArrayList<>(template.getModifiers().values());
        Collections.shuffle(modifiers);
        return modifiers;
    }
}
