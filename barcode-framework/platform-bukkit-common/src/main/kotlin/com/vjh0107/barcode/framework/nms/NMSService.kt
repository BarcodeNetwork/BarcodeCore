package com.vjh0107.barcode.framework.nms

import com.vjh0107.barcode.framework.nbt.NBTItem
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface NMSService {
    /**
     * 엔티티에게 박힌 화살을 제거합니다.
     */
    fun removeArrows(entity: Entity)

    /**
     * 타겟 플레이어에게만 보이는 번개를 소환합니다.
     */
    fun strikeLightning(player: Player, location: Location)

    /**
     * NBT Item 을 구합니다.
     */
    fun getNBTItem(item: ItemStack): NBTItem
}