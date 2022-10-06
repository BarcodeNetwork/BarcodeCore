package com.vjh0107.barcode.api.test.database

import com.vjh0107.barcode.core.database.player.root.RootPlayerDataEntity
import com.vjh0107.barcode.core.database.player.root.RootPlayerDataTable
import com.vjh0107.barcode.framework.database.datasource.BarcodeDataSource
import com.vjh0107.barcode.framework.database.player.MinecraftPlayerID
import com.vjh0107.barcode.framework.database.player.multiprofile.ProfileID
import com.vjh0107.barcode.framework.serialization.serialize
//import com.vjh0107.barcodeapi.components.profileListInfoModule
import com.vjh0107.barcodeapi.repositories.ProfileListRepository
import com.vjh0107.barcodeapi.test.Junhyung
//import com.vjh0107.barcodeapi.test.modules.testDatabaseModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@ExperimentalCoroutinesApi
class RootPlayerTableTest : KoinTest {
    val playerUUID = Junhyung.playerUUID

    companion object {
        suspend fun initRootTable(dataSource: BarcodeDataSource) {
            transaction(dataSource.database) {
                SchemaUtils.drop(RootPlayerDataTable)
                SchemaUtils.createMissingTablesAndColumns(RootPlayerDataTable)
            }

            dataSource.query {
                RootPlayerDataEntity.new {
                    this.nicknames = listOf("Junhyung1", "Junhyung2").serialize()
                    this.playerID = MinecraftPlayerID.of(Junhyung.playerUUID)
                    this.profileID = ProfileID.of(UUID.randomUUID())
                }
                RootPlayerDataEntity.new {
                    this.nicknames = listOf("Junhyung1", "Junhyung2").serialize()
                    this.playerID = MinecraftPlayerID.of(Junhyung.playerUUID)
                    this.profileID = ProfileID.of(UUID.randomUUID())
                }
                RootPlayerDataEntity.new {
                    this.nicknames = listOf("Junhyung1", "Junhyung2").serialize()
                    this.playerID = MinecraftPlayerID.of(Junhyung.playerUUID)
                    this.profileID = ProfileID.of(UUID.randomUUID())
                }
            }
        }
    }

    @BeforeTest
    fun createTable() = runTest {
        startKoin {
//            modules(testDatabaseModule)
//            modules(profileListInfoModule)
        }
        val dataSource: BarcodeDataSource by inject()
        initRootTable(dataSource)
    }

    @Test
    fun getPlayerProfiles() = runTest {
        val profileListRepository by inject<ProfileListRepository>()
//        val profiles = profileListRepository.getProfileID(MinecraftPlayerID.of(playerUUID)).size
//        assert(profiles == 3)
    }

    @Test
    fun getPlayerByRandomProfile() = runTest {
        val profileListRepository: ProfileListRepository by inject()
//        val profile = profileListRepository.getProfileID(MinecraftPlayerID.of(playerUUID)).random()
//        val dataSource: BarcodeDataSource by inject()
//        dataSource.query {
//            RootPlayerDataEntity.findByProfileID(profile)?.let {
//                assert(it.playerID.id == playerUUID)
//            }
//        }
    }

    @AfterTest
    fun teardown() {
        stopKoin()
    }
}