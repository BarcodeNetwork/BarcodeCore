package com.vjh0107.barcode.core.npc.data

import com.vjh0107.barcode.core.npc.NPCComponent
import com.vjh0107.barcode.framework.coroutine.extensions.MinecraftAsync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.citizensnpcs.api.event.SpawnReason
import net.citizensnpcs.api.npc.NPC
import net.citizensnpcs.api.trait.trait.Equipment
import net.citizensnpcs.api.trait.trait.Owner
import org.bukkit.entity.Player

class PlayerOwnedNPCData(val player: Player) {
    private val ownedNPCList: MutableList<NPC> = mutableListOf()

    private fun add(npc: NPC, expiryTick: Long) {
        ownedNPCList.add(npc)
        CoroutineScope(Dispatchers.MinecraftAsync(NPCComponent.instance.plugin)).launch {
            delay(expiryTick * 50L)
            npc.destroy()
            ownedNPCList.remove(npc)
        }
    }
    private fun createNPC(player: Player, expiryTick: Long) {
        val npc = NPCComponent.instance.createNPC(player.name)

        npc.spawn(player.location, SpawnReason.PLUGIN)
        add(npc, expiryTick)
        val trait = npc.getOrAddTrait(Equipment::class.java)
        trait.set(Equipment.EquipmentSlot.HELMET, player.equipment.helmet)
        trait.set(Equipment.EquipmentSlot.CHESTPLATE, player.equipment.chestplate)
        trait.set(Equipment.EquipmentSlot.LEGGINGS, player.equipment.leggings)
        trait.set(Equipment.EquipmentSlot.BOOTS, player.equipment.boots)
        trait.set(Equipment.EquipmentSlot.HAND, player.equipment.itemInMainHand)
        trait.set(Equipment.EquipmentSlot.OFF_HAND, player.equipment.itemInOffHand)
        val ownerTrait = npc.getOrAddTrait(Owner::class.java)
    }
}