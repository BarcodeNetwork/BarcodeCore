package com.vjh0107.barcode.core.meister.service.impl

import com.vjh0107.barcode.core.meister.models.MeisterSkills
import com.vjh0107.barcode.core.meister.repository.MeisterSkillRepository
import com.vjh0107.barcode.core.meister.service.MeisterSkillService
import org.bukkit.entity.Player
import org.koin.core.annotation.Single

@Single(binds = [MeisterSkillService::class])
class MeisterSkillServiceImpl(private val repository: MeisterSkillRepository) : MeisterSkillService {
    override fun upgradeSkill(player: Player, skill: MeisterSkills) {
        repository.getPlayerData(player).skills.
    }
}