package com.vjh0107.barcode.core.dependencies.betonquests.conditions

import com.vjh0107.barcode.core.dependencies.mmo.mmocore.utils.isDuringCrafting
import net.Indyuce.mmoitems.api.player.PlayerData
import org.betonquest.betonquest.Instruction
import org.betonquest.betonquest.api.Condition
import org.betonquest.betonquest.utils.PlayerConverter
import org.bukkit.entity.Player

class IsStationsDuring(instruction: Instruction) : Condition(instruction, false) {
    private val stationId: String = instruction.getOptional("id")

    override fun execute(playerID: String): Boolean {
        val player: Player = PlayerConverter.getPlayer(playerID) ?: return false
        val mmoItemsPlayerData = PlayerData.get(player)
        return mmoItemsPlayerData.isDuringCrafting(stationId)
    }
}

