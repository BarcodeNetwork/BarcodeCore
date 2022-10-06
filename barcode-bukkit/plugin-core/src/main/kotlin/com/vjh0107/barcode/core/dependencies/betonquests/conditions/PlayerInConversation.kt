package com.vjh0107.barcode.core.dependencies.betonquests.conditions

import org.betonquest.betonquest.Instruction
import org.betonquest.betonquest.api.Condition
import org.betonquest.betonquest.conversation.Conversation

class PlayerInConversation(instruction: Instruction) : Condition(instruction, false) {
    private val conversationID: String = instruction.getOptional("id")

    override fun execute(playerID: String): Boolean {
        val conversation = Conversation.getConversation(playerID) ?: return false
        return conversation.id == conversationID
    }
}

