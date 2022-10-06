package com.vjh0107.barcode.core.equip.compatibility

import com.vjh0107.barcode.core.equip.models.EmptyEquipSlot
import io.lumine.mythic.lib.api.player.EquipmentSlot

import net.Indyuce.mmoitems.api.player.inventory.EquippedItem
import net.Indyuce.mmoitems.comp.inventory.PlayerInventory
import org.bukkit.Bukkit.getLogger
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import java.util.ArrayList


@Suppress("NAME_SHADOWING")
class MMOItemsCompatibility: PlayerInventory, Listener {
    override fun getInventory(player: Player?): MutableList<EquippedItem> {
        if (player == null) getLogger().severe("플레이어를 찾을 수 없습니다.")
        val list: MutableList<EquippedItem> = ArrayList()
        repeat(9) { i ->
            val item = player!!.inventory.getItem(i + 9)
            if (item == null) EmptyEquipSlot(i + 9).setEmptyEquipSlot(player)
            list.add(EquippedItem(item, EquipmentSlot.ACCESSORY))
        }

        return list
    }
}