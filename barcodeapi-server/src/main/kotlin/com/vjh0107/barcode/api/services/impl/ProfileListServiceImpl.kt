package com.vjh0107.barcode.api.services.impl

import com.vjh0107.barcode.api.repositories.ProfileListRepository
import com.vjh0107.barcode.api.services.ProfileListService
import com.vjh0107.barcode.core.database.player.root.RootPlayerData
import com.vjh0107.barcode.framework.database.player.MinecraftPlayerID
import org.koin.core.annotation.Single

@Single
class ProfileListServiceImpl(
    private val repository: ProfileListRepository
) : ProfileListService {
    override suspend fun getProfiles(minecraftPlayerID: MinecraftPlayerID): List<RootPlayerData> {
        val profileList = repository.getProfileList(minecraftPlayerID)
            .map { entity ->
                RootPlayerData.of(entity)
            }
        return profileList
    }

    override suspend fun getProfilesIndexed(minecraftPlayerID: MinecraftPlayerID): Map<Int, RootPlayerData> {
        return getProfiles(minecraftPlayerID).mapIndexed { index, profile ->
            index to profile
        }.toMap()
    }
}