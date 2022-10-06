package com.vjh0107.barcode.framework.database.datasource.impl

import com.vjh0107.barcode.framework.database.config.DatabaseConfig
import com.vjh0107.barcode.framework.database.datasource.AbstractBarcodeDataSource
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection
import javax.sql.DataSource

open class HikariBarcodeDataSource(databaseConfig: DatabaseConfig<HikariConfig>) : AbstractBarcodeDataSource() {
    override val dataSource: DataSource = HikariDataSource(databaseConfig.get())

    override fun getConnection(): Connection {
        return dataSource.connection
    }

    override fun close() {
        getHikariDataSource().close()
    }

    override fun isClosed(): Boolean {
        return getHikariDataSource().isClosed
    }

    private fun getHikariDataSource(): HikariDataSource {
        return dataSource as HikariDataSource
    }
}