package com.vjh0107.barcode.core.blacksmith.enhance

import io.lumine.mythic.lib.api.item.NBTItem
import net.Indyuce.mmoitems.ItemStats
import net.Indyuce.mmoitems.MMOItems
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem
import net.Indyuce.mmoitems.stat.data.UpgradeData
import org.bukkit.inventory.ItemStack

object EnhanceBehavior {
    fun upgrade(mmoItem: MMOItem, newLevel: Int) : ItemStack {
        if (newLevel > 20) {
            throw IllegalArgumentException("newLevel must be under 20")
        }
        val upgradeData = UpgradeData(
            null,
            newLevel.toString(),
            false,
            false,
            20,
            100.0
        )
        mmoItem.replaceData(ItemStats.UPGRADE, upgradeData)

        MMOItems.plugin.upgrades.getTemplate(newLevel.toString())?.let {
            it.upgradeTo(mmoItem, newLevel)
        }
        return mmoItem.newBuilder().build() ?: throw NullPointerException("build result should not be null")

    }
    fun isEnhanceable(nbtItem: NBTItem) : Boolean {
        return nbtItem.getBoolean("BARCODE_ENHANCEABLE")
    }
}