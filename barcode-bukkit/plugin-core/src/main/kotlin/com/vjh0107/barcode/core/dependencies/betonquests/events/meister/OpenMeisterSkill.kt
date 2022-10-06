package com.vjh0107.barcode.core.dependencies.betonquests.events.meister

import com.vjh0107.barcode.core.meister.gui.PlayerMeisterInventory
import org.betonquest.betonquest.Instruction
import org.betonquest.betonquest.api.QuestEvent
import org.betonquest.betonquest.utils.PlayerConverter
import org.bukkit.entity.Player

class OpenMeisterSkill(instruction: Instruction) : QuestEvent(instruction, false) {
    override fun execute(playerID: String): Void? {
        val player: Player = PlayerConverter.getPlayer(playerID) ?: return null
        PlayerMeisterInventory(player).open()
        return null
    }
}
