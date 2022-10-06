package net.Indyuce.mmocore.command.rpg.admin;

import net.Indyuce.mmocore.command.CommandVerbose;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.player.profess.PlayerClass;
import io.lumine.mythic.lib.commands.mmolib.api.CommandTreeNode;
import io.lumine.mythic.lib.commands.mmolib.api.Parameter;

public class ForceClassCommandTreeNode extends CommandTreeNode {
	public ForceClassCommandTreeNode(CommandTreeNode parent) {
		super(parent, "force-class");

		addParameter(Parameter.PLAYER);
		addParameter(
				new Parameter("<class>", (explorer, list) -> MMOCore.plugin.classManager.getAll().forEach(profess -> list.add(profess.getId()))));
	}

	@Override
	public CommandResult execute(CommandSender sender, String[] args) {
		if (args.length < 4)
			return CommandResult.THROW_USAGE;

		Player player = Bukkit.getPlayer(args[2]);
		if (player == null) {
			sender.sendMessage(ChatColor.RED + "Could not find the player called " + args[2] + ".");
			return CommandResult.FAILURE;
		}

		String format = args[3].toUpperCase().replace("-", "_");
		if (!MMOCore.plugin.classManager.has(format)) {
			sender.sendMessage(ChatColor.RED + "Could not find class " + format + ".");
			return CommandResult.FAILURE;
		}

		PlayerClass profess = MMOCore.plugin.classManager.get(format);

		PlayerData data = PlayerData.get(player);
		data.setClass(profess);
		CommandVerbose.verbose(sender, CommandVerbose.CommandType.CLASS, ChatColor.GOLD + player.getName()
			+ ChatColor.YELLOW + " is now a " + ChatColor.GOLD + profess.getName() + ChatColor.YELLOW + ".");
		return CommandResult.SUCCESS;
	}
}
