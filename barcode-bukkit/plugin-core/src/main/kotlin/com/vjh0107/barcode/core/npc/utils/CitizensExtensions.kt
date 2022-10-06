package com.vjh0107.barcode.core.npc.utils

import net.citizensnpcs.api.CitizensAPI
import org.bukkit.entity.Entity

fun Entity.isNPC(): Boolean {
    return CitizensAPI.getNPCRegistry().isNPC(this)
}