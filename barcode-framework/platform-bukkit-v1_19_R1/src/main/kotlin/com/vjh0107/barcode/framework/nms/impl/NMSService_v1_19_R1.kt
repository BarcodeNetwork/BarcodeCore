package com.vjh0107.barcode.framework.nms.impl

import com.vjh0107.barcode.framework.nbt.NBTItem
import com.vjh0107.barcode.framework.nms.NMSService
import com.vjh0107.barcode.framework.nms.impl.nbt.NBTItem_v1_19_R1
import com.vjh0107.barcode.framework.utils.uncheckedNonnullCast
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LightningBolt
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Suppress("unused", "ClassName")
class NMSService_v1_19_R1 : NMSService {
    override fun getNBTItem(item: ItemStack): NBTItem {
        return NBTItem_v1_19_R1(item)
    }

    override fun removeArrows(entity: Entity) {
        val entityDataAccessor = EntityDataAccessor(12, EntityDataSerializers.INT)
        entity.uncheckedNonnullCast<CraftEntity>().handle.entityData.set(entityDataAccessor, -1)
    }

    override fun strikeLightning(player: Player, location: Location) {
        val craftWorld = location.world.uncheckedNonnullCast<CraftWorld>()
        val lightning = LightningBolt(EntityType.LIGHTNING_BOLT, craftWorld.handle)
        lightning.moveTo(location.x, location.y, location.z, location.yaw, location.pitch)
        val craftPlayer = player.uncheckedNonnullCast<CraftPlayer>()
        craftPlayer.handle.connection.send(ClientboundAddEntityPacket(lightning))
    }
}