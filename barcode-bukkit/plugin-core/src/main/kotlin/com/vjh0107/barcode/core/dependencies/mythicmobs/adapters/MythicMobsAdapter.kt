package com.vjh0107.barcode.core.dependencies.mythicmobs.adapters

import io.lumine.mythic.api.adapters.*
import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.core.mobs.ActiveMob
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.util.*

object MythicMobsAdapter {
    /**
     * 미몹 팀 애들은 미몹 패키지 리팩토링을 한 적이 있기 때문에 나눴음
     */
    private val mythicBukkit: MythicBukkit = MythicBukkit.inst()

    fun inst() : MythicBukkit {
        return mythicBukkit
    }

    fun <T : Entity> T.isActiveMob(): Boolean {
        return mythicBukkit.mobManager.isActiveMob(this.uniqueId)
    }

    fun <T : Entity> T.toActiveMob(): ActiveMob? {
        return mythicBukkit.mobManager.getActiveMob(this.uniqueId).orElse(null)
    }

    fun <T : Entity> T.toOptionalActiveMob(): Optional<ActiveMob> {
        return mythicBukkit.mobManager.getActiveMob(this.uniqueId)
    }

    fun <T : Entity> T.toAbstractEntity(): AbstractEntity {
        return BukkitAdapter.adapt(this)
    }

    fun AbstractEntity.toBukkitEntity(): Entity {
        return BukkitAdapter.adapt(this)
    }

    fun <T : Location> T.toAbstractLocation(): AbstractLocation {
        return BukkitAdapter.adapt(this)
    }

    fun AbstractLocation.toBukkitLocation(): Location {
        return BukkitAdapter.adapt(this)
    }

    fun <T : ItemStack> T.toAbstractItemStack(): AbstractItemStack {
        return BukkitAdapter.adapt(this)
    }

    fun AbstractItemStack.toBukkitItemStack() : ItemStack {
        return BukkitAdapter.adapt(this)
    }

    fun <T : Player> T.toAbstractPlayer(): AbstractPlayer {
        return BukkitAdapter.adapt(this)
    }

    fun AbstractPlayer.toBukkitPlayer(): Player {
        return BukkitAdapter.adapt(this)
    }

    fun <T : World> T.toAbstractWorld(): AbstractWorld {
        return BukkitAdapter.adapt(this)
    }

    fun AbstractWorld.toBukkitWorld(): World {
        return BukkitAdapter.adapt(this)
    }

    fun <T : Vector> T.toAbstractVector(): AbstractVector {
        return BukkitAdapter.adapt(this)
    }

    fun AbstractVector.toBukkitVector(): Vector {
        return BukkitAdapter.adapt(this)
    }
}