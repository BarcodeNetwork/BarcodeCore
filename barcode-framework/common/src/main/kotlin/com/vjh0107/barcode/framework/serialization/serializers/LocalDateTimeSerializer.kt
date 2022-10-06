package com.vjh0107.barcode.framework.serialization.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    private val timeZone = ZoneId.systemDefault()

    override val descriptor = PrimitiveSerialDescriptor("LocalDateTimeSerializer", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeLong(value.atZone(timeZone).toInstant().toEpochMilli())
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(decoder.decodeLong()), timeZone)
    }
}