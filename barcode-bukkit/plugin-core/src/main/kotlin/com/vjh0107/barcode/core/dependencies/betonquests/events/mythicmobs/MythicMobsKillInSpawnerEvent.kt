package com.vjh0107.barcode.core.dependencies.betonquests.events.mythicmobs

import com.vjh0107.barcode.core.dependencies.mythicmobs.adapters.MythicMobsAdapter
import org.betonquest.betonquest.Instruction
import org.betonquest.betonquest.api.QuestEvent

class MythicMobsKillInSpawnerEvent(instruction: Instruction) : QuestEvent(instruction, true) {
    val id: String = instruction.getOptional("spawnerID")

    override fun execute(p0: String?): Void? {
        MythicMobsAdapter.inst().spawnerManager.getSpawnerByName(id).associatedMobs.forEach { uuid ->
            MythicMobsAdapter.inst().mobManager.getActiveMob(uuid).ifPresent { activeMob ->
                activeMob.remove()
            }
        }
        return null
    }
}