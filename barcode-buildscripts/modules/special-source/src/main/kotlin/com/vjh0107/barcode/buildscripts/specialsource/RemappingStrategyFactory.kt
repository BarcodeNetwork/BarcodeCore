package com.vjh0107.barcode.buildscripts.specialsource

import com.vjh0107.barcode.buildscripts.specialsource.models.Mappings
import com.vjh0107.barcode.buildscripts.specialsource.models.Spigots


object RemappingStrategyFactory {
    fun createMojangToObf(version: String): RemappingStrategy {
        return RemappingStrategy(Spigots.REMAPPED_MOJANG, Mappings.MOJANG, version, true)
    }

    fun createObfToSpigot(version: String): RemappingStrategy {
        return RemappingStrategy(Spigots.REMAPPED_OBF, Mappings.SPIGOT, version, false)
    }
}