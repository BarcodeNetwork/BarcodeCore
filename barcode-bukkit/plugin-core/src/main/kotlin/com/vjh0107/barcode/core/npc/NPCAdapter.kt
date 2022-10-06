package com.vjh0107.barcode.core.npc

import net.citizensnpcs.api.npc.NPC
import net.citizensnpcs.api.npc.NPCRegistry
import org.bukkit.entity.EntityType

interface NPCAdapter {
    fun getNPCRegistry(): NPCRegistry

    fun createNPC(name: String, entityType: EntityType = EntityType.PLAYER): NPC {
        return getNPCRegistry().createNPC(entityType, name)
    }
}