package net.Indyuce.mmoitems.command.mmoitems.list;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.ability.Ability;
import io.lumine.mythic.lib.commands.mmolib.api.CommandTreeNode;

public class AbilityCommandTreeNode extends CommandTreeNode {
	public AbilityCommandTreeNode(CommandTreeNode parent) {
		super(parent, "ability");
	}

	@Override
	public CommandResult execute(CommandSender sender, String[] args) {
		sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------[" + ChatColor.LIGHT_PURPLE + " Abilities "
				+ ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "]-----------------");
		sender.sendMessage(ChatColor.WHITE + "Here are all the abilities you can bind to items.");
		sender.sendMessage(ChatColor.WHITE + "The values inside brackets are " + ChatColor.UNDERLINE + "modifiers" + ChatColor.WHITE
				+ " which allow you to change the ability values (cooldown, damage...)");
		for (Ability a : MMOItems.plugin.getAbilities().getAll()) {
			String modFormat = ChatColor.GRAY + String.join(ChatColor.WHITE + ", " + ChatColor.GRAY, a.getModifiers());
			modFormat = ChatColor.WHITE + "(" + modFormat + ChatColor.WHITE + ")";
			sender.sendMessage("* " + ChatColor.LIGHT_PURPLE + a.getName() + " " + modFormat);
		}
		return CommandResult.SUCCESS;
	}
}
