package io.lumine.mythic.lib.api.crafting.recipes.vmp;

import org.jetbrains.annotations.NotNull;

/**
 * There is a great deal of vanilla stations with 3 slots, and
 * they all happen to have the same mapping, neat!
 * <p></p>
 * <code>1 </code> Smithing Station <i>(ingot)</i><br>
 * <code>2 </code> Furnace <i>(fuel)</i><br>
 * <code>3 </code> Smoker <i>(fuel)</i><br>
 * <code>4 </code> Blast Furnace <i>(fuel)</i><br>
 * <code>5 </code> Anvil <i>(repair)</i><br>
 * <code>6 </code> Cartography Table <i>(map)</i><br>
 *
 * @author Gunging
 */
public abstract class ThreeSlotMapping extends VanillaInventoryMapping {

    @Override
    public int getMainWidth(int slot) throws IllegalArgumentException {
        if (slot == 0) { return 0; }
        throwOutOfBounds(slot); return 0;
    }
    @Override
    public int getMainHeight(int slot) throws IllegalArgumentException {
        if (slot == 0) { return 0; }
        throwOutOfBounds(slot); return 0;
    }
    @Override
    public int getMainSlot(int width, int height) throws IllegalArgumentException {
        /*
         * Only one slot, [0], is the MAIN
         */
        if (height == 0 && width == 0) { return 0; }
        throwOutOfBounds(width, height); return 0;
    }
    @Override
    public int getMainInventoryStart() { return 0; }
    @Override
    public int getMainInventorySize() { return 1; }
    @Override
    public int getMainInventoryWidth() { return 1; }
    @Override
    public int getMainInventoryHeight() { return 1; }

    @Override
    public int getResultWidth(int slot) throws IllegalArgumentException {
        if (slot == 2) { return 0; }
        throwOutOfBounds(slot); return 0;
    }
    @Override
    public int getResultHeight(int slot) throws IllegalArgumentException {
        if (slot == 2) { return 0; }
        throwOutOfBounds(slot); return 0;
    }
    @Override
    public int getResultSlot(int width, int height) throws IllegalArgumentException {
        if (width == 0 && height == 0) { return 2; }
        throwOutOfBounds(width, height); return 2;
    }
    @Override
    public int getResultInventoryStart() { return 2; }
    @Override
    public int getResultInventorySize() { return 1; }
    @Override
    public int getResultInventoryWidth() { return 1; }
    @Override
    public int getResultInventoryHeight() { return 1; }
    @Override public boolean isResultSlot(int slot) { return slot == 2; }

    @Override
    public int getSideWidth(@NotNull String side, int slot) throws IllegalArgumentException {
        considerThrowingSideException(side);
        if (slot == 1) { return 0; }
        throwOutOfBounds(slot); return 0;
    }
    @Override
    public int getSideHeight(@NotNull String side, int slot) throws IllegalArgumentException {
        considerThrowingSideException(side);
        if (slot == 1) { return 0; }
        throwOutOfBounds(slot); return 0;
    }
    @Override
    public int getSideSlot(@NotNull String side, int width, int height) throws IllegalArgumentException {
        considerThrowingSideException(side);
        if (width == 0 && height == 0) { return 1; }
        throwOutOfBounds(width, height); return 1;
    }
    @Override
    public int getSideInventoryStart(@NotNull String side) throws IllegalArgumentException { considerThrowingSideException(side); return 1; }
    @Override
    public int getSideInventorySize(@NotNull String side) throws IllegalArgumentException { considerThrowingSideException(side); return 1; }
    @Override
    public int getSideInventoryWidth(@NotNull String side) throws IllegalArgumentException { considerThrowingSideException(side); return 1; }
    @Override
    public int getSideInventoryHeight(@NotNull String side) throws IllegalArgumentException { considerThrowingSideException(side); return 1; }

    @Override public boolean mainIsResult() { return false; }
}
