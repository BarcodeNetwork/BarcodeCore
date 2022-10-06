package net.Indyuce.mmocore.command.rpg.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.experience.Profession;
import net.Indyuce.mmocore.api.player.PlayerData;
import io.lumine.mythic.lib.commands.mmolib.api.CommandTreeNode;
import io.lumine.mythic.lib.commands.mmolib.api.Parameter;

public class InfoCommandTreeNode extends CommandTreeNode {
	public InfoCommandTreeNode(CommandTreeNode parent) {
		super(parent, "info");

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

		PlayerData playerData = PlayerData.get(player);
		sender.sendMessage(ChatColor.YELLOW + "----------------------------------------------------");
		sender.sendMessage(ChatColor.YELLOW + "Class: " + ChatColor.GOLD + playerData.getProfess().getName());
		sender.sendMessage(ChatColor.YELLOW + "Level: " + ChatColor.GOLD + playerData.getLevel());
		sender.sendMessage(ChatColor.YELLOW + "Experience: " + ChatColor.GOLD + playerData.getExperience() + ChatColor.YELLOW + " / " + ChatColor.GOLD
				+ playerData.getLevelUpExperience());
		sender.sendMessage(ChatColor.YELLOW + "----------------------------------------------------");
		for (Profession profession : MMOCore.plugin.professionManager.getAll())
			sender.sendMessage(
					ChatColor.YELLOW + profession.getName() + ": Lvl " + ChatColor.GOLD + playerData.getCollectionSkills().getLevel(profession)
							+ ChatColor.YELLOW + " - " + ChatColor.GOLD + playerData.getCollectionSkills().getExperience(profession)
							+ ChatColor.YELLOW + " / " + ChatColor.GOLD + playerData.getCollectionSkills().getLevelUpExperience(profession));
		sender.sendMessage(ChatColor.YELLOW + "----------------------------------------------------");
		return CommandResult.SUCCESS;
	}
}
