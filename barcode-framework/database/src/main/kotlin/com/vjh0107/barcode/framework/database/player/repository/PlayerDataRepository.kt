package com.vjh0107.barcode.framework.database.player.repository

import com.vjh0107.barcode.framework.database.player.PlayerIDWrapper
import com.vjh0107.barcode.framework.database.player.data.PlayerData

interface PlayerDataRepository<T : PlayerData> {
    /**
     * 플레이어 데이터를 구합니다.
     */
    fun getPlayerData(playerID: PlayerIDWrapper): T?

    /**
     * 단일 플레이어의 데이터를 setup 한다.
     */
    fun setup(id: PlayerIDWrapper)

    /**
     * 단일 플레이어의 데이터를 unregister 한다.
     */
    fun unregisterSafe(id: PlayerIDWrapper)
}