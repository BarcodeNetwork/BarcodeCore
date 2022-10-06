package net.Indyuce.mmocore.comp.anticheat;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import me.vagdedes.spartan.api.API;
import me.vagdedes.spartan.system.Enums.HackType;

public class SpartanPlugin extends AntiCheatSupport {
	@Override
	public void disableAntiCheat(Player player, Map<CheatType, Integer> map) {
		for(Entry<CheatType, Integer> entry : map.entrySet())
			API.cancelCheck(player, fromCheatType(entry.getKey()), entry.getValue());
	}
	
	private HackType fromCheatType(CheatType ct) {
		return HackType.valueOf(ct.toSpartan());
	}
}
