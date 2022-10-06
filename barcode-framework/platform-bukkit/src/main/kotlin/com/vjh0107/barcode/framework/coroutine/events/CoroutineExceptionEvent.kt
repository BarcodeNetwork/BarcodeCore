package com.vjh0107.barcode.framework.coroutine.events

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class CoroutineExceptionEvent(val plugin: AbstractBarcodePlugin, val exception: Throwable) : Event(), Cancellable {
    private var cancelled: Boolean = false

    companion object {
        private var handlers = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlers
        }
    }

    override fun getHandlers(): HandlerList {
        return CoroutineExceptionEvent.handlers
    }

    override fun isCancelled(): Boolean {
        return cancelled
    }

    /**
     * 캔슬 시 catch 되지 않은 오류로 출력된다.
     */
    override fun setCancelled(flag: Boolean) {
        this.cancelled = flag
    }
}
