package com.vjh0107.barcode.framework.dependencies.placeholderapi

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeRegistrable
import com.vjh0107.barcode.framework.component.handler.impl.registrable.Registrar

class PlaceholderAPIComponent(private val plugin: AbstractBarcodePlugin) : BarcodeRegistrable {
    override val id: String = "PlaceholderAPIComponent"

    @Registrar("PlaceholderAPI")
    fun load() {
        plugin.logger.info("PlaceholderAPI 를 발견하였습니다.")
        isEnabled = true
    }

    companion object {
        var isEnabled: Boolean = false
    }
}