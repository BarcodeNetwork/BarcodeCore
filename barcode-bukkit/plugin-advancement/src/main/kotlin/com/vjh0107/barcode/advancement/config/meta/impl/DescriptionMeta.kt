package com.vjh0107.barcode.advancement.config.meta.impl

import com.vjh0107.barcode.advancement.config.condition.ConditionData
import com.vjh0107.barcode.advancement.services.ConditionManager
import com.vjh0107.barcode.advancement.config.meta.TextComponentMeta
import com.vjh0107.barcode.advancement.config.placeholder.Placeholders
import com.vjh0107.barcode.framework.utils.formatters.parseColorCode
import com.vjh0107.barcode.framework.utils.formatters.toTextComponent
import com.vjh0107.barcode.framework.dependencies.placeholderapi.extensions.parseWithPAPI
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player

class DescriptionMeta(
    private val descriptions: List<String>,
    private val conditionDescriptions: ConfigurationSection?
) : TextComponentMeta {
    private val conditionDescriptionDataMap: MutableMap<String, ConditionData> = mutableMapOf()

    init {
        conditionDescriptions?.getKeys(false)?.mapNotNull { key ->
            val conditionList = conditionDescriptions.getConfigurationSection("$key.conditions")
            val description = conditionDescriptions
                .getString("$key.description") ?: throw NullPointerException("value 없음")
            conditionDescriptionDataMap[key] = ConditionData.of(conditionList, description)
        }
    }

    override fun getResult(player: Player): TextComponent {
        val resultList: MutableList<String> = mutableListOf()

        // placeholder processing
        descriptions.forEach lines@{ line ->
            val key = line.parsePlaceholderKey(Placeholders.OPTIONAL)
            if (key != null) {
                val optionalDescriptionComponent = conditionDescriptionDataMap[key]
                    ?: throw NullPointerException("$key 키의 optional description 이 존재하지 않습니다.")

                if (ConditionManager.evaluateCondition(player, optionalDescriptionComponent.conditions)) {
                    optionalDescriptionComponent.value.split("\n").forEach { optionalDescriptionLine ->
                        resultList.add(optionalDescriptionLine.parseWithPAPI(player).parseColorCode())
                    }
                } else {
                    return@lines
                }

            } else {
                resultList.add(line.parseWithPAPI(player).parseColorCode())
            }
        }

        return resultList.toTextComponent()
    }
}