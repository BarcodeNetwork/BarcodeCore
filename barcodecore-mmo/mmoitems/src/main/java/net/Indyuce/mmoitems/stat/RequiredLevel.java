package net.Indyuce.mmoitems.stat;

import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.api.item.SupportedNBTTagValues;
import io.lumine.mythic.lib.api.util.ui.PlusMinusPercent;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import io.lumine.mythic.lib.version.VersionMaterial;
import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder;
import net.Indyuce.mmoitems.api.item.mmoitem.ReadMMOItem;
import net.Indyuce.mmoitems.api.player.RPGPlayer;
import net.Indyuce.mmoitems.api.util.NumericStatFormula;
import net.Indyuce.mmoitems.api.util.message.Message;
import net.Indyuce.mmoitems.stat.data.DoubleData;
import net.Indyuce.mmoitems.stat.data.RequiredLevelData;
import net.Indyuce.mmoitems.stat.data.random.RandomRequiredLevelData;
import net.Indyuce.mmoitems.stat.data.random.RandomStatData;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import net.Indyuce.mmoitems.stat.type.DoubleStat;
import net.Indyuce.mmoitems.stat.type.ItemRestriction;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class RequiredLevel extends DoubleStat implements ItemRestriction {

	/*
	 * stat that uses a custom DoubleStatData because the merge algorithm is
	 * slightly different. when merging two "required level", MMOItems should
	 * only keep the highest levels of the two and not sum the two values
	 */
	public RequiredLevel() {
		super("REQUIRED_LEVEL", VersionMaterial.EXPERIENCE_BOTTLE.toMaterial(), "Required Level",
				new String[] { "The level your item needs", "in order to be used." }, new String[] { "!block", "all" });
	}

	@Override
	public void whenApplied(@NotNull ItemStackBuilder item, @NotNull StatData data) {

		// Lore Management
		int lvl = (int) ((DoubleData) data).getValue();
		item.getLore().insert("required-level", formatNumericStat(lvl, "#", "" + lvl));

		// Insert NBT
		item.addItemTag(getAppliedNBT(data));
	}
	@Override
	public void whenPreviewed(@NotNull ItemStackBuilder item, @NotNull StatData currentData, @NotNull RandomStatData templateData) throws IllegalArgumentException {
		Validate.isTrue(currentData instanceof DoubleData, "Current Data is not Double Data");
		Validate.isTrue(templateData instanceof NumericStatFormula, "Template Data is not Numeric Stat Formula");

		// Get Value
		double techMinimum = ((NumericStatFormula) templateData).calculate(0, -2.5);
		double techMaximum = ((NumericStatFormula) templateData).calculate(0, 2.5);

		// Cancel if it its NEGATIVE and this doesn't support negative stats.
		if (techMaximum < 0 && !handleNegativeStats()) { return; }
		if (techMinimum < 0 && !handleNegativeStats()) { techMinimum = 0; }
		if (techMinimum < ((NumericStatFormula) templateData).getBase() - ((NumericStatFormula) templateData).getMaxSpread()) { techMinimum = ((NumericStatFormula) templateData).getBase() - ((NumericStatFormula) templateData).getMaxSpread(); }
		if (techMaximum > ((NumericStatFormula) templateData).getBase() + ((NumericStatFormula) templateData).getMaxSpread()) { techMaximum = ((NumericStatFormula) templateData).getBase() + ((NumericStatFormula) templateData).getMaxSpread(); }

		// Add NBT Path
		item.addItemTag(getAppliedNBT(currentData));

		// Display if not ZERO
		if (techMinimum != 0 || techMaximum != 0) {

			String builtRange;
			if (SilentNumbers.round(techMinimum, 2) == SilentNumbers.round(techMaximum, 2)) { builtRange = SilentNumbers.readableRounding(techMinimum, 0); }
			else { builtRange = SilentNumbers.removeDecimalZeros(String.valueOf(techMinimum)) + "-" + SilentNumbers.removeDecimalZeros(String.valueOf(techMaximum)); }

			// Just display normally
			item.getLore().insert("required-level", formatNumericStat(techMinimum, "#", builtRange));
		}
	}

	@NotNull
	@Override
	public ArrayList<ItemTag> getAppliedNBT(@NotNull StatData data) {

		// Make and bake
		ArrayList<ItemTag> ret = new ArrayList<>();
		ret.add(new ItemTag(getNBTPath(), ((DoubleData) data).getValue()));
		return ret;
	}

	@Override
	public RandomStatData whenInitialized(Object object) {
		return new RandomRequiredLevelData(object);
	}

	@Override
	public void whenLoaded(@NotNull ReadMMOItem mmoitem) {

		// Find relevat tgs
		ArrayList<ItemTag> tags = new ArrayList<>();
		if (mmoitem.getNBT().hasTag(getNBTPath()))
			tags.add(ItemTag.getTagAtPath(getNBTPath(), mmoitem.getNBT(), SupportedNBTTagValues.DOUBLE));

		// Build
		StatData data = getLoadedNBT(tags);

		// Valid?
		if (data != null) { mmoitem.setData(this, data); }
	}


	@Nullable
	@Override
	public StatData getLoadedNBT(@NotNull ArrayList<ItemTag> storedTags) {

		// Find
		ItemTag rTag = ItemTag.getTagAtPath(getNBTPath(), storedTags);

		// Found?
		if (rTag != null) {

			// Yes
			return new RequiredLevelData((Double) rTag.getValue());
		}

		// no
		return null;
	}
	@Override
	public boolean canUse(RPGPlayer player, NBTItem item, boolean message) {
		int level = item.getInteger(ItemStats.REQUIRED_LEVEL.getNBTPath());
		if (player.getLevel() < level && !player.getPlayer().hasPermission("mmoitems.bypass.level")) {
			if (message) {
				Message.NOT_ENOUGH_LEVELS.format(ChatColor.RED).send(player.getPlayer());
				player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1.5f);
			}
			return false;
		}
		return true;
	}

	/*
	@NotNull
	@Override
	public StatData apply(@NotNull StatData original, @NotNull UpgradeInfo info, int level) {
		//UPGRD//MMOItems. Log("\u00a7a  --> \u00a77Applying Enchants Upgrade");

		// Must be RequiredLevelData
		if (original instanceof RequiredLevelData && info instanceof DoubleUpgradeInfo) {

			// Get value
			RequiredLevelData originalLevel = ((RequiredLevelData) original);
			DoubleUpgradeInfo dui = (DoubleUpgradeInfo) info;

			// For every enchantment
			int lSimulation = level;

			// Get current level
			double value = originalLevel.getValue();
			PlusMinusPercent pmp = dui.getPMP();

			// If leveling up
			if (lSimulation > 0) {

				// While still positive
				while (lSimulation > 0) {

					// Apply PMP Operation Positively
					value = pmp.apply(value);

					// Decrease
					lSimulation--;
				}

				// Degrading the item
			} else if (lSimulation < 0) {

				// While still negative
				while (lSimulation < 0) {

					// Reverse Operation
					value = pmp.reverse(value);

					// Decrease
					lSimulation++;
				}
			}

			// Update
			originalLevel.setValue(SilentNumbers.floor(value));

			// Yes
			return originalLevel;
		}

		// Upgraded
		return original;
	}	///*/

	@Override
	public @NotNull StatData getClearStatData() {
		return new RequiredLevelData(0D);
	}
}
