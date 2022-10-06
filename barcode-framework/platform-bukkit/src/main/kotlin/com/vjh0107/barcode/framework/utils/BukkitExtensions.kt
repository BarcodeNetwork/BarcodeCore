package com.vjh0107.barcode.framework.utils

import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/**
 * 플레이어의 시야가 가르키는 엔티티를 범위 안에서 찾는다.
 */
fun Player.getEntityTarget(distance: Double): Entity? {
    val player = this

    var targetDistanceSquared = 0.0
    val radiusSquared = 1.0
    val eyeLocationVector = player.eyeLocation.toVector()
    val normalizedDirection = player.location.direction.normalize()
    val cos45 = Math.cos(Math.PI / 4)
    var target: Entity? = null
    for (other in player.world.entities) {
        if (other == null || other === player) continue
        if (target == null || targetDistanceSquared > other.location.distanceSquared(player.location)) {
            val t = other.location.add(0.0, 1.0, 0.0).toVector().subtract(eyeLocationVector)
            if (normalizedDirection.clone().crossProduct(t).lengthSquared() < radiusSquared && t.normalize().dot(normalizedDirection) >= cos45) {
                if (this.location.distance(other.location) <= distance) {
                    target = other
                    targetDistanceSquared = target.location.distanceSquared(player.location)
                }
            }
        }
    }
    return target
}

fun LivingEntity?.underIsAir(blockCount: Int): Boolean {
    repeat(blockCount) { i ->
        if (this == null) return false
        val block: Material = this.location.subtract(0.00, i.toDouble() + 1.00, 0.00).block.type
        if (!block.isAir) return false
    }
    return true
}


private var serverVersionInternal: String? = null

val Plugin.serverVersion: String
    get() {
        if (serverVersionInternal == null) {
            serverVersionInternal = server.javaClass.getPackage().name.replace(".", ",").split(",")[3]
        }

        return serverVersionInternal!!
    }

fun Plugin.findClazz(name: String): Class<*> {
    return Class.forName(name.replace("{VERSION}", serverVersion))
}