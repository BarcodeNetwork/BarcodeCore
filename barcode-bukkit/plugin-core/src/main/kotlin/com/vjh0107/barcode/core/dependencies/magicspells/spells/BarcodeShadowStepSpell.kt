package com.vjh0107.barcode.core.dependencies.magicspells.spells

import com.nisovin.magicspells.spells.TargetedEntitySpell
import com.nisovin.magicspells.spells.TargetedSpell
import com.nisovin.magicspells.util.MagicConfig
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

class BarcodeShadowStepSpell(config: MagicConfig?, spellName: String?) :
    TargetedSpell(config, spellName), TargetedEntitySpell {
    private val yaw: Float
    private val pitch: Float
    private val distance: Double
    private val relativeOffset: Vector
    private val forceTargetPlayer: Boolean

    init {
        yaw = getConfigFloat("yaw", 0f)
        pitch = getConfigFloat("pitch", 0f)
        distance = getConfigDouble("distance", -1.0)
        relativeOffset = getConfigVector("relative-offset", "-1,0,0")
        if (distance != -1.0) relativeOffset.x = distance
        forceTargetPlayer = getConfigBoolean("forceTargetPlayer", false)
    }

    override fun castSpell(
        livingEntity: LivingEntity,
        state: SpellCastState,
        power: Float,
        args: Array<String>?,
    ): PostCastAction {
        if (state == SpellCastState.NORMAL) {

            val target = getTargetedEntity(livingEntity, 1F, forceTargetPlayer, null) ?: return noTarget(livingEntity)
            shadowstep(livingEntity, target.target)
            return PostCastAction.NO_MESSAGES
        }
        return PostCastAction.HANDLE_NORMALLY
    }

    override fun castAtEntity(caster: LivingEntity, target: LivingEntity, power: Float): Boolean {
        return if (!validTargetList.canTarget(caster, target)) false else shadowstep(caster, target)
    }

    override fun castAtEntity(target: LivingEntity, power: Float): Boolean {
        return false
    }

    private fun shadowstep(caster: LivingEntity, target: LivingEntity?): Boolean {
        if (target == null) return false
        val targetLoc = target.location.clone()
        targetLoc.pitch = 0f
        val startDir = targetLoc.direction.setY(0).normalize()
        val horizOffset: Vector = Vector(-startDir.z, 0.0, startDir.x).normalize()
        targetLoc.add(horizOffset.multiply(relativeOffset.z)).block.location
        targetLoc.add(targetLoc.direction.setY(0).multiply(relativeOffset.x))
        targetLoc.y = targetLoc.y + relativeOffset.y
        targetLoc.pitch = pitch
        targetLoc.yaw = targetLoc.yaw + yaw
//        val b = targetLoc.block
//        if (!BlockUtils.isPathable(b.type) || !BlockUtils.isPathable(b.getRelative(BlockFace.UP))) return false
        playSpellEffects(caster.location, targetLoc)
        caster.teleport(targetLoc)
        return true
    }

}
