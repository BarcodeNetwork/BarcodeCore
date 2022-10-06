package com.vjh0107.barcode.core.meister.data

import com.vjh0107.barcode.core.meister.MeisterSkills
import com.vjh0107.barcode.framework.serialization.SerializableData
import kotlinx.serialization.Serializable

@Serializable
data class PlayerMeisterSkill private constructor (
    val skills: MutableMap<String, Int> = mutableMapOf(),
    var skillPoints: Int = 0,
    var resetPoints: Int = 0
) : SerializableData {
    companion object {
        fun init() : PlayerMeisterSkill {
            return PlayerMeisterSkill().apply {
                this.init()
            }
        }
    }

    fun init() {
        MeisterSkills.values().forEach {
            this.setSkillLevel(it, 0)
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
        init()
    }
}