package com.vjh0107.barcode.core.gps.data

import com.vjh0107.barcode.framework.serialization.SerializableData
import kotlinx.serialization.Serializable

@Serializable
data class GPSData(
    val gpsID: String,
    val description: String
) : SerializableData
