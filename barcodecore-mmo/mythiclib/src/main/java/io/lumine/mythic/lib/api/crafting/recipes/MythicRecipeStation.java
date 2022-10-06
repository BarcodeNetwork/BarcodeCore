package io.lumine.mythic.lib.api.crafting.recipes;

/**
 * When set live, which vanilla crafting stations will
 * attempt to read this recipe?
 *
 * @author Gunging
 */
public enum MythicRecipeStation {

    /**
     * Not necessarily only the workbench, will also
     * work for the 2x2 grid of the survival player.
     */
    WORKBENCH,

    /**
     * A recipe with smelting information, for a furnace.
     * Be it a smoker or a blast furnace too.
     */
    FURNACE,

    /**
     * A recipe which combines two items, adding up their NBT
     * data in a specific way.
     */
    SMITHING,

    /**
     * With data for brewing in a brewing stand
     */
    BREWING,

    /**
     * Some edited chest inventory for external plugins
     */
    CUSTOM
}
