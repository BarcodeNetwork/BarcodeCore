package com.vjh0107.barcode.framework.utils.item

import com.vjh0107.barcode.framework.utils.formatters.colorize
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

fun ItemStack.setCustomModelData(value: Int) {
    val itemMeta = this.itemMeta
    itemMeta.setCustomModelData(value)
    this.itemMeta = itemMeta
}

fun ItemStack.getCustomModelData() : Int {
    if (!this.hasItemMeta()) return 0
    if (!this.itemMeta.hasCustomModelData()) return 0
    return this.itemMeta.customModelData
}

fun ItemStack.setDisplayName(value: String) {
    val itemMeta = this.itemMeta
    itemMeta.setDisplayName(value.colorize())
    this.itemMeta = itemMeta
}

@Suppress("DEPRECATION")
fun ItemStack.getDisplayName() : String? {
    if (!this.hasItemMeta()) return null
    if (!this.itemMeta.hasDisplayName()) return null
    return this.itemMeta.displayName
}

fun ItemStack.setColorizedLore(vararg value: String) {
    val itemMeta = this.itemMeta
    itemMeta.lore = value.map { it.colorize() }
    this.itemMeta = itemMeta
}

fun ItemStack.setColorizedLore(value: List<String>) {
    val itemMeta = this.itemMeta
    itemMeta.lore = value.map { it.colorize() }
    this.itemMeta = itemMeta
}

@Suppress("DEPRECATION")
fun createGuiItem(material: Material, amount: Int = 1, displayName: String = "", loreString: List<String>? = null, customModelData: Int? = null): ItemStack {
    val item = ItemStack(material, amount)
    val meta: ItemMeta = item.itemMeta
    meta.setDisplayName(displayName)
    if (loreString != null) {
        meta.lore = loreString
    }
    meta.setCustomModelData(customModelData)
    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
    item.itemMeta = meta
    return item
}

fun String.toBukkitMaterial() : Material {
    return Material.matchMaterial(this.uppercase()) ?: throw NullPointerException("material $this not found")
}