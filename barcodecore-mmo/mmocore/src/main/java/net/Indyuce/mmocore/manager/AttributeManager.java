package net.Indyuce.mmocore.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.ConfigFile;
import net.Indyuce.mmocore.api.player.attribute.PlayerAttribute;

public class AttributeManager implements MMOManager {
	private final Map<String, PlayerAttribute> map = new HashMap<>();

	public PlayerAttribute get(String id) {
		return map.get(id);
	}

	public boolean has(String id) {
		return map.containsKey(id);
	}

	public Collection<PlayerAttribute> getAll() {
		return map.values();
	}
 
	@Override
	public void reload() {

		ConfigFile config = new ConfigFile("attributes");
		for (String key : config.getConfig().getKeys(false))
			try {
				String path = key.toLowerCase().replace("_", "-").replace(" ", "-");
				map.put(path, new PlayerAttribute(config.getConfig().getConfigurationSection(key)));
			} catch (IllegalArgumentException exception) {
				MMOCore.log(Level.WARNING, "Could not load attribute '" + key + "': " + exception.getMessage());
			}
	}

	@Override
	public void clear() {
		map.clear();
	}
}
