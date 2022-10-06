package com.vjh0107.barcode.core.dependencies.betonquests.events

import com.vjh0107.barcode.core.dependencies.BigDoorsAdapter
import org.betonquest.betonquest.Instruction
import org.betonquest.betonquest.api.QuestEvent

class SetDoorState(instruction: Instruction) : QuestEvent(instruction, true) {
    val doorID: Long = instruction.long
    val state: String = instruction.next()

    override fun execute(playerID: String): Void? {
        BigDoorsAdapter.setDoorState(getState(state), doorID)
        return null
    }

    private fun getState(string: String): Boolean {
        return when(state) {
            "open" -> true
            "close" -> false
            else -> throw IllegalArgumentException("open or close expected but inferred is $string ")
        }
    }
}