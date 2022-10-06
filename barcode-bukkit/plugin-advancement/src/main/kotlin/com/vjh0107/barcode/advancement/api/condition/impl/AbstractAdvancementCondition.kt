package com.vjh0107.barcode.advancement.api.condition.impl

import com.vjh0107.barcode.advancement.api.condition.AdvancementCondition
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player

//this process will be run asynchronously
abstract class AbstractAdvancementCondition(override val key: String) : AdvancementCondition {
    abstract override fun evaluate(player: Player, conditionString: String): Boolean

    //config로 conditions의 상위 키를 넘겨주어야한다.
    final override fun evaluateAll(player: Player, conditions: ConfigurationSection?): Boolean {
        conditions ?: return true
        val conditionList = getConditionList(conditions) ?: return true
        conditionList.forEach { condition: String ->
            if (!evaluate(player, condition)) {
                return false
            }
        }
        return true
    }

    private fun getConditionList(conditions: ConfigurationSection): List<String> {
        return conditions.getStringList(key)
    }
}