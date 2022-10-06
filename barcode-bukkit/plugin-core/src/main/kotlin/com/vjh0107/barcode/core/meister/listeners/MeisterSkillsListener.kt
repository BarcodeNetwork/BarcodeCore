package com.vjh0107.barcode.core.meister.listeners

import com.vjh0107.barcode.core.database.getCorePlayerData
import com.vjh0107.barcode.core.meister.events.MeisterDamageEvent
import com.vjh0107.barcode.core.meister.MeisterSkills
import com.vjh0107.barcode.framework.component.BarcodeListener
import org.bukkit.event.EventHandler
import java.util.concurrent.ThreadLocalRandom

//@BarcodeComponent
class MeisterSkillsListener : BarcodeListener {
//    @EventHandler
//    fun onMythicMobDeath(event: MythicMobDeathEvent) {
//
//    }
//
//    @EventHandler
//    fun onMythicMobLootDrop(event: MythicMobLootDropEvent) {
//        event.drops.drops.forEach {
//            it.dropAmount.get(event.mob).print()
//        }
//    }

    @EventHandler
    fun onMeisterDamage(event: MeisterDamageEvent) {
        val player = event.player
        if (MeisterSkills.DEMATERIALIZE.validatePlayer(player)) {
            val chance = MeisterSkills.DEMATERIALIZE.getParameterResult(player.getCorePlayerData())
            val isSuccess = ThreadLocalRandom.current().nextDouble() <= chance / 100.0
            if (isSuccess) {
                event.damage *= 2
                // Dematerialize.runEffect(event.target.location)
            }
        }
    }
}