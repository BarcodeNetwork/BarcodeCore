package com.vjh0107.barcode.core.blacksmith.stats

import io.lumine.mythic.lib.api.item.ItemTag
import net.Indyuce.mmoitems.ItemStats
import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder
import net.Indyuce.mmoitems.stat.data.BooleanData
import net.Indyuce.mmoitems.stat.data.UpgradeData
import net.Indyuce.mmoitems.stat.data.type.StatData
import net.Indyuce.mmoitems.stat.type.BooleanStat
import org.bukkit.Material

class Enhanceable : BooleanStat("BARCODE_ENHANCEABLE",
    Material.ANVIL,
    "바코드 무기 강화가 가능합니까?",
    arrayOf("바코드 네트워크의 강화가 가능하도록 합니다.", "§8Author: vjh0107"),
    arrayOf("all"),
    *arrayOfNulls(0)) {
    override fun whenApplied(item: ItemStackBuilder, data: StatData) {
        if (!(data as BooleanData).isEnabled) return
        item.addItemTag(*arrayOf(ItemTag("BARCODE_ENHANCEABLE", true)))
        val upgradeData = UpgradeData(
            null,
            "1",
            false,
            false,
            20,
            100.0
        )
        item.mmoItem.replaceData(ItemStats.UPGRADE, upgradeData)
    }
}
