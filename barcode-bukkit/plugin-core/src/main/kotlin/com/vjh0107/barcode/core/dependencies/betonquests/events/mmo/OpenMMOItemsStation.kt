package com.vjh0107.barcode.core.dependencies.betonquests.events.mmo

import net.Indyuce.mmoitems.MMOItems
import net.Indyuce.mmoitems.gui.CraftingStationView
import org.betonquest.betonquest.Instruction
import org.betonquest.betonquest.api.QuestEvent
import org.betonquest.betonquest.utils.PlayerConverter
import org.bukkit.entity.Player

class OpenMMOItemsStation(instruction: Instruction) : QuestEvent(instruction, true) {
    val name: String = instruction.getOptional("name")

    override fun execute(playerID: String): Void? {
        val player: Player = PlayerConverter.getPlayer(playerID) ?: return null
        CraftingStationView(
            player,
            MMOItems.plugin.crafting.getStation(
                name.lowercase().replace("_", "-")
            ),
            1
        ).open()
        return null
    }
}
