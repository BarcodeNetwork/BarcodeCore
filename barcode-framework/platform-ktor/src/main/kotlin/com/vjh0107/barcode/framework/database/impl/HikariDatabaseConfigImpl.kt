package com.vjh0107.barcode.framework.database.impl

import com.vjh0107.barcode.framework.AbstractBarcodeApplication
import com.vjh0107.barcode.framework.database.config.DatabaseConfig
import com.vjh0107.barcode.framework.database.config.impl.HikariDatabaseConfig
import org.koin.core.annotation.Single

@Single(binds = [DatabaseConfig::class])
class HikariDatabaseConfigImpl(
    application: AbstractBarcodeApplication
) : HikariDatabaseConfig(application.getDatabaseHost())