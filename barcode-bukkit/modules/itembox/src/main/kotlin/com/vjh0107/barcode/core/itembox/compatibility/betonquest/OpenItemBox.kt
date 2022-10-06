package com.vjh0107.barcode.core.itembox.compatibility.betonquest

import com.vjh0107.barcode.core.itembox.ui.ItemBoxInventory
import com.vjh0107.barcode.framework.koin.injector.inject
import org.betonquest.betonquest.Instruction
import org.betonquest.betonquest.api.QuestEvent
import org.betonquest.betonquest.utils.PlayerConverter
import org.bukkit.entity.Player
import org.koin.core.parameter.parametersOf

class OpenItemBox(instruction: Instruction) : QuestEvent(instruction, false) {
    override fun execute(playerID: String): Void? {
        val player: Player = PlayerConverter.getPlayer(playerID) ?: return null
        val ui: ItemBoxInventory by inject { parametersOf(player) }
        ui.open()
        return null
    }
}
