package com.vjh0107.barcode.advancement.listeners

import com.vjh0107.barcode.advancement.BarcodeAdvancementPlugin
import com.vjh0107.barcode.advancement.services.PlayerRenderersManager
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

/**
 * BarcodeAdvancement.reloadPlayerAdvancement(player, delay) 를 실행시켜주는 리스너이다.
 * BetonQuest 와 MMOCore 를 의존하는 Core 모듈에서도 실행시켜준다.
 *
 * > com.vjh0107.barcode.core.dependencies.advancement.listeners.AdvancementUpdater
 * (현재 advancement 모듈은 core 모듈을 의존하지 않아, @see 로 링크할 수 없어서 패키지명을 남기겠다.)
 */
@BarcodeComponent
class PlayerAdvancementUpdateListener : BarcodeListener {
    private val playerDataManager: PlayerRenderersManager get() {
        return BarcodeAdvancementPlugin.instance.playerRenderersManager
    }

    /**
     * PlayerJoinEvent 에서 2틱 딜레이를 줘야 정상적으로 로드된다.
     */
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        playerDataManager.init(event.player).loadAsynchronously(2)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        playerDataManager.remove(event.player)
    }
}