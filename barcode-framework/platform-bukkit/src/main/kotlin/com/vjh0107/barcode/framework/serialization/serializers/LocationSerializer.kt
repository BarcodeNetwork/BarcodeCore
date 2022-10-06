package com.vjh0107.barcode.framework.serialization.serializers

import com.vjh0107.barcode.framework.serialization.data.LocationWrapper
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import org.bukkit.Location

object LocationSerializer : KSerializer<Location> {
    override val descriptor = PrimitiveSerialDescriptor("Bukkit.LocationSerializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Location) {
        val locationWrapper = LocationWrapper.of(value)
        encoder.encodeString(Json.encodeToString(locationWrapper))
    }

    override fun deserialize(decoder: Decoder): Location {
        val locationWrapper = Json.decodeFromString<LocationWrapper>(decoder.decodeString())
        return locationWrapper.get()
    }
}