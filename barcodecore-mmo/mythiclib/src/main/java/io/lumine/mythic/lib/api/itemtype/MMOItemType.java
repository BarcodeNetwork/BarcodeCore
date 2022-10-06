package io.lumine.mythic.lib.api.itemtype;

import io.lumine.mythic.lib.api.item.NBTItem;
import org.bukkit.inventory.ItemStack;

public class MMOItemType extends ItemType {
    private final String type, id;

    public MMOItemType(String type, String id) {
        this.type = type;
        this.id = id;
    }

    @Override
    public boolean matches(ItemStack stack) {
        NBTItem nbt = NBTItem.get(stack);
        return nbt.getString("MMOITEMS_ITEM_TYPE").equalsIgnoreCase(type)
                && nbt.getString("MMOITEMS_ITEM_ID").equalsIgnoreCase(id);
    }

    @Override
    public String display() {
        return type + "." + id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof MMOItemType))
            return false;
        MMOItemType other = (MMOItemType) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (type == null) {
            return other.type == null;
        } else return type.equals(other.type);
    }
}

