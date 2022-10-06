package com.vjh0107.barcode.core.dependencies.placeholderapi

import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeRegistrable

@BarcodeComponent
class PlaceholderAPIComponent : BarcodeRegistrable {
    override val id: String = "PlaceholderAPIRegistrar"

    override fun register() {
        BarcodePlaceholders().register()
        BarcodeIndicatorPlaceholders().register()
    }
}
