package net.Indyuce.mmocore.manager.profession;

import net.Indyuce.mmocore.MMOCore;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;

public abstract class ExperienceSourceManager<T> implements Listener {

    /**
     * List of all active experience sources
     */
    private final Set<T> sources = new HashSet<>();

    public ExperienceSourceManager() {
        Bukkit.getPluginManager().registerEvents(this, MMOCore.plugin);
    }

    public void registerSource(T source) {
        sources.add(source);

        getSources();
    }

    public Set<T> getSources() {
        return sources;
    }
}
