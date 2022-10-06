package com.vjh0107.barcode.cutscene.data.player

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.database.player.PlayerIDWrapper
import com.vjh0107.barcode.framework.database.player.repository.AbstractPlayerDataRepository

class CutscenePlayerDataRepository(plugin: AbstractBarcodePlugin) : AbstractPlayerDataRepository<CutscenePlayerData>(plugin) {
    override fun createPlayerDataInstance(id: PlayerIDWrapper): CutscenePlayerData {
        return CutscenePlayerData.of(id)
    }
}