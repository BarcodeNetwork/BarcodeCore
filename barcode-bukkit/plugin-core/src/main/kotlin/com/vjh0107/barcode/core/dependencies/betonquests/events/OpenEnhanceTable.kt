package com.vjh0107.barcode.core.dependencies.betonquests.events

import com.vjh0107.barcode.core.blacksmith.enhance.models.EnhanceTableInv
import org.betonquest.betonquest.Instruction
import org.betonquest.betonquest.api.QuestEvent
import org.betonquest.betonquest.utils.PlayerConverter
import org.bukkit.entity.Player

class OpenEnhanceTable(instruction: Instruction) : QuestEvent(instruction, false) {
    override fun execute(playerID: String): Void? {
        val player: Player = PlayerConverter.getPlayer(playerID) ?: return null
        EnhanceTableInv.openEnhanceTable(player)
        return null
    }
}
