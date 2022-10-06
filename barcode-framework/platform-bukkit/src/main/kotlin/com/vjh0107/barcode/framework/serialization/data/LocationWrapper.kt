package com.vjh0107.barcode.framework.serialization.data

import com.vjh0107.barcode.framework.serialization.SerializableData
import com.vjh0107.barcode.framework.serialization.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.*

@Serializable
data class LocationWrapper(
    @Serializable(with = UUIDSerializer::class) val world: UUID,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float
) : SerializableData {
    companion object {
        fun of(location: Location) : LocationWrapper {
            return LocationWrapper(location.world.uid, location.x, location.y, location.z, location.yaw, location.pitch)
        }
    }

    fun get() : Location {
        return Location(Bukkit.getWorld(world), x, y, z, yaw, pitch)
    }
}
