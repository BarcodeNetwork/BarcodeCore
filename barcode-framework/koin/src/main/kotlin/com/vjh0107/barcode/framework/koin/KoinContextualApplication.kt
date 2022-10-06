package com.vjh0107.barcode.framework.koin

import com.vjh0107.barcode.framework.BarcodeApplication
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module

/**
 * 플랫폼에 따라 KoinContext 를 한개씩 가집니다. ex) ktor, bukkit, velocity
 * Koin 은 KoinApplication 마다 독립적인 context 를 가질 수 있으나,
 * Default Context 로 통일하여 사용하도록 합니다.
 * 하나의 런타임에서 어플리케이션끼리 유기적으로 연결되기 위함입니다.
 */
interface KoinContextualApplication : BarcodeApplication {
    override fun onPostDisable() = stopKoin()

    fun getModules(): List<Module> {
        return listOf()
    }
}
