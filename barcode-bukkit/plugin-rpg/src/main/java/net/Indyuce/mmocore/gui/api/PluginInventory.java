package net.Indyuce.mmocore.gui.api;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import net.Indyuce.mmocore.api.player.PlayerData;

public abstract class PluginInventory implements InventoryHolder {
	protected final Player player;
	protected final PlayerData playerData;

	public PluginInventory(PlayerData playerData) {
		this.playerData = playerData;
		player = playerData.getPlayer();
	}

	public PluginInventory(Player player) {
		this.player = player;
		this.playerData = player.getOpenInventory() != null && player.getOpenInventory().getTopInventory().getHolder() instanceof PluginInventory ? ((PluginInventory) player.getOpenInventory().getTopInventory().getHolder()).playerData : PlayerData.get(player);
	}

	public PlayerData getPlayerData() {
		return playerData;
	}

	public Player getPlayer() {
		return player;
	}

	public void open() {
		getPlayer().openInventory(getInventory());
	}

	public abstract Inventory getInventory();

	public abstract void whenClicked(InventoryClickEvent event);

	public void whenClosed(InventoryCloseEvent event) {
	}
}
