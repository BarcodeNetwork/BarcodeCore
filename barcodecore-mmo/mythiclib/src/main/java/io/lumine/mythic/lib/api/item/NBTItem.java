package io.lumine.mythic.lib.api.item;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.utils.adventure.text.Component;

import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class NBTItem {
    protected final ItemStack item;

    public NBTItem(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    public abstract Object get(String path);

    public abstract String getString(String path);

    public abstract boolean hasTag(String path);

    public abstract boolean getBoolean(String path);

    public abstract double getDouble(String path);

    public abstract int getInteger(String path);

    public abstract NBTCompound getNBTCompound(String path);

    public abstract NBTItem addTag(List<ItemTag> tags);

    public abstract NBTItem removeTag(String... paths);

    public abstract Set<String> getTags();

    public abstract ItemStack toItem();

    public abstract int getTypeId(String path);

    public abstract Component getDisplayNameComponent();

    public abstract void setDisplayNameComponent(Component component);

    public abstract List<Component> getLoreComponents();

    public abstract void setLoreComponents(List<Component> components);

    @Deprecated
    public abstract NBTItem cancelVanillaAttributeModifiers();

    public NBTItem addTag(ItemTag... tags) {
        return addTag(Arrays.asList(tags));
    }

    public double getStat(String stat) {
        return getDouble("MMOITEMS_" + stat);
    }

    public boolean hasType() {
        return hasTag("MMOITEMS_ITEM_TYPE");
    }

    public String getType() {
        String tag = getString("MMOITEMS_ITEM_TYPE");
        return !tag.equals("") ? tag : null;
    }

    public static NBTItem get(ItemStack item) {
        return MythicLib.plugin.getVersion().getWrapper().getNBTItem(item);
    }
}

