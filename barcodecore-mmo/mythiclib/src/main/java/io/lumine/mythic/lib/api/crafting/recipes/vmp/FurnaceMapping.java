package io.lumine.mythic.lib.api.crafting.recipes.vmp;

import io.lumine.mythic.lib.api.crafting.recipes.MythicRecipeStation;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * The furnace, with a slot to put your coal (or saplings).
 *
 * @author Gunging
 */
public class FurnaceMapping extends ThreeSlotMapping {
    @NotNull final static ArrayList<String> sNames = SilentNumbers.toArrayList("fuel");
    @NotNull @Override public ArrayList<String> getSideInventoryNames() { return sNames; }
    @NotNull @Override public InventoryType getIntendedInventory() { return InventoryType.FURNACE; }
    @Nullable @Override public MythicRecipeStation getIntendedStation() { return MythicRecipeStation.FURNACE; } }
