package net.Indyuce.mmocore.api.player.attribute;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.stat.modifier.StatModifier;
import net.Indyuce.mmocore.MMOCore;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class PlayerAttribute {
	private final String id, name;
	private final int max;

	/*
	 * used to store stats using StatType, but attributes also need to access
	 * non basic MMOCore stats hence the string maps keys
	 */
	private final Map<String, StatModifier> buffs = new HashMap<>();

	public PlayerAttribute(ConfigurationSection config) {
		Validate.notNull(config, "Could not load config");
		id = config.getName().toLowerCase().replace("_", "-").replace(" ", "-");

		name = MythicLib.plugin.parseColors(config.getString("name", "Attribute"));
		max = config.contains("max-points") ? Math.max(1, config.getInt("max-points")) : 0;

		if (config.contains("buff"))
			for (String key : config.getConfigurationSection("buff").getKeys(false))
				try {
					String stat = key.toUpperCase().replace("-", "_").replace(" ", "_");
					buffs.put(stat, new StatModifier(config.getString("buff." + key)));
				} catch (IllegalArgumentException exception) {
					MMOCore.log(Level.WARNING, "Could not load buff '" + key + "' from attribute '" + id + "': " + exception.getMessage());
				}
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean hasMax() {
		return max > 0;
	}

	public int getMax() {
		return max;
	}

	public Map<String, StatModifier> getBuffs() {
		return buffs;
	}
}
