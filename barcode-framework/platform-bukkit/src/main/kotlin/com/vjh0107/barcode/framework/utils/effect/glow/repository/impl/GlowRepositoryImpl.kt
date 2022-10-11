package com.vjh0107.barcode.framework.utils.effect.glow.repository.impl

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeRepository
import com.vjh0107.barcode.framework.koin.annotation.BarcodeSingleton
import com.vjh0107.barcode.framework.utils.effect.glow.repository.GlowRepository
import org.bukkit.ChatColor
import org.bukkit.NamespacedKey
import org.bukkit.scoreboard.Team

@BarcodeSingleton(binds = [GlowRepository::class])
@BarcodeComponent
class GlowRepositoryImpl(private val plugin: AbstractBarcodePlugin) : BarcodeRepository, GlowRepository {
    private val registeredTeams: MutableMap<ChatColor, Team> = mutableMapOf()

    companion object {
        private var colorId: Int = 0

        private fun getTeamName(color: ChatColor): String {
            validateIsColor(color)
            return "barcode_glow_${colorId++}"
        }

        private fun validateIsColor(color: ChatColor) {
            if (!color.isColor) {
                throw IllegalArgumentException("$color 는 색상이 아닙니다.")
            }
        }
    }

    override fun load() {
        val scoreboard = plugin.server.scoreboardManager.mainScoreboard

        ChatColor.values().forEach { color ->
            if (color.isColor) {
                val teamName = getTeamName(color)
                val team = scoreboard.getTeam(teamName) ?: scoreboard.registerNewTeam(teamName)
                team.color = color
                registeredTeams[color] = team
            }
        }
    }

    override fun close() {
        registeredTeams.forEach { (_, team) ->
            team.unregister()
        }
    }

    override fun findTeam(color: ChatColor): Team? {
        return registeredTeams[color]
    }

    override fun getTeam(color: ChatColor): Team {
        validateIsColor(color)
        return registeredTeams[color] ?: throw NullPointerException("$color 는 등록되지 않았습니다.")
    }
}