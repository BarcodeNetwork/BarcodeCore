package com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics

import io.lumine.mythic.api.skills.ISkillMechanic
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent

interface IBarcodeSkillMechanic : ISkillMechanic {
    fun register(event: MythicMechanicLoadEvent) {
        event.register(this)
    }
}