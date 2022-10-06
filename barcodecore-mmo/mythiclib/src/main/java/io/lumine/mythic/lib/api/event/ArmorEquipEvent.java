package io.lumine.mythic.lib.api.event;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.function.Function;

/**
 * @author Arnah
 * @since Jul 30, 2015
 */
public final class ArmorEquipEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    // private boolean cancelled = false;
    private final ArmorType type;
    private final ItemStack oldArmor, newArmor;

    /**
     * Constructor for the ArmorEquipEvent.
     *
     * @param player
     *            The player who put on / removed the armor.
     * @param type
     *            The ArmorType of the armor added
     * @param oldArmor
     *            The ItemStack of the armor removed.
     * @param newArmor
     *            The ItemStack of the armor added.
     */
    public ArmorEquipEvent(Player player, ArmorType type, ItemStack oldArmor, ItemStack newArmor) {
        super(player);

        this.type = type;
        this.oldArmor = oldArmor;
        this.newArmor = newArmor;
    }

    /**
     * Gets a list of handlers handling this event.
     *
     * @return A list of handlers handling this event.
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets a list of handlers handling this event.
     *
     * @return A list of handlers handling this event.
     */
    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }

    public final ArmorType getType() {
        return type;
    }

    /**
     * Returns the last equipped armor piece, could be a piece of armor,
     * {@link Material#AIR}, or null.
     */
    public final ItemStack getOldArmor() {
        return oldArmor;
    }

    /**
     * Returns the newly equipped armor, could be a piece of armor,
     * {@link Material#AIR}, or null.
     */
    public final ItemStack getNewArmorPiece() {
        return newArmor;
    }

    /**
     * @author Arnah
     * @since Jul 30, 2015
     */
    public enum ArmorType {
        HELMET(5, PlayerInventory::getHelmet),
        CHESTPLATE(6, PlayerInventory::getChestplate),
        LEGGINGS(7, PlayerInventory::getLeggings),
        BOOTS(8, PlayerInventory::getBoots);

        private final int slot;
        private final Function<PlayerInventory, ItemStack> handler;

        ArmorType(int slot, Function<PlayerInventory, ItemStack> handler) {
            this.slot = slot;
            this.handler = handler;
        }

        public int getSlot() {
            return slot;
        }

        public ItemStack getItem(Player player) {
            return handler.apply(player.getInventory());
        }

        /**
         * Attempts to match the ArmorType for the specified ItemStack.
         *
         * @param item The ItemStack to parse the type of.
         * @return The parsed ArmorType. (null if none were found.)
         */
        public static ArmorType matchType(ItemStack item) {
            if (item == null || item.getType().equals(Material.AIR))
                return null;

            Material type = item.getType();
            String name = type.name();
            if (name.endsWith("HELMET") || name.endsWith("SKULL") || name.endsWith("HEAD") || type == Material.PLAYER_HEAD || type == Material.PUMPKIN)
                return HELMET;

            else if (name.endsWith("CHESTPLATE"))
                return CHESTPLATE;

            else if (name.endsWith("LEGGINGS"))
                return LEGGINGS;

            else if (name.endsWith("BOOTS"))
                return BOOTS;

            else
                return null;
        }
    }
}
