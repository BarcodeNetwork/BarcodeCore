package com.vjh0107.barcode.api.test.modules

//import com.vjh0107.barcode.framework.database.config.DatabaseConfigFactory
import com.vjh0107.barcode.framework.database.config.DatabaseHost
import com.vjh0107.barcode.framework.database.datasource.BarcodeDataSource
//import com.vjh0107.barcode.framework.database.datasource.BarcodeDataSourceFactory
import org.koin.dsl.module

//val testDatabaseModule = module {
//    single<BarcodeDataSource> {
//        val host = DatabaseHost("", "", "", "", "")
//        val config = DatabaseConfigFactory.createMySQLConfig(host)
//        BarcodeDataSourceFactory.createMySQLDataSource(config)
//    }
//}