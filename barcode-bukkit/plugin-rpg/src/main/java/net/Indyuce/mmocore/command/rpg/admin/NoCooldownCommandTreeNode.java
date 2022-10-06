package net.Indyuce.mmocore.command.rpg.admin;

import net.Indyuce.mmocore.command.CommandVerbose;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.Indyuce.mmocore.api.player.PlayerData;
import io.lumine.mythic.lib.commands.mmolib.api.CommandTreeNode;
import io.lumine.mythic.lib.commands.mmolib.api.Parameter;

public class NoCooldownCommandTreeNode extends CommandTreeNode {
	public NoCooldownCommandTreeNode(CommandTreeNode parent) {
		super(parent, "nocd");

		addParameter(Parameter.PLAYER);
	}

	@Override
	public CommandResult execute(CommandSender sender, String[] args) {
		if (args.length < 3)
			return CommandResult.THROW_USAGE;

		Player player = Bukkit.getPlayer(args[2]);
		if (player == null) {
			sender.sendMessage(ChatColor.RED + "Could not find the player called " + args[2] + ".");
			return CommandResult.FAILURE;
		}

		PlayerData data = PlayerData.get(player);
		data.noCooldown = !data.noCooldown;
		CommandVerbose.verbose(sender, CommandVerbose.CommandType.NOCD,
			ChatColor.YELLOW + "NoCD " + (data.noCooldown ? "enabled" : "disabled") + " for " + player.getName() + ".");
		return CommandResult.SUCCESS;
	}
}
