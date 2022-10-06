package com.vjh0107.barcode.framework.nbt

import com.vjh0107.barcode.framework.koin.injector.inject
import com.vjh0107.barcode.framework.nbt.data.ItemTag
import com.vjh0107.barcode.framework.nms.NMSWrapper
import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack

abstract class NBTItem(val item: ItemStack) {
    /**
     * path 에 있는 NBTCompound 를 가져옵니다.
     */
    abstract fun getNBTCompound(path: String): NBTCompound

    /**
     * path 에 있는 값을 가져옵니다.
     */
    abstract operator fun get(path: String): Any?

    /**
     * path 에 있는 값을 String 으로 가져옵니다.
     */
    abstract fun getString(path: String): String

    /**
     * path 에 있는 값을 Boolean 으로 가져옵니다.
     */
    abstract fun getBoolean(path: String): Boolean

    /**
     * path 에 있는 값을 Double 으로 가져옵니다.
     */
    abstract fun getDouble(path: String): Double

    /**
     * path 에 있는 값을 Int 로 가져옵니다.
     */
    abstract fun getInt(path: String): Int

    /**
     * path 에 태그가 있는지 확인합니다.
     */
    abstract fun hasTag(path: String): Boolean

    /**
     * 태그를 추가합니다.
     */
    abstract fun addTag(tags: List<ItemTag>): NBTItem

    /**
     * 태그를 삭제합니다.
     */
    abstract fun removeTag(vararg paths: String): NBTItem

    /**
     * 태그 전체를 구합니다.
     */
    abstract val tags: Set<String>

    /**
     * Bukkit ItemStack 으로 변환합니다.
     */
    abstract fun toItem(): ItemStack

    /**
     * Type ID 를 구합니다.
     */
    abstract fun getTypeId(path: String): Int

    /**
     * DisplayName 을 구하거나 설정합니다.
     */
    abstract var displayNameComponent: Component

    /**
     * Lore 을 구하거나 설정합니다.
     */
    abstract var loreComponents: List<Component>

    /**
     * 태그를 추가합니다.
     */
    fun addTag(vararg tags: ItemTag): NBTItem {
        return addTag(listOf(*tags))
    }

    companion object {
        /**
         * NBTItem 을 가져옵니다.
         */
        fun get(item: ItemStack): NBTItem {
            val nmsWrapper: NMSWrapper by inject()
            return nmsWrapper.getNBTItem(item)
        }
    }
}

