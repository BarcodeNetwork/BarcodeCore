package com.vjh0107.barcode.core.listeners.preventers

import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockFadeEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

@BarcodeComponent
class ProtectivePreventer : BarcodeListener {
    /**
     * 얼음류와 나뭇잎류의 아이템 state 변경을 막습니다.
     */
    @EventHandler
    fun onFade(event: BlockFadeEvent) {
        val name = event.block.type.name.lowercase()
        if (name.contains("leaves") || (name.contains("ice") && !name.contains("slice"))) {
            event.isCancelled = true
        }
    }

    /**
     * 염색, 이름붙히기, 물뜨기를 제한합니다.
     */
    @EventHandler
    fun onClick1(event: PlayerInteractEvent) {
        if (!event.player.isOp) {
            val itemType = event.item?.type ?: return

            if (itemType.name.lowercase().contains("dye") || itemType == Material.NAME_TAG || itemType == Material.GLASS_BOTTLE) {
                event.isCancelled = true
            }
        }
    }

    /**
     * 아이템프레임 클릭을 제한합니다.
     */
    @EventHandler
    fun onClick2(event: PlayerInteractEvent) {
        if (event.player.isOp) {
            if (event.player.isSneaking) {
                return
            }
        }
        val clickedBlock = event.clickedBlock?.type ?: return
        if (clickedBlock == Material.ITEM_FRAME || clickedBlock == Material.GLOW_ITEM_FRAME) {
            event.isCancelled = true
        }
    }

    /**
     * 블럭 파괴를 제한합니다.
     */
    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        if (!event.player.isOp) {
            event.isCancelled = true
        }
    }

    /**
     * 블럭 설치를 제한합니다.
     */
    @EventHandler
    fun onPlace(event: BlockPlaceEvent) {
        if (!event.player.isOp) {
            event.isCancelled = true
        }
    }
}