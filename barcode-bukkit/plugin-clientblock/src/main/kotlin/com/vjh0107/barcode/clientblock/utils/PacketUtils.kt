package com.vjh0107.barcode.clientblock.utils

import org.bukkit.Location

object PacketUtils {
    /**
     * PlayerChunk 에서 가져온 로직
     */
    fun locationToShort(location: Location): Short {
        return (location.blockX and 15 shl 8 or (location.blockZ and 15 shl 4) or (location.blockY and 15)).toShort()
    }
}