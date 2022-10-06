package com.vjh0107.barcode.framework.database.exposed.column.impl

import com.vjh0107.barcode.framework.database.exposed.column.AbstractIdentifierColumnType
import com.vjh0107.barcode.framework.database.player.multiprofile.ProfileID

class ProfileIDColumnType : AbstractIdentifierColumnType<ProfileID>() {
    override fun valueFromDB(value: Any): ProfileID = ProfileID(uuidValueFromDB(value))
}