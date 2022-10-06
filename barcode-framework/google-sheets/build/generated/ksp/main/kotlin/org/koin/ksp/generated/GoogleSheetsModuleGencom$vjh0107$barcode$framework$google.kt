@file:JvmName("GoogleSheetsModuleGen")
@file:JvmMultifileClass
package org.koin.ksp.generated

import org.koin.dsl.*


val com_vjh0107_barcode_framework_google_GoogleSheetsModule = module {
	val moduleInstance = com.vjh0107.barcode.framework.google.GoogleSheetsModule()
	single(qualifier=null) { moduleInstance.provideTransport() } 
	single(qualifier=null) { com.vjh0107.barcode.framework.google.data.credentials.impl.GoogleServiceAccountCredentials(get(),get(),get()) } bind(com.vjh0107.barcode.framework.google.data.credentials.BarcodeGoogleCredentials::class)
	single(qualifier=null) { com.vjh0107.barcode.framework.google.service.impl.GoogleSheetsServiceImpl(getProperty("applicationName"),get(),get()) } bind(com.vjh0107.barcode.framework.google.service.GoogleSheetService::class)
}
val com.vjh0107.barcode.framework.google.GoogleSheetsModule.module : org.koin.core.module.Module get() = com_vjh0107_barcode_framework_google_GoogleSheetsModule