package net.Indyuce.mmocore.command.rpg.booster;

import org.bukkit.command.CommandSender;

import io.lumine.mythic.lib.commands.mmolib.api.CommandTreeNode;

public class BoosterCommandTreeNode extends CommandTreeNode {
	public BoosterCommandTreeNode(CommandTreeNode parent) {
		super(parent, "booster");

		addChild(new CreateCommandTreeNode(this));
		addChild(new ListCommandTreeNode(this));
		addChild(new RemoveCommandTreeNode(this));
	}

	@Override
	public CommandResult execute(CommandSender sender, String[] args) {
		return CommandResult.THROW_USAGE;
	}
}
