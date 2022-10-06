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


class EnhanceVolume : DoubleStat("ENHANCE_VOLUME",
        Material.NETHERITE_INGOT,
        "강화 용량",
        arrayOf("바코드 네트워크 강화 부피를 설정합니다.","강화석 필수 옵션 입니다.", "§8Author: vjh0107"),
        arrayOf("miscellaneous"),
        *arrayOfNulls(0)) {
    override fun whenApplied(item: ItemStackBuilder, data: StatData) {
        val enhanceVolume = (data as DoubleData).value
        item.addItemTag(*arrayOf(ItemTag("BARCODE_ENHANCE_VOLUME", enhanceVolume)))
        item.lore.insert("enhance-volume", formatNumericStat(enhanceVolume, "#", "" + String.format("%.1f",enhanceVolume)))
    }

    @Throws(IllegalArgumentException::class)
    override fun whenPreviewed(item: ItemStackBuilder, currentData: StatData, templateData: RandomStatData) {
        Validate.isTrue(currentData is DoubleData, "Current Data is not Double Data")
        Validate.isTrue(templateData is NumericStatFormula, "Template Data is not Numeric Stat Formula")

        item.addItemTag(*arrayOf(ItemTag("BARCODE_ENHANCE_VOLUME", (currentData as DoubleData).value)))
        item.lore.insert("enhance-volume",
            *arrayOf(formatNumericStat(currentData.value, *arrayOf("#", String.format("%.1f",currentData.value)))))

    }
}
