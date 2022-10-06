package com.vjh0107.barcode.core.meister.events

import com.vjh0107.barcode.framework.serialization.data.SoundWrapper
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class MeisterDamageEvent(
    player: Player,
    val target: Entity,
    var damage: Double,
    var soundWrapper: SoundWrapper
) : PlayerEvent(player), Cancellable {

    private var isCancelled = false

    override fun getHandlers(): HandlerList {
        return Companion.handlers
    }

    override fun isCancelled(): Boolean {
        return isCancelled
    }

    override fun setCancelled(bl: Boolean) {
        isCancelled = bl
    }

    companion object {
        @JvmStatic
        private val handlers: HandlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlers
        }
    }
}