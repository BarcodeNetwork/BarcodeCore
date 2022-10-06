package com.vjh0107.barcode.core.database.player

import com.vjh0107.barcode.core.gps.data.PlayerGPS
import com.vjh0107.barcode.core.item.data.player.PlayerItemBox
import com.vjh0107.barcode.core.meister.data.PlayerMeisterSkill

interface SavableCorePlayerData {
    var meisterSkill: PlayerMeisterSkill
    var itemBox: PlayerItemBox
    var gps: PlayerGPS
}