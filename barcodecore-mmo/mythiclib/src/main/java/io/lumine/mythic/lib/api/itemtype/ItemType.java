package io.lumine.mythic.lib.api.itemtype;

import io.lumine.mythic.lib.MythicLib;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class ItemType {
    /*
     * Returns true if the given ItemStack is part of this type.
     */
    public abstract boolean matches(ItemStack stack);
    /*
     * Returns a readable string defining the item type.
     * This is mainly used for error logging
     */
    public abstract String display();


    // TODO remove and move it to an ItemTypeManager
    public static ItemType fromString(String input) {
        if(input.contains(".") || input.contains("%") || input.contains("?")) {
            String[] split = input.split("[.%?]");
            if(split.length != 2)
                MythicLib.plugin.getLogger().warning("More arguments than needed, please check your configs! ( " + input + " )");
            return new MMOItemType(split[0], split[1]);
        }
        else return new VanillaType(Material.valueOf(
                input.toUpperCase().replace("-", "_").replace(" ", "_")));
    }

    @Override
    public abstract int hashCode();
    @Override
    public abstract boolean equals(Object obj);
}
