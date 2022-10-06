package io.lumine.mythic.lib.api.stat.modifier;

public enum ModifierType {

    /**
     * Use this modifier type to multiply the entire stat value by X%. These
     * modifiers scale on how much stat value the player already has
     */
    RELATIVE,

    /**
     * Used to add a flat numeric value to a certain stat. Unlike relative stat
     * modifiers, the result is independent of the current player stat value
     */
    FLAT
}
