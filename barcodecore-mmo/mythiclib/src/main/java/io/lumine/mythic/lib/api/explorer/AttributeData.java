package io.lumine.mythic.lib.api.explorer;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AttributeData {
    private final Material icon;
    private final String description;

    public AttributeData(Material icon, String description) {
        this.icon = icon;
        this.description = description;
    }

    public ItemStack getIcon() {
        return new ItemStack(icon);
    }

    public String getDescription() {
        return description;
    }
}