package com.vjh0107.barcode.core.meister.repository

import com.vjh0107.barcode.core.meister.data.MeisterSkillPlayerData
import org.bukkit.entity.Player

interface MeisterSkillRepository {
    fun getPlayerData(player: Player): MeisterSkillPlayerData
}