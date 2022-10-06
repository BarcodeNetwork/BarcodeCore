package com.vjh0107.barcode.framework.database.config.impl

import com.vjh0107.barcode.framework.database.config.AbstractDatabaseConfig
import com.vjh0107.barcode.framework.database.config.DatabaseHost
import com.zaxxer.hikari.HikariConfig

open class HikariDatabaseConfig(private val host: DatabaseHost) : AbstractDatabaseConfig<HikariConfig>() {
    override fun initDefaultProperties() {
        this.setProperty("maximumPoolSize", "10");
        this.setProperty("cachePrepStmts", "true");
        this.setProperty("prepStmtCacheSize", "250");
        this.setProperty("prepStmtCacheSqlLimit", "2048");
        this.setProperty("useServerPrepStmts", "true");
        this.setProperty("useLocalSessionState", "true");
        this.setProperty("rewriteBatchedStatements", "true");
        this.setProperty("cacheResultSetMetadata", "true");
        this.setProperty("cacheServerConfiguration", "true");
        this.setProperty("elideSetAutoCommits", "true");
        this.setProperty("maintainTimeStats", "false");
        this.setProperty("characterEncoding", "utf8");
        this.setProperty("useUnicode", "true");
    }

    var autoReconnect: Boolean = true
    var allowMultiQueries: Boolean = true

    override fun get(): HikariConfig {
        return HikariConfig().apply {
            jdbcUrl = "jdbc:mysql://${host.address}:${host.port}/${host.databaseName}?autoReconnect=$autoReconnect&allowMultiQueries=$allowMultiQueries"
            username = host.user
            password = host.password
            connectionTestQuery = "SELECT 1"
            poolName = "BarcodeNetwork MySQL Pool"
            properties.forEach { (property, value) ->
                addDataSourceProperty(property, value)
            }
        }
    }
}