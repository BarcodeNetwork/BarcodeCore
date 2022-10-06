package com.vjh0107.barcode.core.dependencies.betonquests

import com.vjh0107.barcode.core.events.PrimaryPlayerJoinEvent
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import org.betonquest.betonquest.compatibility.protocollib.hider.NPCHider
import org.bukkit.event.EventHandler

@BarcodeComponent
class BetonQuestFixer : BarcodeListener {
    @EventHandler
    fun fixNPCVisibilityGlitch(event: PrimaryPlayerJoinEvent) {
        NPCHider.start()
    }
}