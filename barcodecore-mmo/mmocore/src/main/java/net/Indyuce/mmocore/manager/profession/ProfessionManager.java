package net.Indyuce.mmocore.manager.profession;

import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.experience.Profession;
import net.Indyuce.mmocore.manager.MMOManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ProfessionManager implements MMOManager {

	/**
	 * Loaded professions.
	 */
	private final Map<String, Profession> professions = new HashMap<>();

	/**
	 * Saves different experience sources based on experience source type.
	 */
	private final Map<Class<?>, ExperienceSourceManager<?>> managers = new HashMap<>();

	@SuppressWarnings("unchecked")
	public <T> ExperienceSourceManager<T> getManager(Class<T> t) {
		return (ExperienceSourceManager<T>) managers.get(t);
	}

	public void register(Profession profession) {
		professions.put(profession.getId(), profession);
	}

	public Profession get(String id) {
		return professions.get(id);
	}

	public boolean has(String id) {
		return professions.containsKey(id);
	}

	public Collection<Profession> getAll() {
		return professions.values();
	}

	@Override
	public void reload() {
		for (File file : new File(MMOCore.plugin.getDataFolder() + "/professions").listFiles())
			try {
				String id = file.getName().substring(0, file.getName().length() - 4);
				register(new Profession(id, YamlConfiguration.loadConfiguration(file)));
			} catch (IllegalArgumentException exception) {
				MMOCore.plugin.getLogger().log(Level.WARNING, "Could not load profession " + file.getName() + ": " + exception.getMessage());
			}
	}

	@Override
	public void clear() {
		managers.values().forEach(HandlerList::unregisterAll);
		managers.clear();
		professions.clear();
	}
}
