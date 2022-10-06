package com.vjh0107.barcode.framework.koin

import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatformTools

fun KoinContextualApplication.initKoin(): KoinApplication {
    return startKoin koinApplication@{
        printLogger()
        modules(getModules())
    }
}

/**
 * Koin 을 구합니다.
 */
fun getKoin(): Koin {
    return KoinPlatformTools.defaultContext().get()
}