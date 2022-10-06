package com.vjh0107.barcode.advancement.config.condition

import org.bukkit.configuration.ConfigurationSection

interface ConditionData {
    /**
     * conditions configuration section
     */
    val conditions: ConfigurationSection?

    /**
     * value or serialized value
     */
    val value: String

    companion object {
        fun of(conditions: ConfigurationSection?, descriptions: String): ConditionData {
            return ConditionDataImpl(conditions, descriptions)
        }
    }
}