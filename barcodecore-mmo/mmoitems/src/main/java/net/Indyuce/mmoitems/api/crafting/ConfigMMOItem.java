package net.Indyuce.mmoitems.api.crafting;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate;
import net.Indyuce.mmoitems.api.player.RPGPlayer;
import org.jetbrains.annotations.NotNull;

public class ConfigMMOItem {
	private final MMOItemTemplate template;
	private final int amount;

	private ItemStack preview;

	public ConfigMMOItem(ConfigurationSection config) {
		Validate.notNull(config, "Could not read MMOItem config");

		Validate.isTrue(config.contains("type") && config.contains("id"), "Config must contain type and ID");
		Type type = MMOItems.plugin.getTypes().getOrThrow(config.getString("type").toUpperCase().replace("-", "_").replace(" ", "_"));
		template = MMOItems.plugin.getTemplates().getTemplateOrThrow(type, config.getString("id"));

		this.amount = Math.max(1, config.getInt("amount"));
	}

	public ConfigMMOItem(MMOItemTemplate template, int amount) {
		Validate.notNull(template, "Could not register recipe output");

		this.template = template;
		this.amount = Math.max(1, amount);
	}

	/**
	 * This actually generates the item the player wanted to craft.
	 *
	 * @param player Player to roll RNG in base of
	 *
	 * @return A freshly-crafted item to be used by the player.
	 */
	@NotNull public ItemStack generate(@NotNull RPGPlayer player) {
		//ItemStack item = template.newBuilder(player).build().newBuilder().build();
		ItemStack item = MMOItems.plugin.getItem(template.getType(), template.getId(), player.getPlayerData());
		item.setAmount(amount);
		return item;
	}

	public MMOItemTemplate getTemplate() {
		return template;
	}

	/*
	 * reduce startup calculations so that item is calculated the first time it
	 * needs to be displayed
	 */
	public ItemStack getPreview() {
		return preview == null ? (preview = template.newBuilder(0, null).build().newBuilder().build(true)).clone() : preview.clone();
	}

	public int getAmount() {
		return amount;
	}
}
