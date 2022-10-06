package net.Indyuce.mmoitems.stat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.gson.JsonSyntaxException;
import io.lumine.mythic.lib.api.item.SupportedNBTTagValues;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.edition.StatEdition;
import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder;
import net.Indyuce.mmoitems.api.item.mmoitem.ReadMMOItem;
import net.Indyuce.mmoitems.gui.edition.EditionInventory;
import net.Indyuce.mmoitems.stat.data.StringListData;
import net.Indyuce.mmoitems.stat.data.random.RandomStatData;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.util.AltChar;
import io.lumine.mythic.lib.version.VersionMaterial;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CompatibleTypes extends ItemStat {
	public CompatibleTypes() {
		super("COMPATIBLE_TYPES", VersionMaterial.COMMAND_BLOCK.toMaterial(), "Compatible Types",
				new String[] { "The item types this skin is", "compatible with." }, new String[] { "skin" });
	}

	@Override
	@SuppressWarnings("unchecked")
	public StringListData whenInitialized(Object object) {
		Validate.isTrue(object instanceof List<?>, "Must specify a string list");
		return new StringListData((List<String>) object);
	}

	@Override
	public void whenClicked(@NotNull EditionInventory inv, @NotNull InventoryClickEvent event) {
		if (event.getAction() == InventoryAction.PICKUP_ALL)
			new StatEdition(inv, ItemStats.COMPATIBLE_TYPES).enable("Write in the chat the name of the type you want to add.");

		if (event.getAction() == InventoryAction.PICKUP_HALF) {
			if (inv.getEditedSection().contains("compatible-types")) {
				List<String> lore = inv.getEditedSection().getStringList("compatible-types");
				if (lore.size() < 1)
					return;

				String last = lore.get(lore.size() - 1);
				lore.remove(last);
				inv.getEditedSection().set("compatible-types", lore);
				inv.registerTemplateEdition();
				inv.getPlayer().sendMessage(MMOItems.plugin.getPrefix() + "Successfully removed '" + last + "'.");
			}
		}
	}

	@Override
	public void whenInput(@NotNull EditionInventory inv, @NotNull String message, Object... info) {
		List<String> lore = inv.getEditedSection().contains("compatible-types") ? inv.getEditedSection().getStringList("compatible-types")
				: new ArrayList<>();
		lore.add(message.toUpperCase());
		inv.getEditedSection().set("compatible-types", lore);
		inv.registerTemplateEdition();
		inv.getPlayer().sendMessage(MMOItems.plugin.getPrefix() + "Compatible Types successfully added.");
	}

	@Override
	public void whenDisplayed(List<String> lore, Optional<RandomStatData> statData) {

		if (statData.isPresent()) {
			lore.add(ChatColor.GRAY + "Current Value:");
			((StringListData) statData.get()).getList().forEach(str -> lore.add(ChatColor.GRAY + str));

		} else
			lore.add(ChatColor.GRAY + "Current Value: " + ChatColor.RED + "Compatible with any item.");

		lore.add("");
		lore.add(ChatColor.YELLOW + AltChar.listDash + " Click to add a new type.");
		lore.add(ChatColor.YELLOW + AltChar.listDash + " Right click to remove the last type.");
	}

	@NotNull
	@Override
	public StatData getClearStatData() {
		return new StringListData();
	}

	@Override
	public void whenApplied(@NotNull ItemStackBuilder item, @NotNull StatData data) {

		// Copy Array, for lore
		List<String> compatibleTypes = new ArrayList<>(((StringListData) data).getList());
		item.getLore().insert("compatible-types", compatibleTypes);

		// Add data
		item.addItemTag(getAppliedNBT(data));
	}

	@NotNull
	@Override
	public ArrayList<ItemTag> getAppliedNBT(@NotNull StatData data) {

		// Build Json Array
		JsonArray array = new JsonArray();

		// For each string in the ids of the data
		for (String sts : ((StringListData) data).getList()) { array.add(sts); }

		// Make returning array
		ArrayList<ItemTag> tags = new ArrayList<>();

		// Add Json Array
		tags.add(new ItemTag(getNBTPath(), array.toString()));

		return tags;
	}

	@Override
	public void whenLoaded(@NotNull ReadMMOItem mmoitem) {

		// FInd relvant tags
		ArrayList<ItemTag> relevantTags = new ArrayList<>();
		if (mmoitem.getNBT().hasTag(getNBTPath()))
			relevantTags.add(ItemTag.getTagAtPath(getNBTPath(), mmoitem.getNBT(), SupportedNBTTagValues.STRING));

		// Generate data
		StatData data = getLoadedNBT(relevantTags);

		if (data != null) { mmoitem.setData(this, data);}
	}

	@Nullable
	@Override
	public StatData getLoadedNBT(@NotNull ArrayList<ItemTag> storedTags) {

		// Find relevant tag
		ItemTag rTag = ItemTag.getTagAtPath(getNBTPath(), storedTags);

		// Found?
		if (rTag != null) {

			try {

				// Parse onto Json Array
				JsonArray array = new JsonParser().parse((String) rTag.getValue()).getAsJsonArray();

				// Make and return list
				return new StringListData(array);

			} catch (JsonSyntaxException |IllegalStateException exception) {
				/*
				 * OLD ITEM WHICH MUST BE UPDATED.
				 */
			}
		}

		// Nope
		return null;
	}
}
