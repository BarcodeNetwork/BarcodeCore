package com.vjh0107.barcode.framework.database.player.events

import com.vjh0107.barcode.framework.database.player.data.PlayerData
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class BarcodePlayerDataLoadEvent(player: Player, val playerData: PlayerData) : PlayerEvent(player) {
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