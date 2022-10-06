package com.vjh0107.barcode.core.dependencies.betonquests.events

//import com.vjh0107.barcode.cutscene.api.CutsceneAPI
import org.betonquest.betonquest.Instruction
import org.betonquest.betonquest.api.QuestEvent
import org.betonquest.betonquest.utils.PlayerConverter

class BarcodeCutscene(instruction: Instruction) : QuestEvent(instruction, true) {
    val name: String = instruction.getOptional("name")
    val type: Types = Types.valueOf(instruction.getOptional("type")?.uppercase() ?: "BARCODE")

    override fun execute(playerID: String): Void? {
        val player = PlayerConverter.getPlayer(playerID) ?: return null
//        when (type) {
//            Types.BARCODE -> CutsceneAPI.startCutscene(name, player, true)
//            Types.ORIGINAL -> CutsceneAPI.startCutscene(name, player, false)
//        }
        return null
    }

    enum class Types {
        BARCODE,
        ORIGINAL
    }
}