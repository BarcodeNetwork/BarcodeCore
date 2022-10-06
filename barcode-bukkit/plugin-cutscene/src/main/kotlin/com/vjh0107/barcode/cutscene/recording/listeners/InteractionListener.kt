package com.vjh0107.barcode.cutscene.recording.listeners

import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import com.vjh0107.barcode.framework.utils.transformer.isPlayer
import com.vjh0107.barcode.cutscene.recording.RecordSession
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Chest
import org.bukkit.block.DoubleChest
import org.bukkit.block.data.Openable
import org.bukkit.block.data.Powerable
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent

@BarcodeComponent
class InteractionListener : BarcodeListener {
    @EventHandler
    fun interactEvent(event: PlayerInteractEvent) {
        val player = event.player

        val session = RecordSession.recordSessions[player.uniqueId] ?: return
        if (event.action == Action.LEFT_CLICK_AIR) {
            session.animation = 0
        } else if (event.action == Action.LEFT_CLICK_BLOCK) {
            session.animation = 0
        } else if (event.action == Action.RIGHT_CLICK_BLOCK) {
            val block = event.clickedBlock
            if (block!!.type == Material.CHEST || block.type == Material.ENDER_CHEST || block.type == Material.TRAPPED_CHEST) {
                session.animation = 0
                session.queuedAction = "CHEST,," + block.x + ",," + block.y + ",," + block.z
            } else if (block.blockData is Powerable && block.blockData !is Openable) {
                session.animation = 0
                session.queuedAction =
                    "POWERABLE,," + block.x + ",," + block.y + ",," + block.z + ",," + !(block.blockData as Powerable).isPowered
            } else if (block.blockData is Openable) {
                session.animation = 0
                session.queuedAction =
                    "POWERABLE,," + block.x + ",," + block.y + ",," + block.z + ",," + !(block.blockData as Openable).isOpen
            }
        }
    }

    @EventHandler
    fun onClose(event: InventoryCloseEvent) {
        if (event.inventory.type == InventoryType.CHEST || event.inventory.type == InventoryType.ENDER_CHEST) {
            val player = event.player
            if (player.isPlayer()) {

                val location: Location? = when (val holder = event.inventory.holder) {
                    is Chest -> holder.location
                    is DoubleChest -> holder.location
                    else -> null
                }

                if (location != null) {
                    val block = location.block
                    val session = RecordSession.recordSessions[player.uniqueId] ?: return
                    session.queuedAction = "CHEST_CLOSE,," + block.x + ",," + block.y + ",," + block.z
                }
            }
        }
    }
}