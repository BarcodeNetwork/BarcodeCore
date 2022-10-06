package com.vjh0107.barcode.clientblock.data.wrappers

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.BlockPosition
import com.comphenix.protocol.wrappers.WrappedBlockData
import com.vjh0107.barcode.clientblock.utils.PacketUtils
import org.bukkit.Location

data class MultiBlockChangeWrapper private constructor(
    val locationList: MutableList<Location> = mutableListOf(),
    val wrappedBlockDataList: MutableList<WrappedBlockData> = mutableListOf()
) {
    fun addBlockData(location: Location, wrappedBlockData: WrappedBlockData) {
        locationList.add(location)
        wrappedBlockDataList.add(wrappedBlockData)
    }

    fun buildPacketContainer(): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.MULTI_BLOCK_CHANGE)

        val shortArray = ShortArray(locationList.size)
        val blockDataArray = arrayOfNulls<WrappedBlockData>(wrappedBlockDataList.size)

        for (i in wrappedBlockDataList.indices) {
            shortArray[i] = PacketUtils.locationToShort(locationList[i])
            blockDataArray[i] = wrappedBlockDataList[i]
        }

        val location = locationList[0]
        packet.sectionPositions.writeSafely(0, BlockPosition(location.chunk.x, location.blockY shr 4, location.chunk.z))
        packet.shortArrays.writeSafely(0, shortArray)
        packet.blockDataArrays.writeSafely(0, blockDataArray)
        return packet
    }

    companion object {
        fun of(): MultiBlockChangeWrapper {
            return MultiBlockChangeWrapper()
        }
    }
}