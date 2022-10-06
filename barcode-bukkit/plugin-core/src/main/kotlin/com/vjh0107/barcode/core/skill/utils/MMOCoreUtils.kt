package com.vjh0107.barcode.core.skill.utils

import net.Indyuce.mmocore.api.player.PlayerData
import net.Indyuce.mmocore.skill.Skill

//배운 스킬 가져오기
@Suppress("unused")
fun PlayerData.getLearnedSkills() : Collection<Skill.SkillInfo> {
    val skills = this.profess.skills
    val skillsSet = LinkedHashSet<Skill.SkillInfo>()
    skills.forEach { skillInfo ->
        if(this.level < skillInfo.unlockLevel) return@forEach
        skillsSet.add(skillInfo)
    }
    return skillsSet
}