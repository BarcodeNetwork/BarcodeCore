package com.vjh0107.barcode.core.database

import com.vjh0107.barcode.core.database.player.CorePlayerPlayerData
import com.vjh0107.barcode.core.database.player.CorePlayerDataRepository
import com.vjh0107.barcode.framework.database.player.PlayerIDFactory
import com.vjh0107.barcode.framework.exceptions.playerdata.PlayerDataNotFoundException
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

fun Player.getCorePlayerData() : CorePlayerPlayerData {
    return this.getNullableCorePlayerData() ?: throw PlayerDataNotFoundException(this)
}

fun Player.getNullableCorePlayerData() : CorePlayerPlayerData? {
    return CorePlayerDataRepository.instance.getPlayerData(PlayerIDFactory.getPlayerID(this))
}

fun UUID.getNullableCorePlayerData() : CorePlayerPlayerData? {
    val player = Bukkit.getPlayer(this) ?: return null
    return player.getNullableCorePlayerData()
}