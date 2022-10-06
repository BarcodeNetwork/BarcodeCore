package net.Indyuce.mmocore.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import net.Indyuce.mmocore.api.event.MMOCommandEvent;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.manager.InventoryManager;

public class PlayerStatsCommand extends BukkitCommand {
	public PlayerStatsCommand(ConfigurationSection config) {
		super(config.getString("main"));
		
		setAliases(config.getStringList("aliases"));
		setDescription("Show player stats.");
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command is for players only.");
			return true;
		}
		
		PlayerData data = PlayerData.get((Player) sender);
		MMOCommandEvent event = new MMOCommandEvent(data, "profile");
		Bukkit.getServer().getPluginManager().callEvent(event);
		if(!event.isCancelled()) InventoryManager.PLAYER_STATS.newInventory(data).open();
		return true;
	}
}
