package com.vjh0107.barcode.advancement.config.meta

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface ItemMeta {
    fun getResult(player: Player) : ItemStack
}