package com.vjh0107.barcode.core.equip.models

import com.vjh0107.barcode.framework.utils.item.createGuiItem
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class EmptyEquipSlot(slotNum: Int) {
    var slot: Int = slotNum
    val customModelData = getEquipSlot(slot)!!.customModelData

    private fun itemImpl(): ItemStack {
        return createGuiItem(Material.IRON_SHOVEL, 1, getEquipSlot(this.slot)!!.getDisplayName(), getEquipSlot(this.slot)!!.getLore(), customModelData)
    }

    fun getEmptyEquipSlot(): ItemStack {
        return itemImpl()
    }

    fun setEmptyEquipSlot(player: Player) {
        player.inventory.setItem(slot, getEmptyEquipSlot())
    }

    fun isEquipSlotEmpty(player: Player): Boolean {
        if (player.inventory.getItem(slot) == getEmptyEquipSlot()) return true
        return false
    }
}