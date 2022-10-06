package net.Indyuce.mmocore.gui.api.item;

import net.Indyuce.mmocore.gui.api.GeneratedInventory;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class SimplePlaceholderItem<T extends GeneratedInventory> extends InventoryItem<T> {
    public SimplePlaceholderItem(ConfigurationSection config) {
        super(config);
    }

    public SimplePlaceholderItem(Material material, ConfigurationSection config) {
        super(material, config);
    }

    @Override
    public Placeholders getPlaceholders(T inv, int n) {
        return new Placeholders();
    }
}
