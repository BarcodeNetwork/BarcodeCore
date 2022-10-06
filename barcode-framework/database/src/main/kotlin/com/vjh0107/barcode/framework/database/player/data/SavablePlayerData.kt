package com.vjh0107.barcode.framework.database.player.data

interface SavablePlayerData : PlayerData {
    /**
     * 데이터가 전부 정상적으로 로드 되었는가요?
     */
    var isCompletelyLoaded: Boolean
}