package io.lumine.mythic.lib.api.item;

import java.util.Set;

public abstract class NBTCompound {
    public abstract boolean hasTag(String path);

    public abstract Object get(String path);

    public abstract String getString(String path);

    public abstract boolean getBoolean(String path);

    public abstract double getDouble(String path);

    public abstract int getInteger(String path);

    public abstract NBTCompound getNBTCompound(String path);

    public abstract Set<String> getTags();

    public abstract int getTypeId(String path);
}
