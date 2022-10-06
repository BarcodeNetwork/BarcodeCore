package com.vjh0107.barcode.core.blacksmith.enhance

import org.bukkit.inventory.ItemStack

data class EnhanceInfo(
    var chance: Double = 0.0,
    var destroyChance: Double = 0.0,
    var volume: Double = 0.0,
    var gold: Double = 0.0,
    var calculatedEnhanceChance: Double = 0.0,
    var result: ItemStack? = null
) {
    fun addChance(value: Double) {
        chance += value;
        if (chance < 0.0) chance = 0.0
    }
    fun addDestroyChance(value: Double) {
        destroyChance += value
        if (destroyChance < 0.0) destroyChance = 0.0
    }
    fun addVolume(value: Double) {
        volume += value
        if (volume < 0.0) volume = 0.0
    }
}