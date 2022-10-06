package com.vjh0107.barcode.core.itembox.entity

import com.vjh0107.barcode.core.database.player.root.referencer.RootPlayerDataRefEntity
import com.vjh0107.barcode.core.database.player.root.referencer.RootPlayerDataRefEntityClass
import com.vjh0107.barcode.core.database.player.root.referencer.RootPlayerDataRefTable
import com.vjh0107.barcode.framework.database.exposed.extensions.json
import com.vjh0107.barcode.framework.database.player.multiprofile.ProfileID
import org.jetbrains.exposed.dao.id.EntityID

class ItemBoxPlayerDataEntity(id: EntityID<ProfileID>) : RootPlayerDataRefEntity(id) {
    companion object : RootPlayerDataRefEntityClass<ItemBoxPlayerDataEntity>(ItemBoxPlayerDataTable)

    var itemBox by ItemBoxPlayerDataTable.itemBox
}

object ItemBoxPlayerDataTable : RootPlayerDataRefTable("barcodecore_player_rpg") {
    val itemBox = json("item_box")
}
