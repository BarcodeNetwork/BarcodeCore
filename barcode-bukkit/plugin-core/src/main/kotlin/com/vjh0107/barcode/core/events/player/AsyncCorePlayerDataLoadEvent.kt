package com.vjh0107.barcode.core.events.player

import com.vjh0107.barcode.core.database.player.CorePlayerPlayerData
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class AsyncCorePlayerDataLoadEvent(val playerData: CorePlayerPlayerData) : PlayerEvent(playerData.player, true) {
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