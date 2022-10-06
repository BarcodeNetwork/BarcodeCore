package com.vjh0107.barcode.framework.serialization

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun <T> Map<String, T>.serialize(): String {
    return Json.encodeToString(this)
}

fun <T> deserializeMap(map: String): Map<String, T> {
    return Json.decodeFromString(map)
}