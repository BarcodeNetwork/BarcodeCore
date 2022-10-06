package com.vjh0107.barcode.core.listeners

import com.vjh0107.barcode.core.events.PrimaryPlayerJoinEvent
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

@BarcodeComponent
class PrimaryPlayerJoinEventCaller : BarcodeListener {
    private var isPrimaryJoin = true

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        if (isPrimaryJoin) {
            isPrimaryJoin = false
            Bukkit.getPluginManager().callEvent(PrimaryPlayerJoinEvent())
        }
    }
}