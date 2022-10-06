package com.vjh0107.barcode.core.dependencies.mythicmobs.adapters

import org.bukkit.entity.Entity

object MythicMobsAPIAdapter {
    fun castSkill(entity: Entity, skillID: String) {
        MythicMobsAdapter.inst().apiHelper.castSkill(entity, skillID)
    }
}