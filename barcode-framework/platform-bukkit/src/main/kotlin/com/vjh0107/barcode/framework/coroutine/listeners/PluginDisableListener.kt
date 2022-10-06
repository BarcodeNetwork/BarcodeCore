package com.vjh0107.barcode.framework.coroutine.listeners

import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import com.vjh0107.barcode.framework.coroutine.CoroutineComponent
import com.vjh0107.barcode.framework.events.BarcodePluginDisableEvent
import org.bukkit.event.EventHandler

@BarcodeComponent
class PluginDisableListener : BarcodeListener {
    @EventHandler
    fun onDisable(event: BarcodePluginDisableEvent) {
        CoroutineComponent.instance.removeCoroutinePlugin(event.plugin)
    }
}