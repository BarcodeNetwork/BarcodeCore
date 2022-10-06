package io.lumine.mythic.lib.api.crafting.recipes.vmp;

import io.lumine.mythic.lib.api.crafting.recipes.MythicRecipeStation;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

/**
 * How to tell when an inventory view is of the custom crafting station?
 *
 * Well you tell me!
 *
 * @author Gunging
 */
public interface CustomInventoryCheck {

    /**
     * Is this inventory view the one that shows a player
     * the custom crafting station we're dealing with?
     *
     * @param view A view guaranteed to be of {@link InventoryType#CHEST}
     *
     * @return If this view shall be used with this Mapping.
     */
    boolean IsTargetInventory(@NotNull InventoryView view);

    /**
     * When registering recipes, they specify which station
     * they are intended for ~ Because its CUSTOM, you may
     * specify the kind of your station here.
     *              <br><br>
     * The relevant example is {@link MythicRecipeStation#WORKBENCH},
     * it matches inventory types {@link InventoryType#WORKBENCH} and
     * {@link InventoryType#PLAYER}.        <br>
     *
     * This allows the super workbench to easily detect these recipes.
     *
     * @return The custom kind of your station.
     */
    @NotNull String getCustomStationKey();
}
