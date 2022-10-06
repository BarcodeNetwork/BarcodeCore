package com.vjh0107.barcode.core.itembox.data

import com.vjh0107.barcode.framework.serialization.serializers.ItemStackSerializer
import com.vjh0107.barcode.framework.serialization.serializers.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import org.bukkit.inventory.ItemStack
import java.time.LocalDateTime

@Serializable
data class ItemBoxItem private constructor (
    @Serializable(with = ItemStackSerializer::class)
    val item: ItemStack,
    @Serializable(with = LocalDateTimeSerializer::class)
    val receivedDate: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val expiryDate: LocalDateTime,
    val reason: String? = null,
    val whoSent: String? = null
) {
    companion object {
        fun of(
            item: ItemStack,
            receivedDate: LocalDateTime,
            expiryDate: LocalDateTime,
            reason: String? = null,
            whoSent: String? = "바코드 네트워크"
        ) : ItemBoxItem {
            return ItemBoxItem(item.clone(), receivedDate, expiryDate, reason, whoSent)
        }
    }
}