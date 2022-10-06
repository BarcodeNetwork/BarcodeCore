package com.vjh0107.barcode.api.repositories.impl

import com.vjh0107.barcode.api.repositories.RPGPlayerDataRepository
import com.vjh0107.barcode.core.database.player.childs.RPGPlayerDataEntity
import com.vjh0107.barcode.framework.database.datasource.BarcodeDataSource
import com.vjh0107.barcode.framework.database.player.multiprofile.ProfileID
import org.koin.core.annotation.Single

@Single(binds = [RPGPlayerDataRepository::class])
class RPGPlayerDataRepositoryImpl(private val barcodeDataSource: BarcodeDataSource) : RPGPlayerDataRepository {
    override suspend fun getByProfileId(profileID: ProfileID): RPGPlayerDataEntity? {
        return barcodeDataSource.query {
            RPGPlayerDataEntity.findById(profileID)
        }
    }
}