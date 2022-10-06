package com.vjh0107.barcode.cutscene.npc.dto

import com.vjh0107.barcode.framework.serialization.serializers.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class GameProfileWrapper(
    @SerialName("id")
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID,

    @SerialName("name")
    val name: String,

    @SerialName("properties")
    val properties: List<Properties>
) {
    @Serializable
    data class Properties(
        @SerialName("name")
        val name: String,

        @SerialName("value")
        val value: String,

        @SerialName("signature")
        val signature: String
    )
}
