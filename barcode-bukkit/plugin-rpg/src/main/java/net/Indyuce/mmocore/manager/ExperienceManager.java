package net.Indyuce.mmocore.manager;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;

import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.experience.ExpCurve;

public class ExperienceManager {
	private final Map<String, ExpCurve> expCurves = new HashMap<>();

	public boolean hasCurve(String id) {
		return expCurves.containsKey(id);
	}

	public ExpCurve getOrThrow(String id) {
		Validate.isTrue(hasCurve(id), "Could not find exp curve with ID '" + id + "'");
		return expCurves.get(id);
	}

	public ExpCurve getCurve(String id) {
		return expCurves.get(id);
	}

	public Collection<ExpCurve> getCurves() {
		return expCurves.values();
	}

	public void reload() {
		expCurves.clear();
		for (File file : new File(MMOCore.plugin.getDataFolder() + "/expcurves").listFiles())
			try {
				ExpCurve curve = new ExpCurve(file);
				expCurves.put(curve.getId(), curve);
			} catch (IllegalArgumentException | IOException exception) {
				MMOCore.plugin.getLogger().log(Level.WARNING, "Could not load exp curve '" + file.getName() + "': " + exception.getMessage());
			}
	}
}
