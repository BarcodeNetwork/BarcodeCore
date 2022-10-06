package com.vjh0107.barcode.advancement.config.meta.impl

import com.vjh0107.barcode.advancement.config.condition.ConditionData
import com.vjh0107.barcode.advancement.config.meta.BooleanMeta
import com.vjh0107.barcode.advancement.services.ConditionManager
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player

class IsFinishedMeta(
    private val isFinished: Boolean,
    private val conditions: ConfigurationSection?
) : BooleanMeta {

    private val conditionsSet: MutableSet<ConditionData> = mutableSetOf()

    init {
        conditions?.getKeys(false)?.mapNotNull { key ->
            val conditionList = conditions.getConfigurationSection("$key.conditions")
            val description = conditions.getBoolean("$key.isFinished")
            conditionsSet.add(ConditionData.of(conditionList, description.toString()))
        }
    }

    override fun getResult(player: Player): Boolean {
        return let {
            conditionsSet.forEach {
                if (ConditionManager.evaluateCondition(player, it.conditions)) {
                    return@let it.value.toBoolean()
                }
            }
            return@let this.isFinished
        }
    }
}