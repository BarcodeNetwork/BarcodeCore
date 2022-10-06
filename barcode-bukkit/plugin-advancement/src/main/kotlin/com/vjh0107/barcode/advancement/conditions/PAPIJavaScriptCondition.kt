package com.vjh0107.barcode.advancement.conditions

import com.vjh0107.barcode.advancement.api.condition.impl.AbstractAdvancementCondition
import com.vjh0107.barcode.advancement.services.ConditionManager
import com.vjh0107.barcode.framework.dependencies.placeholderapi.extensions.parseWithPAPI
import org.bukkit.entity.Player
import javax.script.ScriptException

class PAPIJavaScriptCondition(key: String) : AbstractAdvancementCondition(key) {
    override fun evaluate(player: Player, conditionString: String): Boolean {
        val parsedConditionString = conditionString.parseWithPAPI(player)
        val evaluate = try {
            ConditionManager.SCRIPT_ENGINE.eval(parsedConditionString)
        } catch (e: ScriptException) {
            throw IllegalArgumentException("JavaScript 규칙을 지켜서 작성해주세요.")
        }
        return evaluate.toString().toBoolean()
    }
}