package net.Indyuce.mmoitems.command.item;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.ItemTier;
import net.Indyuce.mmoitems.api.player.PlayerData;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.commands.mmolib.api.CommandTreeNode;

public class DeconstructCommandTreeNode extends CommandTreeNode {
	public DeconstructCommandTreeNode(CommandTreeNode parent) {
		super(parent, "deconstruct");
	}

	@Override
	public CommandResult execute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command is only for players.");
			return CommandResult.FAILURE;
		}

		Player player = (Player) sender;
		ItemStack stack = player.getInventory().getItemInMainHand();
		NBTItem item = MythicLib.plugin.getVersion().getWrapper().getNBTItem(stack);
		String tag = item.getString("MMOITEMS_TIER");
		if (tag.equals("") || !item.getBoolean("MMOITEMS_CAN_DECONSTRUCT")) {
			sender.sendMessage(MMOItems.plugin.getPrefix() + "The item you are holding can't be deconstructed.");
			return CommandResult.FAILURE;
		}

		ItemTier tier = MMOItems.plugin.getTiers().get(tag);
		PlayerData data = PlayerData.get(player);
		List<ItemStack> loot = tier.getDeconstructedLoot(data);
		if (loot.isEmpty()) {
			sender.sendMessage(
					MMOItems.plugin.getPrefix() + "There we no items to be yielded from the deconstruction.");
			return CommandResult.FAILURE;
		}

		stack.setAmount(stack.getAmount() - 1);
		player.getInventory().setItemInMainHand(stack);
		for (ItemStack drop : player.getInventory().addItem(loot.toArray(new ItemStack[0])).values())
			player.getWorld().dropItem(player.getLocation(), drop);

		sender.sendMessage(MMOItems.plugin.getPrefix() + "Successfully deconstructed the item you are holding.");
		return CommandResult.SUCCESS;
	}
}
