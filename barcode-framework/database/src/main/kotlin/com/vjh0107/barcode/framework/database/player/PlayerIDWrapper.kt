package com.vjh0107.barcode.framework.database.player

import com.vjh0107.barcode.framework.database.player.multiprofile.ProfileID
import com.vjh0107.barcode.framework.serialization.SerializableData
import kotlinx.serialization.Serializable

@Serializable
data class PlayerIDWrapper internal constructor(
    val minecraftPlayerID: MinecraftPlayerID,
    val profileID: ProfileID
) : SerializableData {
    companion object {
        fun of(minecraftPlayerID: MinecraftPlayerID, profileID: ProfileID): PlayerIDWrapper {
            return PlayerIDWrapper(minecraftPlayerID, profileID)
        }
    }
}