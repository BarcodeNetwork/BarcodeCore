package com.vjh0107.barcode.core.database.player

import com.vjh0107.barcode.core.gps.data.PlayerGPS
import com.vjh0107.barcode.core.item.data.player.PlayerItemBox
import com.vjh0107.barcode.core.meister.data.PlayerMeisterSkill
import com.vjh0107.barcode.framework.database.player.data.AbstractSavablePlayerData

abstract class SavableCorePlayerDataProvider : AbstractSavablePlayerData<SavableCorePlayerData>() {
    override val savableDataObject: SavableCorePlayerData = object : SavableCorePlayerData {
        override var meisterSkill: PlayerMeisterSkill = PlayerMeisterSkill.init()
        override var itemBox: PlayerItemBox = PlayerItemBox.of()
        override var gps: PlayerGPS = PlayerGPS.of()
    }

    protected abstract fun callAsyncCorePlayerDataLoadEvent()

    fun whenPostLoad() {
        isCompletelyLoaded = true
        callAsyncCorePlayerDataLoadEvent()
    }
}