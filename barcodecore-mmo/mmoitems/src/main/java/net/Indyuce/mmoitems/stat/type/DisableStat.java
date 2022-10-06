package net.Indyuce.mmoitems.stat.type;

import org.bukkit.Material;

import io.lumine.mythic.lib.api.item.ItemTag;

public class DisableStat extends BooleanStat {
	public DisableStat(String id, Material material, String name, String... lore) {
		super("DISABLE_" + id, material, name, lore, new String[] { "all" });
	}

	public DisableStat(String id, Material material, String name, Material[] materials, String... lore) {
		super("DISABLE_" + id, material, name, lore, new String[] { "all" }, materials);
	}

	public DisableStat(String id, Material material, String name, String[] types, String... lore) {
		super("DISABLE_" + id, material, name, lore, types);
	}
}
