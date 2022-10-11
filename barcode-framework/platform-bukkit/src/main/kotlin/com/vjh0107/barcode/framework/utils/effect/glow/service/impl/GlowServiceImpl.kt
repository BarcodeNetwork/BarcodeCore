package com.vjh0107.barcode.framework.utils.effect.glow.service.impl

import com.vjh0107.barcode.framework.koin.injector.inject
import com.vjh0107.barcode.framework.utils.effect.glow.repository.GlowRepository
import com.vjh0107.barcode.framework.utils.effect.glow.service.GlowService
import io.ktor.http.*
import org.bukkit.ChatColor
import org.bukkit.NamespacedKey
import org.bukkit.entity.Entity
import org.bukkit.persistence.PersistentDataType
import org.koin.core.annotation.Single
import org.koin.core.parameter.parametersOf

@Single(binds = [GlowService::class])
class GlowServiceImpl(private val repository: GlowRepository) : GlowService {
    private val glowKey: NamespacedKey by inject { parametersOf("GlowColor") }

    override fun setGlowing(entity: Entity, color: ChatColor) {
        repository.getTeam(color).addEntry(entity.uniqueId.toString())
        entity.persistentDataContainer.set(glowKey, PersistentDataType.STRING, color.name)
        entity.isGlowing = true
    }

    override fun disableGlowing(entity: Entity) {
        entity.persistentDataContainer.remove(glowKey)
        entity.isGlowing = false
    }
}