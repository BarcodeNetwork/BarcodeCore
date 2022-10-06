package com.vjh0107.barcode.core.indicator

import org.bukkit.entity.Player
import kotlin.math.roundToInt

class PlayerBuffIndicatorData(val player: Player) {
    val indicators = mutableMapOf<String, BuffIndicator>()

    fun removeBuffIndicatorCooldown(key: String) {
        this.indicators[key]?.close()
    }

    fun setBuffIndicatorCooldown(icon: String, cooldown: Double, isDebuff: Boolean, key: String) {
        setBuffIndicatorCooldown(icon, cooldown, isDebuff, key) {}
    }

    fun setBuffIndicatorCooldown(icon: String, cooldown: Double, isDebuff: Boolean, key: String, action: () -> Unit) {
        val doubleToTick: Int = (cooldown * 20).roundToInt()
        val indicators = this.indicators[key]
        if (indicators != null) {
            indicators.run {
                this.icon = icon
                this.second = cooldown.toInt()
                this.onEnd = action
            }
        } else {
            this.indicators[key] = BuffIndicator(player, key, icon, doubleToTick, isDebuff, action)
        }
    }

    fun clearBuffIndicatorDeBuffs() {
        indicators.filter { it.value.isDebuff }.forEach { (_, buffIndicator) ->
            buffIndicator.close()
        }
    }
}