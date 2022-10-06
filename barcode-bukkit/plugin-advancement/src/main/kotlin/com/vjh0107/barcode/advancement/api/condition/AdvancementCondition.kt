package com.vjh0107.barcode.advancement.api.condition

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player

interface AdvancementCondition {
    /**
     * config key 입니다.
     *
     * conditions:
     *   <key>:
     *   - <condition1>
     *   - <condition2>
     *   - ...
     */
    val key: String

    /**
     * 단일 라인 컨디션 체크입니다.
     */
    fun evaluate(player: Player, conditionString: String): Boolean

    /**
     * evaluate 을 줄마다 한 후, 결과를 반환합니다.
     */
    fun evaluateAll(player: Player, conditions: ConfigurationSection?): Boolean
}