package com.vjh0107.barcode.core.meister

import net.Indyuce.mmocore.api.player.PlayerData
import org.bukkit.entity.Player

enum class MeisterProfessions {
    METALLURGY,
    ALCHEMY;

    val id: String get() = this.name.lowercase()

    companion object {
        fun getMeisterLevel(player: Player) : Int {
            val mmoCorePlayerData: PlayerData = PlayerData.get(player)
            var resultLevel = 0
            MeisterProfessions.values().forEach {
                resultLevel += mmoCorePlayerData.collectionSkills.getLevel(it.id)
            }
            return resultLevel
        }
    }
}