package com.vjh0107.barcode.core.listeners.preventers

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import com.vjh0107.barcode.framework.coroutine.extensions.MinecraftAsync
import com.vjh0107.barcode.framework.coroutine.extensions.MinecraftMain
import com.vjh0107.barcode.framework.utils.messagesender.sendBNMessage
import com.vjh0107.barcode.framework.utils.transformer.isPlayer
import kotlinx.coroutines.*
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDropItemEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLevelChangeEvent

@BarcodeComponent
class RPGPreventer(private val plugin: AbstractBarcodePlugin) : BarcodeListener {
    /**
     * 제작을 제한합니다.
     */
    @EventHandler
    fun onCraft(event: CraftItemEvent) {
        if (!event.whoClicked.isOp) {
            event.isCancelled = true
        }
    }

    /**
     * 플레이어 레벨을 0으로 고정합니다.
     */
    @EventHandler
    fun onLevelChange(event: PlayerLevelChangeEvent) {
        event.player.level = 0
    }

    /**
     * 배고픔 레벨의 변경을 막습니다.
     */
    @EventHandler
    fun onHungerLevelChange(event: FoodLevelChangeEvent) {
        event.entity.foodLevel = 7
    }

    /**
     * 서버에 접속할 때, 게임모드를 모험으로 변경합니다.
     */
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        if (event.player.isOp) {
            event.player.sendBNMessage("서버에 접속하여 게임모드를 모험으로 변경합니다. &7(오피 디버그)")
        }
        event.player.gameMode = GameMode.ADVENTURE
    }

    /**
     * 낙사와 블럭에 끼임으로 인한 질식사의 데미지를 없앱니다.
     */
    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        if (event.cause == EntityDamageEvent.DamageCause.FALL || event.cause == EntityDamageEvent.DamageCause.SUFFOCATION) {
            event.isCancelled = true
        }
    }

    /**
     * 플레이어를 2초 후 리스폰 시킵니다.
     *
     * TODO: 테스트되지 않음.
     */
    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        CoroutineScope(Dispatchers.MinecraftAsync(plugin)).launch {
            delay(2000)
            withContext(Dispatchers.MinecraftMain(plugin)) {
                event.player.spigot().respawn()
            }
        }
    }

    /**
     * 아이템을 버리지 못하게 합니다.
     *
     * TODO: shift + Q 시 버려지게 할 여부 결정
     */
    @EventHandler
    fun onDrop(event: EntityDropItemEvent) {
        val entity = event.entity
        if (entity.isPlayer()) {
            if (entity.isOp && entity.isSneaking) {
                return
            }
        }
        event.isCancelled = true
    }


}