package com.vjh0107.barcode.core.itembox

import com.vjh0107.barcode.core.itembox.compatibility.betonquest.ItemBoxItemAmount
import com.vjh0107.barcode.core.itembox.compatibility.betonquest.OpenItemBox
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeRegistrable
import com.vjh0107.barcode.framework.component.handler.impl.registrable.Registrar
import org.betonquest.betonquest.BetonQuest
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.component.KoinComponent
import org.koin.ksp.generated.module

@Module
@ComponentScan
@BarcodeComponent
class ItemBoxModule : BarcodeRegistrable, KoinComponent {
    override val id: String get() = "BarcodeCore.ItemBox"

    override fun onLoad() {
        getKoin().loadModules(listOf(module))
    }

    @Registrar(depend = "BetonQuest")
    fun registerBetonQuestIntegration() {
        with(BetonQuest.getInstance()) {
            registerEvents("Barcode_OpenItemBox", OpenItemBox::class.java)
            registerVariable("Barcode_ItemBoxItemAmount", ItemBoxItemAmount::class.java)
        }
    }
}