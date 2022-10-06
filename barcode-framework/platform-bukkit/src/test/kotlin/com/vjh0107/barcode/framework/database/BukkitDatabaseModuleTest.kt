package com.vjh0107.barcode.framework.database

import com.vjh0107.barcode.framework.TestDatabaseHost
import com.vjh0107.barcode.framework.database.config.DatabaseHost
import com.vjh0107.barcode.framework.database.config.DatabaseHostProvider
import com.vjh0107.barcode.framework.database.datasource.BarcodeDataSource
import com.vjh0107.barcode.framework.koin.injector.inject
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.parameter.parametersOf
import org.koin.ksp.generated.module
import java.util.logging.Logger

class BukkitDatabaseModuleTest : AnnotationSpec() {
    private lateinit var hostProvider: DatabaseHostProvider

    @Before
    fun init() {
        startKoin {
            modules(BukkitDatabaseModule().module)
        }
        hostProvider = object : DatabaseHostProvider {
            override fun getLogger(): Logger {
                return Logger.getGlobal()
            }

            override fun getDatabaseHost(): DatabaseHost {
                return TestDatabaseHost.get
            }
        }
    }

    @Test
    fun databaseConnectingTest() = runTest {
        val dataSource: BarcodeDataSource by inject { parametersOf(hostProvider) }
        dataSource.dataSource.connection.isClosed shouldBe false
    }

    @After
    fun teardown() {
        stopKoin()
    }
}