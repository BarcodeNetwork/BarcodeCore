package com.vjh0107.barcode.core.meister

import com.vjh0107.barcode.core.dependencies.betonquests.BetonQuestComponent
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeRegistrable
import com.vjh0107.barcode.framework.component.handler.impl.registrable.Registrar

@BarcodeComponent
class MeisterComponent : BarcodeRegistrable {
    override val id: String
        get() = "MeisterRegistrar"

    @Registrar(depend = "BetonQuest")
    fun registerBetonQuestIntegration() {
        BetonQuestComponent.registerMeisterIntegration()
    }
}