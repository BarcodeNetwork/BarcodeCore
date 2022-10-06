package com.vjh0107.barcode.cutscene.api

import com.vjh0107.barcode.cutscene.Cutscene
import com.vjh0107.barcode.cutscene.datahandler.DataPath
import com.vjh0107.barcode.cutscene.utils.Standards
import org.bukkit.entity.Player

object CutsceneAPI {
    fun getCutscene(cutsceneID: String, player: Player): Cutscene {
        val dataPath = DataPath(cutsceneID, *Standards.PATH_TO_CUTSCENES)
        return Cutscene(dataPath, player)
    }

    fun startCutscene(cutsceneID: String, player: Player, movable: Boolean) {
        getCutscene(cutsceneID, player).startCutscene(0, movable)
    }
}