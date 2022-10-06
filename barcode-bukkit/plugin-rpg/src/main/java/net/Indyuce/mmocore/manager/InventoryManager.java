package net.Indyuce.mmocore.manager;

import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.ConfigFile;
import net.Indyuce.mmocore.gui.*;
import net.Indyuce.mmocore.gui.api.EditableInventory;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class InventoryManager {
	public static final PlayerStats PLAYER_STATS = new PlayerStats();
	public static final SkillList SKILL_LIST = new SkillList();
	public static final AttributeView ATTRIBUTE_VIEW = new AttributeView();

	public static final List<EditableInventory> list = Arrays.asList(
			PLAYER_STATS,
			ATTRIBUTE_VIEW,
			SKILL_LIST
	);

	public static void load() {
		list.forEach(inv -> {
			MMOCore.plugin.configManager.loadDefaultFile("gui", inv.getId() + ".yml");
			try {
				inv.reload(new ConfigFile("/gui", inv.getId()).getConfig());
			} catch (IllegalArgumentException exception) {
				MMOCore.log(Level.WARNING, "Could not load inventory " + inv.getId() + ": " + exception.getMessage());
			}
		});
	}
}
