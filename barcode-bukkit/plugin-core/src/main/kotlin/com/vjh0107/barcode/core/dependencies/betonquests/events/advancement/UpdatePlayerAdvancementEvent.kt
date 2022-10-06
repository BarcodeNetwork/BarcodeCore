package com.vjh0107.barcode.core.dependencies.betonquests.events.advancement

import com.vjh0107.barcode.advancement.BarcodeAdvancementPlugin
import org.betonquest.betonquest.Instruction
import org.betonquest.betonquest.api.QuestEvent
import org.betonquest.betonquest.utils.PlayerConverter

class UpdatePlayerAdvancementEvent(instruction: Instruction) : QuestEvent(instruction, false) {
    override fun execute(playerID: String): Void? {
        val player = PlayerConverter.getPlayer(playerID) ?: return null
        val playerDataManager = BarcodeAdvancementPlugin.instance.playerRenderersManager.get(player)
        playerDataManager.loadAsynchronously()
        return null
    }
}