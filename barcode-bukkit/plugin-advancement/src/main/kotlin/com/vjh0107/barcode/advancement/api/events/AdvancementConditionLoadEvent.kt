package com.vjh0107.barcode.advancement.api.events

import com.vjh0107.barcode.advancement.api.condition.AdvancementCondition
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class AdvancementConditionLoadEvent : Event() {
    val conditions: MutableSet<AdvancementCondition> = mutableSetOf()

    companion object {
        @JvmStatic
        private val handlers: HandlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlers
        }
    }

    @Suppress("RedundantCompanionReference")
    override fun getHandlers(): HandlerList {
        return Companion.handlers
    }

    fun register(advancementCondition: AdvancementCondition) {
        conditions.add(advancementCondition)
    }
}