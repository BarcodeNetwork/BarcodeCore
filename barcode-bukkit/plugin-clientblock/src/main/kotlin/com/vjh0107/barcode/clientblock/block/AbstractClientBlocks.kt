package com.vjh0107.barcode.clientblock.block

import com.comphenix.protocol.events.PacketContainer
import com.vjh0107.barcode.clientblock.data.wrappers.BlockDataWrapper

abstract class AbstractClientBlocks : ClientBlocks {
    val packetContainers: MutableList<PacketContainer> = mutableListOf()


    protected abstract fun buildPacketList(isClientSide: Boolean)

    protected abstract fun loadBlockDataList(): List<BlockDataWrapper>

    protected abstract fun restoreOriginalBlocks()
}