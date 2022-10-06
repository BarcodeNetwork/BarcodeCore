package net.Indyuce.mmocore.comp.placeholder;

import org.bukkit.OfflinePlayer;

import io.lumine.mythic.lib.MythicLib;

public class DefaultParser implements PlaceholderParser {

	@Override
	public String parse(OfflinePlayer player, String string) {
		return MythicLib.plugin.parseColors(string.replace("%player%", player.getName()));
	}
}
