package com.vjh0107.barcode.framework.database.player.multiprofile

import com.vjh0107.barcode.framework.serialization.serializers.UUIDSerializer
import com.vjh0107.barcode.framework.database.player.Identifier
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class ProfileID internal constructor (
    @Serializable(with = UUIDSerializer::class)
    override val id: UUID
) : Identifier {
    companion object {
        fun of(uuid: UUID): ProfileID {
            return ProfileID(uuid)
        }
    }
}