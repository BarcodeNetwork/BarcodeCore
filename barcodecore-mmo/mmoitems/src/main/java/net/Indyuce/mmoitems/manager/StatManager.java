package net.Indyuce.mmoitems.manager;

import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.stat.type.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Level;

public class StatManager {
	private final Map<String, ItemStat> stats = new LinkedHashMap<>();

	/*
	 * These lists are sets of stats collected when the stats are registered for
	 * the first time to make their access easier. Check the classes
	 * individually to understand better
	 */
	private final Set<DoubleStat> numeric = new HashSet<>();
	private final Set<AttributeStat> attributeBased = new HashSet<>();
	private final Set<ItemRestriction> itemRestriction = new HashSet<>();
	private final Set<ConsumableItemInteraction> consumableActions = new HashSet<>();
	private final Set<PlayerConsumable> playerConsumables = new HashSet<>();

	/*
	 * load default stats using java reflection, get all public static final
	 * fields in the ItemStat and register them as stat instances
	 */
	public StatManager() {
		for (Field field : ItemStats.class.getFields())
			try {
				if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()) && field.get(null) instanceof ItemStat)
					register((ItemStat) field.get(null));
			} catch (IllegalArgumentException | IllegalAccessException exception) {
				MMOItems.plugin.getLogger().log(Level.WARNING, "Couldn't register stat called " + field.getName());
			}
	}

	public Collection<ItemStat> getAll() {
		return stats.values();
	}

	/**
	 * @return Collection of all stats which are based on vanilla player
	 * attributes like movement speed, attack damage, max health..
	 */
	public Set<AttributeStat> getAttributeStats() {
		return attributeBased;
	}

	/**
	 * @return Collection of all numeric stats like atk damage, crit strike
	 *         chance, max mana... which can be applied on a gem stone. This is
	 *         used when applying gem stones to quickly access all the stats
	 *         which needs to be applied
	 */
	public Set<DoubleStat> getNumericStats() {
		return numeric;
	}

	/**
	 * @return Collection of all stats which constitute an item restriction:
	 *         required level, required class, soulbound..
	 */
	public Set<ItemRestriction> getItemRestrictionStats() {
		return itemRestriction;
	}

	/**
	 * @return Collection of all stats implementing a consumable action like
	 *         deconstructing, identifying...
	 */
	public Set<ConsumableItemInteraction> getConsumableActions() {
		return consumableActions;
	}

	/**
	 * @return Collection of all stats implementing self consumable like
	 * restore health, mana, hunger...
	 */
	public Set<PlayerConsumable> getPlayerConsumables() {
		return playerConsumables;
	}

	public boolean has(String id) {
		return stats.containsKey(id);
	}

	public ItemStat get(String id) {
		return stats.getOrDefault(id, null);
	}

	/**
	 * Registers a stat in MMOItems
	 * 
	 * @param      id   Useless.
	 * @param      stat The stat instance
	 * @deprecated      Stat IDs are now stored in the stat instance directly.
	 *                  Please use StatManager#register(ItemStat) instead
	 */
	@Deprecated
	@SuppressWarnings("unused")
	public void register(String id, ItemStat stat) {
		register(stat);
	}

	/**
	 * Registers a stat in MMOItems. It must be done right after MMOItems loads
	 * before any manager is initialized because stats are commonly used when
	 * loading configs.
	 * 
	 * @param stat The stat to register
	 */
	public void register(ItemStat stat) {
		if (!stat.isEnabled())
			return;

		stats.put(stat.getId(), stat);

		if (stat instanceof DoubleStat && !(stat instanceof GemStoneStat) && stat.isCompatible(Type.GEM_STONE))
			numeric.add((DoubleStat) stat);

		if (stat instanceof AttributeStat)
			attributeBased.add((AttributeStat) stat);

		if (stat instanceof ItemRestriction)
			itemRestriction.add((ItemRestriction) stat);

		if (stat instanceof ConsumableItemInteraction)
			consumableActions.add((ConsumableItemInteraction) stat);

		if (stat instanceof PlayerConsumable)
			playerConsumables.add((PlayerConsumable) stat);

		/**
		 * Cache stat for every type which may have this stat. Really important
		 * otherwise the stat will NOT be used anywhere in the plugin. This
		 * process is also done in the TypeManager when registering new types
		 * but since stats can be registered after types are loaded, we must
		 * take it into account
		 */
		if (MMOItems.plugin.getTypes() != null)
			for (Type type : MMOItems.plugin.getTypes().getAll())
				if (stat.isCompatible(type))
					type.getAvailableStats().add(stat);
	}
}
