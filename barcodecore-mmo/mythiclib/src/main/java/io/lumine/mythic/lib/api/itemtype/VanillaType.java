package io.lumine.mythic.lib.api.itemtype;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class VanillaType extends ItemType {
    private final Material mat;

    public VanillaType(Material mat) {
        this.mat = mat;
    }

    @Override
    public boolean matches(ItemStack stack) {
        return stack.getType() == mat;
    }

    @Override
    public String display() {
        return mat.name();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mat == null) ? 0 : mat.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof VanillaType))
            return false;
        VanillaType other = (VanillaType) obj;
        return mat == other.mat;
    }
}
