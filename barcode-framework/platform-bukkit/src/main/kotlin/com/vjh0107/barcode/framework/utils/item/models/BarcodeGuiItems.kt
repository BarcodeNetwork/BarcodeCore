package com.vjh0107.barcode.framework.utils.item.models

import com.vjh0107.barcode.framework.utils.item.createGuiItem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class BarcodeGuiItems(val item: ItemStack) {
    EMPTY(createGuiItem(Material.IRON_SHOVEL, customModelData = 4)),
    LEFT_BUTTON(createGuiItem(Material.IRON_SHOVEL, customModelData = 10)),
    RIGHT_BUTTON(createGuiItem(Material.IRON_SHOVEL, customModelData = 11))
}