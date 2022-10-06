@file:JvmName("BarcodeFrameworkModuleGen")
@file:JvmMultifileClass
package org.koin.ksp.generated

import org.koin.dsl.*


val com_vjh0107_barcode_framework_BarcodeFrameworkModule = module {
	includes(com.vjh0107.barcode.framework.component.handler.BukkitComponentHandlerModule().module, com.vjh0107.barcode.framework.database.BukkitDatabaseModule().module)
	val moduleInstance = com.vjh0107.barcode.framework.BarcodeFrameworkModule()
	single(qualifier=null) { moduleInstance.provideKtorClient() } 
}
val com.vjh0107.barcode.framework.BarcodeFrameworkModule.module : org.koin.core.module.Module get() = com_vjh0107_barcode_framework_BarcodeFrameworkModule