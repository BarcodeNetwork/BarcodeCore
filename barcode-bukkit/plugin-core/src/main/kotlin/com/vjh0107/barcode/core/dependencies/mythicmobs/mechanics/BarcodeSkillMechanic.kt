package com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics

import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent
import io.lumine.mythic.core.skills.SkillMechanic

abstract class BarcodeSkillMechanic(val line: String, val mlc: MythicLineConfig) : SkillMechanic(MythicBukkit.inst().skillManager, line, mlc), IBarcodeSkillMechanic {
    constructor(event: MythicMechanicLoadEvent) : this(event.mechanicName, event.config)
}