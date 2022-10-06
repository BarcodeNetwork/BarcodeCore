package io.lumine.mythic.lib.api.explorer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder extends ItemStack {
    public ItemBuilder(Material material, String name) {
        super(material);

        ItemMeta meta = getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        meta.addItemFlags(ItemFlag.values());
        setItemMeta(meta);
    }

    public ItemBuilder setLore(String... lines) {
        ItemMeta meta = getItemMeta();
        List<String> lore = new ArrayList<>();
        for (String line : lines)
            lore.add(lore.size(), ChatColor.translateAlternateColorCodes('&', line));
        meta.setLore(lore);
        setItemMeta(meta);

        return this;
    }
}
