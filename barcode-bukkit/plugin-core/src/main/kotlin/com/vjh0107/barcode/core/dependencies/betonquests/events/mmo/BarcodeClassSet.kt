package com.vjh0107.barcode.core.dependencies.betonquests.events.mmo

import net.Indyuce.mmocore.MMOCore
import net.Indyuce.mmocore.api.player.PlayerData
import org.betonquest.betonquest.Instruction
import org.betonquest.betonquest.api.QuestEvent
import org.betonquest.betonquest.utils.PlayerConverter
import org.bukkit.ChatColor
import java.util.*

class BarcodeClassSet(instruction: Instruction) : QuestEvent(instruction, true) {
    val name: String = instruction.getOptional("name")

    override fun execute(playerID: String): Void? {
        val player = PlayerConverter.getPlayer(playerID)
        val mmoCorePlayerData = PlayerData.get(player)
        val format: String = name.uppercase(Locale.getDefault()).replace("-", "_")
        if (!MMOCore.plugin.classManager.has(format)) {
            print("Barcode BetonQuest, Exception at BarcodeClassSet: " + ChatColor.RED.toString() + "Could not find class " + format + ".")
            return null
        }

        val profess = MMOCore.plugin.classManager[format]
        mmoCorePlayerData.setClass(profess)
        return null
    }
}