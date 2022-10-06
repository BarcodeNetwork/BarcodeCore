package com.vjh0107.barcode.cutscene.npc.npcs

import com.vjh0107.barcode.cutscene.npc.data.Equipment
import com.vjh0107.barcode.cutscene.npcs.CutsceneNPCImpl
import com.vjh0107.barcode.cutscene.npcs.Position
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

interface CutsceneNPC {
    companion object {
        fun of(): CutsceneNPC {
            return CutsceneNPCImpl()
        }
    }

    /**
     * 엔티티입니다.
     */
    fun getEntityLiving()

    fun getLocation(world: World): Location
    fun spawn(): CutsceneNPC
    fun show(player: Player)
    fun despawn()
    fun teleport(location: Location)
    fun setPosition(status: Position)
    fun playAnimation(animation: Int)
    fun setEquipment(equipment: Equipment)
    fun changeEntityMetadata(one: Int, two: Int)
    fun setChestOpen(chest: Location?, value: Int)
    fun setExclusivePlayer(player: Player)
    fun getEntityType(): EntityType?
}