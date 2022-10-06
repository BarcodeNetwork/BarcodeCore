package com.vjh0107.barcode.core.database.player

import com.vjh0107.barcode.core.database.player.childs.ItemBoxPlayerDataEntity
import com.vjh0107.barcode.core.database.player.childs.MeisterPlayerDataTable
import com.vjh0107.barcode.core.database.player.root.RootPlayerDataEntity
import com.vjh0107.barcode.core.database.player.root.RootPlayerDataTable
import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.database.player.repository.AbstractSavablePlayerDataRepository
import com.vjh0107.barcode.framework.database.player.PlayerIDWrapper
import com.vjh0107.barcode.framework.exceptions.playerdata.PlayerDataNotFoundException
import com.vjh0107.barcode.framework.injection.instance.InjectInstance
import com.vjh0107.barcode.framework.injection.instance.InstanceProvider
import com.vjh0107.barcode.framework.serialization.deserialize
import com.vjh0107.barcode.framework.serialization.serialize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.SchemaUtils
import java.time.LocalDateTime

@BarcodeComponent
class CorePlayerDataRepository(
    plugin: AbstractBarcodePlugin
) : AbstractSavablePlayerDataRepository<CorePlayerPlayerData>(plugin) {

    @InjectInstance
    companion object : InstanceProvider<CorePlayerDataRepository> {
        override lateinit var instance: CorePlayerDataRepository
    }

    override fun load() {
        CoroutineScope(Dispatchers.IO).launch {
            dataSource.query {
                SchemaUtils.createMissingTablesAndColumns(
                    RootPlayerDataTable,
                    MeisterPlayerDataTable,
                )
            }
        }
    }

    override suspend fun saveData(playerData: CorePlayerPlayerData) {
        dataSource.query {
            RootPlayerDataEntity.findByProfileID(playerData.playerID.profileID)?.let {
                it.updatedAt = LocalDateTime.now()
            } ?: throw PlayerDataNotFoundException(playerData.player)

            ItemBoxPlayerDataEntity.findById(playerData.playerID.profileID)?.let {
                it.itemBox = playerData.getSavableData().itemBox.serialize()
            }

//            MeisterPlayerDataEntity.findById(playerData.playerID.profileID)?.let {
//                TODO("levels too")
//                it.skills = playerData.getSavableData().meisterSkill.serialize()
//            }
        }
    }

    override suspend fun loadData(playerData: CorePlayerPlayerData) {
        dataSource.query {
            val result = RootPlayerDataEntity.findByProfileID(playerData.playerID.profileID)
            if (result == null) {
                loadDefaultData(playerData)
            } else {
                loadExistData(playerData, result)
            }
            playerData.whenPostLoad()
        }
    }

    private fun loadDefaultData(playerData: CorePlayerPlayerData) {
        RootPlayerDataEntity.new(playerData) {}
    }
}