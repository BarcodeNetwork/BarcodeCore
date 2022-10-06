@file:JvmName("ItemBoxModuleGen")
@file:JvmMultifileClass
package org.koin.ksp.generated

import org.koin.dsl.*


val com_vjh0107_barcode_core_itembox_ItemBoxModule = module {
	single(qualifier=null) { com.vjh0107.barcode.core.itembox.service.impl.ItemBoxServiceImpl(get()) } bind(com.vjh0107.barcode.core.itembox.service.ItemBoxService::class)
	factory(qualifier=null) { params -> com.vjh0107.barcode.core.itembox.ui.impl.ItemBoxInventoryImpl(params.get(),get()) } bind(com.vjh0107.barcode.core.itembox.ui.ItemBoxInventory::class)
}
val com.vjh0107.barcode.core.itembox.ItemBoxModule.module : org.koin.core.module.Module get() = com_vjh0107_barcode_core_itembox_ItemBoxModule