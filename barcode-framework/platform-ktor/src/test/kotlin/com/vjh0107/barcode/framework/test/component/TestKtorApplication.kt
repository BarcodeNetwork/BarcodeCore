package com.vjh0107.barcode.framework.test.component

import com.typesafe.config.ConfigFactory
import com.vjh0107.barcode.framework.AbstractBarcodeApplication
import com.vjh0107.barcode.framework.database.config.DatabaseHost
import io.ktor.server.config.*
import org.slf4j.LoggerFactory
import java.util.logging.Logger

class TestKtorApplication : AbstractBarcodeApplication() {
    override fun getConfig(): ApplicationConfig {
        return HoconApplicationConfig(ConfigFactory.load())
    }

    override fun getSLF4JLogger(): org.slf4j.Logger {
        return LoggerFactory.getLogger("ktor.application")
    }

    override fun getDatabaseHost(): DatabaseHost {
        val applicationConfig = getConfig()
        val property = applicationConfig.config("database")
        return with(property) {
            val address = property("address").getString()
            val port = property("port").getString()
            val user = property("user").getString()
            val password = property("password").getString()
            val databaseName = property("databaseName").getString()
            DatabaseHost(address, port, user, password, databaseName)
        }
    }
}