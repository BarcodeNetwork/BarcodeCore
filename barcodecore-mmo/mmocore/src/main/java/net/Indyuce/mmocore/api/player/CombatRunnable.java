package net.Indyuce.mmocore.api.player;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.event.PlayerCombatEvent;

public class CombatRunnable extends BukkitRunnable {
	private final PlayerData player;

	private long lastHit = System.currentTimeMillis();

	public CombatRunnable(PlayerData player) {
		this.player = player;

		if (player.isOnline()) {
			MMOCore.plugin.configManager.getSimpleMessage("now-in-combat").send(player.getPlayer());
			Bukkit.getPluginManager().callEvent(new PlayerCombatEvent(player, true));
			runTaskTimer(MMOCore.plugin, 20, 20);
		}
	}

	public void update() {
		lastHit = System.currentTimeMillis();
	}

	@Override
	public void run() {
		if (!player.isOnline()) {
			close();
			return;
		}
		if (lastHit + MMOCore.plugin.configManager.combatLogTimer < System.currentTimeMillis()) {
			Bukkit.getPluginManager().callEvent(new PlayerCombatEvent(player, false));
			MMOCore.plugin.configManager.getSimpleMessage("leave-combat").send(player.getPlayer());
			close();
		}
	}

	private void close() {
		player.combat = null;
		cancel();
	}
}
