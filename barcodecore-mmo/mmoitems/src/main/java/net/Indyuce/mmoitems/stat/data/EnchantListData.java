package net.Indyuce.mmoitems.stat.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import net.Indyuce.mmoitems.stat.data.type.Mergeable;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import org.jetbrains.annotations.NotNull;

public class EnchantListData implements StatData, Mergeable {
	private final Map<Enchantment, Integer> enchants = new HashMap<>();

	public Set<Enchantment> getEnchants() {
		return enchants.keySet();
	}

	public int getLevel(@NotNull Enchantment enchant) {
		if (!enchants.containsKey(enchant)) { return 0; }
		return enchants.get(enchant);
	}

    public void addEnchant(Enchantment enchant, int level) {

		// Ignore lvl 0 enchants :wazowskibruhmoment:
        if (level == 0) { enchants.remove(enchant); } else { enchants.put(enchant, level);}
    }

	public void clear() { enchants.clear(); }

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof EnchantListData)) { return false; }
		if (((EnchantListData) obj).enchants.size() != enchants.size()) { return false; }

		for (Enchantment e : getEnchants()) {

			// Compare
			if (getLevel(e) != ((EnchantListData) obj).getLevel(e)) { return false; } }
		return true;
	}

	@Override
	public void merge(StatData data) {
		Validate.isTrue(data instanceof EnchantListData, "Cannot merge two different stat data types");
		boolean additiveMerge = MMOItems.plugin.getConfig().getBoolean("stat-merging.additive-enchantments", false);

		for (Enchantment enchant : ((EnchantListData) data).getEnchants()) {
			if (additiveMerge) {

				// Additive
				enchants.put(enchant, ((EnchantListData) data).getLevel(enchant) + enchants.get(enchant));
			} else {

				// Max Enchantment
				addEnchant(enchant,

						// Does this one already have the enchant?
						enchants.containsKey(enchant) ?

								// Use the better of the two
								Math.max(((EnchantListData) data).getLevel(enchant), enchants.get(enchant)) :

								// No enchant yet, just copy over
								((EnchantListData) data).getLevel(enchant));
			}
		}
	}

	@Override
	public @NotNull StatData cloneData() {

		// Start Fresh
		EnchantListData ret = new EnchantListData();

		// Enchant
		for (Enchantment enchant : enchants.keySet()) { ret.addEnchant(enchant, enchants.getOrDefault(enchant, 0)); }

		// Thats it
		return ret;
	}

	@Override
	public boolean isClear() {
		// Any non lvl 0 enchantment?
		for (Enchantment e : getEnchants()) { if (getLevel(e) != 0) { return false; } }
		return true;
	}

	/**
	 *  todo We cannot yet assume (for a few months) that the Original Enchantment Data
	 *   registered into the Stat History is actually true to the template (since it may
	 *   be the enchantments of an old-enchanted item, put by the player).
	 *   <br><br>
	 *   Thus this block of code checks the enchantment data of the newly generated
	 *   MMOItem and follows the following logic to give our best guess if the Original
	 *   stats are actually Original:
	 *   <br><br>
	 *   1: Is the item unenchantable and unrepairable? Then they must be original
	 *   <br><br>
	 *   2: Does the template have no enchantments? Then they must be external
	 *   <br><br>
	 *   3: Does the template have this enchantment at an unobtainable level? Then it must be original
	 *   <br><br>
	 *   4: Does the template have this enchantment at a lesser level? Then it must be external (player upgraded it)
	 *   <br><br>
	 *   Original: Included within the template at first creation
	 *   External: Enchanted manually by a player
	 *
	 * @param mmoItem The item, to provide context for adequate guessing.
	 *
	 */
	public void identifyTrueOriginalEnchantments(@NotNull MMOItem mmoItem) {

		//RFG//MMOItems.log(" \u00a7b> \u00a77Original Enchantments Upkeep");

		// 1: The item is unenchantable and unrepairable? Cancel this operation, the cached are Original
		if (mmoItem.hasData(ItemStats.DISABLE_ENCHANTING) && mmoItem.hasData(ItemStats.DISABLE_REPAIRING)) {
			//RFG//MMOItems.log(" \u00a7bType-1 \u00a77Original Identification ~ no transfer");

			clear();
			return;
		}

		if (!mmoItem.hasData(ItemStats.ENCHANTS)) { mmoItem.setData(ItemStats.ENCHANTS, new EnchantListData());}

		EnchantListData mmoData = (EnchantListData) mmoItem.getData(ItemStats.ENCHANTS);

		// 2: If it has data (It always has) and the amount of enchants is zero, the cached are Extraneous
		if (mmoData.getEnchants().size() == 0) {
			//RFG//MMOItems.log(" \u00a73Type-2 \u00a77Extraneous Identification ~ all transferred");

			// All right, lets add those to cached enchantments
			mmoItem.mergeData(ItemStats.ENCHANTS,this, null);
			return;
		}

		// Which enchantments are deemed external, after all?
		EnchantListData processed = new EnchantListData();

		// Identify material
		mmoItem.hasData(ItemStats.MATERIAL); MaterialData mData = (MaterialData) mmoItem.getData(ItemStats.MATERIAL); Material mat = mData.getMaterial();

		// 3 & 4: Lets examine every stat
		for (Enchantment e : getEnchants()) {
			//RFG//MMOItems.log(" \u00a7b  = \u00a77Per Enchant - \u00a7f" + e.getName());

			// Lets see hmm
			int current = getLevel(e);
			int updated = mmoData.getLevel(e);
			//RFG//MMOItems.log(" \u00a73  <=: \u00a77Current \u00a7f" + current);
			//RFG//MMOItems.log(" \u00a73  <=: \u00a77Updated \u00a7f" + updated);

			// 3: Is it at an unobtainable level? Then its Original
			if (updated > e.getMaxLevel() || !e.getItemTarget().includes(mat)) {
				//RFG//MMOItems.log(" \u00a7bType-3 \u00a77Original Identification ~ Impossible through vanilla");

				continue;
			}

			// 4: Is it at a lesser level? Player must have enchanted, take them as External
			if (updated < current) {
				//RFG//MMOItems.log(" \u00a73Type-4 \u00a77Extraneous Identification ~ Improvement from the Template");
				processed.addEnchant(e, current);

				//noinspection UnnecessaryContinue
				continue;
			}

			//RFG//MMOItems.log(" \u00a73Type-5 \u00a77Original Identification ~ Not improved from the template");
		}

		// Finish merge
		if (!processed.isClear()) {

			// As Extraneosu
			mmoItem.mergeData(ItemStats.ENCHANTS, processed, null);
		}
	}
}