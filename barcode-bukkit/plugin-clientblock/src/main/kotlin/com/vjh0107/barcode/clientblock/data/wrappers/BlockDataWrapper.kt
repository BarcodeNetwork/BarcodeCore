package com.vjh0107.barcode.clientblock.data.wrappers

import com.vjh0107.barcode.framework.serialization.SerializableData
import com.vjh0107.barcode.framework.serialization.serializers.LocationSerializer
import kotlinx.serialization.Serializable
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.data.BlockData

@Suppress("DataClassPrivateConstructor")
@Serializable
data class BlockDataWrapper private constructor(
    @Serializable(with = LocationSerializer::class) val location: Location,
    private val blockData: String
) : SerializableData {
    private constructor(location: Location, blockData: BlockData) : this(location, blockData.asString)

    fun getBlockData(): BlockData {
        return Bukkit.createBlockData(blockData)
    }

    companion object {
        fun of(location: Location, blockData: BlockData): BlockDataWrapper {
            return BlockDataWrapper(location, blockData)
        }

        fun of(location: Location, blockData: String): BlockDataWrapper {
            return BlockDataWrapper(location, Bukkit.createBlockData(blockData))
        }
    }
}