package net.Indyuce.mmocore.command.rpg.admin;

import java.util.function.BiConsumer;

import net.Indyuce.mmocore.command.CommandVerbose;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.experience.EXPSource;
import net.Indyuce.mmocore.experience.PlayerProfessions;
import net.Indyuce.mmocore.experience.Profession;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.command.MMOCoreCommandTreeRoot;
import io.lumine.mythic.lib.commands.mmolib.api.CommandTreeNode;
import io.lumine.mythic.lib.commands.mmolib.api.Parameter;

public class LevelCommandTreeNode extends CommandTreeNode {
	public LevelCommandTreeNode(CommandTreeNode parent) {
		super(parent, "level");

		addChild(new ActionCommandTreeNode(this, "set", PlayerData::setLevel, PlayerProfessions::setLevel));
		addChild(new ActionCommandTreeNode(this, "give", (data, value) -> data.giveLevels(value, EXPSource.COMMAND), (professions, profession, value) -> professions.giveLevels(profession, value, EXPSource.COMMAND)));
		addChild(new ActionCommandTreeNode(this, "take", PlayerData::takeLevels, PlayerProfessions::takeLevels));
	}

	public static class ActionCommandTreeNode extends CommandTreeNode {
		private final BiConsumer<PlayerData, Integer> main;
		private final TriConsumer<PlayerProfessions, Profession, Integer> profession;

		public ActionCommandTreeNode(CommandTreeNode parent, String type, BiConsumer<PlayerData, Integer> main,
				TriConsumer<PlayerProfessions, Profession, Integer> profession) {
			super(parent, type);

			this.main = main;
			this.profession = profession;

			addParameter(Parameter.PLAYER);
			addParameter(MMOCoreCommandTreeRoot.PROFESSION);
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

			int amount;
			try {
				amount = Integer.parseInt(args[5]);
			} catch (NumberFormatException exception) {
				sender.sendMessage(ChatColor.RED + args[5] + " is not a valid number.");
				return CommandResult.FAILURE;
			}

			PlayerData data = PlayerData.get(player);
			if (args[4].equalsIgnoreCase("main")) {
				main.accept(data, amount);

				CommandVerbose.verbose(sender, CommandVerbose.CommandType.LEVEL, ChatColor.GOLD + player.getName()
					+ ChatColor.YELLOW + " is now Lvl " + ChatColor.GOLD + data.getLevel() + ChatColor.YELLOW + ".");
				return CommandResult.SUCCESS;
			}

			String format = args[4].toLowerCase().replace("_", "-");
			if (!MMOCore.plugin.professionManager.has(format)) {
				sender.sendMessage(ChatColor.RED + format + " is not a valid profession.");
				return CommandResult.FAILURE;
			}

			Profession profession = MMOCore.plugin.professionManager.get(format);
			this.profession.accept(data.getCollectionSkills(), profession, amount);
			CommandVerbose.verbose(sender, CommandVerbose.CommandType.LEVEL,
					ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " is now Lvl " + ChatColor.GOLD
					+ data.getCollectionSkills().getLevel(profession) + ChatColor.YELLOW + " in " + profession.getName() + ".");
			return CommandResult.SUCCESS;
		}
	}

	@FunctionalInterface
	interface TriConsumer<A, B, C> {
		void accept(A a, B b, C c);
	}

	@Override
	public CommandResult execute(CommandSender sender, String[] args) {
		return CommandResult.THROW_USAGE;
	}
}
