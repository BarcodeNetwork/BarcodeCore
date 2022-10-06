package com.vjh0107.barcode.core.dependencies.mythicmobs.targeters

import io.lumine.mythic.api.skills.targeters.ISkillTargeter
import io.lumine.mythic.bukkit.events.MythicTargeterLoadEvent

interface IBarcodeTargeter : ISkillTargeter {
    fun register(event: MythicTargeterLoadEvent) {
        event.register(this)
    }
}