package com.vjh0107.barcode.clientblock.utils

import com.comphenix.protocol.wrappers.BlockPosition
import com.vjh0107.barcode.clientblock.data.wrappers.MultiBlockChangeWrapper

class MultiBlockChanger {
    private val blockChanges: MutableMap<BlockPosition, MultiBlockChangeWrapper> = mutableMapOf()

    fun getOrCreate(position: BlockPosition): MultiBlockChangeWrapper {
        return getMultiBlockChange(position) ?: createMultiBlockChange(position)
    }

    private fun getMultiBlockChange(position: BlockPosition): MultiBlockChangeWrapper? {
        return blockChanges[position]
    }

    private fun createMultiBlockChange(position: BlockPosition): MultiBlockChangeWrapper {
        val wrapper = MultiBlockChangeWrapper.of()
        blockChanges[position] = wrapper
        return wrapper
    }
}