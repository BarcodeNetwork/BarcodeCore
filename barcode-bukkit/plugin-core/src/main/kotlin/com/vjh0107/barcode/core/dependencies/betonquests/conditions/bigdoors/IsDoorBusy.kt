package com.vjh0107.barcode.core.dependencies.betonquests.conditions.bigdoors

import com.vjh0107.barcode.core.dependencies.BigDoorsAdapter
import org.betonquest.betonquest.Instruction
import org.betonquest.betonquest.api.Condition

class IsDoorBusy(instruction: Instruction) : Condition(instruction, false) {
    private val doorID: Long = instruction.long

    override fun execute(playerID: String): Boolean {
        return BigDoorsAdapter.isDoorBusy(doorID)
    }
}

