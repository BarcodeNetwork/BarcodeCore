package com.vjh0107.barcode.core.database.player.childs

import com.vjh0107.barcode.framework.serialization.SerializableData
import com.vjh0107.barcode.framework.serialization.deserializeCollection
import com.vjh0107.barcode.framework.serialization.deserializeMap
import kotlinx.serialization.Serializable

@Serializable
data class RPGPlayerData(
    val classID: String,
    val level: Int,
    val experience: Long,

    val skills: Map<String, Int>,
    val skillPoints: Int,
    val boundSkills: List<String>,

    val attributes: Map<String, Int>,
    val attributePoints: Int,
    val attributeResetPoints: Int
) : SerializableData {
    companion object {
        fun of(entity: RPGPlayerDataEntity): RPGPlayerData {
            return RPGPlayerData(
                entity.classID,
                entity.level,
                entity.experience,
                entity.skills.deserializeMap(),
                entity.skillPoints,
                entity.boundSkills.deserializeCollection(),
                entity.attributes.deserializeMap(),
                entity.attributePoints,
                entity.attributeResetPoints
            )
        }
    }
}