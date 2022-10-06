package com.vjh0107.barcode.core.events

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PrimaryPlayerJoinEvent : Event() {
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