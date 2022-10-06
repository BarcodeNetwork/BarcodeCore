package com.vjh0107.barcode.core.meister.service

import com.vjh0107.barcode.core.meister.models.MeisterSkills
import org.bukkit.entity.Player

interface MeisterSkillService {
    fun getSkills(player: Player): List<MeisterSkills>

    fun upgradeSkill(player: Player, skill: MeisterSkills)
}