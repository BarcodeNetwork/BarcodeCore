package com.vjh0107.barcode.framework.utils.effect.glow.repository

import org.bukkit.ChatColor
import org.bukkit.scoreboard.Team

interface GlowRepository {
    /**
     * team 을 nullable 하게 구합니다.
     */
    fun findTeam(color: ChatColor): Team?

    /**
     * team 을 구합니다.
     *
     * @throws NullPointerException 색상에 맞는 team 이 없을 때 (상수이기 때문에 버전만 맞으면 거의 뜰 가능성이 없음)
     * @throws IllegalArgumentException 인자가 색상이 아닐 때
     */
    fun getTeam(color: ChatColor): Team
}