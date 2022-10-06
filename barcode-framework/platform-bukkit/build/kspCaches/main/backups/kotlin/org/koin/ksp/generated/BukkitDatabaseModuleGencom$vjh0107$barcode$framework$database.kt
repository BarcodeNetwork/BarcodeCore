@file:JvmName("BukkitDatabaseModuleGen")
@file:JvmMultifileClass
package org.koin.ksp.generated

import org.koin.dsl.*


val com_vjh0107_barcode_framework_database_BukkitDatabaseModule = module {
	val moduleInstance = com.vjh0107.barcode.framework.database.BukkitDatabaseModule()
	factory(qualifier=null) { params -> moduleInstance.provideDataSource(params.get()) } 
	factory(qualifier=null) { params -> moduleInstance.provideDatabaseConfig(params.get()) } 
}
val com.vjh0107.barcode.framework.database.BukkitDatabaseModule.module : org.koin.core.module.Module get() = com_vjh0107_barcode_framework_database_BukkitDatabaseModule