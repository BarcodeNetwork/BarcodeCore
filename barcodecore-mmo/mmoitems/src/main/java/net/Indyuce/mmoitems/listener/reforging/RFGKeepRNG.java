package net.Indyuce.mmoitems.listener.reforging;

import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.api.event.MMOItemReforgeEvent;
import net.Indyuce.mmoitems.stat.data.random.RandomStatData;
import net.Indyuce.mmoitems.stat.data.random.UpdatableRandomStatData;
import net.Indyuce.mmoitems.stat.data.type.Mergeable;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import net.Indyuce.mmoitems.stat.type.StatHistory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Prevent previous RNG rolls of base stats
 * of items from being lost when reforging
 *
 * @author Gunging
 */
public class RFGKeepRNG implements Listener {

    @EventHandler
    public void onReforge(MMOItemReforgeEvent event) {

        // Rerolling stats? Nevermind
        if (event.getOptions().shouldReroll()) {
            //RFG// MMOItems.log("§8Reforge §4EFG§7 Keeping new item (Complete RNG Reroll)");
            return; }

        //RFG// MMOItems.log("§8Reforge §4EFG§7 Keeping old RNG Rolls");

        /*
         * Proceed to go through all stats
         */
        for (ItemStat stat : event.getOldMMOItem().getStats()) {

            // Skip if it cant merge
            if (!(stat.getClearStatData() instanceof Mergeable)) {
                //RFG// MMOItems.log("§8Reforge §3RNG§7 Stat\u00a7f " + stat.getId() + "\u00a77 is \u00a7cnot\u00a77 even mergeable");
                continue; }

            /*
             * These stats are exempt from this 'keeping' operation.
             * Probably because there is a ReforgeOption specifically
             * designed for them that keeps them separately
             */
            if (ItemStats.LORE.equals(stat) ||
                    ItemStats.NAME.equals(stat) ||
                    ItemStats.UPGRADE.equals(stat) ||
                    ItemStats.ENCHANTS.equals(stat) ||
                    ItemStats.SOULBOUND.equals(stat) ||
                    ItemStats.GEM_SOCKETS.equals(stat)) {
                //RFG// MMOItems.log("§8Reforge §3RNG§7 Stat\u00a7f " + stat.getId() + "\u00a77 is \u00a7cnot\u00a77 processed here");
                continue; }

            //RFG// MMOItems.log("§8Reforge §3RNG§7 Stat\u00a7f " + stat.getId() + "\u00a77 being \u00a7bprocessed\u00a77...");

            // Stat history in the old item
            StatHistory hist = StatHistory.from(event.getOldMMOItem(), stat);

            // Alr what the template say, this roll too rare to be kept?
            RandomStatData source = event.getReforger().getTemplate().getBaseItemData().get(stat);

            /*
             * Decide if this data is too far from RNG to
             * preserve its rolls, even if it should be
             * preserving the rolls.
             */
            StatData keptData = shouldRerollRegardless(stat, source, hist.getOriginalData(), event.getReforger().getGenerationItemLevel());

            // Old roll is ridiculously low probability under the new parameters. Forget.
            if (keptData == null) { continue; }

            // Fetch History from the new item
            StatHistory clear = StatHistory.from(event.getNewMMOItem(), stat);

            // Replace original data of the new one with the roll from the old one
            clear.setOriginalData(keptData);
        }
    }

    /**
     * @return The item is supposedly being updated, but that doesnt mean all its stats must remain the same.
     *
     * 		   In contrast to reforging, in which it is expected its RNG to be rerolled, updating should not do it
     * 		   except in the most dire scenarios:
     * 		   <br><br>
     * 		    + The mean/standard deviation changing significantly:
     * 		    	If the chance of getting the same roll is ridiculously low (3.5SD) under the new settings, reroll.
     *         <br><br>
     * 		    + The stat is no longer there: Mean and SD become zero, so the rule above always removes the old roll.
     *         <br><br>
     * 		    + There is a new stat: The original data is null so this method cannot be called, will roll the
     * 		        new stat to actually add it for the first time.
     *
     *
     */
    @Nullable StatData shouldRerollRegardless(@NotNull ItemStat stat, @NotNull RandomStatData source, @NotNull StatData original, int determinedItemLevel) {

        // Not Mergeable, impossible to keep
        if (!(source instanceof UpdatableRandomStatData)) {
            //RFG// MMOItems.log("§8Reforge §3RNG§7 Stat\u00a7f " + stat.getId() + "\u00a77 is not updatable!");
            return null; }

        // Just pass on
        return ((UpdatableRandomStatData) source).reroll(stat, original, determinedItemLevel);
    }

}
