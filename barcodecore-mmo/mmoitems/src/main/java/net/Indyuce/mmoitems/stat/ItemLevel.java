package net.Indyuce.mmoitems.stat;

import io.lumine.mythic.lib.api.item.SupportedNBTTagValues;
import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder;
import net.Indyuce.mmoitems.api.item.mmoitem.ReadMMOItem;
import net.Indyuce.mmoitems.gui.edition.EditionInventory;
import net.Indyuce.mmoitems.stat.data.DoubleData;
import net.Indyuce.mmoitems.stat.data.random.RandomStatData;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import net.Indyuce.mmoitems.stat.type.InternalStat;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.version.VersionMaterial;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class  ItemLevel extends ItemStat implements InternalStat {
	public ItemLevel() {
		super("ITEM_LEVEL", VersionMaterial.EXPERIENCE_BOTTLE.toMaterial(), "Item Level", new String[] { "The item level" }, new String[] { "all" });
	}

	@Override
	public void whenApplied(@NotNull ItemStackBuilder item, @NotNull StatData data) { item.addItemTag(getAppliedNBT(data)); }

	@Nullable
	@Override
	public RandomStatData whenInitialized(Object object) {
		// not supported
		return null;
	}

	@Override
	public void whenClicked(@NotNull EditionInventory inv, @NotNull InventoryClickEvent event) {
		// not supported
	}

	@Override
	public void whenInput(@NotNull EditionInventory inv, @NotNull String message, Object... info) {
		// not supported
	}

	@Override
	public void whenDisplayed(List<String> lore, Optional<RandomStatData> statData) {
		// not supported
	}

	@NotNull
	@Override
	public ArrayList<ItemTag> getAppliedNBT(@NotNull StatData data) {

		// Array
		ArrayList<ItemTag> ret = new ArrayList<>();

		// Add
		ret.add(new ItemTag(getNBTPath(), ((DoubleData) data).getValue()));

		return ret;
	}

	@Override
	public void whenLoaded(@NotNull ReadMMOItem mmoitem) {

		// Find relevant tags
		ArrayList<ItemTag> relevantTags = new ArrayList<>();
		if (mmoitem.getNBT().hasTag(getNBTPath()))
			relevantTags.add(ItemTag.getTagAtPath(getNBTPath(), mmoitem.getNBT(), SupportedNBTTagValues.DOUBLE));

		// Build StatData
		StatData data = getLoadedNBT(relevantTags);

		if (data != null) { mmoitem.setData(this, data); }
	}

	@Nullable
	@Override
	public StatData getLoadedNBT(@NotNull ArrayList<ItemTag> storedTags) {

		// Get tag
		ItemTag lTag = ItemTag.getTagAtPath(getNBTPath(), storedTags);

		// Found?
		if (lTag != null) {

			// Thats it
			return new DoubleData((double) lTag.getValue());
		}

		// Nope
		return null;
	}

	@NotNull
	@Override
	public StatData getClearStatData() {
		return new DoubleData(0D);
	}
}
