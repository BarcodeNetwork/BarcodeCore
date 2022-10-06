package com.vjh0107.barcode.framework.nms.v1_19_R1.nbt

import com.vjh0107.barcode.framework.nbt.NBTCompound
import net.minecraft.nbt.CompoundTag
import org.bukkit.craftbukkit.v1_19_R1.util.CraftMagicNumbers

@Suppress("unused", "ClassName")
class NBTCompound_v1_19_R1 private constructor(private val compound: CompoundTag) : NBTCompound {
    constructor(item: NBTItem_v1_19_R1, path: String) : this(
        if (item.hasTag(path) && CraftMagicNumbers.NBT.TAG_COMPOUND == item.getTypeId(path)) {
            item.compound.getCompound(path)
        } else {
            CompoundTag()
        }
    )

    constructor(compound: NBTCompound_v1_19_R1, path: String) : this(
        if (compound.hasTag(path) && CraftMagicNumbers.NBT.TAG_COMPOUND == compound.getTypeId(path)) {
            compound.compound.getCompound(path)
        } else {
            CompoundTag()
        }
    )

    override fun hasTag(path: String): Boolean {
        return compound.contains(path)
    }

    override fun get(path: String): Any? {
        return compound.get(path)
    }

    override fun getString(path: String): String {
        return compound.getString(path)
    }

    override fun getBoolean(path: String): Boolean {
        return compound.getBoolean(path)
    }

    override fun getDouble(path: String): Double {
        return compound.getDouble(path)
    }

    override fun getInt(path: String): Int {
        return compound.getInt(path)
    }

    override fun getNBTCompound(path: String): NBTCompound {
        return NBTCompound_v1_19_R1(this, path)
    }

    override fun getTags(): Set<String> {
        return compound.allKeys
    }

    override fun getTypeId(path: String): Int {
        return compound.getTagType(path).toInt()
    }
}