package com.vjh0107.barcode.core.equip.listeners

import com.vjh0107.barcode.core.BarcodeCorePlugin
import com.vjh0107.barcode.core.equip.models.EmptyEquipSlot
import com.vjh0107.barcode.core.equip.models.getEquipSlot
import com.vjh0107.barcode.core.equip.playerEquipSlotItemSetter
import com.vjh0107.barcode.core.equip.playerEquipUpdater
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import io.lumine.mythic.lib.api.item.NBTItem
import net.Indyuce.mmoitems.api.Type
import net.Indyuce.mmoitems.api.player.PlayerData
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

@BarcodeComponent
class PlayerEquipListener : BarcodeListener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onInventoryClick(event: InventoryClickEvent) {
        val player: Player = event.whoClicked as Player
        val clickedInventory = event.clickedInventory ?: return
        val item = player.itemOnCursor
        val slot = event.slot
        //불
        if (clickedInventory.type != InventoryType.PLAYER) return
        if (slot !in 9..17) return
        val emptyEquipSlot = EmptyEquipSlot(slot)
        //변

        if (!EmptyEquipSlot(slot).isEquipSlotEmpty(player)) {
            BarcodeCorePlugin.runTaskLater(1L) {
                if (player.inventory.getItem(slot) == null) emptyEquipSlot.setEmptyEquipSlot(player)
                playerEquipUpdater(player)
            }
            return
        }
        event.isCancelled = true
        if (item.type.isAir && emptyEquipSlot.isEquipSlotEmpty(player)) return
        if (!this.equipDiscriminator(player, slot, item)) return

        //val mmoCorePlayerData: net.Indyuce.mmocore.api.player.PlayerData = net.Indyuce.mmocore.api.player.PlayerData.get(player)
        //if (mmoCorePlayerData.isInCombat || mmoCorePlayerData.isCasting) return

        player.inventory.setItem(slot, item)
        player.itemOnCursor.amount = 0

        playerEquipUpdater(player)
    }
    private fun equipDiscriminator(player: Player, slot: Int, item: ItemStack): Boolean {
        val equipmentType = getEquipSlot(slot)
        val id = equipmentType.toString()
        //Level, class
        if (!PlayerData.get(player).rpg.canUse(NBTItem.get(item), false)) return false
        //Type 구해오기
        val type = Type.get(NBTItem.get(item).type) ?: return false
        if (id != type.id) return false

        return true
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        //if (!isPlayerProfileSelected(player)) return
        playerEquipSlotItemSetter(player)
        playerEquipUpdater(player)
    }

    @EventHandler
    fun onRightClickAtSubWeapon(event: PlayerInteractEvent) {
        if (event.hand == EquipmentSlot.OFF_HAND) return
        val player = event.player
        val item = event.item ?: return
        val type = NBTItem.get(item)?.type ?: return
        if (type == "OFF_CATALYST") {
            player.inventory.removeItem(item)
            player.inventory.setItem(EquipmentSlot.OFF_HAND, item)
            event.isCancelled = true
        }
    }
}