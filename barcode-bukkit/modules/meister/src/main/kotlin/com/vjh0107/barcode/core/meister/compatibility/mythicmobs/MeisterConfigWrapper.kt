package com.vjh0107.barcode.core.meister.compatibility.mythicmobs

import com.vjh0107.barcode.framework.serialization.data.SoundWrapper
import io.lumine.mythic.core.mobs.ActiveMob

class MeisterConfigWrapper(val activeMob: ActiveMob) {
    private val soundPath: String = activeMob.type.config.getString(SOUND_PATH, "")
    private val soundVolume: Double = activeMob.type.config.getDouble(SOUND_VOLUME, 0.0)
    private val soundPitch: Double = activeMob.type.config.getDouble(SOUND_PITCH, 0.0)

    val meisterType: String = activeMob.type.config.getString(MEISTER_TYPE)
    val isMeisterMob: Boolean = activeMob.type.config.getBoolean(IS_MEISTER_MOB)

    fun getSound(): SoundWrapper {
        return SoundWrapper.of(soundPath, soundVolume, soundPitch)
    }


    companion object ConfigPath {
        const val SOUND_PATH = "Barcode.MeisterSound.Path"
        const val SOUND_VOLUME = "Barcode.MeisterSound.Volume"
        const val SOUND_PITCH = "Barcode.MeisterSound.Pitch"
        const val MEISTER_TYPE = "Barcode.MeisterType"
        const val IS_MEISTER_MOB = "Barcode.IsMeisterMob"
    }
}