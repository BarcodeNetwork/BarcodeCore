package com.vjh0107.barcode.core.equip

import net.Indyuce.mmoitems.api.player.PlayerData
import org.bukkit.entity.Player

fun playerEquipUpdater(player: Player) {
    PlayerData.get(player).updateInventory()
}
