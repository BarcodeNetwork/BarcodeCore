package com.vjh0107.barcode.core.events.player

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class BarcodeDamageEvent(player: Player, val isCriticalAttack: Boolean) : PlayerEvent(player), Cancellable {
    companion object {
        @JvmStatic
        private val handlers: HandlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlers
        }
    }

    private var isCancelled = false

    @Suppress("RedundantCompanionReference")
    override fun getHandlers(): HandlerList {
        return Companion.handlers
    }

    override fun isCancelled(): Boolean { return isCancelled }

    override fun setCancelled(bl: Boolean) { isCancelled = bl }
}