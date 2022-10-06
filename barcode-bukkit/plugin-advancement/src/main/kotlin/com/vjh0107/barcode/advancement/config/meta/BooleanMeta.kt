package com.vjh0107.barcode.advancement.config.meta

import org.bukkit.entity.Player

interface BooleanMeta {
    fun getResult(player: Player): Boolean
}