package com.vjh0107.barcode.cutscene.utils

import com.mojang.authlib.GameProfile
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Tag
import org.bukkit.block.Block

object CutsceneUtils {
    @JvmStatic
    fun isWalkable(location: Location): Boolean {
        val block: Block = location.block
        val type = block.type

        return if (!isMovable(type)) {
            // make sure the blocks above are air or can be walked through
            isMovable(block.getRelative(0, 1, 0).type) && block.getRelative(0, 2, 0).type.isAir
        } else {
            false
        }
    }

    @JvmStatic
    fun isMovable(material: Material): Boolean {

        val list = listOf(Tag.DOORS, Tag.SAPLINGS, Tag.FLOWERS, Tag.RAILS, Tag.SIGNS, Tag.SNOW)
        list.forEach {
            if (it.isTagged(material)) {
                return true
            }
        }

        val extraExcludes = listOf(Material.LADDER)
        if (extraExcludes.contains(material)) {
            return true
        }

        if (!material.isSolid || !material.isCollidable || material.isAir) {
            return true
        }

        return false
    }
}