package com.vjh0107.barcode.core.skill.damager.listeners

import com.vjh0107.barcode.core.events.player.BarcodeDamageEvent
import io.lumine.mythic.lib.MythicLib
import io.lumine.mythic.lib.api.event.PlayerAttackEvent
import io.lumine.mythic.lib.damage.DamageType
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.max

class BarcodeDamageListener : Listener {
    private var weaponCritCoef: Double = MythicLib.plugin.config.getDouble("critical-strikes.weapon.coefficient", 1.3)

    companion object {
        private val random = ThreadLocalRandom.current()
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onHitAttackEffects(event: PlayerAttackEvent) {
        val stats = event.data.statMap
        DamageType.values().forEach { type ->
            event.damage.multiply(Math.max(0.0, 1.0 + stats.getStat(type.offenseStat) / 100.0), type)
        }

        val versusDamageCoef = 1.0 + stats.getStat(if (event.entity is Player) "PVP_DAMAGE" else "PVE_DAMAGE") / 100.0
        event.damage.multiply(versusDamageCoef)

        val isCriticalAttack = random.nextDouble() <= stats.getStat("CRITICAL_STRIKE_CHANCE") / 100.0

        val called = BarcodeDamageEvent(event.player, isCriticalAttack)
        Bukkit.getPluginManager().callEvent(called)

        if (isCriticalAttack && !called.isCancelled) {
            event.damage.multiply(max(0.0, weaponCritCoef + stats.getStat("CRITICAL_STRIKE_POWER") / 100.0))
            event.entity.world.playSound(event.entity.location, Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 1.0f)
            event.entity.world.spawnParticle(Particle.CRIT, event.entity.location.add(0.0, 1.0, 0.0), 16, 0.3, 0.3, 0.3, 0.1)
        }
    }
}