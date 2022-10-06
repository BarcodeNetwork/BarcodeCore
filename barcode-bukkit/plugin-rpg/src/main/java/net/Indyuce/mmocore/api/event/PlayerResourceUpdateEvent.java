package net.Indyuce.mmocore.api.event;

import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.player.profess.resource.PlayerResource;
import net.Indyuce.mmocore.skill.list.Neptune_Gift;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerResourceUpdateEvent extends PlayerDataEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	/**
	 * Type of resource being regenerated, this way
	 * this event handles all four resources.
	 */
	private final PlayerResource resource;
	private final UpdateReason reason;

	/**
	 * Amount of resource regenerated. The whole point of the event is
	 * being able to modify it, for instance to apply the mana regeneration stat.
	 */
	private double amount;

	private boolean cancelled = false;

	/**
	 * Called when a player gains some resource back. This can
	 * be used to handle stats like health or mana regeneration.
	 * <p>
	 * Example use: {@link Neptune_Gift} which is a skill
	 * that temporarily increases resource regeneration for a short amount of time.
	 *
	 * @param playerData Player regenerating
	 * @param resource   Resource being increased
	 * @param amount     Amount being taken away/regenerated
	 * @param reason     The reason why this event was called
	 */
	public PlayerResourceUpdateEvent(PlayerData playerData, PlayerResource resource, double amount, UpdateReason reason) {
		super(playerData);

		this.resource = resource;
		this.amount = amount;
		this.reason = reason;
	}

	public PlayerResource getResource() {
		return resource;
	}

	public double getAmount() {
		return amount;
	}

	/**
	 * Changes the amount of resource given/taken away
	 *
	 * @param amount New amount
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public enum UpdateReason {

		/**
		 * When resource is being regenerated
		 */
		REGENERATION,

		/**
		 * When some resource is gained, or consumed by some skills
		 */
		SKILL_REGENERATION,

		/**
		 * When some resource is gained, or consumed by some skills
		 */
		SKILL_COST,

		/**
		 * When using the resource command {@link net.Indyuce.mmocore.command.rpg.admin.ResourceCommandTreeNode}
		 */
		COMMAND,

		/**
		 * Anything else
		 */
		OTHER;

		public boolean isSkill() {
			return this == SKILL_COST || this == SKILL_REGENERATION;
		}
	}
}
