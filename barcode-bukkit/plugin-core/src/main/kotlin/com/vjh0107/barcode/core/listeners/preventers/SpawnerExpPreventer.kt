package com.vjh0107.barcode.core.listeners.preventers

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.metadata.FixedMetadataValue

@BarcodeComponent
class SpawnerExpPreventer(private val plugin: AbstractBarcodePlugin) : BarcodeListener {
    @EventHandler
    fun onSpawn(event: CreatureSpawnEvent) {
        if (event.spawnReason == CreatureSpawnEvent.SpawnReason.SPAWNER) {
            event.entity.setMetadata("spawner_spawned", FixedMetadataValue(plugin, true))
        }
    }
}