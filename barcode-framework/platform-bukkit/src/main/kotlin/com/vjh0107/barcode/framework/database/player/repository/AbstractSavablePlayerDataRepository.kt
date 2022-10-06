package com.vjh0107.barcode.framework.database.player.repository

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.LoggerProvider
import com.vjh0107.barcode.framework.component.BarcodePluginManager
import com.vjh0107.barcode.framework.component.BarcodeRepository
import com.vjh0107.barcode.framework.coroutine.extensions.MinecraftAsync
import com.vjh0107.barcode.framework.coroutine.extensions.MinecraftMain
import com.vjh0107.barcode.framework.database.datasource.BarcodeDataSource
import com.vjh0107.barcode.framework.database.player.PlayerIDWrapper
import com.vjh0107.barcode.framework.database.player.data.SavablePlayerData
import com.vjh0107.barcode.framework.database.player.events.BarcodePlayerDataLoadEvent
import com.vjh0107.barcode.framework.database.player.getPlayer
import com.vjh0107.barcode.framework.exceptions.playerdata.PlayerDataNotFoundException
import com.vjh0107.barcode.framework.koin.injector.inject
import kotlinx.coroutines.*
import org.bukkit.Bukkit
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.koin.core.parameter.parametersOf
import java.util.logging.Logger

/**
 * BarcodeRepository 입니다.
 * 가장 먼저 초기화되는 컴포넌트 입니다.
 */
abstract class AbstractSavablePlayerDataRepository<T : SavablePlayerData>(
    plugin: AbstractBarcodePlugin
) : AbstractPlayerDataRepository<T>(plugin), SavablePlayerDataRepository<T>, BarcodeRepository, LoggerProvider {
    val dataSource: BarcodeDataSource by inject { parametersOf(plugin) }

    final override fun close() {
        dataSource.close()
        getLogger().info("BarcodeDataSource 를 성공적으로 close 하였습니다.")
    }

    abstract fun getTablesToLoad(): List<Table>

    final override fun load() {
        CoroutineScope(Dispatchers.IO).launch {
            dataSource.query {
                SchemaUtils.createMissingTablesAndColumns(*getTablesToLoad().toTypedArray())
            }
        }
    }

    final override fun setup(id: PlayerIDWrapper) {
        CoroutineScope(Dispatchers.MinecraftMain(plugin)).launch {
            if (!dataMap.containsKey(id)) {
                val playerData = withContext(Dispatchers.MinecraftAsync(plugin)) {
                    // TODO: atomicfu 로 변경
                    val data = loadData(id)
                    dataMap[id] = data
                    data
                }
                withContext(Dispatchers.MinecraftMain(plugin)) {
                    Bukkit.getPluginManager().callEvent(BarcodePlayerDataLoadEvent(id.getPlayer(), playerData))
                }
            }
        }
    }

    final override fun unregisterSafe(id: PlayerIDWrapper) {
        val playerData = getPlayerData(id) ?: throw PlayerDataNotFoundException(id)
        if (playerData.isCompletelyLoaded) {
            CoroutineScope(Dispatchers.IO).launch {
                saveData(id, playerData)
                playerData.close()
                this@AbstractSavablePlayerDataRepository.dataMap.remove(id)
            }
        } else {
            playerData.close()
            this.dataMap.remove(id)
        }
    }

    open override fun getLogger(): Logger {
        return plugin.logger
    }
}

