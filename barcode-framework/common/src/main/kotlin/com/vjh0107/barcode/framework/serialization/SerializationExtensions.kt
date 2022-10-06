package com.vjh0107.barcode.framework.serialization

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * @see SerializableData
 */
inline fun <reified T : SerializableData> T.serialize(json: Json = Json): String {
    return json.encodeToString(this)
}

/**
 * @see Collection
 */
inline fun <reified T : Collection<*>> T.serialize(json: Json = Json): String {
    return json.encodeToString(this)
}

/**
 * @see Map
 */
inline fun <reified T : Map<*, *>> T.serialize(json: Json = Json): String {
    return json.encodeToString(this)
}

/**
 * @see SerializableData
 */
inline fun <reified T : SerializableData> String.deserialize(json: Json = Json): T {
    return json.decodeFromString(this)
}

/**
 * @see Collection
 */
inline fun <reified T : Collection<*>> String.deserializeCollection(json: Json = Json): T {
    return json.decodeFromString(this)
}

/**
 * @see Map
 */
inline fun <reified T : Map<*, *>> String.deserializeMap(json: Json = Json): T {
    return json.decodeFromString(this)
}