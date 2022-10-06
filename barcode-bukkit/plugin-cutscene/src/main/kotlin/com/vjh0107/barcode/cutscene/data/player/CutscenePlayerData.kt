package com.vjh0107.barcode.cutscene.data.player

import com.vjh0107.barcode.framework.database.player.data.PlayerData
import com.vjh0107.barcode.framework.database.player.PlayerIDWrapper
import com.vjh0107.barcode.framework.utils.isNull
import com.vjh0107.barcode.cutscene.Cutscene

class CutscenePlayerData private constructor (override val playerID: PlayerIDWrapper) : PlayerData {
    var cutscene: Cutscene? = null

    fun whenCutsceneStop() {
        cutscene = null
    }

    fun isInCutscene(): Boolean {
        return cutscene.isNull().not()
    }

    companion object {
        fun of(playerID: PlayerIDWrapper): CutscenePlayerData {
            return CutscenePlayerData(playerID)
        }
    }
}