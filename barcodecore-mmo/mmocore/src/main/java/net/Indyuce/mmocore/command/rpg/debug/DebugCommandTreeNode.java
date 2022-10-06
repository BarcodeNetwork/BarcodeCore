package net.Indyuce.mmocore.command.rpg.debug;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import io.lumine.mythic.lib.commands.mmolib.api.CommandTreeNode;

public class DebugCommandTreeNode extends CommandTreeNode {
	public static final String commandPrefix = ChatColor.YELLOW + "[" + ChatColor.RED + "DEBUG" + ChatColor.GOLD + "] " + ChatColor.RESET;

	public DebugCommandTreeNode(CommandTreeNode parent) {
		super(parent, "debug");

		addChild(new StatValueCommandTreeNode(this));
		addChild(new StatModifiersCommandTreeNode(this));
	}

	@Override
	public CommandResult execute(CommandSender sender, String[] args) {
		return CommandResult.THROW_USAGE;
	}
}
