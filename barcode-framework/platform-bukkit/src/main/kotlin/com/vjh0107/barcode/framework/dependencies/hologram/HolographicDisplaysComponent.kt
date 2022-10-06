package com.vjh0107.barcode.framework.dependencies.hologram

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeRegistrable
import com.vjh0107.barcode.framework.component.handler.impl.registrable.Registrar

class HolographicDisplaysComponent(private val plugin: AbstractBarcodePlugin) : BarcodeRegistrable {
    override val id: String = "HolographicDisplaysComponent"

    @Registrar("HolographicDisplays")
    fun load() {
        plugin.logger.info("HolographicDisplays 를 발견하였습니다.")
    }
}