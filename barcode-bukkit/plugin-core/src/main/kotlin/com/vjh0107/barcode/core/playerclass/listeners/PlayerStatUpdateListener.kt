package com.vjh0107.barcode.core.playerclass.listeners

import com.vjh0107.barcode.core.playerclass.models.BarcodeClasses
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import io.lumine.mythic.lib.api.player.EquipmentSlot
import io.lumine.mythic.lib.api.stat.modifier.ModifierSource
import io.lumine.mythic.lib.api.stat.modifier.ModifierType
import io.lumine.mythic.lib.api.stat.modifier.StatModifier
import net.Indyuce.mmocore.barcode.events.PlayerStatUpdateEvent
import org.bukkit.event.EventHandler

@BarcodeComponent
class PlayerStatUpdateListener : BarcodeListener {
    @EventHandler
    fun onStatUpdate(event: PlayerStatUpdateEvent) {
        val barcodeClass = BarcodeClasses.SubClasses.values().filter { it.name == event.data.profess.id }
        if (barcodeClass.isEmpty()) {
            event.data.stats.map.instances.forEach { it.remove("barcodeClassStat") }
            return
        }

        val calculatedStats = barcodeClass.first().statMap.calculateStats(event.data.level)
        calculatedStats.statMap.forEach { (statType, value) ->
            val statModifier = StatModifier(value, ModifierType.FLAT, EquipmentSlot.OTHER, ModifierSource.OTHER)
            event.data.stats.getInstance(statType).addModifier("barcodeClassStat", statModifier)
        }
    }
}