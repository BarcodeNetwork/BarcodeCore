package com.vjh0107.barcode.api.services

import com.vjh0107.barcode.core.database.player.childs.RPGPlayerData
import com.vjh0107.barcode.framework.database.player.MinecraftPlayerID
import com.vjh0107.barcode.framework.database.player.multiprofile.ProfileID

interface RPGPlayerDataService {
    /**
     * 프로파일 id 를 통해 RpgPlayerData 를 구합니다.
     *
     * @param profileID 프로파일 id
     */
    suspend fun getByProfileId(profileID: ProfileID): RPGPlayerData?

    /**
     * @param minecraftPlayerID 마인크래프트 uuid
     * @param index 프로파일 index
     */
    suspend fun getByIndex(minecraftPlayerID: MinecraftPlayerID, index: Int): RPGPlayerData?
}