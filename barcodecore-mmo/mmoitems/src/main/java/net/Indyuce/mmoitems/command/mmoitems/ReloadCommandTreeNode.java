package net.Indyuce.mmoitems.command.mmoitems;

import io.lumine.mythic.lib.commands.mmolib.api.CommandTreeNode;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.util.MMOItemReforger;
import net.Indyuce.mmoitems.api.util.NumericStatFormula;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.function.Consumer;

public class ReloadCommandTreeNode extends CommandTreeNode {
	public ReloadCommandTreeNode(CommandTreeNode parent) {
		super(parent, "reload");

		addChild(new SubReloadCommandTreeNode("recipes", this, this::reloadRecipes));
		addChild(new SubReloadCommandTreeNode("stations", this, this::reloadStations));
		addChild(new SubReloadCommandTreeNode("all", this, (sender) -> {
			reloadMain(sender);
			reloadRecipes(sender);
			reloadStations(sender);
		}));
	}

	@Override
	public CommandResult execute(CommandSender sender, String[] args) {
		reloadMain(sender);
		return CommandResult.SUCCESS;
	}

	public static class SubReloadCommandTreeNode extends CommandTreeNode {
		private final Consumer<CommandSender> action;
		
		public SubReloadCommandTreeNode(String sub, CommandTreeNode parent, Consumer<CommandSender> action) {
			super(parent, sub);
			this.action = action;
		}

		@Override
		public CommandResult execute(CommandSender sender, String[] args) {
			action.accept(sender);
			return CommandResult.SUCCESS;
		}
	}

	public void reloadMain(CommandSender sender) {
		MMOItems.plugin.getEquipListener().reload();
		MMOItems.plugin.getLanguage().reload();
		MMOItems.plugin.getDropTables().reload();
		MMOItems.plugin.getTypes().reload();
		MMOItems.plugin.getTiers().reload();
		MMOItems.plugin.getSets().reload();
		MMOItems.plugin.getUpgrades().reload();
		MMOItems.plugin.getWorldGen().reload();
		MMOItems.plugin.getCustomBlocks().reload();
		MMOItems.plugin.getLayouts().reload();
		MMOItems.plugin.getFormats().reload();
		MMOItems.plugin.getTemplates().reload();
		sender.sendMessage(MMOItems.plugin.getPrefix() + MMOItems.plugin.getName() + " "
				+ MMOItems.plugin.getDescription().getVersion() + " reloaded.");
		sender.sendMessage(MMOItems.plugin.getPrefix() + "- " + ChatColor.RED
				+ MMOItems.plugin.getTypes().getAll().size() + ChatColor.GRAY + " Item Types");
		sender.sendMessage(MMOItems.plugin.getPrefix() + "- " + ChatColor.RED
				+ MMOItems.plugin.getTiers().getAll().size() + ChatColor.GRAY + " Item Tiers");
		sender.sendMessage(MMOItems.plugin.getPrefix() + "- " + ChatColor.RED
				+ MMOItems.plugin.getSets().getAll().size() + ChatColor.GRAY + " Item Sets");
		sender.sendMessage(MMOItems.plugin.getPrefix() + "- " + ChatColor.RED
				+ MMOItems.plugin.getUpgrades().getAll().size() + ChatColor.GRAY + " Upgrade Templates");
		sender.sendMessage(MMOItems.plugin.getPrefix() + "- " + ChatColor.RED
				+ MMOItems.plugin.getAbilities().getAll().size() + ChatColor.GRAY + " BarcodeNetwork Abilities");


		// *This one is not implementing Reloadable
		NumericStatFormula.reload();
		MMOItemReforger.reload();

		MMOItems.plugin.getAbilities().reload();
	}

	public void reloadRecipes(CommandSender sender) {
		MMOItems.plugin.getRecipes().reload();
		sender.sendMessage(MMOItems.plugin.getPrefix() + "Successfully reloaded recipes.");
		sender.sendMessage(MMOItems.plugin.getPrefix() + "- " + ChatColor.RED
						+ (MMOItems.plugin.getRecipes().getLoadedLegacyRecipes().size()
						+ MMOItems.plugin.getRecipes().getLegacyCustomRecipes().size()
						+ MMOItems.plugin.getRecipes().getBooklessRecipes().size()
						+ MMOItems.plugin.getRecipes().getCustomRecipes().size())
						+ ChatColor.GRAY + " Recipes");
	}

	public void reloadStations(CommandSender sender) {
		MMOItems.plugin.getLayouts().reload();
		MMOItems.plugin.getCrafting().reload();
		sender.sendMessage(MMOItems.plugin.getPrefix() + "Successfully reloaded the crafting stations..");
		sender.sendMessage(MMOItems.plugin.getPrefix() + "- " + ChatColor.RED
				+ MMOItems.plugin.getCrafting().getAll().size() + ChatColor.GRAY + " Crafting Stations");
		sender.sendMessage(MMOItems.plugin.getPrefix() + "- " + ChatColor.RED
				+ MMOItems.plugin.getCrafting().countRecipes() + ChatColor.GRAY + " Recipes");
	}
}
