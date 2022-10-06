package net.Indyuce.mmoitems.stat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import io.lumine.mythic.lib.api.item.SupportedNBTTagValues;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.potion.PotionEffectType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.MMOUtils;
import net.Indyuce.mmoitems.api.edition.StatEdition;
import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder;
import net.Indyuce.mmoitems.api.item.mmoitem.ReadMMOItem;
import net.Indyuce.mmoitems.gui.edition.EditionInventory;
import net.Indyuce.mmoitems.stat.data.PotionEffectData;
import net.Indyuce.mmoitems.stat.data.PotionEffectListData;
import net.Indyuce.mmoitems.stat.data.random.RandomPotionEffectData;
import net.Indyuce.mmoitems.stat.data.random.RandomPotionEffectListData;
import net.Indyuce.mmoitems.stat.data.random.RandomStatData;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.util.AltChar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArrowPotionEffects extends ItemStat {
	private final DecimalFormat durationFormat = new DecimalFormat("0.#");

	public ArrowPotionEffects() {
		super("ARROW_POTION_EFFECTS", Material.TIPPED_ARROW, "Arrow Potion Effects",
				new String[] { "The effects to be applied when", "entities are shot by this bow" }, new String[] { "bow", "crossbow" });
	}

	@Override
	public RandomStatData whenInitialized(Object object) {
		Validate.isTrue(object instanceof ConfigurationSection, "Must specify a config section");
		return new RandomPotionEffectListData((ConfigurationSection) object);
	}

	@Override
	public void whenClicked(@NotNull EditionInventory inv, @NotNull InventoryClickEvent event) {
		if (event.getAction() == InventoryAction.PICKUP_ALL)
			new StatEdition(inv, ItemStats.ARROW_POTION_EFFECTS).enable("Write in the chat the potion effect you want to add.",
					ChatColor.AQUA + "Format: [POTION_EFFECT] [DURATION] [AMPLIFIER]");

		if (event.getAction() == InventoryAction.PICKUP_HALF) {
			if (inv.getEditedSection().contains("arrow-potion-effects")) {
				Set<String> set = inv.getEditedSection().getConfigurationSection("arrow-potion-effects").getKeys(false);
				String last = Arrays.asList(set.toArray(new String[0])).get(set.size() - 1);
				inv.getEditedSection().set("arrow-potion-effects." + last, null);
				if (set.size() <= 1)
					inv.getEditedSection().set("arrow-potion-effects", null);
				inv.registerTemplateEdition();
				inv.getPlayer().sendMessage(MMOItems.plugin.getPrefix() + "Successfully removed " + last.substring(0, 1).toUpperCase()
						+ last.substring(1).toLowerCase() + ChatColor.GRAY + ".");
			}
		}
	}

	@Override
	public void whenInput(@NotNull EditionInventory inv, @NotNull String message, Object... info) {
		String[] split = message.split(" ");
		Validate.isTrue(split.length == 3,
				message + " is not a valid [POTION_EFFECT] [DURATION] [AMPLIFIER]. Example: 'FAST_DIGGING 30 3' stands for Haste 3 for 30 seconds.");

		PotionEffectType effect = null;
		for (PotionEffectType effect1 : PotionEffectType.values())
			if (effect1 != null)
				if (effect1.getName().equalsIgnoreCase(split[0].replace("-", "_"))) {
					effect = effect1;
					break;
				}
		Validate.notNull(effect, split[0] + " is not a valid potion effect.");

		double duration = MMOUtils.parseDouble(split[1]);
		int amplifier = (int) MMOUtils.parseDouble(split[2]);

		ConfigurationSection section = inv.getEditedSection().createSection("arrow-potion-effects." + effect.getName());
		section.set("duration", duration);
		section.set("amplifier", amplifier); 
		inv.getEditedSection().set("arrow-potion-effects." + effect.getName(), section);
		inv.registerTemplateEdition();
		inv.getPlayer().sendMessage(MMOItems.plugin.getPrefix() + effect.getName() + " " + amplifier + " successfully added.");
	}

	@Override
	public void whenDisplayed(List<String> lore, Optional<RandomStatData> statData) {
		if (statData.isPresent()) {
			lore.add(ChatColor.GRAY + "Current Value:");
			RandomPotionEffectListData data = (RandomPotionEffectListData) statData.get();
			for (RandomPotionEffectData effect : data.getEffects())
				lore.add(ChatColor.GRAY + "* " + ChatColor.GREEN + MMOUtils.caseOnWords(effect.getType().getName().toLowerCase().replace("_", " "))
				+ ChatColor.GRAY + " Level: " + ChatColor.GREEN + effect.getAmplifier() + ChatColor.GRAY + " Duration: " + ChatColor.GREEN
				+ effect.getDuration());
		} else
			lore.add(ChatColor.GRAY + "Current Value: " + ChatColor.RED + "None");

		lore.add("");
		lore.add(ChatColor.YELLOW + AltChar.listDash + " Click to add an effect.");
		lore.add(ChatColor.YELLOW + AltChar.listDash + " Right click to remove the last effect.");
	}

	@NotNull
	@Override
	public StatData getClearStatData() {
		return new PotionEffectListData();
	}


	@Override
	public void whenApplied(@NotNull ItemStackBuilder item, @NotNull StatData data) {
		List<String> lore = new ArrayList<>();

		String permEffectFormat = ItemStat.translate("arrow-potion-effects");
		((PotionEffectListData) data).getEffects().forEach(effect -> {
			lore.add(permEffectFormat.replace("#", MMOItems.plugin.getLanguage().getPotionEffectName(effect.getType())
				+ " " + MMOUtils.intToRoman(effect.getLevel()) + "(" + durationFormat.format(effect.getDuration()) + "s)"));
		});

		item.getLore().insert("arrow-potion-effects", lore);

		// Add tags
		item.addItemTag(getAppliedNBT(data));
	}

	@NotNull
	@Override
	public ArrayList<ItemTag> getAppliedNBT(@NotNull StatData data) {

		// Build Tags list
		ArrayList<ItemTag> tags = new ArrayList<>();

		// Will make use of a JsonArray
		JsonArray array = new JsonArray();

		// For every potion effect
		((PotionEffectListData) data).getEffects().forEach(effect -> {

			// Get as Json Object
			JsonObject object = new JsonObject();
			object.addProperty("type", effect.getType().getName());
			object.addProperty("level", effect.getLevel());
			object.addProperty("duration", effect.getDuration());
			array.add(object);
		});

		// Add as tag
		tags.add(new ItemTag(getNBTPath(), array.toString()));

		// Thats it
		return tags;
	}

	@Override
	public void whenLoaded(@NotNull ReadMMOItem mmoitem) {

		// Find relevant tag
		ArrayList<ItemTag> relevantTags = new ArrayList<>();
		if (mmoitem.getNBT().hasTag(getNBTPath()))
			relevantTags.add(ItemTag.getTagAtPath(getNBTPath(), mmoitem.getNBT(), SupportedNBTTagValues.STRING));

		// Generate Data
		StatData data = getLoadedNBT(relevantTags);

		// Valid?
		if (data != null) { mmoitem.setData(this, data); }
	}

	@Nullable
	@Override
	public StatData getLoadedNBT(@NotNull ArrayList<ItemTag> storedTags) {

		// Found at path?
		ItemTag tg = ItemTag.getTagAtPath(getNBTPath(), storedTags);

		// Yes?
		if (tg != null) {

			// Fresh data
			PotionEffectListData effects = new PotionEffectListData();

			// All right, parse Json
			for(JsonElement entry : MythicLib.plugin.getJson().parse((String) tg.getValue(), JsonArray.class)) {

				// Fail if not Json Object
				if(!entry.isJsonObject()) continue;
				JsonObject object = entry.getAsJsonObject();

				// Add
				effects.add(new PotionEffectData(
						PotionEffectType.getByName(object.get("type").getAsString()),
						object.get("duration").getAsDouble(), object.get("level").getAsInt()));
			}

			// Return
			return effects;
		}

		return null;
	}
}
