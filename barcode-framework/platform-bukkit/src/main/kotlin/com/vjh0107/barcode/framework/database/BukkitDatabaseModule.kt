package com.vjh0107.barcode.framework.database

import com.vjh0107.barcode.framework.database.config.DatabaseConfig
import com.vjh0107.barcode.framework.database.config.DatabaseHost
import com.vjh0107.barcode.framework.database.config.DatabaseHostProvider
import com.vjh0107.barcode.framework.database.config.impl.HikariDatabaseConfig
import com.vjh0107.barcode.framework.database.datasource.BarcodeDataSource
import com.vjh0107.barcode.framework.database.datasource.impl.HikariBarcodeDataSource
import com.vjh0107.barcode.framework.koin.injector.inject
import com.zaxxer.hikari.HikariConfig
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.Module
import org.koin.core.parameter.parametersOf

/**
 * 버킷 데이터베이스 팩토리입니다.
 * plugin 파라미터가 inject 되지 않습니다. 값과 함께 inject 해주세요.
 */
@Module
class BukkitDatabaseModule {
    @Factory
    fun provideDataSource(@InjectedParam hostProvider: DatabaseHostProvider): BarcodeDataSource {
        val config: DatabaseConfig<HikariConfig> by inject { parametersOf(hostProvider.getDatabaseHost()) }
        hostProvider.getLogger().info("HikariDataSource 에 성공적으로 연결하였습니다.")
        return HikariBarcodeDataSource(config)
    }

    @Factory
    fun provideDatabaseConfig(@InjectedParam host: DatabaseHost): DatabaseConfig<HikariConfig> {
        return HikariDatabaseConfig(host)
    }
}