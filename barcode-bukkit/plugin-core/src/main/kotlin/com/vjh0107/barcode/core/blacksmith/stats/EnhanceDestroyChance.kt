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

class EnhanceDestroyChance : DoubleStat(
    "ENHANCE_DESTROY_CHANCE",
    Material.TNT_MINECART,
    "§c강화 실패 확률",
    arrayOf("바코드 네트워크 강화 실패시 파괴 확률을 설정합니다.", "강화 실패시 파괴 확률은 음수로 설정할 수 있습니다.", "음수로 설정할 경우, 실패 확률이 감소합니다.", "§8Author: vjh0107"),
    arrayOf("miscellaneous"),
    *arrayOfNulls(0)
) {
    override fun whenApplied(item: ItemStackBuilder, data: StatData) {
        val enhanceDestroyChance = (data as DoubleData).value
        item.addItemTag(*arrayOf(ItemTag("BARCODE_ENHANCE_DESTROY_CHANCE", enhanceDestroyChance)))
        item.lore.insert("enhance-destroy-chance", formatNumericStat(enhanceDestroyChance, "#", "" + String.format("%.1f", enhanceDestroyChance)))
    }

    @Throws(IllegalArgumentException::class)
    override fun whenPreviewed(item: ItemStackBuilder, currentData: StatData, templateData: RandomStatData) {
        Validate.isTrue(currentData is DoubleData, "Current Data is not Double Data")
        Validate.isTrue(templateData is NumericStatFormula, "Template Data is not Numeric Stat Formula")

        item.addItemTag(*arrayOf(ItemTag("BARCODE_ENHANCE_DESTROY_CHANCE", (currentData as DoubleData).value)))
        item.lore.insert(
            "enhance-destroy-chance",
            *arrayOf(formatNumericStat(currentData.value, *arrayOf("#", String.format("%.1f", currentData.value))))
        )
    }
}
