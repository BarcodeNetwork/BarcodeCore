package com.vjh0107.barcode.api.test.database

import com.vjh0107.barcode.api.repositories.ProfileListRepository
import com.vjh0107.barcode.api.test.Junhyung
import com.vjh0107.barcode.core.database.player.childs.RPGPlayerDataTable
import com.vjh0107.barcode.framework.database.datasource.BarcodeDataSource
//import com.vjh0107.barcodeapi.components.profileListInfoModule
//import com.vjh0107.barcodeapi.components.rpgPlayerInfoModule
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
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@ExperimentalCoroutinesApi
class RPGPlayerTableTest : KoinTest {
    private val playerUUID = Junhyung.playerUUID

    @BeforeTest
    fun initTable() = runTest {
        startKoin {
//            modules(testDatabaseModule)
//            modules(rpgPlayerInfoModule)
//            modules(profileListInfoModule)
        }
        val dataSource: BarcodeDataSource by inject()
        RootPlayerTableTest.initRootTable(dataSource)
        transaction(dataSource.database) {
            SchemaUtils.drop(RPGPlayerDataTable)
            SchemaUtils.createMissingTablesAndColumns(RPGPlayerDataTable)
        }

        val profileListRepository: ProfileListRepository by inject()
//        dataSource.query {
//            profileListRepository.getProfileID(MinecraftPlayerID.of(playerUUID)).forEachIndexed { index, profile ->
//                RPGPlayerDataEntity.new(profile) {
//                    Junhyung.profiles[index].let {
//                        classID = it.classID
//                        level = it.level
//                        experience = it.experience
//                        skills = it.skills.serialize()
//                        skillPoints = it.skillPoints
//                        boundSkills = it.boundSkills.serialize()
//                        attributes = it.attributes.serialize()
//                        attributePoints = it.attributePoints
//                        attributeResetPoints = it.attributeResetPoint
//                    }
//                }
//            }
//        }
    }

    @Test
    fun getRPGPlayerData() = runTest {
        val profileListRepository: ProfileListRepository by inject()
//        val profileIDs = profileListRepository.getProfileID(MinecraftPlayerID.of(Junhyung.playerUUID))
//
//        val rpgPlayerDataRepository: RPGPlayerDataRepository by inject()
//        val bladeProfile = profileIDs.map {
//            rpgPlayerDataRepository.getByProfileId(it)
//        }.first {
//            it?.classID == "BLADE"
//        }
//
//        if (bladeProfile == null) {
//            assert(false)
//        }
//        assert(bladeProfile!!.skills.filter { it.key == "어설트" }.isNotEmpty())
    }

    @AfterTest
    fun teardown() {
        stopKoin()
    }
}