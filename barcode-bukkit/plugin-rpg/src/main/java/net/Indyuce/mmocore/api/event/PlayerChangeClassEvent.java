package net.Indyuce.mmocore.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.player.profess.PlayerClass;

public class PlayerChangeClassEvent extends PlayerDataEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	private final PlayerClass newClass;

	private boolean cancelled = false;

	public PlayerChangeClassEvent(PlayerData player, PlayerClass newClass) {
		super(player);

		this.newClass = newClass;
	}

	public PlayerClass getNewClass() {
		return newClass;
	}

	public boolean isSubclass() {
		return getData().getProfess().getSubclasses().stream().anyMatch(sub -> sub.getProfess().equals(newClass));
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
