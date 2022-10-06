package com.vjh0107.barcode.core.itembox.service.impl

import com.vjh0107.barcode.core.itembox.data.ItemBoxItem
import com.vjh0107.barcode.core.itembox.repository.ItemBoxPlayerDataRepository
import com.vjh0107.barcode.core.itembox.service.ItemBoxService
import com.vjh0107.barcode.framework.utils.formatters.toBarcodeFormat
import com.vjh0107.barcode.framework.utils.item.getDisplayName
import com.vjh0107.barcode.framework.utils.item.giveItem
import com.vjh0107.barcode.framework.utils.messagesender.sendBNWarnMessage
import org.bukkit.entity.Player
import org.koin.core.annotation.Single
import java.time.LocalDateTime

@Single(binds = [ItemBoxService::class])
class ItemBoxServiceImpl(private val repository: ItemBoxPlayerDataRepository) : ItemBoxService {
    override fun getItems(player: Player): List<ItemBoxItem> {
        return repository.getPlayerData(player).items
    }

    override fun addItem(player: Player, item: ItemBoxItem) {
        repository.getPlayerData(player).items.add(item)
    }

    override fun getExpiredItems(player: Player): List<ItemBoxItem> {
        return repository.getPlayerData(player).items.filter { isExpiredItem(it) }
    }

    override fun getItem(player: Player, predicate: (ItemBoxItem) -> Boolean): ItemBoxItem? {
        return repository.getPlayerData(player).items.firstOrNull {
            predicate(it)
        }
    }

    override fun receiveItem(player: Player, predicate: (ItemBoxItem) -> Boolean): Boolean {
        val item = getItem(player) { predicate(it) }?.item?.clone() ?: return false
        removeItem(player) { predicate(it) }
        player.giveItem(item)
        return true
    }

    override fun removeExpiredItems(player: Player): Boolean {
        val expiredItems = getExpiredItems(player)
        if (expiredItems.isNotEmpty()) {
            repository.getPlayerData(player).items.removeAll(expiredItems)
            val formattedItems = expiredItems
                .map { it.item.getDisplayName() ?: it.item.type.name }
                .toBarcodeFormat(2, "및 <remain>개")
            player.sendBNWarnMessage("${formattedItems}의 기간이 만료된 아이템이 우편함에서 파기되었습니다.")
        }
        return expiredItems.isNotEmpty()
    }

    override fun removeItem(player: Player, predicate: (ItemBoxItem) -> Boolean) {
        repository.getPlayerData(player).items.removeIf { predicate(it) }
    }

    companion object {
        private fun isExpiredItem(itemBoxItem: ItemBoxItem): Boolean {
            return itemBoxItem.expiryDate.isBefore(LocalDateTime.now())
        }
    }
}