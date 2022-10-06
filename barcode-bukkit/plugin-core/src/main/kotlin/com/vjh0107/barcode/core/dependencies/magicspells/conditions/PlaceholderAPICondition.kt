package com.vjh0107.barcode.core.dependencies.magicspells.conditions

import com.nisovin.magicspells.castmodifiers.Condition
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class PlaceholderAPICondition : Condition() {
    private var placeholder: String? = null
    private var value: String? = null

    override fun initialize(p0: String): Boolean {
        this.placeholder = p0.split("|")[0]
        this.value = p0.split("|")[1]
        return true
    }

    override fun check(livingEntity: LivingEntity?): Boolean {
        val livingEntityNotNull = livingEntity ?: return false
        val player: Player = Bukkit.getPlayerExact(livingEntityNotNull.name) ?: return false
        return PlaceholderAPI.setPlaceholders(player, "%$placeholder%") == value
    }

    override fun check(livingEntity: LivingEntity?, target: LivingEntity?): Boolean {
        val targetNotNull = target ?: return false
        val player: Player = Bukkit.getPlayerExact(targetNotNull.name) ?: return false
        return PlaceholderAPI.setPlaceholders(player, "%$placeholder%") == value

    }

    override fun check(livingEntity: LivingEntity?, location: Location?): Boolean {
        return false
    }

}
