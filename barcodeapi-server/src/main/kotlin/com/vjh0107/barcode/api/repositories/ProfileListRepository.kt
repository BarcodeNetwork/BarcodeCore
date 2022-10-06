package com.vjh0107.barcode.api.repositories

import com.vjh0107.barcode.core.database.player.root.RootPlayerDataEntity
import com.vjh0107.barcode.framework.database.player.MinecraftPlayerID

interface ProfileListRepository {

    /**
     * 마인크래프트 uuid 를 통해 profile list 를 구합니다.
     *
     * @param minecraftPlayerID 마인크래프트 uuid
     * @return profile 이 존재하지 않는다면, empty List 를 반환합니다.
     */
    suspend fun getProfileList(minecraftPlayerID: MinecraftPlayerID): List<RootPlayerDataEntity>
}