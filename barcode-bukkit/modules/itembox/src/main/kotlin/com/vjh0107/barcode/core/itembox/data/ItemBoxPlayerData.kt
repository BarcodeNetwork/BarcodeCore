package com.vjh0107.barcode.core.itembox.data

import com.vjh0107.barcode.core.itembox.entity.ItemBoxPlayerDataEntity
import com.vjh0107.barcode.framework.database.player.data.AbstractSavablePlayerData
import com.vjh0107.barcode.framework.serialization.deserializeCollection
import kotlinx.serialization.Serializable

@Serializable
data class ItemBoxPlayerData(
    val items: MutableList<ItemBoxItem> = mutableListOf(),
) : AbstractSavablePlayerData() {
    companion object {
        fun of(entity: ItemBoxPlayerDataEntity): ItemBoxPlayerData {
            return ItemBoxPlayerData(entity.itemBox.deserializeCollection())
        }
    }
}