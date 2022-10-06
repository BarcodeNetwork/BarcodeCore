package com.vjh0107.barcode.advancement.services

import com.vjh0107.barcode.advancement.BarcodeAdvancementPlugin
import com.vjh0107.barcode.advancement.renderer.AdvancementRenderer
import com.vjh0107.barcode.advancement.renderer.impl.AdvancementRendererImpl
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodePluginManager
import com.vjh0107.barcode.framework.component.Reloadable
import org.bukkit.entity.Player
import java.util.*

@BarcodeComponent
class PlayerRenderersManager(val plugin: BarcodeAdvancementPlugin) : BarcodePluginManager, Reloadable {
    private val advancementRenderers = mutableMapOf<UUID, AdvancementRenderer>()

    init {
        plugin.playerRenderersManager = this
    }

    fun get(key: Player): AdvancementRenderer {
        return advancementRenderers[key.uniqueId] ?: throw NullPointerException("플레이어 데이터를 가져오지 못하였습니다.")
    }

    fun init(player: Player) : AdvancementRenderer {
        advancementRenderers[player.uniqueId] = AdvancementRendererImpl(player)
        return get(player)
    }

    fun remove(player: Player) {
        advancementRenderers.remove(player.uniqueId)
    }

    override fun load() {
        plugin.server.onlinePlayers.forEach {
            init(it).loadAsynchronously(2)
        }
    }

    override fun close() {
        advancementRenderers.clear()
    }
}