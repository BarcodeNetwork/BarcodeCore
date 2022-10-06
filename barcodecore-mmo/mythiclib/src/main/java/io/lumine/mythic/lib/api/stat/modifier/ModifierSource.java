package io.lumine.mythic.lib.api.stat.modifier;

/**
 * Main problem solved by the modifier source is being able to calculate
 * specific statistics while ignoring other modifiers. When calculating the player's
 * attack damage when using a main hand weapon, MMOItems must completely ignore
 * attack damage given by off-hand modifiers.
 *
 * @author indyuce
 */
public enum ModifierSource {

    /**
     * Modifier given by a melee weapon. These modifiers should only
     * be taken into account when the player wears the item in the main hand.
     */
    MELEE_WEAPON,

    /**
     * Modifier given by a ranged weapon. Ranged weapons are handled separately
     * in MMOItems so MythicLib must not add the corresponding attribute modifiers.
     */
    RANGED_WEAPON,

    /**
     * Modifier given by anything else
     */
    OTHER;

    public boolean isWeapon() {
        return this == MELEE_WEAPON || this == RANGED_WEAPON;
    }
}
