package net.Indyuce.mmoitems.stat;

import io.lumine.mythic.lib.api.item.SupportedNBTTagValues;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder;
import net.Indyuce.mmoitems.api.item.mmoitem.ReadMMOItem;
import net.Indyuce.mmoitems.stat.data.DoubleData;
import net.Indyuce.mmoitems.stat.data.random.RandomStatData;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import net.Indyuce.mmoitems.stat.type.DoubleStat;
import net.Indyuce.mmoitems.stat.type.GemStoneStat;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.item.ItemTag;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class CustomModelData extends DoubleStat implements GemStoneStat {
	public CustomModelData() {
		super("CUSTOM_MODEL_DATA", Material.PAINTING, "Custom Model Data", new String[] { "Your 1.14+ model data." }, new String[] { "!block", "all" });

		if (MythicLib.plugin.getVersion().isBelowOrEqual(1, 13))
			disable();
	}

	@Override
	public void whenApplied(@NotNull ItemStackBuilder item, @NotNull StatData data) {

		// Edit meta
		item.getMeta().setCustomModelData((int) ((DoubleData) data).getValue());

		// Apply Custom Model Data
		item.addItemTag(getAppliedNBT(data));
	}

	@Override
	public void whenPreviewed(@NotNull ItemStackBuilder item, @NotNull StatData currentData, @NotNull RandomStatData templateData) throws IllegalArgumentException { whenApplied(item, currentData); }

	@NotNull
	@Override public ArrayList<ItemTag> getAppliedNBT(@NotNull StatData data) {

		// Make new ArrayList
		ArrayList<ItemTag> ret = new ArrayList<>();

		// Add Integer
		ret.add(new ItemTag(getNBTPath(), (int) ((DoubleData) data).getValue()));

		// Return thay
		return ret;
	}

	@Override
	public void whenLoaded(@NotNull ReadMMOItem mmoitem) {

		// Get Relevant tags
		ArrayList<ItemTag> relevantTags = new ArrayList<>();
		if (mmoitem.getNBT().hasTag(getNBTPath()))
			relevantTags.add(ItemTag.getTagAtPath(getNBTPath(), mmoitem.getNBT(), SupportedNBTTagValues.INTEGER));

		// Attempt to build data
		StatData data = getLoadedNBT(relevantTags);

		// Success?
		if (data != null) { mmoitem.setData(this, data);}
	}

	@Nullable
	@Override
	public StatData getLoadedNBT(@NotNull ArrayList<ItemTag> storedTags) {

		// Find Tag
		ItemTag cmd = ItemTag.getTagAtPath(getNBTPath(), storedTags);

		// Found?
		if (cmd != null) {

			// Well thats it
			return new DoubleData((Integer) cmd.getValue());
		}

		return null;

	}
}
