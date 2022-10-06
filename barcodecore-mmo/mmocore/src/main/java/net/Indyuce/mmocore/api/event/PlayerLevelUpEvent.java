package net.Indyuce.mmocore.api.event;

import org.bukkit.event.HandlerList;

import net.Indyuce.mmocore.experience.Profession;
import net.Indyuce.mmocore.api.player.PlayerData;

public class PlayerLevelUpEvent extends PlayerDataEvent {
	private static final HandlerList handlers = new HandlerList();

	// if null, this is main level
	private final Profession profession;
	private final int oldLevel, newLevel;

	public PlayerLevelUpEvent(PlayerData player, int oldLevel, int newLevel) {
		this(player, null, oldLevel, newLevel);
	}

	public PlayerLevelUpEvent(PlayerData player, Profession profession, int oldLevel, int newLevel) {
		super(player);

		this.profession = profession;
		this.oldLevel = oldLevel;
		this.newLevel = newLevel;
	}

	public int getNewLevel() {
		return newLevel;
	}

	public int getOldLevel() {
		return oldLevel;
	}

	public boolean hasProfession() {
		return profession != null;
	}

	public Profession getProfession() {
		return profession;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
