package com.vjh0107.barcode.core.dependencies.mythicmobs.targeters.impl

import com.vjh0107.barcode.core.dependencies.mythicmobs.targeters.BarcodeLocationSelector
import io.lumine.mythic.api.adapters.AbstractLocation
import io.lumine.mythic.api.adapters.AbstractVector
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.core.utils.MythicUtil
import io.lumine.mythic.core.utils.annotations.MythicTargeter
import kotlin.math.cos
import kotlin.math.sin

@MythicTargeter(author = "vjh0107",
    name = "barcode",
    aliases = [],
    description = "Targets a point any directions of the caster")
class BarcodeTargeter(mlc: MythicLineConfig) : BarcodeLocationSelector(mlc) {
    private val yOffset: Double = mlc.getDouble(arrayOf("yoffset", "y"), 0.0)
    private val zOffset: Double = mlc.getDouble(arrayOf("sideOffset", "z"), 0.0)
    //private val startOffset: Double = mlc.getDouble(arrayOf("startOffset", "x"), 0.0)
    private val forward: Double = mlc.getDouble(arrayOf("forward", "f"), 0.0)
    private val isTargeted = mlc.getBoolean(arrayOf("isTargeted", "iT"), false)

    override fun getLocations(data: SkillMetadata): HashSet<AbstractLocation> {
        if (isTargeted) {
            val am = data.entityTargets
            val targets: HashSet<AbstractLocation> = HashSet()
            am.forEach { entity ->
                var location = entity.location
                        .add(entity.location.getFlatDirection().normalize().multiply(this.forward))
                        .add(0.0, this.yOffset, 0.0)
                if (this.zOffset != 0.0) location = location.moveFlatly(0.0, 0.0, this.zOffset)
                targets.add(location)
            }
            return targets
        }
        else {
            val am = data.caster
            val targets: HashSet<AbstractLocation> = HashSet()
            var location = am.entity.location
                    .add(am.location.getFlatDirection().normalize().multiply(this.forward))
                    .add(0.0, this.yOffset, 0.0)
            //if (this.startOffset != 0.0) location = location.moveFlatly(this.startOffset, 0.0, 0.0)
            if (this.zOffset != 0.0) location = location.moveFlatly(0.0, 0.0, this.zOffset)
            targets.add(location)
            return targets
        }
    }

    private fun AbstractLocation.moveFlatly(dx: Double, dy: Double, dz: Double): AbstractLocation {
        val loc = this
        val off = MythicUtil.rotate(loc.yaw, 0f, dx, dy, dz)
        val x = loc.x + off.x
        val y = loc.y + off.y
        val z = loc.z + off.z
        return AbstractLocation(loc.world, x, y, z, loc.yaw, loc.pitch)
    }

    private fun AbstractLocation.getFlatDirection(): AbstractVector {
        val vector = AbstractVector()
        val rotX = this.yaw.toDouble()
        val rotY = 0.0
        vector.y = -sin(Math.toRadians(rotY))
        val xz = cos(Math.toRadians(rotY))
        vector.x = -xz * sin(Math.toRadians(rotX))
        vector.z = xz * cos(Math.toRadians(rotX))
        return vector
    }

}
