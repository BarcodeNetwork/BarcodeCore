package net.Indyuce.mmocore.command.rpg.booster;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;

import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.ConfigMessage;
import net.Indyuce.mmocore.experience.Booster;
import net.Indyuce.mmocore.experience.Profession;
import net.Indyuce.mmocore.command.MMOCoreCommandTreeRoot;
import io.lumine.mythic.lib.commands.mmolib.api.CommandTreeNode;
import io.lumine.mythic.lib.commands.mmolib.api.Parameter;

public class CreateCommandTreeNode extends CommandTreeNode {
	public CreateCommandTreeNode(CommandTreeNode parent) {
		super(parent, "create");

		addParameter(MMOCoreCommandTreeRoot.PROFESSION);
		addParameter(new Parameter("<extra>",
				(explorer, list) -> list.addAll(Arrays.asList("0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1"))));
		addParameter(new Parameter("<length>", (explorer, list) -> list.addAll(Arrays.asList("60", "300", "3600", "43200", "86400"))));
		addParameter(Parameter.PLAYER_OPTIONAL);
	}

	@Override
	public CommandResult execute(CommandSender sender, String[] args) {
		if (args.length < 5)
			return CommandResult.THROW_USAGE;

		double extra;
		try {
			extra = Double.parseDouble(args[3]);
		} catch (NumberFormatException exception) {
			sender.sendMessage(ChatColor.RED + args[3] + " is not a valid number.");
			return CommandResult.FAILURE;
		}

		long length;
		try {
			length = Long.parseLong(args[4]);
		} catch (NumberFormatException exception) {
			sender.sendMessage(ChatColor.RED + args[4] + " is not a valid number.");
			return CommandResult.FAILURE;
		}

		if (args[2].equalsIgnoreCase("main")) {
			MMOCore.plugin.boosterManager.register(new Booster(args.length > 5 ? args[5] : null, extra, length));
			new ConfigMessage("booster-main").addPlaceholders("multiplier", "" + (1 + extra)).send(Bukkit.getOnlinePlayers());
			Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1));
			return CommandResult.SUCCESS;
		}

		String format = args[2].toLowerCase().replace("_", "-");
		if (!MMOCore.plugin.professionManager.has(format)) {
			sender.sendMessage(ChatColor.RED + format + " is not a valid profession.");
			return CommandResult.FAILURE;
		}

		Profession profession = MMOCore.plugin.professionManager.get(format);
		MMOCore.plugin.boosterManager.register(new Booster(args.length > 5 ? args[5] : null, profession, extra, length));
		new ConfigMessage("booster-skill").addPlaceholders("multiplier", "" + (1 + extra), "profession", profession.getName())
				.send(Bukkit.getOnlinePlayers());
		Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1));
		return CommandResult.SUCCESS;
	}
}
