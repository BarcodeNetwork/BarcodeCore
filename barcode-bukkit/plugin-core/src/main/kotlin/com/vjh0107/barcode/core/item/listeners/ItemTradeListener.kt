package com.vjh0107.barcode.core.item.listeners

import com.vjh0107.barcode.core.dependencies.mmo.mmoitems.extensions.isMMOItem
import com.vjh0107.barcode.core.dependencies.mmo.mmoitems.extensions.toLiveMMOItem
import com.vjh0107.barcode.core.item.models.NotExchangeableTypes
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import com.vjh0107.barcode.framework.utils.messagesender.sendBNWarnMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerDropItemEvent

@BarcodeComponent
class ItemTradeListener : BarcodeListener {
    @EventHandler
    fun onDrop(event: PlayerDropItemEvent) {
        val player = event.player
        val item = event.itemDrop.itemStack
        if (item.isMMOItem()) {
            val typeID = item.toLiveMMOItem().type.id
            if (NotExchangeableTypes.values().map { it.id }.contains(typeID)) {
                event.isCancelled = true
                player.sendBNWarnMessage("버리기 및 교환이 불가능한 아이템입니다.")
            }
        } else {
            event.isCancelled = true
        }
    }
}