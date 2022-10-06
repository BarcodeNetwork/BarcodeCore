package net.Indyuce.mmocore.command.rpg.debug;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.player.stats.StatType;
import io.lumine.mythic.lib.commands.mmolib.api.CommandTreeNode;
import io.lumine.mythic.lib.commands.mmolib.api.Parameter;

public class StatValueCommandTreeNode extends CommandTreeNode {
	public StatValueCommandTreeNode(CommandTreeNode parent) {
		super(parent, "statvalue");

		addParameter(new Parameter("<stat>", (explorer, list) -> {
			for (StatType stat : StatType.values())
				list.add(stat.name());
		}));
		addParameter(new Parameter("(formatted)", (explorer, list) -> list.add("true")));
	}

	@Override
	public CommandResult execute(CommandSender sender, String[] args) {
		if (args.length < 3)
			return CommandResult.THROW_USAGE;

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command can only be used by a player.");
			return CommandResult.FAILURE;
		}
		PlayerData data = PlayerData.get((Player) sender);

		StatType stat;
		try {
			stat = StatType.valueOf(args[2].toUpperCase().replace("-", "_").replace(" ", "_"));
		} catch (IllegalArgumentException exception) {
			sender.sendMessage(ChatColor.RED + "Could not find stat: " + args[2] + ".");
			return CommandResult.FAILURE;
		}

		if (args.length > 3 && args[3].equals("true"))
			sender.sendMessage(DebugCommandTreeNode.commandPrefix + "Stat Value (" + ChatColor.BLUE + stat.name() + ChatColor.WHITE + "): "
					+ ChatColor.GREEN + stat.format(data.getStats().getStat(stat)) + ChatColor.WHITE + " *");
		else
			sender.sendMessage(DebugCommandTreeNode.commandPrefix + "Stat Value (" + ChatColor.BLUE + stat.name() + ChatColor.WHITE + "): "
					+ ChatColor.GREEN + data.getStats().getStat(stat));

		return CommandResult.SUCCESS;
	}
}
