package com.vjh0107.barcode.framework.events

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class BarcodePluginDisableEvent(val plugin: AbstractBarcodePlugin) : Event() {
    companion object {
        private var handlers = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlers
        }
    }

    override fun getHandlers(): HandlerList {
        return Companion.handlers
    }
}
