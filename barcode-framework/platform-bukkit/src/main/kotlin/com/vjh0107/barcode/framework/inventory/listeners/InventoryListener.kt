package com.vjh0107.barcode.framework.inventory.listeners

import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import com.vjh0107.barcode.framework.inventory.BarcodeInventory
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

@BarcodeComponent
class InventoryListener : BarcodeListener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val holder = event.inventory.holder
        if (holder is BarcodeInventory) {
            if (event.clickedInventory == event.whoClicked.inventory) {
                holder.whenClickedBottom(event)
            } else if (event.clickedInventory != null && event.clickedInventory != event.whoClicked.inventory) {
                holder.whenClickedTop(event)
            }
        }
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val holder = event.inventory.holder
        if (holder is BarcodeInventory) {
            holder.whenClosed(event)
        }
    }
}