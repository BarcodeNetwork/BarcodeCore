package com.vjh0107.barcode.cutscene.npc.data

import com.vjh0107.barcode.framework.serialization.SerializableData
import com.vjh0107.barcode.framework.serialization.serialize
import com.vjh0107.barcode.framework.serialization.serializers.ItemStackSerializer
import kotlinx.serialization.Serializable
import org.bukkit.inventory.ItemStack

@Serializable
data class Equipment(
    @Serializable(with = ItemStackSerializer::class)
    val helmet: ItemStack,
    @Serializable(with = ItemStackSerializer::class)
    val chestplate: ItemStack,
    @Serializable(with = ItemStackSerializer::class)
    val leggings: ItemStack,
    @Serializable(with = ItemStackSerializer::class)
    val boots: ItemStack,
    @Serializable(with = ItemStackSerializer::class)
    val mainHand: ItemStack,
    @Serializable(with = ItemStackSerializer::class)
    val offHand: ItemStack
) : SerializableData {
    companion object {
        @JvmStatic
        fun serialize(equipment: Equipment): String {
            return equipment.serialize()
        }
    }
}