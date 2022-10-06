package com.vjh0107.barcode.core.listeners

import com.vjh0107.barcode.core.npc.utils.isNPC
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEntityEvent

@BarcodeComponent
class PlayerProfileOpener : BarcodeListener {


    @EventHandler
    fun onClick(event: PlayerInteractEntityEvent) {
        val targetPlayer = event.rightClicked as? Player ?: return
        if (event.rightClicked.type != EntityType.PLAYER || !event.player.isSneaking || targetPlayer.isNPC()) {
            return
        }

        // TODO: Not Implemented Yet
    }
}