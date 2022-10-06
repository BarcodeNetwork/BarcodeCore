package com.vjh0107.barcode.framework.database

import com.vjh0107.barcode.framework.TestDatabaseHost
import com.vjh0107.barcode.framework.database.config.DatabaseHost
import com.vjh0107.barcode.framework.database.config.impl.HikariDatabaseConfig
import com.vjh0107.barcode.framework.database.datasource.impl.HikariBarcodeDataSource
import com.vjh0107.barcode.framework.database.exposed.entity.BarcodePlayerEntity
import com.vjh0107.barcode.framework.database.exposed.entity.BarcodeEntityClass
import com.vjh0107.barcode.framework.database.exposed.table.BarcodeIDTable
import com.vjh0107.barcode.framework.database.player.MinecraftPlayerID
import com.vjh0107.barcode.framework.database.player.multiprofile.ProfileID
import kotlinx.coroutines.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.javatime.datetime
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class DatabaseTest {
    val host = TestDatabaseHost.get

    object TestTable : BarcodeIDTable("test_table1") { // 1
        val content = text("content")
        val done = bool("done").default(false)
        val createdAt = datetime("created_at").index().default(LocalDateTime.now())
        val updatedAt = datetime("updated_at").default(LocalDateTime.now())
    }

    class TestEntity(id: EntityID<Int>) : BarcodePlayerEntity(id) { // 2
        companion object : BarcodeEntityClass<TestEntity>(TestTable)

        override var profileID by TestTable.profileID
        override var playerID by TestTable.playerID
        var content by TestTable.content
        var done by TestTable.done
        var createdAt by TestTable.createdAt
        var updatedAt by TestTable.updatedAt
    }

    @Test
    fun databaseTest() {
        val barcodeHikariConfig = HikariDatabaseConfig(host)
        val hikariDataSource = HikariBarcodeDataSource(barcodeHikariConfig)
        println("entering coroutine scope...")
        val mutableList = mutableListOf<String>()
        runBlocking {
            CoroutineScope(Dispatchers.Default).launch {
                hikariDataSource.query {
                    create(TestTable)
                }
                hikariDataSource.query {
                    val playerID = MinecraftPlayerID(UUID.randomUUID())
                    val profileID = ProfileID.of(UUID.randomUUID())
                    TestEntity.new(profileID, playerID) {
                        this.content = "testContentS12112412"
                    }

                    val profileID2 = ProfileID.of(UUID.randomUUID())

                    TestEntity.new(profileID2, playerID) {
                        this.content = "testContentS12212412562"
                    }

                    TestEntity.findByMinecraftPlayerID(playerID).forEach {
                        it.content = "asdfasdfas4taw4a34"
                    }
                    //assert(playerID.id.toString() == mutableList.first())
                }
            }
        }
        mutableList.forEach {
            println(it)
        }
    }
}
