package net.Indyuce.mmocore.api.event;

import net.Indyuce.mmocore.api.player.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * In order to create bukkit async events we must call
 * the right constructor:
 * <p>
 * {@link Event#Event(boolean)} and have the boolean set to true
 */
public abstract class AsyncPlayerDataEvent extends Event {
	private final PlayerData playerData;

	public AsyncPlayerDataEvent(PlayerData playerData) {
		super(true);

		this.playerData = playerData;
	}

	public PlayerData getData() {
		return playerData;
	}

	public Player getPlayer() {
		return playerData.getPlayer();
	}
}
