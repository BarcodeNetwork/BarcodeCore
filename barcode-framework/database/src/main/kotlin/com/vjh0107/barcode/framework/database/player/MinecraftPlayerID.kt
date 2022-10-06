package com.vjh0107.barcode.framework.database.player

import com.vjh0107.barcode.framework.serialization.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class MinecraftPlayerID (
    @Serializable(with = UUIDSerializer::class)
    override val id: UUID
) : Identifier {
    companion object {
        fun of(uuid: UUID): MinecraftPlayerID {
            return MinecraftPlayerID(uuid)
        }
    }
}
