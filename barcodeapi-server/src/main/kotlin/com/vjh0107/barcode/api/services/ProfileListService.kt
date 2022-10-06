package com.vjh0107.barcode.api.services

import com.vjh0107.barcode.core.database.player.root.RootPlayerData
import com.vjh0107.barcode.framework.database.player.MinecraftPlayerID

interface ProfileListService {
    /**
     * RootPlayerData DTO 를 반환합니다.
     */
    suspend fun getProfiles(minecraftPlayerID: MinecraftPlayerID): List<RootPlayerData>

    /**
     * 인덱싱 된 프로파일 ID 를 반환합니다.
     */
    suspend fun getProfilesIndexed(minecraftPlayerID: MinecraftPlayerID): Map<Int, RootPlayerData>
}