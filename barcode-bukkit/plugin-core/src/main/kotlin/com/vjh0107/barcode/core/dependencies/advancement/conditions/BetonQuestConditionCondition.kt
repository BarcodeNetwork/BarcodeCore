package com.vjh0107.barcode.core.dependencies.advancement.conditions

import com.vjh0107.barcode.advancement.api.condition.impl.AbstractAdvancementCondition
import org.betonquest.betonquest.BetonQuest
import org.betonquest.betonquest.id.ConditionID
import org.betonquest.betonquest.utils.PlayerConverter
import org.bukkit.entity.Player

class BetonQuestConditionCondition(key: String) : AbstractAdvancementCondition(key) {
    override fun evaluate(player: Player, conditionString: String): Boolean {
        val playerId = PlayerConverter.getID(player)
        val condition = ConditionID(null, conditionString)

        return BetonQuest.condition(playerId, condition)
    }
}