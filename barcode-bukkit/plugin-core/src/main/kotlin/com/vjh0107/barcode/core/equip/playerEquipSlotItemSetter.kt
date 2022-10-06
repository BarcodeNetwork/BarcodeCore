package com.vjh0107.barcode.core.equip

import com.vjh0107.barcode.core.equip.models.EmptyEquipSlot
import org.bukkit.entity.Player

fun playerEquipSlotItemSetter(player: Player) {
    repeat(9){ i ->
        val slot = i + 9
        if (player.inventory.getItem(slot) == null) {
            EmptyEquipSlot(slot).setEmptyEquipSlot(player)
        }
    }
}
