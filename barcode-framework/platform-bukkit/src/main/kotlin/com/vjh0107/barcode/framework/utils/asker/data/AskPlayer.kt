package com.vjh0107.barcode.framework.utils.asker.data

import com.vjh0107.barcode.framework.utils.asker.data.impl.AskPlayerImpl
import org.bukkit.entity.Player

interface AskPlayer {
    /**
     * 플레이어 입니다.
     */
    val player: Player

    /**
     * Ask 에 수락한 여부입니다.
     */
    var isAccepted: Boolean

    companion object {
        fun of(player: Player, isAccepted: Boolean) : AskPlayer {
            return AskPlayerImpl(player, isAccepted)
        }
    }
}