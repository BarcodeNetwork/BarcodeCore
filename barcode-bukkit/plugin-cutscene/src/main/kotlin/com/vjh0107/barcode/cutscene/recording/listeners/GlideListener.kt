package com.vjh0107.barcode.cutscene.recording.listeners

import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityToggleGlideEvent
import java.util.*

@BarcodeComponent
class GlideListener : BarcodeListener {
    companion object {
        @JvmStatic
        val gliding: MutableList<UUID> = mutableListOf()
    }

    @EventHandler
    fun toggleGlideEvent(event: EntityToggleGlideEvent) {
        if (event.entity is Player) {
            val player = event.entity as Player
            if (event.isGliding) {
                if (!gliding.contains(player.uniqueId)) {
                    gliding.add(player.uniqueId)
                }
            } else {
                if (gliding.contains(player.uniqueId)) {
                    gliding.remove(player.uniqueId)
                }
            }
        }
    }
}