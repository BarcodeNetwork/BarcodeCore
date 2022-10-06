package com.vjh0107.barcode.framework

import com.vjh0107.barcode.framework.koin.KoinContextualApplication
import com.vjh0107.barcode.framework.koin.initKoin
import org.koin.ksp.generated.*

class BarcodeFrameworkPlugin : AbstractBarcodePlugin(), KoinContextualApplication {
    override fun getModules() = listOf(BarcodeFrameworkModule().module)

    override fun onPostLoad() { initKoin() }
}