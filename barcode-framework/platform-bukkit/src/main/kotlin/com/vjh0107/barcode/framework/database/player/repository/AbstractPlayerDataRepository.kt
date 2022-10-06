package com.vjh0107.barcode.framework.database.player.repository

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.database.player.PlayerIDFactory
import com.vjh0107.barcode.framework.database.player.PlayerIDWrapper
import com.vjh0107.barcode.framework.database.player.data.PlayerData
import com.vjh0107.barcode.framework.exceptions.playerdata.PlayerDataNotFoundException
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

abstract class AbstractPlayerDataRepository<T : PlayerData>(
    val plugin: AbstractBarcodePlugin
) : PlayerDataRepository<T>, Listener {
    init {
        @Suppress("LeakingThis")
        plugin.registerListener(this)
    }

    protected val dataMap: MutableMap<PlayerIDWrapper, T> = Collections.synchronizedMap(HashMap())

    override fun getPlayerData(playerID: PlayerIDWrapper): T? {
        return dataMap[playerID]
    }

    override fun unregisterSafe(id: PlayerIDWrapper) {
        getPlayerData(id)?.close() ?: throw PlayerDataNotFoundException(id)
        dataMap.remove(id)
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onJoin(event: PlayerJoinEvent) {
        setup(PlayerIDFactory.getPlayerID(event.player))
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onQuit(event: PlayerQuitEvent) {
        unregisterSafe(PlayerIDFactory.getPlayerID(event.player))
    }
}