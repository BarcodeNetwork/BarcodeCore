package com.vjh0107.barcode.framework.utils.effect

import org.bukkit.Location

object EffectUtils {
    fun getHollowCube(corner1: Location, corner2: Location, particleDistance: Double): List<Location> {
        val result: MutableList<Location> = ArrayList()
        val world = corner1.world
        val startX = corner1.x.coerceAtMost(corner2.x).toInt()
        val startY = corner1.y.coerceAtMost(corner2.y).toInt()
        val startZ = corner1.z.coerceAtMost(corner2.z).toInt()
        val endX = corner1.x.coerceAtLeast(corner2.x).toInt()
        val endY = corner1.y.coerceAtLeast(corner2.y).toInt()
        val endZ = corner1.z.coerceAtLeast(corner2.z).toInt()
        for (x in startX..endX + 1) {
            for (y in startY..endY + 1) {
                for (z in startZ..endZ + 1) {
                    var edge = false
                    if ((x == startX || x == endX + 1) &&
                        (y == startY || y == endY + 1)
                    ) edge = true
                    if ((z == startZ || z == endZ + 1) &&
                        (y == startY || y == endY + 1)
                    ) edge = true
                    if ((x == startX || x == endX + 1) &&
                        (z == startZ || z == endZ + 1)
                    ) edge = true
                    if (edge) {
                        result.add(Location(world, x.toDouble(), y.toDouble(), z.toDouble()))
                    }
                }
            }
        }
        return result
    }
}