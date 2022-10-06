package com.vjh0107.barcode.cutscene.data

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeRegistrable
import com.vjh0107.barcode.framework.injection.instance.InjectInstance
import com.vjh0107.barcode.framework.injection.instance.InstanceProvider
import com.vjh0107.barcode.cutscene.data.player.CutscenePlayerDataRepository

@BarcodeComponent
class CutsceneDataComponent(val plugin: AbstractBarcodePlugin) : BarcodeRegistrable {
    override val id: String = "CutsceneDataComponent"

    val playerDataManager = CutscenePlayerDataRepository(plugin)

    @InjectInstance
    companion object : InstanceProvider<CutsceneDataComponent> {
        override lateinit var instance: CutsceneDataComponent
    }
}