package com.vjh0107.barcode.core.blacksmith.stats

import io.lumine.mythic.lib.api.item.ItemTag
import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder
import net.Indyuce.mmoitems.api.util.NumericStatFormula
import net.Indyuce.mmoitems.stat.data.DoubleData
import net.Indyuce.mmoitems.stat.data.random.RandomStatData
import net.Indyuce.mmoitems.stat.data.type.StatData
import net.Indyuce.mmoitems.stat.type.DoubleStat
import org.apache.commons.lang.Validate
import org.bukkit.Material


class EnhanceChance : DoubleStat("ENHANCE_CHANCE",
        Material.GOLD_INGOT,
        "강화 성공 확률",
        arrayOf("바코드 네트워크 강화 성공 확률을 설정합니다.","강화테이블에서의 강화석 인식 조건은 강화 용량, 강화 확률, 강화 파괴 확률","3개 중 1개라도 들어간것만을 인식합니다.", "§8Author: vjh0107"),
        arrayOf("miscellaneous"),
        *arrayOfNulls(0)) {
    override fun whenApplied(item: ItemStackBuilder, data: StatData) {
        val enhanceChance = (data as DoubleData).value
        item.addItemTag(*arrayOf(ItemTag("BARCODE_ENHANCE_CHANCE", enhanceChance)))
        item.lore.insert("enhance-chance", formatNumericStat(enhanceChance, "#", "" + String.format("%.1f",enhanceChance)))
    }

    @Throws(IllegalArgumentException::class)
    override fun whenPreviewed(item: ItemStackBuilder, currentData: StatData, templateData: RandomStatData) {
        Validate.isTrue(currentData is DoubleData, "Current Data is not Double Data")
        Validate.isTrue(templateData is NumericStatFormula, "Template Data is not Numeric Stat Formula")

        item.addItemTag(*arrayOf(ItemTag("BARCODE_ENHANCE_CHANCE", (currentData as DoubleData).value)))
        item.lore.insert("enhance-chance",
            *arrayOf(formatNumericStat(currentData.value, *arrayOf("#", String.format("%.1f",currentData.value)))))

    }
}
