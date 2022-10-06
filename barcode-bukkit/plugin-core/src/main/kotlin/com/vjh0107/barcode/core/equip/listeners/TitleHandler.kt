package com.vjh0107.barcode.core.equip.listeners

import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import io.lumine.mythic.lib.api.item.NBTItem
import me.neznamy.tab.api.TabAPI
import me.neznamy.tab.shared.TabConstants
import net.Indyuce.mmoitems.api.Type
import net.Indyuce.mmoitems.api.event.RefreshInventoryEvent
import net.Indyuce.mmoitems.api.player.inventory.EquippedPlayerItem
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler

@BarcodeComponent
class TitleHandler : BarcodeListener {
    @EventHandler
    fun onTitleEquip(event: RefreshInventoryEvent) {
        val player = event.player
        val equipList = event.itemsToEquip
        val isTypeTitle: (NBTItem) -> Boolean = fun(nbtItem: NBTItem): Boolean = run {
            Type.get(nbtItem.type)?.id == "TITLE"
        }

        val filter: List<EquippedPlayerItem> = equipList.filter { isTypeTitle(it.item.nbt) }
        if (filter.isEmpty()) {
            removeTitle(player)
        } else {
            equipList.filter { isTypeTitle(it.item.nbt) }.forEach { equippedPlayerItem ->
                val item = equippedPlayerItem.item
                val titleString = item.nbt.getString("BARCODE_TITLE_VALUE")
                applyTitle(player, titleString)
            }
        }
    }

    private fun applyTitle(player: Player, titleString: String) {
        val tabPlayer = TabAPI.getInstance().getPlayer(player.uniqueId)
        tabPlayer.getProperty(TabConstants.Property.ABOVENAME).run {
            temporaryValue = titleString
            update()
        }
    }
    private fun removeTitle(player: Player) {
        val tabPlayer = TabAPI.getInstance().getPlayer(player.uniqueId)
        tabPlayer.getProperty(TabConstants.Property.ABOVENAME)?.let {
            it.temporaryValue?.let { _ ->
                it.temporaryValue = null
            }
            it.update()
        }
    }
}