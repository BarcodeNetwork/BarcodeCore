package com.vjh0107.barcode.core.database.player.root.referencer

import com.vjh0107.barcode.core.database.player.api.referencer.AbstractRefTable
import com.vjh0107.barcode.core.database.player.root.RootPlayerDataTable
import com.vjh0107.barcode.framework.database.player.multiprofile.ProfileID
import org.jetbrains.exposed.sql.ReferenceOption

abstract class RootPlayerDataRefTable(name: String) : AbstractRefTable<ProfileID>(
    name,
    "profile_id",
    RootPlayerDataTable.profileID,
    ReferenceOption.CASCADE,
    ReferenceOption.RESTRICT
)