package com.vjh0107.barcode.framework.database.player.multiprofile

import org.bukkit.entity.Player
import java.util.*

class MultiProfileManager {
    //TODO("NOT IMPLEMENTED YET")
    companion object {
        fun getID(player: Player): UUID {
            return player.uniqueId // ?: throw PlayerProfileNotFoundException(player)
        }
    }
}