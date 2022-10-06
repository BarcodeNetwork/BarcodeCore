package com.vjh0107.barcode.core.meister.data

import com.vjh0107.barcode.core.database.player.childs.MeisterPlayerDataEntity
import com.vjh0107.barcode.core.meister.models.MeisterSkills
import com.vjh0107.barcode.framework.database.player.data.AbstractSavablePlayerData
import com.vjh0107.barcode.framework.serialization.deserializeMap
import kotlinx.serialization.Serializable

@Serializable
data class MeisterSkillPlayerData private constructor (
    val skills: MutableMap<String, Int> = mutableMapOf(),
    var skillPoints: Int = 0,
    var resetPoints: Int = 0
) : AbstractSavablePlayerData() {

    companion object {
        fun of(entity: MeisterPlayerDataEntity) : MeisterSkillPlayerData {
            return MeisterSkillPlayerData(entity.skills.deserializeMap(), entity.skillPoints, entity.resetPoints)
        }
    }

    fun setSkillLevel(skill: MeisterSkills, level: Int) {
        skills[skill.id] = level
    }

    fun getSkillLevel(skill: MeisterSkills) : Int {
        return skills[skill.id] ?: 0
    }

    fun upgradeSkillLevel(skill: MeisterSkills) {
        setSkillLevel(skill, getSkillLevel(skill) + 1)
    }

    fun getAllSkillLevel() : Int {
        var result = 0
        skills.forEach { (_, level) ->
            result += level
        }
        return result
    }

    fun resetSkills() {
        skillPoints += getAllSkillLevel()
    }
}