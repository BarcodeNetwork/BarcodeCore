package net.Indyuce.mmoitems.listener;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.block.CustomBlock;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.item.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;

public class CustomBlockListener implements Listener {

	private static final Random RANDOM = new Random();

	public CustomBlockListener() {
		if (MMOItems.plugin.getLanguage().replaceMushroomDrops)
			Bukkit.getPluginManager().registerEvents(new MushroomReplacer(), MMOItems.plugin);
	}

	@EventHandler(ignoreCancelled = true)
	public void a(BlockPhysicsEvent event) {
		if (MMOItems.plugin.getCustomBlocks().isMushroomBlock(event.getChangedType())) {
			event.setCancelled(true);
			event.getBlock().getState().update(true, false);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void b(BlockBreakEvent event) {
		Optional<CustomBlock> opt = MMOItems.plugin.getCustomBlocks().getFromBlock(event.getBlock().getBlockData());
		if (!opt.isPresent()) {
			return;
		}

		CustomBlock block = opt.get();
		
		final int power = CustomBlockListener.getPickaxePower(event.getPlayer());
		
		if(block.requirePowerToBreak() && power < block.getRequiredPower()) {
		    event.setCancelled(true);
		    return;
		}
		
		event.setDropItems(false);
		event.setExpToDrop(event.getPlayer().getGameMode() == GameMode.CREATIVE ? 0
				: CustomBlockListener.getPickaxePower(event.getPlayer()) >= block.getRequiredPower()
						? block.getMaxExpDrop() == 0 && block.getMinExpDrop() == 0 ? 0
								: RANDOM.nextInt((block.getMaxExpDrop() - block.getMinExpDrop()) + 1) + block.getMinExpDrop()
						: 0);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void c(BlockPlaceEvent event) {
		if (!event.isCancelled() && !isMushroomBlock(event.getBlockPlaced().getType())) {
			NBTItem nbtItem = MythicLib.plugin.getVersion().getWrapper().getNBTItem(event.getItemInHand());
			int blockId = nbtItem.getInteger("MMOITEMS_BLOCK_ID");
			if (blockId > 160 || blockId < 1 || blockId == 54) // checks if block is a custom block
				return;
			if (MMOItems.plugin.getCustomBlocks().getBlock(blockId) == null) {
				MMOItems.plugin.getLogger().log(Level.SEVERE, "Could not load custom block '" + blockId + "': " + " Block is not registered.");
				MMOItems.plugin.getLogger().log(Level.SEVERE, "Try reloading the plugin to solve the issue.");
				event.setCancelled(true);
				return;
			}

			CustomBlock block = MMOItems.plugin.getCustomBlocks().getBlock(blockId); // stores the custom block
			Block newBlock = event.getBlockPlaced();
			newBlock.setType(block.getState().getType(), false);
			newBlock.setBlockData(block.getState().getBlockData(), false);

			BlockPlaceEvent bpe = new BlockPlaceEvent(newBlock, newBlock.getState(), event.getBlockAgainst(), event.getItemInHand(), event.getPlayer(), true,
					EquipmentSlot.HAND);
			Bukkit.getServer().getPluginManager().callEvent(bpe);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void d(BlockIgniteEvent event) {
		if (event.getCause() == IgniteCause.LAVA || event.getCause() == IgniteCause.SPREAD) {
			BlockFace[] faces = { BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST };
			for (BlockFace face : faces)
				if (MMOItems.plugin.getCustomBlocks().getFromBlock(event.getBlock().getRelative(face).getBlockData()).isPresent())
					event.setCancelled(true);
		}
	}

	public static int getPickaxePower(Player player) {
		ItemStack item = player.getInventory().getItemInMainHand();
		if (item != null && item.getType() != Material.AIR) {
			NBTItem nbt = NBTItem.get(item);
			if (nbt.hasType())
				return nbt.getInteger("MMOITEMS_PICKAXE_POWER");
			else {
				switch(item.getType().name()) {
					case "WOODEN_PICKAXE":
					case "WOOD_PICKAXE":
						return 5;
					case "STONE_PICKAXE":
						return 10;
					case "GOLDEN_PICKAXE":
					case "GOLD_PICKAXE":
						return 15;
					case "IRON_PICKAXE":
						return 20;
					case "DIAMOND_PICKAXE":
						return 25;
					case "NETHERITE_PICKAXE":
						return 30;
					default:
						return 0;
				}
			}
		}

		return 0;
	}

	private boolean isMushroomBlock(Material material) {
		return (material == Material.BROWN_MUSHROOM_BLOCK || material == Material.MUSHROOM_STEM || material == Material.RED_MUSHROOM_BLOCK);
	}

	public static class MushroomReplacer implements Listener {
		@EventHandler(ignoreCancelled = true)
		public void d(BlockBreakEvent event) {
			if (MMOItems.plugin.getCustomBlocks().isMushroomBlock(event.getBlock().getType())
					&& MMOItems.plugin.getDropTables().hasSilkTouchTool(event.getPlayer()))
				event.setDropItems(false);
		}
	}
}
