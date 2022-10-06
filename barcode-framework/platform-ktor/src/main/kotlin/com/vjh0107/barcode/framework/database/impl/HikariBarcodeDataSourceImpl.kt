package com.vjh0107.barcode.framework.database.impl

import com.vjh0107.barcode.framework.database.config.DatabaseConfig
import com.vjh0107.barcode.framework.database.datasource.BarcodeDataSource
import com.vjh0107.barcode.framework.database.datasource.impl.HikariBarcodeDataSource
import com.zaxxer.hikari.HikariConfig
import org.koin.core.annotation.Single

@Single(binds = [BarcodeDataSource::class])
class HikariBarcodeDataSourceImpl(
    config: DatabaseConfig<HikariConfig>
): HikariBarcodeDataSource(config)