package com.vjh0107.barcode.core.dependencies.skript

import com.vjh0107.barcode.framework.component.BarcodeRegistrable
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.handler.impl.registrable.Registrar

@BarcodeComponent
class SkriptComponent : BarcodeRegistrable {
    override val id: String = "SkriptRegistrar"

    // disabled
    // @Registrar
    @Deprecated("removal", ReplaceWith("none"))
    fun registerDungeonsIntegration() {
        SkriptRegistrarFactory.registerDungeonsIntegrations()
    }

    @Registrar
    fun registerMagicSpellsEvents() {
        SkriptRegistrarFactory.registerMagicSpellsEvents()

    }
}
