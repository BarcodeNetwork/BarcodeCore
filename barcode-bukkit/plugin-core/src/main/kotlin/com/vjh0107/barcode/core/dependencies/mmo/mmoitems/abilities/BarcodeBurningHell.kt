package com.vjh0107.barcode.core.dependencies.mmo.mmoitems.abilities

import com.vjh0107.barcode.core.BarcodeCorePlugin
import com.vjh0107.barcode.core.skill.damager.giveBarcodeDamage
import io.lumine.mythic.lib.damage.AttackMetadata
import io.lumine.mythic.lib.damage.DamageType
import net.Indyuce.mmoitems.MMOItems
import net.Indyuce.mmoitems.MMOUtils
import net.Indyuce.mmoitems.ability.SimpleAbility
import net.Indyuce.mmoitems.ability.metadata.SimpleAbilityMetadata
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class BarcodeBurningHell : SimpleAbility() {
    init {
        addModifier("duration", 0.2)
        addModifier("damage", 100.0)
        addModifier("cooldown", 0.0)
        addModifier("mana", 0.0)
        addModifier("stamina", 0.0)
    }

    override fun whenCast(attack: AttackMetadata, ability: SimpleAbilityMetadata) {
        val duration = ability.getModifier("duration") * 10
        val damage = ability.getModifier("damage")
        val loc = attack.player.location.add(0.0, 1.2, 0.0)
        object : BukkitRunnable() {
            var j = 0
            override fun run() {
                if (j++ > duration) cancel()
                loc.world.playSound(loc, Sound.BLOCK_FIRE_AMBIENT, 1f, 1f)
                var m = -45.0
                while (m < 45) {
                    val a = (m + attack.player.eyeLocation.yaw + 90) * Math.PI / 180
                    val vec = Vector(Math.cos(a), (random.nextDouble() - .5) * .2, Math.sin(a))
                    val source = loc.clone().add(vec.clone().setY(0))
                    source.world.spawnParticle(Particle.FLAME, source, 0, vec.x, vec.y, vec.z, .5)
                    if (j % 2 == 0) source.world.spawnParticle(
                        Particle.SMOKE_NORMAL,
                        source,
                        0,
                        vec.x,
                        vec.y,
                        vec.z,
                        .5
                    )
                    m += 5.0
                }
            }
        }.runTaskTimer(MMOItems.plugin, 0, 2)
        val damagingMethod: () -> Unit = {
            for (entity in MMOUtils.getNearbyChunkEntities(loc)) if (entity.location.distanceSquared(
                    loc
                ) < 100 && attack.player.eyeLocation.direction
                    .angle(
                        entity.location.toVector().subtract(attack.player.location.toVector())
                    ) < Math.PI / 4 && MMOUtils.canTarget(attack.player, entity)
            ) {
                entity.fireTicks = 10
                (entity as LivingEntity).giveBarcodeDamage(
                    attack.player,
                    damage,
                    arrayOf(DamageType.MAGIC, DamageType.WEAPON)
                )
            }
        }

        damagingMethod()
        BarcodeCorePlugin.runTaskLater(2) {
            damagingMethod()
        }
    }
}
