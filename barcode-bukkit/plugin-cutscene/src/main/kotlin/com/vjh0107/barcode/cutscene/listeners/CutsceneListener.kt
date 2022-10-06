package com.vjh0107.barcode.cutscene.listeners

import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import com.vjh0107.barcode.framework.utils.transformer.isPlayer
import com.vjh0107.barcode.cutscene.data.getNullableCutscenePlayerData
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.*

@BarcodeComponent
class CutsceneListener : BarcodeListener {

    @EventHandler
    fun onTarget(event: EntityTargetEvent) {
        val player = event.target as? Player ?: return
        val playerData = player.getNullableCutscenePlayerData() ?: return
        if (playerData.isInCutscene()) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onDropItem(event: PlayerDropItemEvent) {
        setCancelInCutscene(event.player, event)
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        setCancelInCutscene(event.player, event)
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        setCancelInCutscene(event.player, event)
    }

    @EventHandler
    fun onBlockDestroy(event: BlockBreakEvent) {
        setCancelInCutscene(event.player, event)
    }

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        val cutscene = event.player.getNullableCutscenePlayerData()?.cutscene ?: return
        if (cutscene.isFrozen.not()) {
            return
        }
        if (cutscene.isPaused) {
            return
        }
        event.isCancelled = true
    }

    @EventHandler
    fun onHealthChange(event: EntityDamageEvent) {
        val mayPlayer = event.entity
        if (mayPlayer.isPlayer()) {
            setCancelInCutscene(mayPlayer, event)
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        event.player.getNullableCutscenePlayerData()?.cutscene?.stopCutscene(false)
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val mayPlayer = event.whoClicked
        if (mayPlayer.isPlayer()) {
            setCancelInCutscene(mayPlayer, event)
        }
    }

    @EventHandler
    fun onInventoryOpen(event: InventoryOpenEvent) {
        val mayPlayer = event.player
        if (mayPlayer.isPlayer()) {
            setCancelInCutscene(mayPlayer, event)
        }
    }

    @Suppress("DEPRECATION")
    @EventHandler
    fun onPickup(event: PlayerPickupItemEvent) {
        setCancelInCutscene(event.player, event)
    }

    private fun setCancelInCutscene(player: Player, event: Cancellable) {
        if (!player.isOnline) return
        val playerData = player.getNullableCutscenePlayerData() ?: return
        val cutscene = playerData.cutscene ?: return

        if (cutscene.isPaused) {
            return
        }
        event.isCancelled = true
    }

}