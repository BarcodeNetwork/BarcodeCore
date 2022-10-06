package com.vjh0107.barcode.core.dependencies.advancement.conditions

import com.vjh0107.barcode.advancement.api.condition.impl.AbstractAdvancementCondition
import org.betonquest.betonquest.BetonQuest
import org.betonquest.betonquest.utils.PlayerConverter
import org.bukkit.entity.Player

class BetonQuestTagCondition(key: String) : AbstractAdvancementCondition(key) {
    override fun evaluate(player: Player, conditionString: String): Boolean {
        val playerId = PlayerConverter.getID(player)

        return if (conditionString.startsWith("!")) {
            val replacedCondition = conditionString.replace("!", "")
            val evaluate = !BetonQuest.getInstance().getPlayerData(playerId).hasTag(replacedCondition)
            evaluate
        } else {
            val evaluate = BetonQuest.getInstance().getPlayerData(playerId).hasTag(conditionString)
            evaluate
        }
    }
}