package com.vjh0107.barcode.core.equip.listeners

import com.vjh0107.barcode.core.dependencies.mmo.mmocore.utils.getMMOCoreData
import com.vjh0107.barcode.core.dependencies.mmo.mmocore.utils.isCasting
import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import com.vjh0107.barcode.framework.coroutine.extensions.MinecraftMain
import com.vjh0107.barcode.framework.coroutine.extensions.launchScope
import com.vjh0107.barcode.framework.utils.formatters.decolorize
import com.vjh0107.barcode.framework.utils.item.createGuiItem
import com.vjh0107.barcode.framework.utils.item.getCustomModelData
import com.vjh0107.barcode.framework.utils.item.getDisplayName
import com.vjh0107.barcode.framework.utils.messagesender.sendBNWarnMessage
import io.lumine.mythic.lib.api.item.NBTItem
import kotlinx.coroutines.*
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerPickupItemEvent
import org.bukkit.inventory.ItemStack

@BarcodeComponent
class DaggerWeaponReflector(private val plugin: AbstractBarcodePlugin) : BarcodeListener {
    companion object {
        const val DUMMY_ITEM_ID = "§d§u§m§m§y"
        const val TARGET_CLASS_ID = "BLADE"
        const val DAGGER_ITEM_ID = "DAGGER"

        const val OFFHAND_SLOT = 40
    }

    @EventHandler
    fun onHeldDagger(event: PlayerItemHeldEvent) {
        val player = event.player
        if (player.isCasting()) return
        refreshDaggerSlot(player, player.inventory.getItem(event.newSlot))
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) = Dispatchers.MinecraftMain(plugin).launchScope {
        val player = event.view.player as Player
        if (player.getMMOCoreData().profess.id.uppercase() == TARGET_CLASS_ID && player.gameMode != GameMode.CREATIVE) {
            if (event.inventory.type == InventoryType.CRAFTING && event.slot == OFFHAND_SLOT) {
                player.sendBNWarnMessage("블레이드 직업군은 보조무기를 장착할 수 없습니다.")
                val itemOnCursor = event.cursor
                if (itemOnCursor != null) {
                    val clonedItem = itemOnCursor.clone()
                    itemOnCursor.amount = 0
                    player.inventory.addItem(clonedItem)
                }
                event.isCancelled = true
                return@launchScope
            }
        }
        delay(50)
        refreshDaggerSlot(player, player.inventory.itemInMainHand)
    }

    @EventHandler
    fun onDummyItemClick(event: InventoryClickEvent) {
        val itemName = event.currentItem?.getDisplayName() ?: return
        if (itemName.decolorize() == DUMMY_ITEM_ID.decolorize()) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPickUpDagger(event: PlayerPickupItemEvent) = CoroutineScope(Dispatchers.MinecraftMain(plugin)).launch {
        val player = event.player
        val nbtItem: NBTItem = NBTItem.get(event.item.itemStack) ?: return@launch
        if (nbtItem.type == DAGGER_ITEM_ID) {
            delay(50)
            refreshDaggerSlot(player, player.inventory.itemInMainHand)
        }
    }

    private fun refreshDaggerSlot(player: Player, item: ItemStack?) {
        val offhandItem = player.inventory.itemInOffHand
        val removeDummyItem: () -> Unit = {
            if (offhandItem.getDisplayName()?.decolorize() == DUMMY_ITEM_ID.decolorize()) {
                player.inventory.setItemInOffHand(null)
            }
        }
        if (item == null) {
            removeDummyItem()
            return
        }

        val nbtItem = NBTItem.get(item)
        if (nbtItem.type == DAGGER_ITEM_ID) {
            val dummyItem = createGuiItem(item.type, 1, DUMMY_ITEM_ID, null, item.getCustomModelData())
            player.inventory.setItemInOffHand(dummyItem)
        } else {
            removeDummyItem()
        }
    }

    private fun removeDummyItem() {

    }
}