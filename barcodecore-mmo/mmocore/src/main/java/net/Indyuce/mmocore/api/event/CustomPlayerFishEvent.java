package net.Indyuce.mmocore.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.mmocore.api.player.PlayerData;

public class CustomPlayerFishEvent extends PlayerDataEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	private ItemStack caught;

	private boolean cancelled = false;

	public CustomPlayerFishEvent(PlayerData player, ItemStack caught) {
		super(player);

		this.caught = caught;
	}

	public ItemStack getCaught() {
		return caught;
	}

	public void setCaught(ItemStack caught) {
		this.caught = caught;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean value) {
		cancelled = value;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
