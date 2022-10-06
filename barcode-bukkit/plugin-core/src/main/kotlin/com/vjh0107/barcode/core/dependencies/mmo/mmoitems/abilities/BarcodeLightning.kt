package com.vjh0107.barcode.core.dependencies.mmo.mmoitems.abilities

import com.vjh0107.barcode.core.skill.damager.giveBarcodeDamage
import io.lumine.mythic.lib.damage.AttackMetadata
import io.lumine.mythic.lib.damage.DamageType
import io.lumine.mythic.lib.version.VersionSound
import net.Indyuce.mmoitems.MMOUtils
import net.Indyuce.mmoitems.ability.LocationAbility
import net.Indyuce.mmoitems.ability.metadata.LocationAbilityMetadata
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

class BarcodeLightning : LocationAbility() {
    override fun whenCast(attack: AttackMetadata, ability: LocationAbilityMetadata) {
        val loc = getFirstNonSolidBlock(ability.target)
        val damage = ability.getModifier("damage")
        val radius = ability.getModifier("radius")
        for (entity in MMOUtils.getNearbyChunkEntities(loc)) if (MMOUtils.canTarget(
                attack.player,
                entity
            ) && entity.location.distanceSquared(loc) <= radius * radius
        ) {
            (entity as LivingEntity).giveBarcodeDamage(attack.player, damage, arrayOf(DamageType.WEAPON, DamageType.MAGIC))
        }
        attack.player.world.playSound(
            attack.player.location,
            VersionSound.ENTITY_FIREWORK_ROCKET_BLAST.toSound(),
            1f,
            0f
        )
        attack.player.world.playSound(attack.player.location, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1f, 0f)
        loc.world.spawnParticle(Particle.FIREWORKS_SPARK, loc, 64, 0.0, 0.0, 0.0, .2)
        loc.world.strikeLightningEffect(loc)
        loc.world.spawnParticle(Particle.EXPLOSION_NORMAL, loc, 32, 0.0, 0.0, 0.0, .2)
        val vec: Vector = Vector(0.0, .3, 0.0)
        //var j = 0.0
        //while (j < 40) {
        //    loc.world.spawnParticle(Particle.FIREWORKS_SPARK, loc.add(vec), 6, .1, .1, .1, .01)
        //    j += .3
        //}
    }

    private fun getFirstNonSolidBlock(loc: Location): Location {
        val initial = loc.clone()
        for (j in 0..4) if (!loc.add(0.0, 1.0, 0.0).block.type.isSolid) return loc
        return initial
    }

    init {
        addModifier("damage", 100.0)
        addModifier("radius", 5.0)
        addModifier("cooldown", 10.0)
        addModifier("mana", 0.0)
        addModifier("stamina", 0.0)
    }
}