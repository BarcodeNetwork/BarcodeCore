package net.Indyuce.mmoitems.stat;

import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.util.AltChar;
import io.lumine.mythic.lib.api.util.EnumUtils;
import io.lumine.mythic.lib.version.VersionMaterial;
import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.MMOUtils;
import net.Indyuce.mmoitems.api.edition.StatEdition;
import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder;
import net.Indyuce.mmoitems.api.item.mmoitem.ReadMMOItem;
import net.Indyuce.mmoitems.gui.edition.EditionInventory;
import net.Indyuce.mmoitems.stat.data.MaterialData;
import net.Indyuce.mmoitems.stat.data.random.RandomStatData;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MaterialStat extends ItemStat {
	public MaterialStat() {
		super("MATERIAL", VersionMaterial.GRASS_BLOCK.toMaterial(), "Material", new String[] { "Your item material." }, new String[] { "all" });
	}

	@Override
	public MaterialData whenInitialized(Object object) {
		Validate.isTrue(object instanceof String, "Must specify material name as string");
		return new MaterialData(Material.valueOf(((String) object).toUpperCase().replace("-", "_").replace(" ", "_")));
	}

	@Override
	public void whenClicked(@NotNull EditionInventory inv, @NotNull InventoryClickEvent event) {
		new StatEdition(inv, ItemStats.MATERIAL).enable("Write in the chat the material you want.");
	}

	@Override
	public void whenInput(@NotNull EditionInventory inv, @NotNull String message, Object... info) {
		Optional<Material> material = EnumUtils.getIfPresent(Material.class, message.toUpperCase().replace("-", "_").replace(" ", "_"));
		if (material.isPresent()) {
			inv.getEditedSection().set("material", material.get().name());
			inv.registerTemplateEdition();
			inv.getPlayer().sendMessage(MMOItems.plugin.getPrefix() + "Material successfully changed to " + material.get().name() + ".");
		} else
			inv.getPlayer().spigot().sendMessage(new ComponentBuilder("Invalid material! (Click for a list of valid materials)").color(ChatColor.RED)
					.event(new ClickEvent(Action.OPEN_URL, "https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html")).create());
	}

	@Override
	public void whenApplied(@NotNull ItemStackBuilder item, @NotNull StatData data) {
		/*
		 * material is handled directly in the MMOBuilder constructor, therefore
		 * nothing needs to be done here
		 */
	}

	@Override
	public void whenLoaded(@NotNull ReadMMOItem mmoitem) {
		mmoitem.setData(this, new MaterialData(mmoitem.getNBT().getItem().getType()));
	}

	@Override
	public void whenDisplayed(List<String> lore, Optional<RandomStatData> statData) {
		lore.add(ChatColor.GRAY + "Current Value: "
				+ (statData.isPresent()
						? ChatColor.GREEN + MMOUtils.caseOnWords(((MaterialData) statData.get()).getMaterial().name().toLowerCase().replace("_", " "))
						: ChatColor.RED + "None"));

		lore.add("");
		lore.add(ChatColor.YELLOW + AltChar.listDash + " Left click to change this value.");
		lore.add(ChatColor.YELLOW + AltChar.listDash + " Right click to remove this value.");
	}

	@NotNull
	@Override
	public StatData getClearStatData() {
		return new MaterialData(Material.IRON_ORE);
	}

	/**
	 * This stat is saved not as a custom tag, but as the vanilla material itself.
	 * Alas this is an empty array
	 */
	@NotNull
	@Override
	public ArrayList<ItemTag> getAppliedNBT(@NotNull StatData data) { return new ArrayList<>(); }

	/**
	 * This stat is saved not as a custom tag, but as the vanilla material itself.
	 * Alas this method returns null.
	 */
	@Nullable
	@Override
	public StatData getLoadedNBT(@NotNull ArrayList<ItemTag> storedTags) { return null; }
}
