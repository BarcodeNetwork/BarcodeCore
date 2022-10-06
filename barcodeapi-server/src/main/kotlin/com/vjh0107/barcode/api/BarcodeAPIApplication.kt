package com.vjh0107.barcode.api

import com.typesafe.config.ConfigFactory
import com.vjh0107.barcode.framework.AbstractBarcodeApplication
import com.vjh0107.barcode.framework.database.config.DatabaseHost
import com.vjh0107.barcode.framework.koin.injector.inject
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import kotlinx.serialization.json.Json
import org.koin.ksp.generated.module
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class BarcodeAPIApplication : AbstractBarcodeApplication() {
    companion object {
        @JvmStatic
        fun main(args: Array<String>?) {
            BarcodeAPIApplication().build().start(wait = true)
        }
    }

    override fun getSLF4JLogger(): Logger = LoggerFactory.getLogger("ktor.application")

    private val config = HoconApplicationConfig(ConfigFactory.load())
    override fun getConfig() = config

    override fun getApplicationModule() = BarcodeAPIModule().module

    override fun Application.applicationDeclaration() {
        install(StatusPages) {
            exception<Throwable> { call, cause ->
                call.respondText(
                    text = cause.cause?.localizedMessage ?: "잘못된 요청입니다.",
                    status = HttpStatusCode.BadRequest
                )
            }
        }
        install(Resources) {
            val json: Json by inject()
            this.serializersModule = json.serializersModule
        }
    }

    override fun getDatabaseHost(): DatabaseHost {
        val property = getConfig().config("database")
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
