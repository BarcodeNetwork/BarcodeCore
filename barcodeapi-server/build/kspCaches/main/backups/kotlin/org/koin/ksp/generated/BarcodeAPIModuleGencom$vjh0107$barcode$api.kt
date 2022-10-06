@file:JvmName("BarcodeAPIModuleGen")
@file:JvmMultifileClass
package org.koin.ksp.generated

import org.koin.dsl.*


val com_vjh0107_barcode_api_BarcodeAPIModule = module {
	single(qualifier=null) { com.vjh0107.barcode.api.repositories.impl.ProfileListRepositoryImpl(get()) } bind(com.vjh0107.barcode.api.repositories.ProfileListRepository::class)
	single(qualifier=null) { com.vjh0107.barcode.api.repositories.impl.RPGPlayerDataRepositoryImpl(get()) } bind(com.vjh0107.barcode.api.repositories.RPGPlayerDataRepository::class)
	single(qualifier=null) { com.vjh0107.barcode.api.services.impl.ProfileListServiceImpl(get()) } bind(com.vjh0107.barcode.api.services.ProfileListService::class)
	single(qualifier=null) { com.vjh0107.barcode.api.services.impl.RPGPlayerDataServiceImpl(get(),get()) } bind(com.vjh0107.barcode.api.services.RPGPlayerDataService::class)
}
val com.vjh0107.barcode.api.BarcodeAPIModule.module : org.koin.core.module.Module get() = com_vjh0107_barcode_api_BarcodeAPIModule