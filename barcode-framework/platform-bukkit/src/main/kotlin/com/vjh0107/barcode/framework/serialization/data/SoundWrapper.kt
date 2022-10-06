package com.vjh0107.barcode.framework.serialization.data

import com.vjh0107.barcode.framework.serialization.SerializableData
import kotlinx.serialization.Serializable
import org.bukkit.Location
import org.bukkit.SoundCategory
import org.bukkit.World
import org.bukkit.entity.Entity

@Serializable
data class SoundWrapper(
    val sound: String,
    val volume: Float,
    val pitch: Float
) : SerializableData {

    fun playSound(entity: Entity, location: Location = entity.location, world: World = location.world) {
        world.playSound(location, sound, SoundCategory.MASTER, volume, pitch)
    }

    companion object {
        fun of(sound: String, volume: Float, pitch: Float) : SoundWrapper {
            return SoundWrapper(sound, volume, pitch)
        }

        fun of(sound: String, volume: Double, pitch: Double) : SoundWrapper {
            return SoundWrapper(sound, volume.toFloat(), pitch.toFloat())
        }
    }
}