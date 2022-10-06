package com.vjh0107.barcode.core.itembox.listener

import com.vjh0107.barcode.core.itembox.data.ItemBoxItem
import com.vjh0107.barcode.core.itembox.service.ItemBoxService
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import com.vjh0107.barcode.framework.database.player.events.BarcodePlayerDataLoadEvent
import com.vjh0107.barcode.framework.utils.item.events.PlayerItemNotReceivedEvent
import com.vjh0107.barcode.framework.utils.messagesender.sendBNWarnMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import java.time.LocalDateTime

@BarcodeComponent
class ItemBoxListener(private val service: ItemBoxService) : BarcodeListener {
    @EventHandler(priority = EventPriority.LOW)
    fun onNotReceived(event: PlayerItemNotReceivedEvent) {
        val player = event.player
        player.sendBNWarnMessage("인벤토리가 부족하여 아이템이 우편함으로 발송되었습니다. 7일 내로 수령해주세요.")

        event.items.forEach { (_, item) ->
            val now = LocalDateTime.now()
            val itemBoxItem = ItemBoxItem.of(item, now, now.plusDays(7), "인벤토리 부족")
            service.addItem(player, itemBoxItem)
        }
        event.items.clear()
    }

    @EventHandler
    fun onPlayerDataLoad(event: BarcodePlayerDataLoadEvent) {
        service.removeExpiredItems(event.player)
    }
}