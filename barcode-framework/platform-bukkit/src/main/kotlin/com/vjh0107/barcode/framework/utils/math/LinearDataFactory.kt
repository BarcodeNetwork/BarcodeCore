package com.vjh0107.barcode.framework.utils.math

import org.bukkit.configuration.ConfigurationSection

fun LinearData.Companion.of(config: ConfigurationSection): LinearData {
    val base = config.getDouble("base")
    val perLevel = config.getDouble("per-level")
    val min = if (config.contains("min")) config.getDouble("min") else 0.0
    val max = if (config.contains("max")) config.getDouble("max") else 0.0
    return of(base, perLevel, min, max)
}