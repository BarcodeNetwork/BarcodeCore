package com.vjh0107.barcode.framework.database.exposed.entity

import com.vjh0107.barcode.framework.database.player.MinecraftPlayerID
import com.vjh0107.barcode.framework.database.player.multiprofile.ProfileID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.id.EntityID

abstract class BarcodePlayerEntity(id: EntityID<Int>/*, barcodeIDTable: BarcodeIDTable*/) : IntEntity(id) {
    /**
     * ProfileID UUID
     */
    abstract var profileID: ProfileID
    //TODO: var profileID by barcodeIDTable.profileID

    /**
     * PlayerID (마인크래프트 UUID)
     */
    abstract var playerID: MinecraftPlayerID
}