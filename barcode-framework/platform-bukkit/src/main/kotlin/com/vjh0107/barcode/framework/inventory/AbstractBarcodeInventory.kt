package com.vjh0107.barcode.framework.inventory

import com.vjh0107.barcode.framework.utils.formatters.colorize
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory

abstract class AbstractBarcodeInventory : BarcodeInventory {

    override fun open() {
        player.openInventory(inventory)
        whenPostOpen()
    }

    open fun whenPostOpen() {}

    // 가독성을 위한 메소드
    fun refresh() {
        open()
    }

    override fun Inventory.setItems(item: BarcodeInventory.SlotItem) : Inventory {
        item.slots.forEach {
            this.setItem(it, item.get())
        }
        return this
    }

    fun createInventory(size: Int, name: String): Inventory {
        return Bukkit.createInventory(this, size, name.colorize())
    }

    abstract override fun getInventory(): Inventory

    abstract override fun whenClickedTop(event: InventoryClickEvent)

    override fun whenClickedBottom(event: InventoryClickEvent) {}

    override fun whenClosed(event: InventoryCloseEvent) {}
}
