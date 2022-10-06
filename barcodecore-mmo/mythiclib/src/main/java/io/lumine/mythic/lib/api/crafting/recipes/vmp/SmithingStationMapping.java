package io.lumine.mythic.lib.api.crafting.recipes.vmp;

import io.lumine.mythic.lib.api.crafting.recipes.MythicRecipeStation;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * The Smithing Station, with a slot for anything, a slot for an INGOT, and a result slot!
 *
 * @author Gunging
 */
public class SmithingStationMapping extends ThreeSlotMapping {
    @NotNull final static ArrayList<String> sNames = SilentNumbers.toArrayList("ingot");
    @NotNull @Override public ArrayList<String> getSideInventoryNames() { return sNames; }
    @NotNull @Override public InventoryType getIntendedInventory() { return InventoryType.valueOf("SMITHING"); }
    @Nullable @Override public MythicRecipeStation getIntendedStation() { return MythicRecipeStation.SMITHING; } }
