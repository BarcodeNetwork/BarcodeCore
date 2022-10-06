package com.vjh0107.barcode.core.dependencies.magicspells.spells

import com.nisovin.magicspells.spelleffects.EffectPosition
import com.nisovin.magicspells.spells.InstantSpell
import com.nisovin.magicspells.util.MagicConfig
import com.nisovin.magicspells.util.Util
import com.vjh0107.barcode.core.BarcodeCorePlugin
import com.vjh0107.barcode.framework.utils.atan2toAngle
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class TumbleSpell(config: MagicConfig, spellName: String) : InstantSpell(config, spellName) {
    private val forwardVelocity = this.getConfigFloat("forward-velocity", 30F) / 10F
    private val upwardVelocity = this.getConfigFloat("upward-velocity", 15F) / 10F
    override fun castSpell(
        caster: LivingEntity,
        state: SpellCastState?,
        power: Float,
        args: Array<out String>?
    ): PostCastAction {
        val player = caster as? Player ?: return PostCastAction.HANDLE_NORMALLY
        val playerYaw = player.location.yaw.toDouble()
        val playerX = player.location.x
        val playerZ = player.location.z
        var finalRotation: Float = 0F

        BarcodeCorePlugin.runTaskLater(1L) {
            var rotation: Double = -1 * atan2toAngle(player.location.z - playerZ, player.location.x - playerX)
            repeat(2) {
                if (rotation < 0.0) rotation += 360.00
                if (it == 0) rotation -= playerYaw
            }
            if (!(player.location.z - playerZ == 0.00 && player.location.x - playerX == 0.00)) finalRotation = rotation.toFloat()

            val v: Vector = caster.location.direction
            v.setY(0).normalize().multiply(forwardVelocity * power).setY(upwardVelocity * power)
            Util.rotateVector(v, finalRotation)
            player.velocity = caster.velocity.add(v)
            playSpellEffects(EffectPosition.CASTER, player)

        }
        return PostCastAction.HANDLE_NORMALLY
    }

}

