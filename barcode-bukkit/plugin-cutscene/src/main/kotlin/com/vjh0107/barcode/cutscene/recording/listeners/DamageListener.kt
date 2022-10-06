package com.vjh0107.barcode.cutscene.recording.listeners

import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import com.vjh0107.barcode.framework.utils.transformer.isPlayer
import com.vjh0107.barcode.cutscene.recording.RecordSession
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent

@BarcodeComponent
class DamageListener : BarcodeListener {
    @EventHandler
    fun damageEvent(event: EntityDamageEvent) {
        val player = event.entity
        if (player.isPlayer()) {
            val session = RecordSession.recordSessions[player.uniqueId] ?: return
            session.animation = 1
        }
    }
}