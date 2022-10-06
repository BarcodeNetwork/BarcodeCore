package com.vjh0107.barcode.core.itembox.ui.impl

import com.vjh0107.barcode.core.itembox.data.ItemBoxItem
import com.vjh0107.barcode.core.itembox.service.ItemBoxService
import com.vjh0107.barcode.core.itembox.ui.ItemBoxInventory
import com.vjh0107.barcode.framework.inventory.AbstractBarcodeInventory
import com.vjh0107.barcode.framework.inventory.BarcodeInventory
import com.vjh0107.barcode.framework.utils.formatters.colorize
import com.vjh0107.barcode.framework.utils.formatters.toBarcodeFormat
import com.vjh0107.barcode.framework.utils.item.models.BarcodeGuiItems
import com.vjh0107.barcode.framework.utils.item.setColorizedLore
import com.vjh0107.barcode.framework.utils.item.setDisplayName
import com.vjh0107.barcode.framework.utils.messagesender.sendBNMessage
import com.vjh0107.barcode.framework.utils.messagesender.sendBNWarnMessage
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam

@Factory(binds = [ItemBoxInventory::class])
class ItemBoxInventoryImpl(
    @InjectedParam override val player: Player,
    private val service: ItemBoxService
) : AbstractBarcodeInventory(), ItemBoxInventory {
    companion object {
        val itemBoxSlots = listOf(
            1, 2, 3, 4, 5, 6, 7,
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25
        ).mapIndexed { index, slot ->
            index to slot
        }
    }

    var page: Int = 1

    private fun getSlots(): List<Int> {
        return itemBoxSlots.map { it.second }
    }

    private fun ItemBoxItem.getDisplay(): ItemStack {
        val resultLore: MutableList<String> = mutableListOf()
        val item = this.item.clone()

        val lore = item.lore
        if (lore != null) {
            resultLore.addAll(lore)
        }

        resultLore.add("")
        if (this.whoSent != null) {
            resultLore.add("&7보낸 이: &e${this.whoSent}")
        } else {
            resultLore.add("&7보낸 이: &e바코드 네트워크")
        }
        if (this.reason != null) {
            resultLore.add("&7사유: &e${this.reason}")
        }
        resultLore.add("")
        resultLore.add("&7수취 날짜: &e${this.receivedDate.toBarcodeFormat()}")
        resultLore.add("&7만료 기한: &e${this.expiryDate.toBarcodeFormat()}")
        resultLore.add("")
        resultLore.add("&a클릭 시 아이템을 인벤토리로 수령합니다.")
        val resultItemMeta = item.itemMeta
        resultItemMeta.lore = resultLore.map { it.colorize() }
        item.itemMeta = resultItemMeta
        return item
    }

    private fun isNextPageExist(): Boolean {
        return service.getItems(player).size > itemBoxSlots.size * this.page
    }

    override fun getInventory(): Inventory {
        val inventory = createInventory(36, "&6우편함 &e#$page")
            .setItems(rightButton)
            .setItems(leftButton)
            .setItems(empty)

        val items = service.getItems(player)

        val repeatTimes = if (items.size >= itemBoxSlots.size * this.page) {
            itemBoxSlots.size
        } else {
            items.size - (this.page - 1) * itemBoxSlots.size
        }

        repeat(repeatTimes) { index ->
            val slot = itemBoxSlots[index].second
            val item = items[index + (this.page - 1) * itemBoxSlots.size]
            inventory.setItem(slot, item.getDisplay())
        }

        return inventory
    }

    private fun checkExpiredItemsAndRefresh() {
        val isRemoved = service.removeExpiredItems(player)
        if (isRemoved) {
            this.refresh()
        }
    }

    override fun whenClickedTop(event: InventoryClickEvent) {
        event.isCancelled = true
        if (this.getSlots().contains(event.slot) && event.currentItem != null) {
            this.whenClickedItemBoxItem(event)
        }

        if (event.slot == this.rightButton.slots.first() && this.isNextPageExist()) {
            this.page += 1
            this.refresh()
        }

        if (event.slot == this.leftButton.slots.first() && this.page != 1) {
            this.page -= 1
            this.refresh()
        }
    }

    private fun whenClickedItemBoxItem(event: InventoryClickEvent) {
        if (player.inventory.firstEmpty() < 0) {
            player.sendBNWarnMessage("인벤토리에 공간이 부족합니다.")
            return
        }
        val isReceived = service.receiveItem(player) { it.item == event.currentItem }
        if (isReceived) {
            player.sendBNMessage("아이템을 성공적으로 수령하였습니다.")
            refresh()
        }
    }

    override fun whenPostOpen() {
        this.checkExpiredItemsAndRefresh()
    }

    private val leftButton = object : BarcodeInventory.SlotItem {
        override val slots: List<Int> = listOf(28)

        override fun get(): ItemStack {
            val item = BarcodeGuiItems.LEFT_BUTTON.item
            item.setDisplayName("&6이전 페이지")
            if (this@ItemBoxInventoryImpl.page == 1) {
                item.setColorizedLore("&7이동할 수 있는 페이지가 없습니다.")
            } else {
                item.setColorizedLore("&7클릭하여 이전 페이지로 이동합니다.")
            }
            return item
        }
    }

    private val rightButton = object : BarcodeInventory.SlotItem {
        override val slots: List<Int> = listOf(34)

        override fun get(): ItemStack {
            val item = BarcodeGuiItems.RIGHT_BUTTON.item
            item.setDisplayName("&6다음 페이지")
            if (this@ItemBoxInventoryImpl.isNextPageExist()) {
                item.setColorizedLore("&7클릭하여 다음 페이지로 이동합니다.")
            } else {
                item.setColorizedLore("&7이동할 수 있는 페이지가 없습니다.")
            }
            return item
        }
    }

    private val empty = object : BarcodeInventory.SlotItem {
        override val slots: List<Int> = listOf(0, 8, 9, 17, 18, 26, 27, 29, 30, 31, 32, 33, 35)

        override fun get(): ItemStack {
            return BarcodeGuiItems.EMPTY.item
        }
    }
}