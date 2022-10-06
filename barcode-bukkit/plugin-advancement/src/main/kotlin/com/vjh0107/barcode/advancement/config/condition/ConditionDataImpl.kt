package com.vjh0107.barcode.advancement.config.condition

import org.bukkit.configuration.ConfigurationSection

data class ConditionDataImpl(
    override val conditions: ConfigurationSection?,
    override val value: String
) : ConditionData