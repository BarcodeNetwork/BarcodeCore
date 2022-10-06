package com.vjh0107.barcode.advancement.listeners

import com.vjh0107.barcode.advancement.api.events.AdvancementConditionLoadEvent
import com.vjh0107.barcode.advancement.conditions.PAPIJavaScriptCondition
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import org.bukkit.event.EventHandler

@BarcodeComponent
class AdvancementConditionLoadListener : BarcodeListener {
    @EventHandler
    fun onLoad(event: AdvancementConditionLoadEvent) {
        event.register(PAPIJavaScriptCondition("papi"))
    }
}