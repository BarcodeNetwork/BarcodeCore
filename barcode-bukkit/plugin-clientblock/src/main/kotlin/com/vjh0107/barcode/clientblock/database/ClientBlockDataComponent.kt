package com.vjh0107.barcode.clientblock.database

import com.vjh0107.barcode.clientblock.BarcodeClientBlockPlugin
import com.vjh0107.barcode.clientblock.data.ClientBlocksData
import com.vjh0107.barcode.clientblock.data.wrappers.BlockDataWrapper
import com.vjh0107.barcode.clientblock.database.entity.ClientBlockDataEntity
import com.vjh0107.barcode.clientblock.database.entity.ClientBlockDataTable
import com.vjh0107.barcode.clientblock.managers.ClientBlocksManager
import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeRegistrable
import com.vjh0107.barcode.framework.database.datasource.BarcodeDataSource
import com.vjh0107.barcode.framework.database.datasource.BarcodeDataSourceFactory
import com.vjh0107.barcode.framework.database.datasource.PluginDataSourceProvider
import com.vjh0107.barcode.framework.database.utils.syncTablesWithDatabase
import com.vjh0107.barcode.framework.injection.instance.InjectInstance
import com.vjh0107.barcode.framework.injection.instance.InstanceProvider
import com.vjh0107.barcode.framework.serialization.serializers.LocationSerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bukkit.Location

@BarcodeComponent
class ClientBlockDataComponent(override val plugin: AbstractBarcodePlugin) : PluginDataSourceProvider,
    BarcodeRegistrable {
    override val id: String = "ClientBlockDataComponent"

    override val dataSource: BarcodeDataSource = BarcodeDataSourceFactory.createSQLiteDataSource(plugin, "database.db")

    @InjectInstance
    companion object : InstanceProvider<ClientBlockDataComponent> {
        override lateinit var instance: ClientBlockDataComponent
    }

    private fun serializeLocation(location: Location): String {
        return Json.encodeToString(LocationSerializer, location)
    }

    private fun deserializeLocation(string: String): Location {
        return Json.decodeFromString(LocationSerializer, string)
    }

    suspend fun saveClientBlocks(clientBlocksData: ClientBlocksData) {
        dataSource.query {
            ClientBlockDataEntity.new {
                this.blockName = clientBlocksData.name
                this.locationPos1 = serializeLocation(clientBlocksData.locationPos1)
                this.locationPos2 = serializeLocation(clientBlocksData.locationPos2)
                this.blockData = Json.encodeToString(clientBlocksData.clientBlocks)
            }
        }
    }

    suspend fun loadClientBlocks() {
        dataSource.query {
            ClientBlockDataEntity.all().forEach {
                val locationPos1 = deserializeLocation(it.locationPos1)
                val locationPos2 = deserializeLocation(it.locationPos2)
                val blockData = Json.decodeFromString<MutableList<BlockDataWrapper>>(it.blockData)
                ClientBlocksManager.add(ClientBlocksData.of(it.blockName, locationPos1, locationPos2, blockData))
            }
        }
    }

    override fun register() {
        CoroutineScope(BarcodeClientBlockPlugin.getAsyncDispatcher()).launch {
            dataSource.query {
                this@ClientBlockDataComponent.syncTablesWithDatabase(ClientBlockDataTable)
            }
        }
    }

    override fun unregister() {
        dataSource.close()
        plugin.logger.info("SQLite 를 성공적으로 close 했습니다.")
    }
}