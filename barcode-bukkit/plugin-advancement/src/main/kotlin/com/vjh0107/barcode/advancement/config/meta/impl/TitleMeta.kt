package com.vjh0107.barcode.advancement.config.meta.impl

import com.vjh0107.barcode.advancement.services.ConditionManager
import com.vjh0107.barcode.advancement.config.meta.TextComponentMeta
import com.vjh0107.barcode.advancement.config.condition.ConditionData
import com.vjh0107.barcode.framework.utils.formatters.parseColorCode
import com.vjh0107.barcode.framework.dependencies.placeholderapi.extensions.parseWithPAPI
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player

class TitleMeta(
    private val title: String,
    private val conditionTitles: ConfigurationSection?
) : TextComponentMeta {
    private val conditionTitleDataSet: MutableSet<ConditionData> = mutableSetOf()

    init {
        conditionTitles?.getKeys(false)?.mapNotNull { key ->
            val conditionList = conditionTitles.getConfigurationSection("$key.conditions")
            val description = conditionTitles
                .getString("$key.title") ?: throw NullPointerException("value 없음")
            conditionTitleDataSet.add(ConditionData.of(conditionList, description))
        }
    }

    override fun getResult(player: Player): TextComponent {
        var resultTitle = title
        conditionTitleDataSet.forEach {
            if (ConditionManager.evaluateCondition(player, it.conditions)) {
                resultTitle = it.value
            }
        }
        return TextComponent(resultTitle.parseWithPAPI(player).parseColorCode())
    }
}