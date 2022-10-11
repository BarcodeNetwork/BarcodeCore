package com.vjh0107.barcode.framework

import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeKoinModule
import com.vjh0107.barcode.framework.component.BarcodeRegistrable
import com.vjh0107.barcode.framework.component.handler.impl.registrable.Registrar
import com.vjh0107.barcode.framework.component.handler.impl.registrable.UnRegistrar
import com.vjh0107.barcode.framework.koin.getKoin
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.bukkit.NamespacedKey
import org.koin.core.annotation.InjectedParam
import org.koin.dsl.module

@BarcodeComponent
class BarcodeFrameworkModule(private val plugin: AbstractBarcodePlugin) : BarcodeKoinModule {
    /**
     * 플러그인를 인자로 받는 NamespacedKey 의 Factory 입니다.
     */
    private fun providePluginNamespacedKey(@InjectedParam serviceName: String): NamespacedKey {
        return NamespacedKey(plugin, serviceName)
    }

    /**
     * KtorClient 입니다.
     */
    private fun provideKtorClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    override val targetModule = module {
        factory { params -> providePluginNamespacedKey(params.get()) }
        single { provideKtorClient() }
    }
}