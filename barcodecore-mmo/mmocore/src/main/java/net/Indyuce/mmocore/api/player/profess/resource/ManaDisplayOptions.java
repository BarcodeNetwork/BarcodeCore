package net.Indyuce.mmocore.api.player.profess.resource;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.util.AltChar;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;


public class ManaDisplayOptions {
	private final ChatColor full, half, empty;
	private final String name, icon;
	private final char barCharacter;

	public static final ManaDisplayOptions DEFAULT = new ManaDisplayOptions(ChatColor.AQUA, ChatColor.BLUE, ChatColor.WHITE, "Mana",
			AltChar.listSquare.charAt(0), ChatColor.BLUE + AltChar.star2);

	public ManaDisplayOptions(ConfigurationSection config) {
		Validate.notNull(config, "Could not load mana display options");

		name = config.getString("name");
		Validate.notNull(name, "Could not load mana name");

		Validate.notNull(config.getConfigurationSection("color"), "Could not find mana color config");
		full = ChatColor.valueOf(config.getString("color.full", "NO_INPUT"));
		half = ChatColor.valueOf(config.getString("color.half", "NO_INPUT"));
		empty = ChatColor.valueOf(config.getString("color.empty", "NO_INPUT"));

		String format = config.getString("char", "");
		Validate.notEmpty(format, "Could not load mana bar character");
		barCharacter = format.charAt(0);

		icon = MythicLib.plugin.parseColors(config.getString("icon", ""));
		Validate.notEmpty(format, "Could not load mana action bar icon");
	}

	public ManaDisplayOptions(ChatColor full, ChatColor half, ChatColor empty, String name, char barCharacter, String icon) {
		Validate.notNull(full, "Color cannot be null");
		Validate.notNull(half, "Color cannot be null");
		Validate.notNull(empty, "Color cannot be null");
		Validate.notNull(name, "Name cannot be null");
		Validate.notNull(barCharacter, "Bar character cannot be null");

		this.full = full;
		this.half = half;
		this.empty = empty;
		this.name = name;
		this.barCharacter = barCharacter;
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	/*
	 * used to display mana on the action bar
	 */
	public String getIcon() {
		return icon;
	}

	public String generateBar(double mana, double max) {
		StringBuilder format = new StringBuilder();
		double ratio = 20 * mana / max;

		for (double j = 1; j < 20; j++)
			format.append(ratio >= j ? full : ratio >= j - .5 ? half : empty).append(barCharacter);

		return format.toString();
	}
}
