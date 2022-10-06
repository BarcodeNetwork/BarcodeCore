package com.vjh0107.barcode.core.dependencies.mmo.mmocore.passives

import com.vjh0107.barcode.framework.utils.atan2toAngle
import io.lumine.mythic.lib.api.event.PlayerAttackEvent
import net.Indyuce.mmocore.api.player.PlayerData
import net.Indyuce.mmocore.api.util.math.formula.LinearValue
import net.Indyuce.mmocore.skill.PassiveSkill
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.event.EventHandler

class BarcodeBackStab : PassiveSkill() {
    init {
        setMaterial(Material.FLINT)
        setLore("Backstabs deal &c{extra}%&7 damage.", "", "&9Costs {mana} {mana_name}")
        setPassive()
        addModifier("cooldown", LinearValue(0.0, 0.0))
        addModifier("mana", LinearValue(8.0, 1.0))
        addModifier("extra", LinearValue(10.0, 0.5))
    }
    @EventHandler
    fun a(event: PlayerAttackEvent) {
        val data = PlayerData.get(event.data.uniqueId)
        if (!data.profess.hasSkill(this)) return
        val cast = data.cast(this)
        if (!cast.isSuccessful) return
        data.cast(cast.info)
        val player = event.player

        val target = event.entity
        val targetYaw = target.location.yaw.toDouble()
        val calcZ = player.location.z - target.location.z
        val calcX = player.location.x - target.location.x
        var angle: Double = -1 * atan2toAngle(calcZ, calcX)
        repeat(2) {
            if (angle < 0.0) angle += 360.00
            if (it == 0) angle -= targetYaw
        }
        if (angle < 90.0 || angle > 270.0) return
        val multiplyDamage = 1.0 + (cast.getModifier("extra") / 100.0)
        target.world.spawnParticle(
            Particle.CRIT_MAGIC,
            target.location.add(0.0, target.height / 2, 0.0),
            16,
            0.3,
            0.3,
            0.3,
            .05
        )
        event.attack.damage.multiply(multiplyDamage)
    }
}