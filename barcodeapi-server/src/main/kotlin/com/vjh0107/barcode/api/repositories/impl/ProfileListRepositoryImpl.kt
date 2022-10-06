package com.vjh0107.barcode.api.repositories.impl

import com.vjh0107.barcode.api.repositories.ProfileListRepository
import com.vjh0107.barcode.core.database.player.root.RootPlayerDataEntity
import com.vjh0107.barcode.framework.database.datasource.BarcodeDataSource
import com.vjh0107.barcode.framework.database.player.MinecraftPlayerID
import org.koin.core.annotation.Single

@Single(binds = [ProfileListRepository::class])
class ProfileListRepositoryImpl(private val barcodeDataSource: BarcodeDataSource) : ProfileListRepository {
    override suspend fun getProfileList(minecraftPlayerID: MinecraftPlayerID) = barcodeDataSource.query {
        RootPlayerDataEntity.findByMinecraftPlayerID(minecraftPlayerID)
    }
}