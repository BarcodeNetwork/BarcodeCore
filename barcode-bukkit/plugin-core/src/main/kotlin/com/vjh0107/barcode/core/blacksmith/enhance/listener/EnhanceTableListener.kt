package com.vjh0107.barcode.core.blacksmith.enhance.listener

import com.vjh0107.barcode.core.blacksmith.enhance.EnhanceInfo
import com.vjh0107.barcode.core.blacksmith.enhance.models.EnhanceSchedulers
import com.vjh0107.barcode.core.blacksmith.enhance.models.EnhanceTableInv
import com.vjh0107.barcode.core.blacksmith.enhance.models.EnhanceInventorySlots
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import com.vjh0107.barcode.framework.dependencies.economy.EconomyProvider.Companion.money
import com.vjh0107.barcode.framework.dependencies.economy.EconomyProvider.Companion.takeMoney
import com.vjh0107.barcode.framework.utils.messagesender.sendBNMessage
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import java.util.*

@BarcodeComponent
class EnhanceTableListener : BarcodeListener {

    @EventHandler
    fun onEnhanceTableClick(event: InventoryClickEvent) {
        if (event.view.title != EnhanceTableInv.title) return
        //엠티 슬롯과 미리보기 슬롯 비활
        if (event.rawSlot in (EnhanceInventorySlots.EMPTY_SLOT.slotNums + EnhanceInventorySlots.PREVIEW_SLOT.slotNums)) {
            event.isCancelled = true
            return
        }
        val player: Player = event.whoClicked as Player
        val clickedItem: ItemStack = event.currentItem ?: return
        val enhanceTable = player.openInventory.topInventory
        if (event.rawSlot in EnhanceInventorySlots.ENHANCE_BUTTON_SLOT.slotNums) event.isCancelled = true
        if (event.rawSlot in EnhanceInventorySlots.ENHANCE_BUTTON_SLOT.slotNums && clickedItem.type == Material.ANVIL) {
            val enhanceInfo: EnhanceInfo = EnhanceSchedulers.playerSet[player] ?: run {
                Bukkit.getLogger().severe("can't get EnhanceInfo")
                return
            }
            if (player.money < enhanceInfo.gold) {
                player.sendBNMessage("§c골드가 부족합니다.")
                return
            }
            player.takeMoney(enhanceInfo.gold)

            EnhanceInventorySlots.ENHANCE_MATERIAL_SLOT.slotNums.forEach { slotNum ->
                enhanceTable.setItem(slotNum, null)
            }

            if (Random().nextDouble() <= enhanceInfo.calculatedEnhanceChance / 100) {
                player.sendBNMessage("§a성공적으로 강화에 성공하였습니다!")

                enhanceTable.setItem(EnhanceInventorySlots.ITEM_SLOT.slotNums[0], enhanceInfo.result)
            } else {
                if (Random().nextDouble() <= enhanceInfo.destroyChance / 100) {
                    player.sendBNMessage("§c강화에 실패하여 아이템이 파괴되었습니다.")
                    enhanceTable.getItem(EnhanceInventorySlots.ITEM_SLOT.slotNums[0])!!.amount = 0
                } else {
                    player.sendBNMessage("§c강화에 실패하였습니다.")
                }
            }

        }
    }

    @EventHandler
    fun onEnhanceTableClose(event: InventoryCloseEvent) {
        if(event.view.title != EnhanceTableInv.title) return
        EnhanceSchedulers.playerSet.remove(event.player)
        (EnhanceInventorySlots.ENHANCE_MATERIAL_SLOT.slotNums + EnhanceInventorySlots.ITEM_SLOT.slotNums).forEach { slotNum ->
            event.player.inventory.addItem(event.inventory.getItem(slotNum) ?: return@forEach)
        }
    }
}