package com.vjh0107.barcode.framework.nms.v1_19_R1.nbt

import com.vjh0107.barcode.framework.nbt.data.ItemTag
import com.vjh0107.barcode.framework.nbt.NBTCompound
import com.vjh0107.barcode.framework.nbt.NBTItem
import com.vjh0107.barcode.framework.utils.uncheckedNonnullCast
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.StringTag
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack
import org.bukkit.craftbukkit.v1_19_R1.util.CraftMagicNumbers
import org.bukkit.inventory.ItemStack

@Suppress("unused", "ClassName")
class NBTItem_v1_19_R1(item: ItemStack) : NBTItem(item) {
    private val nmsItemStack: net.minecraft.world.item.ItemStack = CraftItemStack.asNMSCopy(item)
    internal val compound: CompoundTag = if (nmsItemStack.hasTag()) {
        nmsItemStack.tag!!
    } else {
        CompoundTag()
    }

    override fun getNBTCompound(path: String): NBTCompound {
        return NBTCompound_v1_19_R1(this, path)
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

    override fun hasTag(path: String): Boolean {
        return compound.contains(path)
    }

    @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
    override fun addTag(tags: List<ItemTag>): NBTItem {
        tags.forEach { tag ->
            when (tag.value) {
                is Boolean -> compound.putBoolean(tag.path, tag.value.uncheckedNonnullCast())
                is Double -> compound.putDouble(tag.path, tag.value.uncheckedNonnullCast())
                is String -> compound.putString(tag.path, tag.value.uncheckedNonnullCast())
                is Int -> compound.putInt(tag.path, tag.value.uncheckedNonnullCast())
                is List<*> -> {
                    val listTag = ListTag()
                    tag.value.uncheckedNonnullCast<List<*>>().forEach { element ->
                        if (element is String) {
                            listTag.add(StringTag.valueOf(element))
                        }
                    }
                    compound.put(tag.path, listTag)
                }
            }
        }
        return this
    }

    override fun removeTag(vararg paths: String): NBTItem {
        paths.forEach { path ->
            compound.remove(path)
        }
        return this
    }

    override val tags: Set<String> get() {
        return compound.allKeys
    }

    override fun toItem(): ItemStack {
        nmsItemStack.tag = compound
        return CraftItemStack.asBukkitCopy(nmsItemStack)
    }

    override fun getTypeId(path: String): Int {
        return compound.getTagType(path).toInt()
    }

    override var displayNameComponent: Component
        get() {
            if (compound.getCompound("display").contains("name")) {
                val displayName = compound.getCompound("display").getString("Name")
                return GsonComponentSerializer.gson().deserialize(displayName)
            }
            return Component.empty()
        }
        set(component) {
            val serializedComponent = GsonComponentSerializer.gson().serialize(component)
            compound.getCompound("display").putString("Name", serializedComponent)
        }

    override var loreComponents: List<Component>
        get() {
            val lore = mutableListOf<Component>()
            if (compound.getCompound("display").contains("Lore")) {
                val strings: ListTag = compound.getCompound("display").getList("Lore", CraftMagicNumbers.NBT.TAG_STRING)
                strings.forEachIndexed { index, _ ->
                    lore.add(GsonComponentSerializer.gson().deserialize(strings.getString(index)))
                }
            }
            return lore
        }
        set(components) {
            val lore = ListTag()
            if (components.isNotEmpty()) {
                components.forEach { component ->
                    val serializedComponent = GsonComponentSerializer.gson().serialize(component)
                    lore.add(StringTag.valueOf(serializedComponent))
                }
                compound.getCompound("display").put("Lore", lore)
            } else {
                compound.getCompound("display").remove("Lore")
            }
        }
}