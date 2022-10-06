package io.lumine.mythic.lib.api.crafting.recipes.vmp;

import io.lumine.mythic.lib.api.crafting.ingredients.MythicRecipeInventory;
import io.lumine.mythic.lib.api.crafting.recipes.MythicRecipeStation;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * The 3x3 grid of a crafting table, and its result slot.
 * <p></p>
 * Slot definitions:
 * <p></p>
 * Inventory 1 (Main)
 * <p> #1 TOP LEFT
 * </p>#2 TOP CENTER
 * <p> #3 TOP RIGHT
 * </p>#4 MID LEFT
 * <p> #5 MID CENTER
 * </p>#6 MID RIGHT
 * <p> #7 BOTTOM LEFT
 * </p>#8 BOTTOM CENTER
 * <p> #9 BOTTOM RIGHT
 * </p>
 * <p></p>
 * <p>Inventory 2 (Result)
 * </p> #0 RESULT
 *
 * @author Gunging
 */
public class WorkbenchMapping extends VanillaInventoryMapping {

    @Override
    public int getMainWidth(int slot) throws IllegalArgumentException {
        switch (slot) {
            case 1:
            case 4:
            case 7:
                return 0;
            case 2:
            case 5:
            case 8:
                return 1;
            case 3:
            case 6:
            case 9:
                return 2;
            default: break; }

        throwOutOfBounds(slot); return 0;
    }
    @Override
    public int getMainHeight(int slot) throws IllegalArgumentException {
        switch (slot) {
            case 1:
            case 2:
            case 3:
                return 0;
            case 4:
            case 5:
            case 6:
                return -1;
            case 7:
            case 8:
            case 9:
                return -2;
            default: break; }

        throwOutOfBounds(slot); return 0;
    }
    @Override
    public int getMainSlot(int width, int height) throws IllegalArgumentException {
        /*
         * Don't be thrown off by seeing negative height in these slot
         * numbers, it is kind of inverted, this is how it looks:
         *
         * [0] [0] [0]  This is height 0
         * [1] [1] [1]  This is height -1
         * [2] [2] [2]  This is height -2
         *
         * The horizontal number is the normal, intuitive though.
         * [0] [1] [2]  This is height 0
         * [0] [1] [2]  This is height -1
         * [0] [1] [2]  This is height -2
         */
        switch (height) {
            case 0:
                switch (width) {
                    case 0: return 1;
                    case 1: return 2;
                    case 2: return 3;
                    default: break; }
            case -1:
                switch (width) {
                    case 0: return 4;
                    case 1: return 5;
                    case 2: return 6;
                    default: break; }
            case -2:
                switch (width) {
                    case 0: return 7;
                    case 1: return 8;
                    case 2: return 9;
                    default: break; }
            default: break; }

        throwOutOfBounds(width, height); return 0;
    }
    @Override
    public int getMainInventoryStart() { return 1; }
    @Override
    public int getMainInventorySize() { return 9; }
    @Override
    public int getMainInventoryWidth() { return 3; }
    @Override
    public int getMainInventoryHeight() { return 3; }

    @Override
    public int getResultWidth(int slot) throws IllegalArgumentException {
        if (slot == 0) { return 0; }

        throwOutOfBounds(slot); return 0;
    }
    @Override
    public int getResultHeight(int slot) throws IllegalArgumentException {
        if (slot == 0) { return 0; }

        throwOutOfBounds(slot); return 0;
    }
    @Override
    public int getResultSlot(int width, int height) throws IllegalArgumentException {
        if (width == 0 && height == 0) { return 0; }

        throwOutOfBounds(width, height); return 0;
    }
    @Override
    public int getResultInventoryStart() { return 0; }
    @Override
    public int getResultInventorySize() { return 1; }
    @Override
    public int getResultInventoryWidth() { return 1; }
    @Override
    public int getResultInventoryHeight() { return 1; }
    @Override public boolean isResultSlot(int slot) { return slot == 0; }

    @Override
    public int getSideWidth(@NotNull String side, int slot) throws IllegalArgumentException { throwSideInventoryException(side); return 0; }
    @Override
    public int getSideHeight(@NotNull String side, int slot) throws IllegalArgumentException { throwSideInventoryException(side); return 0; }
    @Override
    public int getSideSlot(@NotNull String side, int width, int height) throws IllegalArgumentException { throwSideInventoryException(side); return 0; }
    @Override
    public int getSideInventoryStart(@NotNull String side) throws IllegalArgumentException { throwSideInventoryException(side); return 0; }
    @Override
    public int getSideInventorySize(@NotNull String side) throws IllegalArgumentException { throwSideInventoryException(side); return 0; }
    @Override
    public int getSideInventoryWidth(@NotNull String side) throws IllegalArgumentException { throwSideInventoryException(side); return 0; }
    @Override
    public int getSideInventoryHeight(@NotNull String side) throws IllegalArgumentException { throwSideInventoryException(side); return 0; }

    @Override
    public boolean applyToSideInventory(@NotNull Inventory inventory, @NotNull MythicRecipeInventory finalSide, @NotNull String sideKeyName, boolean amountOnly) { return false; }

    @Override public boolean mainIsResult() { return false; }
    @Nullable @Override public MythicRecipeStation getIntendedStation() { return MythicRecipeStation.WORKBENCH; }
    @NotNull @Override public InventoryType getIntendedInventory() { return InventoryType.WORKBENCH; }
    @NotNull @Override public ArrayList<String> getSideInventoryNames() { return sNames; }
    @NotNull final static ArrayList<String> sNames = new ArrayList<>();
}
