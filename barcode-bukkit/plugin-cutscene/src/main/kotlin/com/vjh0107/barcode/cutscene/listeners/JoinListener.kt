package com.vjh0107.barcode.cutscene.listeners

import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

@BarcodeComponent
class JoinListener : BarcodeListener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        if (event.player.gameMode != GameMode.CREATIVE) {
            event.player.allowFlight = false
        }
    }
}