package com.vjh0107.barcode.core.gps.data

import com.vjh0107.barcode.framework.serialization.SerializableData
import kotlinx.serialization.Serializable

@Serializable
data class PlayerGPS private constructor(
    val gpsList: List<GPSData> = mutableListOf()
) : SerializableData {
    companion object {
        fun of(): PlayerGPS {
            return PlayerGPS()
        }
    }
}
