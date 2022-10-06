package com.vjh0107.barcode.core.dependencies.betonquests.events

import com.vjh0107.barcode.core.gps.GPSComponent
import org.betonquest.betonquest.Instruction
import org.betonquest.betonquest.api.QuestEvent
import org.betonquest.betonquest.utils.PlayerConverter

class GPSStartEvent(instruction: Instruction) : QuestEvent(instruction, true) {
    val name: String = instruction.getOptional("name")

    override fun execute(playerID: String): Void? {
        val player = PlayerConverter.getPlayer(playerID) ?: return null
        GPSComponent.instance.gpsAPI.startGPS(player, name)
        return null
    }
}