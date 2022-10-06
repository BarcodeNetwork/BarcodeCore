package com.vjh0107.barcode.core.dependencies.betonquests.variables.mythicmobs

import com.vjh0107.barcode.core.dependencies.mythicmobs.adapters.MythicMobsAdapter
import org.betonquest.betonquest.Instruction
import org.betonquest.betonquest.api.Variable

class MythicMobsInSpawnerAmount(instruction: Instruction) : Variable(instruction) {
    private val spawnerID: String = instruction.next()

    override fun getValue(playerID: String?): String {
        val spawner = MythicMobsAdapter
            .inst()
            .spawnerManager
            .getSpawnerByName(spawnerID) ?: throw NullPointerException("spawner $spawnerID not found")
        return spawner.numberOfMobs.toString()
    }
}