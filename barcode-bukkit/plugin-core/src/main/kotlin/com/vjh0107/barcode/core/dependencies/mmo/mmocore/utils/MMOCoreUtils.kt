package com.vjh0107.barcode.core.dependencies.mmo.mmocore.utils


import net.Indyuce.mmocore.api.player.social.Party
import net.Indyuce.mmoitems.MMOItems
import net.Indyuce.mmoitems.api.player.PlayerData
import org.bukkit.entity.Player


//CraftingStation ID = lowercase, " "->"-"
fun PlayerData.isDuringCrafting(stationId: String): Boolean {
    val station = MMOItems.plugin.crafting.getStation(stationId) ?: throw NullPointerException("station is null")
    this.crafting.getQueue(station).crafts.forEach { craftingInfo ->
        if (craftingInfo.left > 0) return true
    }
    return false
}

fun PlayerData.getStationsReadySize(): Int {
    val stations = MMOItems.plugin.crafting.stations
    var readys: Int = 0
    stations.forEach { craftingStation ->
        this.crafting.getQueue(craftingStation).crafts.forEach { craftingInfo ->
            if (craftingInfo.isReady) readys += 1
        }
    }
    return readys
}

fun Party.PartyMembers.getMemberList(): Collection<Player> {
    val playerList = mutableListOf<Player>()
    this.forEach { playerList.add(it.player) }
    return playerList
}

fun Player.isCasting(): Boolean {
    val mmoCorePlayerData = this.getMMOCoreData()
    return mmoCorePlayerData.isCasting
}

fun Player.getMMOCoreData() : net.Indyuce.mmocore.api.player.PlayerData {
    return net.Indyuce.mmocore.api.player.PlayerData.get(player)
}
