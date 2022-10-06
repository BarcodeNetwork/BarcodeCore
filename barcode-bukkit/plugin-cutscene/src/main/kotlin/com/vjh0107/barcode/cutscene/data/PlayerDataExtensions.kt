package com.vjh0107.barcode.cutscene.data

import com.vjh0107.barcode.framework.exceptions.playerdata.PlayerDataNotFoundException
import com.vjh0107.barcode.cutscene.data.player.CutscenePlayerData
import org.bukkit.entity.Player

fun Player.getCutscenePlayerData(): CutscenePlayerData {
    return this.getNullableCutscenePlayerData() ?: throw PlayerDataNotFoundException(this)
}

fun Player.getNullableCutscenePlayerData(): CutscenePlayerData? {
    return CutsceneDataComponent.instance
        .playerDataManager
        .getPlayerData(this)
}