package com.vjh0107.barcode.core.itembox.repository

import com.vjh0107.barcode.core.itembox.data.ItemBoxPlayerData
import org.bukkit.entity.Player

interface ItemBoxPlayerDataRepository {
    /**
     * ItemBoxPlayerData 를 구합니다.
     */
    fun getPlayerData(player: Player): ItemBoxPlayerData
}