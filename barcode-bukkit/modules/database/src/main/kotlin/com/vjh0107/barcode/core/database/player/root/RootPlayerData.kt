package com.vjh0107.barcode.core.database.player.root

import com.vjh0107.barcode.framework.serialization.SerializableData
import com.vjh0107.barcode.framework.serialization.deserializeCollection
import com.vjh0107.barcode.framework.serialization.serializers.LocalDateTimeSerializer
import com.vjh0107.barcode.framework.serialization.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.*

@Serializable
data class RootPlayerData(
    @Serializable(with = UUIDSerializer::class) val minecraftPlayerID: UUID,
    @Serializable(with = UUIDSerializer::class) val profileID: UUID,
    @Serializable(with = LocalDateTimeSerializer::class) val createdAt: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class) val updatedAt: LocalDateTime,
    val nicknames: List<String>
) : SerializableData {
    companion object {
        fun of(entity: RootPlayerDataEntity): RootPlayerData {
            return RootPlayerData(
                entity.playerID.id,
                entity.profileID.id,
                entity.createdAt,
                entity.updatedAt,
                entity.nicknames.deserializeCollection()
            )
        }
    }
}