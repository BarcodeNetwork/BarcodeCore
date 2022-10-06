package com.vjh0107.barcode.framework

import com.vjh0107.barcode.framework.component.handler.BukkitComponentHandlerModule
import com.vjh0107.barcode.framework.database.BukkitDatabaseModule
import com.vjh0107.barcode.framework.nms.NMSModule
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(includes = [BukkitComponentHandlerModule::class, BukkitDatabaseModule::class, NMSModule::class])
class BarcodeFrameworkModule {
    @Single
    fun provideKtorClient() = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
}