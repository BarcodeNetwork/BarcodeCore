package com.vjh0107.barcode.core.item.stats

import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder
import net.Indyuce.mmoitems.stat.data.BooleanData
import net.Indyuce.mmoitems.stat.data.type.StatData
import net.Indyuce.mmoitems.stat.type.BooleanStat
import org.bukkit.Material

class NotExchangeableStat : BooleanStat("NOT_EXCHANGEABLE",
    Material.EMERALD,
    "교환 불가능 한가요?",
    arrayOf("교환 불가능하게 합니다.", "§8Author: vjh0107"),
    arrayOf("all"),
    *arrayOfNulls(0)) {
    override fun whenApplied(item: ItemStackBuilder, data: StatData) {
        if (!(data as BooleanData).isEnabled) return
        item.addItemTag(getAppliedNBT(data))
    }
}