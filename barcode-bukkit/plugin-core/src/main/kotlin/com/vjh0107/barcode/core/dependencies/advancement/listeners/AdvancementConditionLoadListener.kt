package com.vjh0107.barcode.core.dependencies.advancement.listeners

import com.vjh0107.barcode.advancement.api.events.AdvancementConditionLoadEvent
import com.vjh0107.barcode.core.dependencies.advancement.conditions.BetonQuestConditionCondition
import com.vjh0107.barcode.core.dependencies.advancement.conditions.BetonQuestTagCondition
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import org.bukkit.event.EventHandler

@BarcodeComponent
class AdvancementConditionLoadListener : BarcodeListener {
    @EventHandler
    fun onLoad(event: AdvancementConditionLoadEvent) {
        event.register(BetonQuestTagCondition("bq-tags"))
        event.register(BetonQuestConditionCondition("bq-conditions"))
    }
}