package net.Indyuce.mmocore.command.rpg.admin;

import java.util.function.BiConsumer;
import java.util.function.Function;

import net.Indyuce.mmocore.command.CommandVerbose;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.Indyuce.mmocore.api.player.PlayerData;
import io.lumine.mythic.lib.commands.mmolib.api.CommandTreeNode;
import io.lumine.mythic.lib.commands.mmolib.api.Parameter;

public class PointsCommandTreeNode extends CommandTreeNode {
	private final String type;
	private final Function<PlayerData, Integer> get;

	public PointsCommandTreeNode(String type, CommandTreeNode parent, BiConsumer<PlayerData, Integer> set, BiConsumer<PlayerData, Integer> give,
			Function<PlayerData, Integer> get) {
		super(parent, type + "-points");

		this.type = type;
		this.get = get;

		addChild(new ActionCommandTreeNode(this, "set", set));
		addChild(new ActionCommandTreeNode(this, "give", give));
	}

	public class ActionCommandTreeNode extends CommandTreeNode {
		private final BiConsumer<PlayerData, Integer> action;

		public ActionCommandTreeNode(CommandTreeNode parent, String type, BiConsumer<PlayerData, Integer> action) {
			super(parent, type);

			this.action = action;

			addParameter(Parameter.PLAYER);
			addParameter(Parameter.AMOUNT);
		}

		@Override
		public CommandResult execute(CommandSender sender, String[] args) {
			if (args.length < 5)
				return CommandResult.THROW_USAGE;

			Player player = Bukkit.getPlayer(args[3]);
			if (player == null) {
				sender.sendMessage(ChatColor.RED + "Could not find the player called " + args[3] + ".");
				return CommandResult.FAILURE;
			}

			int amount;
			try {
				amount = Integer.parseInt(args[4]);
			} catch (Exception e) {
				sender.sendMessage(ChatColor.RED + args[4] + " is not a valid number.");
				return CommandResult.FAILURE;
			}

			PlayerData data = PlayerData.get(player);
			action.accept(data, amount);
			CommandVerbose.verbose(sender, CommandVerbose.CommandType.POINTS, ChatColor.GOLD + player.getName()
				+ ChatColor.YELLOW + " now has " + ChatColor.GOLD + get.apply(data) + ChatColor.YELLOW + " " + type + " points.");
			return CommandResult.SUCCESS;
		}
	}

	@Override
	public CommandResult execute(CommandSender sender, String[] args) {
		return CommandResult.THROW_USAGE;
	}
}
