package com.vjh0107.barcode.api.services.impl

import com.vjh0107.barcode.api.repositories.RPGPlayerDataRepository
import com.vjh0107.barcode.api.services.ProfileListService
import com.vjh0107.barcode.api.services.RPGPlayerDataService
import com.vjh0107.barcode.core.database.player.childs.RPGPlayerData
import com.vjh0107.barcode.framework.database.player.MinecraftPlayerID
import com.vjh0107.barcode.framework.database.player.multiprofile.ProfileID
import org.koin.core.annotation.Single

@Single
class RPGPlayerDataServiceImpl(
    private val repository: RPGPlayerDataRepository,
    private val profileListService: ProfileListService,
) : RPGPlayerDataService {
    override suspend fun getByProfileId(profileID: ProfileID): RPGPlayerData? {
        val entity = repository.getByProfileId(profileID) ?: return null
        return RPGPlayerData.of(entity)
    }

    override suspend fun getByIndex(minecraftPlayerID: MinecraftPlayerID, index: Int): RPGPlayerData? {
        val profileID = profileListService
            .getProfiles(minecraftPlayerID)
            .getOrNull(index)?.profileID ?: return null
        return this.getByProfileId(ProfileID.of(profileID))
    }
}