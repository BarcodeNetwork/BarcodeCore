package net.Indyuce.mmoitems.api.player;

import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.stat.StatInstance;
import io.lumine.mythic.lib.api.stat.StatMap;
import io.lumine.mythic.lib.api.stat.modifier.ModifierSource;
import io.lumine.mythic.lib.api.stat.modifier.ModifierType;
import io.lumine.mythic.lib.api.stat.modifier.StatModifier;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.player.inventory.EquippedPlayerItem;
import net.Indyuce.mmoitems.stat.type.AttributeStat;
import net.Indyuce.mmoitems.stat.type.ItemStat;

public class PlayerStats {
    private final PlayerData playerData;

    public PlayerStats(PlayerData playerData) {
        this.playerData = playerData;
    }

    public PlayerData getData() {
        return playerData;
    }

    public StatMap getMap() {
        return playerData.getMMOPlayerData().getStatMap();
    }

    public double getStat(ItemStat stat) {
        return getMap().getInstance(stat.getId()).getTotal();
    }

    public StatInstance getInstance(ItemStat stat) {
        return getMap().getInstance(stat.getId());
    }

    /**
     * Used to cache stats when a player casts a skill so that if the player
     * swaps items or changes any of his stat value before the end of the
     * spell duration, the stat value is not updated.
     *
     * @param castSlot Every stat modifier with the opposite modifier
     *                 source will NOT be taken into account for stat calculation
     * @return
     */
    public StatMap.CachedStatMap newTemporary(EquipmentSlot castSlot) {
        return playerData.getMMOPlayerData().getStatMap().cache(castSlot);
    }

    public void updateStats() {

        for (ItemStat stat : MMOItems.plugin.getStats().getNumericStats()) {

            // Let MMOItems first add stat modifiers, and then update the stat instance
            StatInstance.ModifierPacket packet = getInstance(stat).newPacket();

            // Remove previous potential modifiers
            packet.removeIf(name -> name.startsWith("MMOItem"));

            // Add set bonuses
            if (playerData.hasSetBonuses() && playerData.getSetBonuses().hasStat(stat))
                packet.addModifier("MMOItemSetBonus",
                        new StatModifier(playerData.getSetBonuses().getStat(stat), ModifierType.FLAT, EquipmentSlot.OTHER, ModifierSource.OTHER));

            // The index of the mmoitem stat modifier being added
            int index = 0;
            for (EquippedPlayerItem item : playerData.getInventory().getEquipped()) {
                double value = item.getItem().getNBT().getStat(stat.getId());
                if (value != 0) {
                    Type type = item.getItem().getType();
                    ModifierSource source = type == null ? ModifierSource.OTHER : type.getItemSet().getModifierSource();
                    // Apply main hand weapon stat offset ie 4 for attack speed and 1 for attack damage.
                    if (item.getSlot() == EquipmentSlot.MAIN_HAND && stat instanceof AttributeStat)
                        value -= ((AttributeStat) stat).getOffset();

                    packet.addModifier("MMOItem-" + index++, new StatModifier(value, ModifierType.FLAT, item.getSlot(), source));
                }
            }

            // Finally run a stat update after all modifiers have been gathered in the packet
            packet.runUpdate();
        }
    }
}
