package com.vjh0107.barcode.cutscene.recording.listeners

import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import com.vjh0107.barcode.cutscene.recording.RecordSession
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

@BarcodeComponent
class BlockListener : BarcodeListener {
    @EventHandler
    fun placeBlockEvent(event: BlockPlaceEvent) {
        val player = event.player
        if (!RecordSession.recordSessions.containsKey(player.uniqueId)) {
            return
        }
        val session = RecordSession.recordSessions[player.uniqueId]
        val block = event.block
        session!!.animation = 0
        session.queuedAction =
            "BLOCK_PLACE,," + block.x + ",," + block.y + ",," + block.z + ",," + block.type.toString() + ",," + block.blockData.asString
        if (!session.changedBlocks.contains(block.location)) {
            session.changedBlocks.add(block.location)
            session.revertBlock[block.location] = Material.AIR.createBlockData()
        }
    }

    @EventHandler
    fun destroyBlockEvent(event: BlockBreakEvent) {
        val player = event.player
        if (!RecordSession.recordSessions.containsKey(player.uniqueId)) {
            return
        }
        val session = RecordSession.recordSessions[player.uniqueId]
        val block = event.block
        session!!.animation = 0
        session.queuedAction = "BLOCK_DESTROY,," + block.x + ",," + block.y + ",," + block.z
        if (!session.changedBlocks.contains(block.location)) {
            session.changedBlocks.add(block.location)
            session.revertBlock[block.location] = block.blockData
        }
    }
}