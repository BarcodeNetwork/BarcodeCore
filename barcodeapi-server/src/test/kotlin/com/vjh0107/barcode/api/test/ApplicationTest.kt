package com.vjh0107.barcode.api.test

import com.vjh0107.barcode.api.controller.routers.profileListRouter
import com.vjh0107.barcode.api.controller.routers.rpgPlayerDataInfoRouter
import com.vjh0107.barcode.core.database.player.childs.RPGPlayerData
import com.vjh0107.barcode.framework.database.player.multiprofile.ProfileID
import com.vjh0107.barcode.framework.serialization.deserialize
import com.vjh0107.barcode.framework.serialization.deserializeMap
import com.vjh0107.barcode.framework.utils.toUUID
//import com.vjh0107.barcodeapi.test.modules.testDatabaseModule
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest

class ApplicationTest {
    private fun preBuiltApplication(block: suspend ApplicationTestBuilder.() -> Unit) {
        testApplication {
            startKoin {
//                modules(testDatabaseModule)
//                modules(profileListInfoModule)
//                modules(rpgPlayerInfoModule)
//                modules(serializationModule)
            }

            install(Routing) {
                profileListRouter()
                rpgPlayerDataInfoRouter()
                rpgPlayerDataInfoRouter()
            }

            block(this)
        }
    }

    @Test
    fun getProfiles() = preBuiltApplication {
        client.get("/profiles/${Junhyung.playerUUID}").apply {
            val profiles = bodyAsText()
                .deserializeMap<Map<Int, String>>()
                .map { it.key to ProfileID.of(it.value.toUUID()) }
                .toMap()
            assert(profiles.size == 3)
        }
    }

    @Test
    fun getRPGPlayerDataByIndex() = preBuiltApplication {
        client.get("/rpgPlayerDataByIndex/${Junhyung.playerUUID}/0").apply {
            val rpgPlayerData: RPGPlayerData = bodyAsText().deserialize()
            assert(rpgPlayerData.classID == Junhyung.RPGPlayerData1.classID)
        }
    }

    @Test
    fun getRPGPlayerData() = preBuiltApplication {
        val profile = client.get("/profiles/${Junhyung.playerUUID}").run {
            bodyAsText()
                .deserializeMap<Map<Int, String>>()
                .map { it.key to ProfileID.of(it.value.toUUID()) }
                .toMap()[0] ?: run {
                    assert(false)
                    return@preBuiltApplication
                }
        }
        client.get("/rpgPlayerData/${profile.id}").apply {
            val rpgPlayerData: RPGPlayerData = bodyAsText().deserialize()
            assert(rpgPlayerData.classID == Junhyung.RPGPlayerData1.classID)
        }
    }

    @AfterTest
    fun teardown() {
        stopKoin()
    }
}