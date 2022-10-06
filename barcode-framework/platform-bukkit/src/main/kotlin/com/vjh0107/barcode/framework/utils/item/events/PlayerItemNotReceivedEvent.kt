package com.vjh0107.barcode.framework.utils.item.events

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent
import org.bukkit.inventory.ItemStack

class PlayerItemNotReceivedEvent(player: Player, val items: MutableMap<Int, ItemStack>) : PlayerEvent(player) {
    companion object {
        @JvmStatic
        private val handlers: HandlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlers
        }
    }

    @Suppress("RedundantCompanionReference")
    override fun getHandlers(): HandlerList {
        return Companion.handlers
    }
}