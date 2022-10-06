package com.vjh0107.barcode.core.dependencies.mythicmobs.targeters

import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.core.skills.targeters.ILocationSelector

abstract class BarcodeLocationSelector(mlc: MythicLineConfig) : ILocationSelector(MythicBukkit.inst().skillManager, mlc), IBarcodeTargeter {
}