package com.vjh0107.barcode.core.skill.damager.listeners

import com.vjh0107.barcode.core.skill.isCalledByAllowedDamageSources
import com.vjh0107.barcode.core.skill.isCalledByMeisterBehavior
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import net.Indyuce.mmocore.api.player.PlayerData

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerDropItemEvent

@BarcodeComponent
class DefaultAttackListener : BarcodeListener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onDamage(event: EntityDamageByEntityEvent) {
        val attacker = event.damager
        if (attacker !is Player) return

        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK || !attacker.isOnline) return

        if (!isCalledByAllowedDamageSources() && !isCalledByMeisterBehavior()) {
            event.isCancelled = true
            return
        }


    }
    @EventHandler
    fun onDrop(event: PlayerDropItemEvent) {
        if (PlayerData.get(event.player).isCasting) {
            event.isCancelled = true
        }
    }
}