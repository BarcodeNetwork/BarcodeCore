@file:JvmName("KtorDatabaseModuleGen")
@file:JvmMultifileClass
package org.koin.ksp.generated

import org.koin.dsl.*


val com_vjh0107_barcode_framework_database_KtorDatabaseModule = module {
	single(qualifier=null) { com.vjh0107.barcode.framework.database.impl.HikariBarcodeDataSourceImpl(get()) } bind(com.vjh0107.barcode.framework.database.datasource.BarcodeDataSource::class)
	single(qualifier=null) { com.vjh0107.barcode.framework.database.impl.HikariDatabaseConfigImpl(get()) } bind(com.vjh0107.barcode.framework.database.config.DatabaseConfig::class)
}
val com.vjh0107.barcode.framework.database.KtorDatabaseModule.module : org.koin.core.module.Module get() = com_vjh0107_barcode_framework_database_KtorDatabaseModule