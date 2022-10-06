package com.vjh0107.barcode.cutscene.npc.npcs.impl

import com.mojang.authlib.GameProfile
import com.vjh0107.barcode.cutscene.npc.data.Skin
import com.vjh0107.barcode.cutscene.npc.npcs.CutsceneNPC
import com.vjh0107.barcode.framework.utils.formatters.translateAlternateColorCodes
import com.vjh0107.barcode.framework.version.NMSConverter.toCraftWorld
import com.vjh0107.barcode.framework.version.adapter.NMSAdapter
import net.minecraft.server.level.EntityPlayer
import net.minecraft.world.entity.EntityLiving
import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.*

class PlayerCutsceneNPC(val displayName: String, skin: Skin, val location: Location) : CutsceneNPC {
    override val entityLiving: EntityLiving = let {
        val gameProfile = GameProfile(UUID.randomUUID(), displayName.translateAlternateColorCodes()).apply {
            this.properties.put("textures", skin.createProperty())
        }
        EntityPlayer(NMSAdapter.getCraftServer().server, location.world.toCraftWorld().handle, gameProfile)
    }

    override fun spawn(): CutsceneNPC {
        this.entityLiving.a(location.x, location.y, location.z, location.yaw, location.pitch)
        this.entityLiving.aF = false
    }

    override fun changeEntityMetadata(one: Int, two: Int) {
        Bukkit.getOnlinePlayers().forEach {
            if ()
        }
    }
}