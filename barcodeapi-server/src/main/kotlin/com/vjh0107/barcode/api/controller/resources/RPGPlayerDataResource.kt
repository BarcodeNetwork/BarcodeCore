package com.vjh0107.barcode.api.controller.resources

import com.vjh0107.barcode.framework.serialization.serializers.UUIDSerializer
import io.ktor.resources.*
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@Resource("/rpgPlayerData")
class RPGPlayerDataResource {

    @Serializable
    @Resource("{id}/{index}")
    class ByIndex(
        val parent: RPGPlayerDataResource = RPGPlayerDataResource(),
        @Serializable(with = UUIDSerializer::class) val id: UUID,
        val index: Int
    )

    @Serializable
    @Resource("{id}")
    class ByProfileID(
        val parent: RPGPlayerDataResource = RPGPlayerDataResource(),
        @Serializable(with = UUIDSerializer::class) val id: UUID
    )
}