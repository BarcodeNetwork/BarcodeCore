package io.lumine.mythic.lib.api.stat.handler;

import io.lumine.mythic.lib.api.stat.StatMap;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;

/**
 * Used to handle complex stat behaviours, including updates
 * ran whenever a player stat changes or stat base values.
 *
 * @author indyuce
 */
public interface StatHandler {

    /**
     * Update ran whenever a player equips an item, or something happens that makes
     * the player's stat value changes. This is important eg for attribute based stats
     * like Max Health, because the player's spigot Max Health attribute must be updated.
     *
     * @param map Player that needs updating
     */
    public void runUpdate(StatMap map);

    /**
     * This is an import class for vanilla attribute based statistics like Max Health.
     * MythicLib can't use 20 as base stat value because this can be edited by other
     * plugins. It must retrieve the actual player's base attribute value
     *
     * @param map Player to get base stat value from
     * @return The player's base stat value
     */
    public double getBaseValue(StatMap map);

    /**
     * Used by attribute based stats like Max Healh or Attack Damage. Clears
     * attribute modifiers due to MythicLib ie modifiers which names start
     * with "mmolib." or "mythiclib." or "mmoitems."
     *
     * @param ins The attribute instance to clean from indesired modifiers
     */
    public default void removeModifiers(AttributeInstance ins) {
        for (AttributeModifier attribute : ins.getModifiers())

        /**
         * mmoitems. is not used as an attribute modifier name prefix
         * anymore but old modifiers still have it so we need to clear these
         */
            if (attribute.getName().startsWith("mmolib.") || attribute.getName().startsWith("mmoitems.") || attribute.getName().startsWith("mythiclib."))
                ins.removeModifier(attribute);
    }
}
