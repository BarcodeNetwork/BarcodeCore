package net.Indyuce.mmoitems.api.util;

import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.ItemTier;
import net.Indyuce.mmoitems.api.ReforgeOptions;
import net.Indyuce.mmoitems.api.event.MMOItemReforgeEvent;
import net.Indyuce.mmoitems.api.event.MMOItemReforgeFinishEvent;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate;
import net.Indyuce.mmoitems.api.player.PlayerData;
import net.Indyuce.mmoitems.api.player.RPGPlayer;
import net.Indyuce.mmoitems.stat.data.DoubleData;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import net.Indyuce.mmoitems.stat.type.StatHistory;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A class to manage modification of items with reference to what they used to be
 * (and apparently also used to automatically apply SoulBounds):
 *
 * <p><code><b>updating</b></code> refers to changing the base stats
 * of a MMOItem instance to what the template currently has, usually
 * keeping gem stones and upgrade level. This wont reroll RNG stats.</p>
 *
 * <p><code><b>reforging</b></code> same thing as updating, but rerolling
 * the RNG stats - basically transferring the data specified by the
 * {@link ReforgeOptions} into a new item of the same Type-ID</p>
 *
 * @author Gunging
 */
public class MMOItemReforger {

	/**
	 * Create this reforger to handle all operations regarding RevID
	 * increases on any ItemStack, including: 									<br>
	 *   * Should it update? 					{@link #shouldReforge(String)} 	<br>
	 *   * Make a fresh version					<br>
	 *   * Transfer stats from old to fresh 	<br>
	 *   * Build the fresh version	 			<br>
	 *
	 * @param stack The ItemStack you want to update, or at least
	 *              know if it should update due to RevID increase.
	 *              <br><br>
	 *              Gets the NBTItem from {@link NBTItem#get(ItemStack)}
	 */
	public MMOItemReforger(@NotNull ItemStack stack) {
		this.stack = stack;
		nbtItem = NBTItem.get(stack);

		Validate.isTrue(stack.getItemMeta() != null, "ItemStack has no ItemMeta, cannot be reforged.");
		meta = stack.getItemMeta();
	}
	/**
	 * Create this reforger to handle all operations regarding RevID
	 * increases on any ItemStack, including: 									<br>
	 *   * Should it update? 					{@link #shouldReforge(String)} 	<br>
	 *   * Make a fresh version					<br>
	 *   * Transfer stats from old to fresh 	<br>
	 *   * Build the fresh version	 			<br>
	 *
	 * @param nbtItem If for any reason you already generated an NBTItem,
	 *                you may pass it here I guess, the ItemStack will be
	 *                regenerated through {@link NBTItem#getItem()}
	 */
	public MMOItemReforger(@NotNull NBTItem nbtItem) {
		this.nbtItem = nbtItem;
		stack = nbtItem.getItem();

		Validate.isTrue(stack.getItemMeta() != null, "ItemStack has no ItemMeta, cannot be reforged.");
		meta = stack.getItemMeta();
	}
	/**
	 * Create this reforger to handle all operations regarding RevID
	 * increases on any ItemStack, including: 									<br>
	 *   * Should it update? 					{@link #shouldReforge(String)} 	<br>
	 *   * Make a fresh version					<br>
	 *   * Transfer stats from old to fresh 	<br>
	 *   * Build the fresh version	 			<br>
	 *
	 *
	 * @param nbtItem If for any reason you already generated an NBTItem,
	 *                you may pass it here to ease the performance of
	 *                generating it again from the ItemStack.
	 *
	 * @param stack The ItemStack you want to update, or at least
	 *              know if it should update due to RevID increase.
	 */
	public MMOItemReforger(@NotNull ItemStack stack, @NotNull NBTItem nbtItem) {
		this.stack = stack;
		this.nbtItem = nbtItem;

		Validate.isTrue(stack.getItemMeta() != null, "ItemStack has no ItemMeta, cannot be reforged.");
		meta = stack.getItemMeta();
	}

	/**
	 * The original ItemStack itself, not even a clone.
	 */
	@NotNull final ItemStack stack;
	/**
	 * @return The original ItemStack, not even a clone.
	 */
	@NotNull public ItemStack getStack() { return stack; }

	/**
	 * The original ItemStack itself, not even a clone.
	 */
	@Nullable ItemStack result;
	/**
	 * @return The original ItemStack, not even a clone.
	 */
	@Nullable public ItemStack getResult() { return result; }
	/**
	 * The original ItemStack itself, not even a clone.
	 */
	public void setResult(@Nullable ItemStack item) { result = item; }

	/**
	 * The original NBTItem information.
	 */
	@NotNull final NBTItem nbtItem;
	/**
	 * @return The original NBTItem information.
	 */
	@NotNull public NBTItem getNBTItem() { return nbtItem; }

	/**
	 * The meta of {@link #getStack()} but without
	 * that pesky {@link Nullable} annotation.
	 */
	@NotNull final ItemMeta meta;
	/**
	 * @return The meta of {@link #getStack()} but without that
	 * 		   pesky {@link Nullable} annotation.
	 */
	@NotNull public ItemMeta getMeta() { return meta; }

	/**
	 * The player to reroll modifiers based on their level
	 */
	@Nullable RPGPlayer player;
	/**
	 * @return  player The player to reroll modifiers based on their level
	 */
	@Nullable public RPGPlayer getPlayer() { return player; }
	/**
	 * @param player The player to reroll modifiers based on their level
	 */
	public void setPlayer(@Nullable RPGPlayer player) { this.player = player;}
	/**
	 * @param player The player to reroll modifiers based on their level
	 */
	public void setPlayer(@Nullable Player player) {
		if (player == null) { this.player = null; return; }

		// Get data
		this.player = PlayerData.get(player).getRPG();
	}

	/**
	 * If the item should update, this wont be null anymore.
	 *
	 * Guaranteed not-null when updating.
	 */
	@Nullable LiveMMOItem oldMMOItem;
	/**
	 * @return The MMOItem being updated. For safety, it should be cloned,
	 * 		   in case any plugin decides to make changes in it... though
	 * 		   this should be entirely for <b>reading purposes only</b>.
	 */
	@SuppressWarnings({"NullableProblems", "ConstantConditions"})
	@NotNull public LiveMMOItem getOldMMOItem() { return oldMMOItem; }

	/**
	 * The loaded template of the MMOItem in question.
	 *
	 * Guaranteed not-null when updating.
	 */
	@Nullable private MMOItemTemplate template;
	/**
	 * @return The loaded template of the MMOItem in question.
	 */
	@SuppressWarnings({"NullableProblems", "ConstantConditions"})
	@NotNull public MMOItemTemplate getTemplate() { return template; }

	/**
	 * The Updated version of the MMOItem, with
	 * its revised stats.
	 *
	 * Guaranteed not-null when updating.
	 */
	@Nullable private MMOItem freshMMOItem;
	/**
	 * @return The Updated version of the MMOItem, with
	 *         its revised stats.
	 */
	@SuppressWarnings({"NullableProblems", "ConstantConditions"})
	@NotNull public MMOItem getFreshMMOItem() { return freshMMOItem; }
	/**
	 * @param mmo The Updated version of the MMOItem, with
	 *            the revised stats.
	 */
	public void setFreshMMOItem(@NotNull MMOItem mmo) { freshMMOItem = mmo; }

	/**
	 * If it is possible to update this ItemStack via this class.
	 */
	@Nullable Boolean canUpdate;
	/**
	 * The value is stored so the operations don't have to run again on
	 * subsequent calls, which also means you have to make a new
	 * MMOItemReforger if you make changes, because it wont be read again.
	 *
	 * @return If this is a loaded MMOItem. That's all required.
	 */
	@SuppressWarnings("NestedAssignment")
	public boolean canReforge() {
		//RFG//MMOItems.log("§8Reforge §4CAN §7Can reforge? " + SilentNumbers.getItemName(getStack()));

		// Already went through these operations ~
		if (canUpdate != null) { return canUpdate; }

		// Does it not have type?
		if (!getNBTItem().hasType()) { return canUpdate = false; }

		// Ay get template
		template = MMOItems.plugin.getTemplates().getTemplate(getNBTItem());
		if (template == null) { return canUpdate = false; }

		// Success
		return canUpdate = true;
	}

	/**
	 * If it is recommended to update because the RevID value in the ItemStack is outdated.
	 */
	@Nullable Boolean shouldUpdate;
	/**
	 * The value is stored so the operations don't have to run again on
	 * subsequent calls, which also means you have to make a new
	 * MMOItemReforger if you make changes, because it wont be read again.
	 *
	 * @param reason Why the item should be updated?
	 *
	 *               Used to disable updating items in the config,
	 *               only during specific 'reasons' like the player
	 *               joining or picking the item up.
	 *
	 * @return If this is an MMOItem with an outdated RevID value.
	 */
	@SuppressWarnings("NestedAssignment")
	public boolean shouldReforge(@Nullable String reason) {
		//RFG//MMOItems.log("§8Reforge §4SHD §7Should reforge? " + SilentNumbers.getItemName(getStack()));

		// Already went through these operations ~
		if (shouldUpdate != null) { return shouldUpdate; }

		// Fist of all, can it update?
		if (!canReforge()) { return shouldUpdate = false; }

		// Its not GooP Converter's VANILLA is it?
		if ("VANILLA".equals(nbtItem.getString("MMOITEMS_ITEM_ID"))) { return false; }

		// Disabled in config?
		if (reason != null && MMOItems.plugin.getConfig().getBoolean("item-revision.disable-on." + reason)) { return shouldUpdate = false; }

		// Greater RevID in template? Go ahead, update!
		int templateRevision = getTemplate().getRevisionId();
		int mmoitemRevision = (getNBTItem().hasTag(ItemStats.REVISION_ID.getNBTPath()) ? getNBTItem().getInteger(ItemStats.REVISION_ID.getNBTPath()) : 1);
		if (templateRevision > mmoitemRevision) { return shouldUpdate = true; }

		// What about in the internal revision?
		int internalRevision = (nbtItem.hasTag(ItemStats.INTERNAL_REVISION_ID.getNBTPath()) ? nbtItem.getInteger(ItemStats.INTERNAL_REVISION_ID.getNBTPath()) : 1);
		if (MMOItems.INTERNAL_REVISION_ID > internalRevision) { return shouldUpdate = true; }

		// Actually, no need
		return shouldUpdate = false;
	}

	/**
	 * Sometimes, reforging will take away things from the item that
	 * we don't want to be destroyed forever, these items will drop
	 * back to the player at the end of the operation.
	 * <br><br>
	 * One example are gemstones, when the updated object has less
	 * gemstone capacity or different color slots, it would be sad
	 * if the current gemstones ceased to exist. Instead, when the
	 * event runs, the gemstone updater stores the gem items here
	 * so that the player gets them back at the completion of this.
	 */
	@NotNull final ArrayList<ItemStack> reforgingOutput = new ArrayList<>();
	/**
	 * Sometimes, reforging will take away things from the item that
	 * we don't want to be destroyed forever, these items will drop
	 * back to the player at the end of the operation.
	 * <br><br>
	 * One example are gemstones, when the updated object has less
	 * gemstone capacity or different color slots, it would be sad
	 * if the current gemstones ceased to exist. Instead, when the
	 * event runs, the gemstone updater stores the gem items here
	 * so that the player gets them back at the completion of this.
	 *
	 * @param item Add an item to this process.
	 */
	public void addReforgingOutput(@Nullable ItemStack item) {

		// Ew
		if (SilentNumbers.isAir(item)) { return; }
		if (!item.getType().isItem()) { return; }

		// Add that
		reforgingOutput.add(item);
	}
	/**
	 * Sometimes, reforging will take away things from the item that
	 * we don't want to be destroyed forever, these items will drop
	 * back to the player at the end of the operation.
	 * <br><br>
	 * One example are gemstones, when the updated object has less
	 * gemstone capacity or different color slots, it would be sad
	 * if the current gemstones ceased to exist. Instead, when the
	 * event runs, the gemstone updater stores the gem items here
	 * so that the player gets them back at the completion of this.
	 */
	public void clearReforgingOutput() { reforgingOutput.clear(); }
	/**
	 * Sometimes, reforging will take away things from the item that
	 * we don't want to be destroyed forever, these items will drop
	 * back to the player at the end of the operation.
	 * <br><br>
	 * One example are gemstones, when the updated object has less
	 * gemstone capacity or different color slots, it would be sad
	 * if the current gemstones ceased to exist. Instead, when the
	 * event runs, the gemstone updater stores the gem items here
	 * so that the player gets them back at the completion of this.
	 *
	 * @return All the items that will be dropped. The list itself.
	 */
	@NotNull public ArrayList<ItemStack> getReforgingOutput() { return reforgingOutput; }

	/**
	 * The item level modifying the values of RandomStatData
	 * upon creating a new MMOItem from the template.
	 */
	int generationItemLevel;
	/**
	 * @return The item level modifying the values of RandomStatData
	 * 		   upon creating a new MMOItem from the template.
	 */
	public int getGenerationItemLevel() { return generationItemLevel; }

	/**
	 * <b>Make sure to check {@link #canReforge()} because an
	 * exception will be generated if you don't!</b>
	 * <br><br>
	 * Go through all the modules and build the output item!
	 * <br><br>
	 * Subsequent calls will destroy the past result and
	 * generate a brand-new one.
	 *
	 * @param options Additional options to pass onto the modules.
	 *
	 * @return If reforged successfully. Basically <code>true</code>, unless cancelled.
	 */
	public boolean reforge(@NotNull ReforgeOptions options) {
		//RFG//MMOItems.log("§8Reforge §4RFG§7 Reforging " + SilentNumbers.getItemName(getStack()));

		// Throw fail
		if (!canReforge()) { throw new IllegalArgumentException("Unreforgable Item " + SilentNumbers.getItemName(getStack())); }

		// Prepare everything properly
		oldMMOItem = new LiveMMOItem(getNBTItem());

		// Not blacklisted right!?
		if (options.isBlacklisted(getOldMMOItem().getId())) { return false; }

		/*
		 * THis chunk will determine the level the item was, and
		 * regenerate a new one based on that level ~ the "Item Level" which
		 *
		 *
		 * which I honestly don't know how to use I've just been
		 * copying and pasting this around, leaving this code untouched
		 * since I first started polishing the RevID workings.
		 *
		 *                      - gunging
		 */
		int iLevel = MMOItemReforger.defaultItemLevel;

		// What level with the regenerated item will be hmmmm.....
		generationItemLevel =

				// No default level specified?
				(iLevel == -32767) ?

						// Does the item have level?
						(getOldMMOItem().hasData(ItemStats.ITEM_LEVEL) ? (int) ((DoubleData)getOldMMOItem().getData(ItemStats.ITEM_LEVEL)).getValue() : 0 )

						// Default level was specified, use that.
						: iLevel;


		// Identify tier.
		ItemTier tier =

				// Does the item have a tier, and it should keep it?
				(MMOItemReforger.keepTiersWhenReroll && getOldMMOItem().hasData(ItemStats.TIER)) ?

						// The tier will be the current tier
						MMOItems.plugin.getTiers().get(getOldMMOItem().getData(ItemStats.TIER).toString())

						// The item either has no tier, or shouldn't keep it. Null
						: null;

		// Build it again (Reroll RNG)
		setFreshMMOItem(getTemplate().newBuilder(generationItemLevel, tier).build());
		//RFG//MMOItems.log("§8Reforge §4RFG§7 Generated at Lvl \u00a73" + generationItemLevel);

		// Run event
		//RFG//MMOItems.log("§8Reforge §3RFG§7 Running Reforge Event");
		MMOItemReforgeEvent mmoREV = new MMOItemReforgeEvent(this, options);
		Bukkit.getPluginManager().callEvent(mmoREV);

		// Cancelled? it ends there
		if (mmoREV.isCancelled()) {
			//RFG//MMOItems.log("§8Reforge §4RFG§c Event Cancelled");
			return false; }
		//RFG//MMOItems.log("§8Reforge §2RFG§7 Running Reforge Finish");

		/*
		 * Properly recalculate all based on histories
		 */
		for (StatHistory hist : getFreshMMOItem().getStatHistories()) {

			// Recalculate that shit
			getFreshMMOItem().setData(hist.getItemStat(), hist.recalculate(getFreshMMOItem().getUpgradeLevel()));
		}
		if (getFreshMMOItem().hasUpgradeTemplate()) {

			for (ItemStat stat : getFreshMMOItem().getUpgradeTemplate().getKeys()) {

				// That stat yes
				StatHistory hist = StatHistory.from(getFreshMMOItem(), stat);

				// Recalculate that shit
				getFreshMMOItem().setData(hist.getItemStat(), hist.recalculate(getFreshMMOItem().getUpgradeLevel()));
			}
		}

		// Build ItemStack
		result = getFreshMMOItem().newBuilder().build();

		// Run another event...
		MMOItemReforgeFinishEvent mmoFIN = new MMOItemReforgeFinishEvent(result,this, options);
		Bukkit.getPluginManager().callEvent(mmoFIN);

		// Finally, the result item.
		setResult(mmoFIN.getFinishedItem());
		//RFG//MMOItems.log("§8Reforge §6RFG§7 Finished " + SilentNumbers.getItemName(getResult()));

		// That's the result
		return !mmoFIN.isCancelled();
	}

	//region Config Values
	public static int autoSoulbindLevel = 1;
	public static int defaultItemLevel = -32767;
	public static boolean keepTiersWhenReroll = true;

	public static void reload() {
		autoSoulbindLevel = MMOItems.plugin.getConfig().getInt("soulbound.auto-bind.level", 1);
		defaultItemLevel = MMOItems.plugin.getConfig().getInt("item-revision.default-item-level", -32767);
		keepTiersWhenReroll = MMOItems.plugin.getConfig().getBoolean("item-revision.keep-tiers");
	}
	//endregion

	//region Deprecated API
	@Deprecated
	public void update(@Nullable Player p, @NotNull ReforgeOptions options) {
		if (p != null) { setPlayer(p); }
		reforge(options);
	}
	@Deprecated
	public void update(@Nullable RPGPlayer player, @NotNull ReforgeOptions options) {
		if (player != null) { setPlayer(player); }
		reforge(options);
	}
	@Deprecated
	void regenerate(@Nullable RPGPlayer p) {
		if (p != null) { setPlayer(p); }
		reforge(new ReforgeOptions(false, false, false, false, false, false, false, true));
	}
	@Deprecated
	int regenerate(@Nullable RPGPlayer player, @NotNull MMOItemTemplate template) {
		if (player != null) { setPlayer(player); }
		canUpdate = true;	//If the template exists, it EXISTS!
		this.template = template;
		reforge(new ReforgeOptions(false, false, false, false, false, false, false, true));
		return 0;
	}
	@Deprecated
	public void reforge(@Nullable Player p, @NotNull ReforgeOptions options) {
		if (p != null) { setPlayer(p); }
		reforge(options);
	}
	@Deprecated
	public void reforge(@Nullable RPGPlayer player, @NotNull ReforgeOptions options) {
		if (player != null) { setPlayer(player); }
		reforge(options);
	}
	@Deprecated
	public ItemStack toStack() {
		return getResult();
	}
	@Deprecated
	public boolean hasChanges() {
		return getResult() != null;
	}
	@Deprecated
	@NotNull public ArrayList<MMOItem> getDestroyedGems() { return new ArrayList<>(); }

	//endregion
}
