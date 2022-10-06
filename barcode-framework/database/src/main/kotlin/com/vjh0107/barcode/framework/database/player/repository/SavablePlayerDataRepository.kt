package com.vjh0107.barcode.framework.database.player.repository

import com.vjh0107.barcode.framework.database.player.PlayerIDWrapper
import com.vjh0107.barcode.framework.database.player.data.PlayerData

interface SavablePlayerDataRepository<T : PlayerData> : PlayerDataRepository<T> {
    /**
     * 단일 플레이어의 데이터를 불러올 때 사용된다.
     * 꼭 Async 디스패처로 로드해야한다.
     * @see setup 메소드로만 불러오도록 설계되었기 때문에 접근을 명시적으로 제한한다.
     */
    suspend fun loadData(id: PlayerIDWrapper): T

    /**
     * 단일 플레이어의 데이터를 저장할 때 사용된다.
     * 꼭 Async 디스패처로 호출해야한다.
     *
     * @param playerData AbstractPlayerData 를 상속하는 실체 클래스를 인자로 받는다.
     */
    suspend fun saveData(id: PlayerIDWrapper, playerData: T)
}