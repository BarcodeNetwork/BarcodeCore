package com.vjh0107.barcode.framework.database.exposed.column.impl

import com.vjh0107.barcode.framework.database.exposed.column.AbstractIdentifierColumnType
import com.vjh0107.barcode.framework.database.player.MinecraftPlayerID

class PlayerIDColumnType : AbstractIdentifierColumnType<MinecraftPlayerID>() {
    override fun valueFromDB(value: Any): MinecraftPlayerID = MinecraftPlayerID(uuidValueFromDB(value))
}
