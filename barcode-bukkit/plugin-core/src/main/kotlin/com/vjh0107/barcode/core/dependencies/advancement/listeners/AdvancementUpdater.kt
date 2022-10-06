package com.vjh0107.barcode.core.dependencies.advancement.listeners

import com.vjh0107.barcode.advancement.BarcodeAdvancementPlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import net.Indyuce.mmocore.api.event.PlayerLevelUpEvent
import org.betonquest.betonquest.api.PlayerTagAddEvent
import org.betonquest.betonquest.api.PlayerTagRemoveEvent
import org.bukkit.event.EventHandler

@BarcodeComponent
class AdvancementUpdater : BarcodeListener {
    @EventHandler
    fun onMMOCoreLevelUp(event: PlayerLevelUpEvent) {
        BarcodeAdvancementPlugin.reloadPlayerAdvancement(event.player)
    }

    @EventHandler
    fun onBetonQuestTagAdded(event: PlayerTagAddEvent) {
        BarcodeAdvancementPlugin.reloadPlayerAdvancement(event.player)
    }

    @EventHandler
    fun onBetonQuestTagRemoved(event: PlayerTagRemoveEvent)  {
        BarcodeAdvancementPlugin.reloadPlayerAdvancement(event.player)
    }
}