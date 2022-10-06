package com.vjh0107.barcode.core.gps.listeners

import com.live.bemmamin.gps.api.events.GPSStartEvent
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import org.bukkit.event.EventHandler

@BarcodeComponent
class GPSListener : BarcodeListener {
    @EventHandler
    fun onGPSStart(event: GPSStartEvent) {
        event.startSource
    }
}