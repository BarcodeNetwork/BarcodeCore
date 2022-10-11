package com.vjh0107.barcode.mmoitems.modules.tier.listeners

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import com.vjh0107.barcode.framework.component.Reloadable
import com.vjh0107.barcode.framework.nms.NMSService
import com.vjh0107.barcode.framework.utils.effect.glow.service.GlowService
import com.vjh0107.barcode.framework.utils.traceableInfo
import com.vjh0107.barcode.mmoitems.nbt.getTier
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.ItemSpawnEvent

@BarcodeComponent
class ItemTierListener(
    private val plugin: AbstractBarcodePlugin,
    private val glowService: GlowService,
    private val nmsService: NMSService
) : BarcodeListener, Reloadable {
    var glowEnabled = true
    var hintEnabled = true

    override fun load() {
        plugin.logger.traceableInfo("tier glow: $glowEnabled, tier hint: $hintEnabled")
        glowEnabled = plugin.config.getBoolean("tier.glow")
        hintEnabled = plugin.config.getBoolean("tier.hint")
    }

    override fun close() {}

    @EventHandler
    fun onSpawn(event: ItemSpawnEvent) {
        val tier = nmsService.getNBTItem(event.entity.itemStack).getTier() ?: return
        if (glowEnabled) {
            glowService.setGlowing(event.entity, )
        }
        val item = event.entity.itemStack
    }
}