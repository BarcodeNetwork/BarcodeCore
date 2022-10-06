package net.Indyuce.mmoitems.manager;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.ConfigFile;
import net.Indyuce.mmoitems.api.ItemSet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class SetManager implements Reloadable {
	private final Map<String, ItemSet> itemSets = new HashMap<>();

	public SetManager() {
		reload();
	}

	public void reload() {
		itemSets.clear();

		ConfigFile config = new ConfigFile("item-sets");
		for (String id : config.getConfig().getKeys(false))
			try {
				itemSets.put(id, new ItemSet(config.getConfig().getConfigurationSection(id)));
			} catch (IllegalArgumentException exception) {
				MMOItems.plugin.getLogger().log(Level.WARNING, "Could not load item set '" + id + "': " + exception.getMessage());
			}
	}

	public void register(ItemSet set) {
		itemSets.put(set.getId(), set);
	}

	public boolean has(String id) {
		return itemSets.containsKey(id);
	}

	public Collection<ItemSet> getAll() {
		return itemSets.values();
	}

	public ItemSet get(String id) {
		return itemSets.getOrDefault(id, null);
	}
}
