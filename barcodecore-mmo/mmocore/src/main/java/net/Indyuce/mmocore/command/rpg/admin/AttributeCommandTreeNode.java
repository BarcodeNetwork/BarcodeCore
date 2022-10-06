package net.Indyuce.mmocore.command.rpg.admin;

import net.Indyuce.mmocore.command.CommandVerbose;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.player.attribute.PlayerAttribute;
import net.Indyuce.mmocore.api.player.attribute.PlayerAttributes.AttributeInstance;
import io.lumine.mythic.lib.commands.mmolib.api.CommandTreeNode;
import io.lumine.mythic.lib.commands.mmolib.api.Parameter;

public class AttributeCommandTreeNode extends CommandTreeNode {
	public AttributeCommandTreeNode(CommandTreeNode parent) {
		super(parent, "attribute");

		addChild(new ActionCommandTreeNode(this, "give", false));
		addChild(new ActionCommandTreeNode(this, "take", true));
	}

	public class ActionCommandTreeNode extends CommandTreeNode {
		private final int c;

		public ActionCommandTreeNode(CommandTreeNode parent, String type, boolean take) {
			super(parent, type);

			this.c = take ? -1 : 1;

			addParameter(Parameter.PLAYER);
			addParameter(new Parameter("<attribute>",
					(explorer, list) -> MMOCore.plugin.attributeManager.getAll().forEach(attribute -> list.add(attribute.getId()))));
			addParameter(Parameter.AMOUNT);
		}

		@Override
		public CommandResult execute(CommandSender sender, String[] args) {
			if (args.length < 6)
				return CommandResult.THROW_USAGE;

			Player player = Bukkit.getPlayer(args[3]);
			if (player == null) {
				sender.sendMessage(ChatColor.RED + "Could not find the player called " + args[3] + ".");
				return CommandResult.FAILURE;
			}

			String format = args[4].toLowerCase().replace("_", "-");
			if (!MMOCore.plugin.attributeManager.has(format)) {
				sender.sendMessage(ChatColor.RED + "Could not find the attribute called " + args[4] + ".");
				return CommandResult.FAILURE;
			}
			PlayerAttribute attribute = MMOCore.plugin.attributeManager.get(format);

			int amount;
			try {
				amount = Integer.parseInt(args[5]);
			} catch (Exception e) {
				sender.sendMessage(ChatColor.RED + args[5] + " is not a valid number.");
				return CommandResult.FAILURE;
			}

			AttributeInstance instance = PlayerData.get(player).getAttributes().getInstance(attribute);
			instance.setBase(Math.min(attribute.getMax(), instance.getBase() + c * amount));
			CommandVerbose.verbose(sender, CommandVerbose.CommandType.ATTRIBUTE, ChatColor.GOLD + player.getName() + ChatColor.YELLOW
					+ " now has " + ChatColor.GOLD + instance.getBase() + ChatColor.YELLOW + " " + attribute.getName() + ".");
			return CommandResult.SUCCESS;
		}
	}

	@Override
	public CommandResult execute(CommandSender sender, String[] args) {
		return CommandResult.THROW_USAGE;
	}
}
