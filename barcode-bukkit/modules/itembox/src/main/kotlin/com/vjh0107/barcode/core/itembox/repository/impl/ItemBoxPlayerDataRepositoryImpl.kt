package com.vjh0107.barcode.core.itembox.repository.impl

import com.vjh0107.barcode.core.itembox.data.ItemBoxPlayerData
import com.vjh0107.barcode.core.itembox.entity.ItemBoxPlayerDataEntity
import com.vjh0107.barcode.core.itembox.entity.ItemBoxPlayerDataTable
import com.vjh0107.barcode.core.itembox.repository.ItemBoxPlayerDataRepository
import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.database.player.PlayerIDWrapper
import com.vjh0107.barcode.framework.database.player.getPlayerID
import com.vjh0107.barcode.framework.database.player.repository.AbstractSavablePlayerDataRepository
import com.vjh0107.barcode.framework.exceptions.playerdata.PlayerDataNotFoundException
import com.vjh0107.barcode.framework.koin.annotation.BarcodeSingleton
import com.vjh0107.barcode.framework.serialization.serialize
import org.bukkit.entity.Player

@BarcodeSingleton(binds = [ItemBoxPlayerDataRepository::class])
@BarcodeComponent
class ItemBoxPlayerDataRepositoryImpl(
    plugin: AbstractBarcodePlugin
) : AbstractSavablePlayerDataRepository<ItemBoxPlayerData>(plugin), ItemBoxPlayerDataRepository {
    override fun getTablesToLoad() = listOf(ItemBoxPlayerDataTable)

    override suspend fun loadData(id: PlayerIDWrapper): ItemBoxPlayerData {
        return dataSource.query {
            val result = ItemBoxPlayerDataEntity.findByIdOrNew(id.profileID)
            ItemBoxPlayerData.of(result)
        }
    }

    override suspend fun saveData(id: PlayerIDWrapper, playerData: ItemBoxPlayerData) {
        dataSource.query {
            with(ItemBoxPlayerDataEntity[id.profileID]) {
                itemBox = playerData.items.serialize()
            }
        }
    }

    override fun getPlayerData(player: Player): ItemBoxPlayerData {
        return getPlayerData(player.getPlayerID()) ?: throw PlayerDataNotFoundException(player)
    }
}