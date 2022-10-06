package com.vjh0107.barcode.framework.database.player

import com.vjh0107.barcode.framework.database.player.multiprofile.MultiProfileManager
import com.vjh0107.barcode.framework.database.player.multiprofile.ProfileID
import com.vjh0107.barcode.framework.exceptions.PlayerNotFoundException
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object PlayerIDFactory {

    fun getPlayerID(player: Player): PlayerIDWrapper {
        return PlayerIDWrapper.of(getPlayerMinecraftID(player), getPlayerProfileID(player))
    }

    fun getPlayerMinecraftID(player: Player): MinecraftPlayerID {
        return MinecraftPlayerID.of(player.uniqueId)
    }

    fun getPlayerProfileID(player: Player): ProfileID {
        return ProfileID.of(MultiProfileManager.getID(player))
    }
}

fun Player.getPlayerID(): PlayerIDWrapper {
    return PlayerIDFactory.getPlayerID(this)
}

fun PlayerIDWrapper.getPlayer(): Player {
    return Bukkit.getPlayer(this.minecraftPlayerID.id) ?: throw PlayerNotFoundException(this.minecraftPlayerID.id)
}

fun MinecraftPlayerID.getPlayer(): Player {
    return Bukkit.getPlayer(this.id) ?: throw PlayerNotFoundException(this.id)
}