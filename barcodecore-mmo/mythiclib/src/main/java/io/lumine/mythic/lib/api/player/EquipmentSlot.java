package io.lumine.mythic.lib.api.player;

import org.apache.commons.lang.Validate;

/**
 * Used by MythicLib to make a difference between stat
 * modifiers granted by off hand and main hand items.
 * <p>
 * Used by MMOItems player inventory updates to differentiate where
 * items were placed in the player inventory.
 *
 * @author indyuce
 */
public enum EquipmentSlot {

    /**
     * Can only apply stats in armor slots
     */
    ARMOR,

    /**
     * Can't apply stats in vanilla slots
     */
    ACCESSORY,

    /**
     * Cannot apply its stats anywhere
     */
    OTHER,

    /**
     * Always apply its stats
     */
    ANY,

    /**
     * Apply stats in main hands only
     */
    MAIN_HAND,

    /**
     * Apply stats in off hand slot only (off hand catalysts mainly)
     */
    OFF_HAND,

    /**
     * Apply stats in both hands, ie shields or catalysts.
     */
    BOTH_HANDS;

    public EquipmentSlot getOppositeHand() {
        Validate.isTrue(this == MAIN_HAND || this == OFF_HAND, "Not a hand equipment slot");
        return this == MAIN_HAND ? OFF_HAND : MAIN_HAND;
    }

    public boolean isHand() {
        return this == MAIN_HAND || this == OFF_HAND || this == BOTH_HANDS;
    }

    public static EquipmentSlot fromBukkit(org.bukkit.inventory.EquipmentSlot slot) {
        switch (slot) {
            case HAND:
                return MAIN_HAND;
            case OFF_HAND:
                return OFF_HAND;
            case FEET:
            case HEAD:
            case LEGS:
            case CHEST:
                return ARMOR;
            default:
                return OTHER;
        }
    }
}