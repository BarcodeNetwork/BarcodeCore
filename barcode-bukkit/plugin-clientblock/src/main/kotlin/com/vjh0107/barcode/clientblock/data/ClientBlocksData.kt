package com.vjh0107.barcode.clientblock.data

import com.vjh0107.barcode.clientblock.data.wrappers.BlockDataWrapper
import org.bukkit.Location

@Suppress("DataClassPrivateConstructor")
data class ClientBlocksData private constructor(
    val name: String,
    val locationPos1: Location,
    val locationPos2: Location,
    val clientBlocks: MutableList<BlockDataWrapper>
) {
    companion object {
        fun of(
            name: String, locationPos1: Location, locationPos2: Location, clientBlocks: MutableList<BlockDataWrapper>
        ): ClientBlocksData {
            return ClientBlocksData(name, locationPos1, locationPos2, clientBlocks)
        }
    }
}
