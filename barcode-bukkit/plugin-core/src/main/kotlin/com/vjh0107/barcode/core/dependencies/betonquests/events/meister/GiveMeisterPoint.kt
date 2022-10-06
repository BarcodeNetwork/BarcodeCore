package com.vjh0107.barcode.core.dependencies.betonquests.events.meister

import com.vjh0107.barcode.core.database.getCorePlayerData
import org.betonquest.betonquest.Instruction
import org.betonquest.betonquest.api.QuestEvent
import org.betonquest.betonquest.exceptions.InstructionParseException
import org.betonquest.betonquest.utils.PlayerConverter
import org.bukkit.entity.Player

class GiveMeisterPoint(instruction: Instruction) : QuestEvent(instruction, false) {
    val type = instruction.getOptional("type") ?: throw InstructionParseException("type:(reset|skill) 을 명시해주세요.")
    val amount = instruction.int

    override fun execute(playerID: String): Void? {
        val player: Player = PlayerConverter.getPlayer(playerID) ?: return null
        when (type) {
            "reset" -> player.getCorePlayerData().savableDataObject.meisterSkill.resetPoints += amount
            "skill" -> player.getCorePlayerData().savableDataObject.meisterSkill.skillPoints += amount
        }
        return null
    }
}
