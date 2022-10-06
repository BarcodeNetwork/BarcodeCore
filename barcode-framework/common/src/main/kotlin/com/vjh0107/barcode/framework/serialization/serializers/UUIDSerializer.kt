package com.vjh0107.barcode.framework.serialization.serializers

import com.vjh0107.barcode.framework.utils.toUUID
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

object UUIDSerializer : KSerializer<UUID> {
    override val descriptor = PrimitiveSerialDescriptor("UUIDSerializer", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): UUID {
        val string = decoder.decodeString()
        return string.toUUID()
    }

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }
}
