package com.vjh0107.barcode.core.database.player.root.referencer

import com.vjh0107.barcode.core.database.player.api.referencer.AbstractRefEntity
import com.vjh0107.barcode.framework.database.player.multiprofile.ProfileID
import org.jetbrains.exposed.dao.id.EntityID

abstract class RootPlayerDataRefEntity(id: EntityID<ProfileID>) : AbstractRefEntity<ProfileID>(id)