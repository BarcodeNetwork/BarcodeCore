package com.vjh0107.barcode.core.dependencies.magicspells.conditions

import com.nisovin.magicspells.castmodifiers.Condition
import net.Indyuce.mmocore.api.player.PlayerData
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player


class MMOCoreLevelRequiredCondition : Condition() {
    private var requiredLevel: Int = 0

    override fun initialize(value: String): Boolean {
        this.requiredLevel = value.toIntOrNull() ?: return false
        return true
    }

    override fun check(livingEntity: LivingEntity?): Boolean {
        if(livingEntity == null) return false
        val player: Player = livingEntity as? Player ?: return false
        val mmoCorePlayerData: PlayerData? = PlayerData.get(player) ?: return false
        val playerLevel = mmoCorePlayerData!!.level

        if (playerLevel < requiredLevel) return false
        return true
    }

    override fun check(p0: LivingEntity?, livingEntity: LivingEntity?): Boolean {
        return false
    }

    override fun check(p0: LivingEntity?, p1: Location?): Boolean {
        return false
    }

}