package com.vjh0107.barcode.framework.utils.effect.glow.service

import org.bukkit.ChatColor
import org.bukkit.entity.Entity

interface GlowService {
    /**
     * 엔티티에게 발광효과를 부여합니다.
     */
    fun setGlowing(entity: Entity, color: ChatColor)

    /**
     * 엔티티에게서 발광효과를 제거합니다.
     */
    fun disableGlowing(entity: Entity)
}