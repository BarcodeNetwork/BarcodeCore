package com.vjh0107.barcode.api.repositories

import com.vjh0107.barcode.core.database.player.childs.RPGPlayerDataEntity
import com.vjh0107.barcode.framework.database.player.multiprofile.ProfileID

interface RPGPlayerDataRepository {
    /**
     * 프로파일 id 를 통해 RpgPlayerData 를 구합니다.
     *
     * @param profileID 프로파일 id
     */
    suspend fun getByProfileId(profileID: ProfileID): RPGPlayerDataEntity?
}