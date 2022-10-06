package com.vjh0107.barcode.framework.utils.item

import com.vjh0107.barcode.framework.utils.item.events.PlayerItemNotReceivedEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun Player.giveItem(item: ItemStack) {
    val addItem = this.inventory.addItem(item)
    if (addItem.isNotEmpty()) {
        val called = PlayerItemNotReceivedEvent(this, addItem)
        Bukkit.getPluginManager().callEvent(called)
    }
}

fun ItemStack.giveItem(player: Player) {
    player.giveItem(this)
}