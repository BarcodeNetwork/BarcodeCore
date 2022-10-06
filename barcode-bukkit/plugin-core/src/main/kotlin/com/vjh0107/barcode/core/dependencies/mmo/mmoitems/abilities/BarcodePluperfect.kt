package com.vjh0107.barcode.core.dependencies.mmo.mmoitems.abilities

import com.vjh0107.barcode.core.skill.damager.giveBarcodeDamage
import io.lumine.mythic.lib.damage.AttackMetadata
import io.lumine.mythic.lib.damage.DamageType
import io.lumine.mythic.lib.version.VersionSound
import net.Indyuce.mmoitems.MMOItems
import net.Indyuce.mmoitems.MMOUtils
import net.Indyuce.mmoitems.ability.LocationAbility
import net.Indyuce.mmoitems.ability.metadata.LocationAbilityMetadata
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class BarcodePluperfect : LocationAbility() {
    init {
        addModifier("damage", 3.0)
        addModifier("duration", 4.0)
        addModifier("radius", 3.0)
        addModifier("cooldown", 10.0)
        addModifier("mana", 0.0)
        addModifier("stamina", 0.0)
    }
    override fun whenCast(attack: AttackMetadata, ability: LocationAbilityMetadata) {
        val loc = ability.target
        val damage = ability.getModifier("damage")
        val duration = ability.getModifier("duration") * 10
        val radius = ability.getModifier("radius")
        object : BukkitRunnable() {
            var j = 0
            override fun run() {
                j++
                if (j > duration) {
                    cancel()
                    return
                }
                val loc1 = loc.clone().add(randomCoordMultiplier() * radius, 0.0, randomCoordMultiplier() * radius)
                loc1.world!!.playSound(loc1, VersionSound.ENTITY_ENDERMAN_HURT.toSound(), 1f, 0f)
                for (entity in MMOUtils.getNearbyChunkEntities(loc1)) if (MMOUtils.canTarget(
                        attack.player,
                        entity
                    ) && entity.location.distanceSquared(loc1) <= 4
                ) (entity as LivingEntity).giveBarcodeDamage(attack.player, damage, arrayOf(DamageType.WEAPON, DamageType.MAGIC))
                loc1.world!!.spawnParticle(Particle.SPELL_WITCH, loc1, 12, 0.0, 0.0, 0.0, .1)
                loc1.world!!.spawnParticle(Particle.SMOKE_NORMAL, loc1, 6, 0.0, 0.0, 0.0, .1)
                val vector = Vector(randomCoordMultiplier() * .03, .3, randomCoordMultiplier() * .03)
                for (k in 0..59) loc1.world!!.spawnParticle(Particle.SPELL_WITCH, loc1.add(vector), 0)
            }
        }.runTaskTimer(MMOItems.plugin, 0, 2)
    }

    // random double between -1 and 1
    private fun randomCoordMultiplier(): Double {
        return (random.nextDouble() - .5) * 2
    }
}